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
package com.viaversion.vialoader.netty;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.exception.CancelCodecException;
import com.viaversion.viaversion.exception.CancelEncoderException;
import com.viaversion.viaversion.util.PipelineUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class ViaEncoder extends MessageToMessageEncoder<ByteBuf> {

    protected final UserConnection user;

    public ViaEncoder(final UserConnection user) {
        this.user = user;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (!this.user.checkOutgoingPacket()) throw CancelEncoderException.generate(null);
        if (!this.user.shouldTransformPacket()) {
            out.add(in.retain());
            return;
        }

        final ByteBuf transformedBuf = ctx.alloc().buffer().writeBytes(in);
        try {
            this.user.transformOutgoing(transformedBuf, CancelEncoderException::generate);
            out.add(transformedBuf.retain());
        } finally {
            transformedBuf.release();
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        try {
            super.write(ctx, msg, promise);
        } catch (Throwable e) {
            if (!PipelineUtil.containsCause(e, CancelCodecException.class)) {
                throw e;
            } else {
                promise.setSuccess();
            }
        }
    }

    @Override
    public boolean isSharable() {
        // Netty doesn't allow codecs to be shared, but we need it to be shared because of the pipeline reordering.
        // The check if it is sharable is done in the constructor and can be bypassed by returning false during that check.
        return this.user != null;
    }
}
