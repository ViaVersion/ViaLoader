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
package net.raphimc.viaprotocolhack.impl.viaversion;


import com.google.common.collect.Lists;
import com.viaversion.viaversion.configuration.AbstractViaConfig;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class VPViaConfig extends AbstractViaConfig {

    protected final List<String> UNSUPPORTED = Lists.newArrayList(
            "checkforupdates", "bungee-ping-interval", "bungee-ping-save", "bungee-servers",
            "velocity-ping-interval", "velocity-ping-save", "velocity-servers",
            "block-protocols", "block-disconnect-msg", "reload-disconnect-msg", "max-pps",
            "max-pps-kick-msg", "tracking-period", "tracking-warning-pps", "tracking-max-warnings", "tracking-max-kick-msg",
            "blockconnection-method", "quick-move-action-fix", "item-cache", "change-1_9-hitbox", "change-1_14-hitbox",
            "use-new-deathmessages", "nms-player-ticking"
    );

    public VPViaConfig(final File configFile) {
        super(configFile);
    }

    @Override
    protected void handleConfig(Map<String, Object> config) {
    }

    @Override
    public List<String> getUnsupportedOptions() {
        return Collections.unmodifiableList(UNSUPPORTED);
    }

    @Override
    public boolean isCheckForUpdates() {
        return false;
    }

    @Override
    public String getBlockConnectionMethod() {
        return "packet";
    }

    @Override
    public boolean is1_12QuickMoveActionFix() {
        return false;
    }

    @Override
    public boolean isItemCache() {
        return false;
    }

    @Override
    public boolean is1_9HitboxFix() {
        return false;
    }

    @Override
    public boolean is1_14HitboxFix() {
        return false;
    }

    @Override
    public boolean isShowNewDeathMessages() {
        return false;
    }

    @Override
    public boolean isNMSPlayerTicking() {
        return false;
    }

}
