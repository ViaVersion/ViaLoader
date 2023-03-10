/*
 * This file is part of ViaProtocolHack - https://github.com/RaphiMC/ViaProtocolHack
 * Copyright (C) 2023 RK_01/RaphiMC and contributors
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
package net.raphimc.viaprotocolhack.impl.platform;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import com.viaversion.viaversion.api.configuration.ConfigurationProvider;
import com.viaversion.viaversion.api.configuration.ViaVersionConfig;
import com.viaversion.viaversion.api.platform.ViaPlatform;
import com.viaversion.viaversion.configuration.AbstractViaConfig;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.util.VersionInfo;
import net.raphimc.viaprotocolhack.ViaProtocolHack;
import net.raphimc.viaprotocolhack.commands.UserCommandSender;
import net.raphimc.viaprotocolhack.impl.viaversion.VPHApiBase;
import net.raphimc.viaprotocolhack.impl.viaversion.VPHViaConfig;
import net.raphimc.viaprotocolhack.util.JLoggerToSLF4J;
import net.raphimc.viaprotocolhack.util.VPHTask;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ViaVersionPlatformImpl implements ViaPlatform<UUID> {

    private static final Logger LOGGER = new JLoggerToSLF4J(LoggerFactory.getLogger("ViaVersion"));

    private final File dataFolder;
    private final AbstractViaConfig config;
    private final ViaAPI<UUID> api;

    public ViaVersionPlatformImpl(final File rootFolder) {
        this.dataFolder = new File(rootFolder, "ViaProtocolHack");
        this.config = this.createConfig();
        this.api = this.createApi();
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    public String getPlatformName() {
        return "ViaProtocolHack";
    }

    @Override
    public String getPlatformVersion() {
        return ViaProtocolHack.VERSION;
    }

    @Override
    public String getPluginVersion() {
        return VersionInfo.VERSION;
    }

    @Override
    public VPHTask runAsync(Runnable runnable) {
        return new VPHTask(Via.getManager().getScheduler().execute(runnable));
    }

    @Override
    public VPHTask runRepeatingAsync(Runnable runnable, long ticks) {
        return new VPHTask(Via.getManager().getScheduler().scheduleRepeating(runnable, 0, ticks * 50, TimeUnit.MILLISECONDS));
    }

    @Override
    public VPHTask runSync(Runnable runnable) {
        return this.runAsync(runnable);
    }

    @Override
    public VPHTask runSync(Runnable runnable, long ticks) {
        return new VPHTask(Via.getManager().getScheduler().schedule(runnable, ticks * 50, TimeUnit.MILLISECONDS));
    }

    @Override
    public VPHTask runRepeatingSync(Runnable runnable, long ticks) {
        return this.runRepeatingAsync(runnable, ticks);
    }

    @Override
    public ViaCommandSender[] getOnlinePlayers() {
        return Via.getManager().getConnectionManager().getConnectedClients().values().stream().map(UserCommandSender::new).toArray(ViaCommandSender[]::new);
    }

    @Override
    public void sendMessage(UUID uuid, String msg) {
        if (uuid == null) {
            this.getLogger().info(msg);
        } else {
            this.getLogger().info("[" + uuid + "] " + msg);
        }
    }

    @Override
    public boolean kickPlayer(UUID uuid, String s) {
        return false;
    }

    @Override
    public boolean isPluginEnabled() {
        return true;
    }

    @Override
    public boolean isOldClientsAllowed() {
        return true;
    }

    @Override
    public boolean hasPlugin(String s) {
        return s.equalsIgnoreCase("ViaVersion") || s.equalsIgnoreCase("ViaBackwards") || s.equalsIgnoreCase("ViaRewind");
    }

    @Override
    public boolean isProxy() {
        return true;
    }

    @Override
    public ViaAPI<UUID> getApi() {
        return this.api;
    }

    @Override
    public ViaVersionConfig getConf() {
        return this.config;
    }

    @Override
    public ConfigurationProvider getConfigurationProvider() {
        return this.config;
    }

    @Override
    public File getDataFolder() {
        return this.dataFolder;
    }

    @Override
    public void onReload() {
    }

    @Override
    public JsonObject getDump() {
        return new JsonObject();
    }

    protected AbstractViaConfig createConfig() {
        final AbstractViaConfig config = new VPHViaConfig(new File(this.dataFolder, "viaversion.yml"));
        config.reloadConfig();
        return config;
    }

    protected ViaAPI<UUID> createApi() {
        return new VPHApiBase();
    }

}
