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
package net.raphimc.vialoader.util;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProtocolVersionList {

    public static List<ProtocolVersion> getProtocolsNewToOld() {
        final List<ProtocolVersion> protocolVersions = new ArrayList<>(ProtocolVersion.getProtocols());
        Collections.reverse(protocolVersions);
        return Collections.unmodifiableList(protocolVersions);
    }

    /**
     * Returns true if first is closer to version than second
     *
     * @param version The version to compare to
     * @param first   The first version
     * @param second  The second version
     * @return true if first is closer to version than second
     */
    public static boolean isCloserTo(final ProtocolVersion version, final ProtocolVersion first, final ProtocolVersion second) {
        if (version.getVersionType() == first.getVersionType() || version.getVersionType() == second.getVersionType()) {
            return Math.abs(version.getVersion() - first.getVersion()) < Math.abs(version.getVersion() - second.getVersion());
        } else {
            final int ordinal = version.getVersionType().ordinal();
            return Math.abs(ordinal - first.getVersionType().ordinal()) < Math.abs(ordinal - second.getVersionType().ordinal());
        }
    }

}
