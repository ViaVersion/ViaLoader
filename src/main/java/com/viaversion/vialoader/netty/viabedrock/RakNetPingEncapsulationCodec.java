/*
 * This file is part of ViaLoader - https://github.com/ViaVersion/ViaLoader
 * Copyright (C) 2020-2026 the original authors
 *                         - RK_01/RaphiMC
 *                         - FlorianMichael/EnZaXD <git@florianmichael.de>
 * Copyright (C) 2023-2026 ViaVersion and contributors
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
package com.viaversion.vialoader.netty.viabedrock;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.cloudburstmc.netty.channel.raknet.RakConstants;
import org.cloudburstmc.netty.channel.raknet.RakPing;
import org.cloudburstmc.netty.channel.raknet.RakPong;

import java.net.InetSocketAddress;
import java.util.List;

@Deprecated
public class RakNetPingEncapsulationCodec extends MessageToMessageCodec<RakPong, ByteBuf> {

    private final InetSocketAddress remoteAddress;

    public RakNetPingEncapsulationCodec(final InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        final int packetId = in.readUnsignedByte();

        if (packetId == RakConstants.ID_UNCONNECTED_PING) {
            out.add(new RakPing(in.readLong(), this.remoteAddress));
        } else {
            ctx.close();
            throw new IllegalStateException("Unexpected packet ID: " + packetId);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, RakPong in, List<Object> out) {
        if (!this.remoteAddress.equals(in.getSender())) {
            ctx.close();
            throw new IllegalStateException("Received pong from unexpected address: " + in.getSender());
        }

        final ByteBuf buf = ctx.alloc().buffer();
        buf.writeByte(RakConstants.ID_UNCONNECTED_PONG);
        buf.writeLong(in.getPingTime());
        buf.writeLong(in.getGuid());
        buf.writeBytes(RakConstants.DEFAULT_UNCONNECTED_MAGIC);
        buf.writeShort(in.getPongData().readableBytes());
        buf.writeBytes(in.getPongData());
        out.add(buf);
    }

}
