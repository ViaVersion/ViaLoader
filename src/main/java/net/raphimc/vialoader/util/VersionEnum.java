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

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

import java.util.*;

public enum VersionEnum {

    c0_0_15a_1(getViaLegacyProtocol("c0_0_15a_1")),
    c0_0_16a_02(getViaLegacyProtocol("c0_0_16a_02")),
    c0_0_18a_02(getViaLegacyProtocol("c0_0_18a_02")),
    c0_0_19a_06(getViaLegacyProtocol("c0_0_19a_06")),
    c0_0_20ac0_27(getViaLegacyProtocol("c0_0_20ac0_27")),
    c0_30cpe(getViaLegacyProtocol("c0_30cpe")),
    c0_28toc0_30(getViaLegacyProtocol("c0_28toc0_30")),
    a1_0_15(getViaLegacyProtocol("a1_0_15")),
    a1_0_16toa1_0_16_2(getViaLegacyProtocol("a1_0_16toa1_0_16_2")),
    a1_0_17toa1_0_17_4(getViaLegacyProtocol("a1_0_17toa1_0_17_4")),
    a1_1_0toa1_1_2_1(getViaLegacyProtocol("a1_1_0toa1_1_2_1")),
    a1_2_0toa1_2_1_1(getViaLegacyProtocol("a1_2_0toa1_2_1_1")),
    a1_2_2(getViaLegacyProtocol("a1_2_2")),
    a1_2_3toa1_2_3_4(getViaLegacyProtocol("a1_2_3toa1_2_3_4")),
    a1_2_3_5toa1_2_6(getViaLegacyProtocol("a1_2_3_5toa1_2_6")),
    b1_0tob1_1_1(getViaLegacyProtocol("b1_0tob1_1_1")),
    b1_1_2(getViaLegacyProtocol("b1_1_2")),
    b1_2_0tob1_2_2(getViaLegacyProtocol("b1_2_0tob1_2_2")),
    b1_3tob1_3_1(getViaLegacyProtocol("b1_3tob1_3_1")),
    b1_4tob1_4_1(getViaLegacyProtocol("b1_4tob1_4_1")),
    b1_5tob1_5_2(getViaLegacyProtocol("b1_5tob1_5_2")),
    b1_6tob1_6_6(getViaLegacyProtocol("b1_6tob1_6_6")),
    b1_7tob1_7_3(getViaLegacyProtocol("b1_7tob1_7_3")),
    b1_8tob1_8_1(getViaLegacyProtocol("b1_8tob1_8_1")),
    r1_0_0tor1_0_1(getViaLegacyProtocol("r1_0_0tor1_0_1")),
    r1_1(getViaLegacyProtocol("r1_1")),
    r1_2_1tor1_2_3(getViaLegacyProtocol("r1_2_1tor1_2_3")),
    r1_2_4tor1_2_5(getViaLegacyProtocol("r1_2_4tor1_2_5")),
    r1_3_1tor1_3_2(getViaLegacyProtocol("r1_3_1tor1_3_2")),
    r1_4_2(getViaLegacyProtocol("r1_4_2")),
    r1_4_4tor1_4_5(getViaLegacyProtocol("r1_4_4tor1_4_5")),
    r1_4_6tor1_4_7(getViaLegacyProtocol("r1_4_6tor1_4_7")),
    r1_5tor1_5_1(getViaLegacyProtocol("r1_5tor1_5_1")),
    r1_5_2(getViaLegacyProtocol("r1_5_2")),
    r1_6_1(getViaLegacyProtocol("r1_6_1")),
    r1_6_2(getViaLegacyProtocol("r1_6_2")),
    r1_6_4(getViaLegacyProtocol("r1_6_4")),
    r1_7_2tor1_7_5(ProtocolVersion.v1_7_1),
    r1_7_6tor1_7_10(ProtocolVersion.v1_7_6),
    r1_8(ProtocolVersion.v1_8),
    r1_9(ProtocolVersion.v1_9),
    r1_9_1(ProtocolVersion.v1_9_1),
    r1_9_2(ProtocolVersion.v1_9_2),
    r1_9_3tor1_9_4(ProtocolVersion.v1_9_3),
    r1_10(ProtocolVersion.v1_10),
    r1_11(ProtocolVersion.v1_11),
    r1_11_1to1_11_2(ProtocolVersion.v1_11_1),
    r1_12(ProtocolVersion.v1_12),
    r1_12_1(ProtocolVersion.v1_12_1),
    r1_12_2(ProtocolVersion.v1_12_2),
    r1_13(ProtocolVersion.v1_13),
    r1_13_1(ProtocolVersion.v1_13_1),
    r1_13_2(ProtocolVersion.v1_13_2),
    s3d_shareware(getViaAprilFoolsProtocol("s3d_shareware")),
    r1_14(ProtocolVersion.v1_14),
    r1_14_1(ProtocolVersion.v1_14_1),
    r1_14_2(ProtocolVersion.v1_14_2),
    r1_14_3(ProtocolVersion.v1_14_3),
    r1_14_4(ProtocolVersion.v1_14_4),
    r1_15(ProtocolVersion.v1_15),
    r1_15_1(ProtocolVersion.v1_15_1),
    r1_15_2(ProtocolVersion.v1_15_2),
    s20w14infinite(getViaAprilFoolsProtocol("s20w14infinite")),
    r1_16(ProtocolVersion.v1_16),
    r1_16_1(ProtocolVersion.v1_16_1),
    sCombatTest8c(getViaAprilFoolsProtocol("sCombatTest8c")),
    r1_16_2(ProtocolVersion.v1_16_2),
    r1_16_3(ProtocolVersion.v1_16_3),
    r1_16_4tor1_16_5(ProtocolVersion.v1_16_4),
    r1_17(ProtocolVersion.v1_17),
    r1_17_1(ProtocolVersion.v1_17_1),
    r1_18tor1_18_1(ProtocolVersion.v1_18),
    r1_18_2(ProtocolVersion.v1_18_2),
    r1_19(ProtocolVersion.v1_19),
    r1_19_1tor1_19_2(ProtocolVersion.v1_19_1),
    r1_19_3(ProtocolVersion.v1_19_3),
    r1_19_4(ProtocolVersion.v1_19_4),
    bedrockLatest(getViaBedrockProtocol("bedrockLatest")),
    r1_20tor1_20_1(ProtocolVersion.v1_20),

    //
    UNKNOWN(ProtocolVersion.unknown), // Not in Registry
    ;


    private static final Map<ProtocolVersion, VersionEnum> VERSION_REGISTRY = new HashMap<>();
    public static final List<VersionEnum> SORTED_VERSIONS = new ArrayList<>();
    public static final List<VersionEnum> OFFICIAL_SUPPORTED_PROTOCOLS = new ArrayList<>();

    static {
        for (VersionEnum version : VersionEnum.values()) {
            if (version == UNKNOWN) continue;
            if (!version.protocolVersion.isKnown()) continue;
            VERSION_REGISTRY.put(version.protocolVersion, version);
        }
        for (VersionEnum version : VersionEnum.getAllVersions()) {
            if (version.isNewerThan(VersionEnum.r1_6_4) && version != VersionEnum.s3d_shareware && version != VersionEnum.s20w14infinite && version != VersionEnum.sCombatTest8c && version != VersionEnum.bedrockLatest) {
                OFFICIAL_SUPPORTED_PROTOCOLS.add(version);
            }
        }

        SORTED_VERSIONS.add(r1_20tor1_20_1);
        SORTED_VERSIONS.add(r1_19_4);
        SORTED_VERSIONS.add(r1_19_3);
        SORTED_VERSIONS.add(r1_19_1tor1_19_2);
        SORTED_VERSIONS.add(r1_19);
        SORTED_VERSIONS.add(r1_18_2);
        SORTED_VERSIONS.add(r1_18tor1_18_1);
        SORTED_VERSIONS.add(r1_17_1);
        SORTED_VERSIONS.add(r1_17);
        SORTED_VERSIONS.add(r1_16_4tor1_16_5);
        SORTED_VERSIONS.add(r1_16_3);
        SORTED_VERSIONS.add(r1_16_2);
        SORTED_VERSIONS.add(r1_16_1);
        SORTED_VERSIONS.add(r1_16);
        SORTED_VERSIONS.add(r1_15_2);
        SORTED_VERSIONS.add(r1_15_1);
        SORTED_VERSIONS.add(r1_15);
        SORTED_VERSIONS.add(r1_14_4);
        SORTED_VERSIONS.add(r1_14_3);
        SORTED_VERSIONS.add(r1_14_2);
        SORTED_VERSIONS.add(r1_14_1);
        SORTED_VERSIONS.add(r1_14);
        SORTED_VERSIONS.add(r1_13_2);
        SORTED_VERSIONS.add(r1_13_1);
        SORTED_VERSIONS.add(r1_13);
        SORTED_VERSIONS.add(r1_12_2);
        SORTED_VERSIONS.add(r1_12_1);
        SORTED_VERSIONS.add(r1_12);
        SORTED_VERSIONS.add(r1_11_1to1_11_2);
        SORTED_VERSIONS.add(r1_11);
        SORTED_VERSIONS.add(r1_10);
        SORTED_VERSIONS.add(r1_9_3tor1_9_4);
        SORTED_VERSIONS.add(r1_9_2);
        SORTED_VERSIONS.add(r1_9_1);
        SORTED_VERSIONS.add(r1_9);
        SORTED_VERSIONS.add(r1_8);
        SORTED_VERSIONS.add(r1_7_6tor1_7_10);
        SORTED_VERSIONS.add(r1_7_2tor1_7_5);
        SORTED_VERSIONS.add(r1_6_4);
        SORTED_VERSIONS.add(r1_6_2);
        SORTED_VERSIONS.add(r1_6_1);
        SORTED_VERSIONS.add(r1_5_2);
        SORTED_VERSIONS.add(r1_5tor1_5_1);
        SORTED_VERSIONS.add(r1_4_6tor1_4_7);
        SORTED_VERSIONS.add(r1_4_4tor1_4_5);
        SORTED_VERSIONS.add(r1_4_2);
        SORTED_VERSIONS.add(r1_3_1tor1_3_2);
        SORTED_VERSIONS.add(r1_2_4tor1_2_5);
        SORTED_VERSIONS.add(r1_2_1tor1_2_3);
        SORTED_VERSIONS.add(r1_1);
        SORTED_VERSIONS.add(r1_0_0tor1_0_1);
        SORTED_VERSIONS.add(b1_8tob1_8_1);
        SORTED_VERSIONS.add(b1_7tob1_7_3);
        SORTED_VERSIONS.add(b1_6tob1_6_6);
        SORTED_VERSIONS.add(b1_5tob1_5_2);
        SORTED_VERSIONS.add(b1_4tob1_4_1);
        SORTED_VERSIONS.add(b1_3tob1_3_1);
        SORTED_VERSIONS.add(b1_2_0tob1_2_2);
        SORTED_VERSIONS.add(b1_1_2);
        SORTED_VERSIONS.add(b1_0tob1_1_1);
        SORTED_VERSIONS.add(a1_2_3_5toa1_2_6);
        SORTED_VERSIONS.add(a1_2_3toa1_2_3_4);
        SORTED_VERSIONS.add(a1_2_2);
        SORTED_VERSIONS.add(a1_2_0toa1_2_1_1);
        SORTED_VERSIONS.add(a1_1_0toa1_1_2_1);
        SORTED_VERSIONS.add(a1_0_17toa1_0_17_4);
        SORTED_VERSIONS.add(a1_0_16toa1_0_16_2);
        SORTED_VERSIONS.add(a1_0_15);
        SORTED_VERSIONS.add(c0_28toc0_30);
        SORTED_VERSIONS.add(c0_0_20ac0_27);
        SORTED_VERSIONS.add(c0_0_19a_06);
        SORTED_VERSIONS.add(c0_0_18a_02);
        SORTED_VERSIONS.add(c0_0_16a_02);
        SORTED_VERSIONS.add(c0_0_15a_1);
        SORTED_VERSIONS.add(bedrockLatest);
        SORTED_VERSIONS.add(sCombatTest8c);
        SORTED_VERSIONS.add(s20w14infinite);
        SORTED_VERSIONS.add(s3d_shareware);
        SORTED_VERSIONS.add(c0_30cpe);

        SORTED_VERSIONS.removeIf(v -> !v.protocolVersion.isKnown());
    }

    public static VersionEnum fromProtocolVersion(final ProtocolVersion protocolVersion) {
        if (!protocolVersion.isKnown()) return UNKNOWN;
        return VERSION_REGISTRY.getOrDefault(protocolVersion, UNKNOWN);
    }

    public static VersionEnum fromProtocolId(final int protocolId) {
        return fromProtocolVersion(ProtocolVersion.getProtocol(protocolId));
    }

    public static VersionEnum fromUserConnection(final UserConnection userConnection) {
        return fromUserConnection(userConnection, true);
    }

    public static VersionEnum fromUserConnection(final UserConnection userConnection, final boolean serverProtocol) {
        return fromProtocolId(serverProtocol ? userConnection.getProtocolInfo().getServerProtocolVersion() : userConnection.getProtocolInfo().getProtocolVersion());
    }

    public static Collection<VersionEnum> getAllVersions() {
        return VERSION_REGISTRY.values();
    }

    private static ProtocolVersion getViaLegacyProtocol(final String name) {
        try {
            return (ProtocolVersion) Class.forName("net.raphimc.vialegacy.api.LegacyProtocolVersion").getField(name).get(null);
        } catch (Throwable e) {
            return ProtocolVersion.unknown;
        }
    }

    private static ProtocolVersion getViaAprilFoolsProtocol(final String name) {
        try {
            return (ProtocolVersion) Class.forName("net.raphimc.viaaprilfools.api.AprilFoolsProtocolVersion").getField(name).get(null);
        } catch (Throwable e) {
            return ProtocolVersion.unknown;
        }
    }

    private static ProtocolVersion getViaBedrockProtocol(final String name) {
        try {
            return (ProtocolVersion) Class.forName("net.raphimc.viabedrock.api.BedrockProtocolVersion").getField(name).get(null);
        } catch (Throwable e) {
            return ProtocolVersion.unknown;
        }
    }


    private final ProtocolVersion protocolVersion;

    VersionEnum(final ProtocolVersion protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public ProtocolVersion getProtocol() {
        return this.protocolVersion;
    }

    public String getName() {
        return this.protocolVersion.getName();
    }

    public int getVersion() {
        return this.protocolVersion.getVersion();
    }

    public int getOriginalVersion() {
        return this.protocolVersion.getOriginalVersion();
    }

    public boolean isOlderThan(final VersionEnum other) {
        return this.ordinal() < other.ordinal();
    }

    public boolean isOlderThanOrEqualTo(final VersionEnum other) {
        return this.ordinal() <= other.ordinal();
    }

    public boolean isNewerThan(final VersionEnum other) {
        return this.ordinal() > other.ordinal();
    }

    public boolean isNewerThanOrEqualTo(final VersionEnum other) {
        return this.ordinal() >= other.ordinal();
    }

    public boolean isBetweenInclusive(final VersionEnum min, final VersionEnum max) {
        return this.isNewerThanOrEqualTo(min) && this.isOlderThanOrEqualTo(max);
    }

    public boolean isBetweenExclusive(final VersionEnum min, final VersionEnum max) {
        return this.isNewerThan(min) && this.isOlderThan(max);
    }

}
