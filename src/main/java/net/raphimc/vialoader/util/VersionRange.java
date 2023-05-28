/*
 * This file is part of ViaLoader - https://github.com/RaphiMC/ViaLoader
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
package net.raphimc.vialoader.util;

public class VersionRange {

    private final VersionEnum lowerBound;
    private final VersionEnum upperBound;

    public VersionRange(VersionEnum lowerBound, VersionEnum upperBound) {
        if (lowerBound == null && upperBound == null) {
            throw new RuntimeException("Invalid protocol range");
        }
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public static VersionRange andNewer(final VersionEnum version) {
        return new VersionRange(null, version);
    }

    public static VersionRange singleton(final VersionEnum version) {
        return new VersionRange(version, version);
    }

    public static VersionRange andOlder(final VersionEnum version) {
        return new VersionRange(version, null);
    }

    public boolean contains(final VersionEnum protocolVersion) {
        if (this.lowerBound != null && lowerBound.isOlderThan(protocolVersion)) return false;

        return this.upperBound == null || upperBound.isOlderThanOrEqualTo(protocolVersion);
    }

    @Override
    public String toString() {
        if (lowerBound == null) return upperBound.getName() + "+";
        if (upperBound == null) return lowerBound.getName() + "-";
        if (lowerBound == upperBound) return lowerBound.getName();

        return lowerBound.getName() + " - " + upperBound.getName();
    }

}
