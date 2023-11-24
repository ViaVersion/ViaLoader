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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VersionRange {

    private final VersionEnum min;
    private final VersionEnum max;
    private final List<VersionRange> ranges;

    private VersionRange(final VersionEnum min, final VersionEnum max) {
        this.min = min;
        this.max = max;
        this.ranges = new ArrayList<>();
    }

    public static VersionRange andNewer(final VersionEnum version) {
        return new VersionRange(version, null);
    }

    public static VersionRange single(final VersionEnum version) {
        return new VersionRange(version, version);
    }

    public static VersionRange andOlder(final VersionEnum version) {
        return new VersionRange(null, version);
    }

    public static VersionRange of(final VersionEnum min, final VersionEnum max) {
        return new VersionRange(min, max);
    }

    public static VersionRange all() {
        return new VersionRange(null, null);
    }

    public VersionRange add(final VersionRange range) {
        this.ranges.add(range);
        return this;
    }

    public boolean contains(final VersionEnum version) {
        if (this.ranges.stream().anyMatch(range -> range.contains(version))) return true;
        if (this.min == null && this.max == null) return true;
        else if (this.min == null) return version.isOlderThanOrEqualTo(this.max);
        else if (this.max == null) return version.isNewerThanOrEqualTo(this.min);
        return version.ordinal() >= this.min.ordinal() && version.ordinal() <= this.max.ordinal();
    }

    public VersionEnum getMin() {
        return this.min;
    }

    public VersionEnum getMax() {
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

    public static VersionRange fromString(String str) {
        if ("*".equals(str)) return all();
        else if (str.contains(",")) {
            String[] rangeParts = str.split(", ");
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

    private static VersionRange parseSinglePart(String part) {
        if (part.startsWith("<= ")) return andOlder(VersionEnum.fromProtocolName(part.substring(3)));
        else if (part.startsWith(">= ")) return andNewer(VersionEnum.fromProtocolName(part.substring(3)));
        else if (part.contains(" - ")) {
            String[] rangeParts = part.split(" - ");
            VersionEnum min = VersionEnum.fromProtocolName(rangeParts[0]);
            VersionEnum max = VersionEnum.fromProtocolName(rangeParts[1]);
            return of(min, max);
        } else return single(VersionEnum.fromProtocolName(part));
    }

}
