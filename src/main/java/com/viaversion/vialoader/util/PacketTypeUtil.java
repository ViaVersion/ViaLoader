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

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.ProtocolInfo;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.ProtocolPathEntry;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.packet.provider.PacketTypeMap;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

import java.util.List;

public class PacketTypeUtil {

    public static ServerboundPacketType getServerboundPacketType(final String packetName, final UserConnection connection) {
        return getServerboundPacketType(State.PLAY, packetName, connection);
    }

    public static ServerboundPacketType getServerboundPacketType(final State state, final String packetName, final UserConnection connection) {
        final ProtocolInfo protocolInfo = connection.getProtocolInfo();
        return getServerboundPacketType(state, packetName, protocolInfo.protocolVersion(), protocolInfo.serverProtocolVersion());
    }

    /**
     * Returns the {@link ServerboundPacketType} for the given packet name, state, client version and server version. Note that this
     * function does not support packet mappings and will simply return null if the packet is not found in the server version.
     *
     * @param state         The {@link State} of the packet
     * @param packetName    The name of the packet
     * @param clientVersion The  client version
     * @param serverVersion The server version
     * @return The {@link ServerboundPacketType} for the given packet name, state, client version and server version
     */
    public static ServerboundPacketType getServerboundPacketType(final State state, final String packetName, final ProtocolVersion clientVersion, final ProtocolVersion serverVersion) {
        final List<ProtocolPathEntry> protocols = Via.getManager().getProtocolManager().getProtocolPath(clientVersion, serverVersion);
        if (protocols != null) {
            final ProtocolPathEntry last = protocols.get(protocols.size() - 1);
            final PacketTypeMap<? extends ServerboundPacketType> map = last.protocol().getPacketTypesProvider().mappedServerboundPacketTypes().get(state);
            if (map != null) {
                return map.typeByName(packetName);
            }
        }
        return null;
    }

    public static ClientboundPacketType getClientboundPacketType(final String packetName, final UserConnection connection) {
        return getClientboundPacketType(State.PLAY, packetName, connection);
    }

    public static ClientboundPacketType getClientboundPacketType(final State state, final String packetName, final UserConnection connection) {
        final ProtocolInfo protocolInfo = connection.getProtocolInfo();
        return getClientboundPacketType(state, packetName, protocolInfo.protocolVersion(), protocolInfo.serverProtocolVersion());
    }

    /**
     * Returns the {@link ClientboundPacketType} for the given packet name, state, client version and server version. Note that this
     * function does not support packet mappings and will simply return null if the packet is not found in the server version.
     *
     * @param state         The {@link State} of the packet
     * @param packetName    The name of the packet
     * @param clientVersion The client version
     * @param serverVersion The server version
     * @return The {@link ClientboundPacketType} for the given packet name, state, client version and server version
     */
    public static ClientboundPacketType getClientboundPacketType(final State state, final String packetName, final ProtocolVersion clientVersion, final ProtocolVersion serverVersion) {
        final List<ProtocolPathEntry> protocols = Via.getManager().getProtocolManager().getProtocolPath(clientVersion, serverVersion);
        if (protocols != null) {
            final ProtocolPathEntry last = protocols.get(protocols.size() - 1);
            final PacketTypeMap<? extends ClientboundPacketType> map = last.protocol().getPacketTypesProvider().mappedClientboundPacketTypes().get(state);
            if (map != null) {
                return map.typeByName(packetName);
            }
        }
        return null;
    }

}
