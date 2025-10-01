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
package com.viaversion.vialoader.util;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VersionRange {

    private final ProtocolVersion min;
    private final ProtocolVersion max;
    private final List<VersionRange> ranges;

    private VersionRange(final ProtocolVersion min, final ProtocolVersion max) {
        this.min = min;
        this.max = max;
        this.ranges = new ArrayList<>(3);
    }

    public static VersionRange andNewer(final ProtocolVersion version) {
        Objects.requireNonNull(version, "Version cannot be null");

        return new VersionRange(version, null);
    }

    public static VersionRange single(final ProtocolVersion version) {
        Objects.requireNonNull(version, "Version cannot be null");

        return new VersionRange(version, version);
    }

    public static VersionRange andOlder(final ProtocolVersion version) {
        Objects.requireNonNull(version, "Version cannot be null");

        return new VersionRange(null, version);
    }

    public static VersionRange of(final ProtocolVersion min, final ProtocolVersion max) {
        Objects.requireNonNull(min, "Min version cannot be null");
        Objects.requireNonNull(max, "Max version cannot be null");
        if (min.newerThan(max)) {
            throw new IllegalArgumentException("Min version cannot be newer than max version");
        }

        return new VersionRange(min, max);
    }

    public static VersionRange all() {
        return new VersionRange(null, null);
    }

    public VersionRange add(final VersionRange range) {
        this.ranges.add(range);
        return this;
    }

    public boolean contains(final ProtocolVersion version) {
        Objects.requireNonNull(version, "Version cannot be null");

        for (VersionRange range : this.ranges) {
            if (range.contains(version)) return true;
        }
        if (this.min == null && this.max == null) return true;
        else if (this.min == null) return version.olderThanOrEqualTo(this.max);
        else if (this.max == null) return version.newerThanOrEqualTo(this.min);
        return version.newerThanOrEqualTo(this.min) && version.olderThanOrEqualTo(this.max);
    }

    public ProtocolVersion getMin() {
        return this.min;
    }

    public ProtocolVersion getMax() {
        return this.max;
    }

    @Override
    public String toString() {
        if (this.min == null && this.max == null) return "*";
        else {
            StringBuilder rangeString = new StringBuilder();
            if (!this.ranges.isEmpty()) {
                for (VersionRange range : this.ranges) rangeString.append(", ").append(range.toString());
            }
            if (this.min == null) return "<= " + this.max.getName() + rangeString;
            else if (this.max == null) return ">= " + this.min.getName() + rangeString;
            else if (Objects.equals(this.min, this.max)) return this.min.getName();
            else return this.min.getName() + " - " + this.max.getName() + rangeString;
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        VersionRange that = (VersionRange) object;
        return min == that.min && max == that.max && Objects.equals(ranges, that.ranges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max, ranges);
    }

    public static VersionRange fromString(final String str) {
        if ("*".equals(str)) return all();
        else if (str.contains(",")) {
            final String[] rangeParts = str.split(", ");
            VersionRange versionRange = null;

            for (String part : rangeParts) {
                if (versionRange == null) versionRange = parseSinglePart(part);
                else versionRange.add(parseSinglePart(part));
            }
            return versionRange;
        } else {
            return parseSinglePart(str);
        }
    }

    private static VersionRange parseSinglePart(final String part) {
        if (part.startsWith("<= ")) return andOlder(ProtocolVersion.getClosest(part.substring(3)));
        else if (part.startsWith(">= ")) return andNewer(ProtocolVersion.getClosest(part.substring(3)));
        else if (part.contains(" - ")) {
            final String[] rangeParts = part.split(" - ");
            final ProtocolVersion min = ProtocolVersion.getClosest(rangeParts[0]);
            final ProtocolVersion max = ProtocolVersion.getClosest(rangeParts[1]);
            return of(min, max);
        } else return single(ProtocolVersion.getClosest(part));
    }

}
