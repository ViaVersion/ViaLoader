/*
 * This file is part of ViaLoader - https://github.com/ViaVersion/ViaLoader
 * Copyright (C) 2020-2025 the original authors
 *                         - RK_01/RaphiMC
 *                         - FlorianMichael/EnZaXD <florian.michael07@gmail.com>
 * Copyright (C) 2023-2025 ViaVersion and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.viaversion.vialoader.impl.platform;

import com.viaversion.vialoader.ViaLoader;
import com.viaversion.vialoader.impl.viaversion.VLApiBase;
import com.viaversion.vialoader.impl.viaversion.VLViaConfig;
import com.viaversion.vialoader.util.JLoggerToSLF4J;
import com.viaversion.vialoader.util.PacketTypeUtil;
import com.viaversion.vialoader.util.VLTask;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import com.viaversion.viaversion.api.configuration.ViaVersionConfig;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.platform.ViaPlatform;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.configuration.AbstractViaConfig;
import com.viaversion.viaversion.protocols.base.InitialBaseProtocol;
import com.viaversion.viaversion.util.VersionInfo;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ViaVersionPlatformImpl implements ViaPlatform<UserConnection> {

    private static final Logger LOGGER = new JLoggerToSLF4J(LoggerFactory.getLogger("ViaVersion"));

    private final File dataFolder;
    private final AbstractViaConfig config;
    private final ViaAPI<UserConnection> api;

    public ViaVersionPlatformImpl(final File rootFolder) {
        this.dataFolder = new File(rootFolder, "ViaLoader");
        this.config = this.createConfig();
        this.api = this.createApi();
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    public String getPlatformName() {
        return "ViaLoader";
    }

    @Override
    public String getPlatformVersion() {
        return ViaLoader.VERSION;
    }

    @Override
    public String getPluginVersion() {
        return VersionInfo.getVersion();
    }

    @Override
    public VLTask runAsync(Runnable runnable) {
        return new VLTask(Via.getManager().getScheduler().execute(runnable));
    }

    @Override
    public VLTask runRepeatingAsync(Runnable runnable, long period) {
        return new VLTask(Via.getManager().getScheduler().scheduleRepeating(runnable, 0, period * 50, TimeUnit.MILLISECONDS));
    }

    @Override
    public VLTask runSync(Runnable runnable) {
        return this.runAsync(runnable);
    }

    @Override
    public VLTask runSync(Runnable runnable, long delay) {
        return new VLTask(Via.getManager().getScheduler().schedule(runnable, delay * 50, TimeUnit.MILLISECONDS));
    }

    @Override
    public VLTask runRepeatingSync(Runnable runnable, long period) {
        return this.runRepeatingAsync(runnable, period);
    }

    @Override
    public void sendCustomPayload(UUID uuid, String channel, String message) {
        UserConnection connection = Via.getManager().getConnectionManager().getConnectedClient(uuid);
        if (connection == null) {
            // The connection field will always be null on clientside platforms, get the first connection instead
            connection = Via.getManager().getConnectionManager().getConnections().stream().findFirst().orElse(null);
        }

        if (connection != null) {
            final PacketWrapper customPayload = PacketWrapper.create(PacketTypeUtil.getServerboundPacketType("CUSTOM_PAYLOAD", connection), connection);
            customPayload.write(Types.STRING, channel);
            customPayload.write(Types.REMAINING_BYTES, message.getBytes());
            customPayload.sendToServer(InitialBaseProtocol.class);
        }
    }

    @Override
    public boolean hasPlugin(String name) {
        return false; // Used for ViaPlatform#getUnsupportedSoftwareClasses
    }

    @Override
    public boolean couldBeReloading() {
        return false;
    }

    @Override
    public boolean isProxy() {
        return true;
    }

    @Override
    public ViaAPI<UserConnection> getApi() {
        return this.api;
    }

    @Override
    public ViaVersionConfig getConf() {
        return this.config;
    }

    @Override
    public File getDataFolder() {
        return this.dataFolder;
    }

    protected AbstractViaConfig createConfig() {
        return new VLViaConfig(new File(this.dataFolder, "viaversion.yml"), this.getLogger());
    }

    protected ViaAPI<UserConnection> createApi() {
        return new VLApiBase();
    }

}
