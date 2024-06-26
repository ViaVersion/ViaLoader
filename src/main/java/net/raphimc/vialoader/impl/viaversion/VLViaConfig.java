/*
 * This file is part of ViaLoader - https://github.com/RaphiMC/ViaLoader
 * Copyright (C) 2020-2024 RK_01/RaphiMC and contributors
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
package net.raphimc.vialoader.impl.viaversion;

import com.viaversion.viaversion.configuration.AbstractViaConfig;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

public class VLViaConfig extends AbstractViaConfig {

    protected final List<String> UNSUPPORTED = new ArrayList<>();

    public VLViaConfig(final File configFile, final Logger logger) {
        super(configFile, logger);

        UNSUPPORTED.addAll(BUKKIT_ONLY_OPTIONS);
        UNSUPPORTED.addAll(VELOCITY_ONLY_OPTIONS);
        UNSUPPORTED.add("check-for-updates");
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

}
