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
package com.viaversion.vialoader.netty;

import com.viaversion.vialoader.netty.viabedrock.DisconnectHandler;
import com.viaversion.vialoader.netty.viabedrock.RakNetMessageCodec;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.protocol.version.VersionProvider;
import com.viaversion.viaversion.api.protocol.version.VersionType;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import net.raphimc.viabedrock.netty.BatchLengthCodec;
import net.raphimc.viabedrock.netty.PacketCodec;
import net.raphimc.vialegacy.netty.PreNettyLengthCodec;

public abstract class VLPipeline extends ChannelInboundHandlerAdapter {

    public static final String VIA_CODEC_NAME = "via-codec";

    public static final String VIALEGACY_PRE_NETTY_LENGTH_CODEC_NAME = "vialegacy-pre-netty-length-codec";

    public static final String VIABEDROCK_DISCONNECT_HANDLER_NAME = "viabedrock-disconnect-handler";
    public static final String VIABEDROCK_RAKNET_MESSAGE_CODEC_NAME = "viabedrock-raknet-message-codec";
    public static final String VIABEDROCK_PACKET_CODEC_NAME = "viabedrock-packet-codec";

    protected final UserConnection connection;
    protected final ProtocolVersion version;

    public VLPipeline(final UserConnection connection) {
        this(connection, Via.getManager().getProviders().get(VersionProvider.class).getServerProtocol(connection));
    }

    public VLPipeline(final UserConnection connection, final ProtocolVersion version) {
        this.connection = connection;
        this.version = version;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        ctx.pipeline().addBefore(this.packetCodecName(), VIA_CODEC_NAME, this.createViaCodec());

        if (this.connection.isClientSide()) {
            final ProtocolVersion r1_6_4 = ProtocolVersion.getProtocol(VersionType.RELEASE_INITIAL, 78);
            if (ProtocolVersion.isRegistered(r1_6_4.getVersionType(), r1_6_4.getOriginalVersion()) && this.version.olderThanOrEqualTo(r1_6_4)) {
                ctx.pipeline().addBefore(this.lengthCodecName(), VIALEGACY_PRE_NETTY_LENGTH_CODEC_NAME, this.createViaLegacyPreNettyLengthCodec());
            } else if (this.version.getName().startsWith("Bedrock")) {
                ctx.pipeline().addBefore(this.lengthCodecName(), VIABEDROCK_DISCONNECT_HANDLER_NAME, this.createViaBedrockDisconnectHandler());
                ctx.pipeline().addBefore(this.lengthCodecName(), VIABEDROCK_RAKNET_MESSAGE_CODEC_NAME, this.createViaBedrockRakNetMessageCodec());
                this.replaceLengthCodec(ctx, this.createViaBedrockBatchLengthCodec());
                ctx.pipeline().addBefore(VIA_CODEC_NAME, VIABEDROCK_PACKET_CODEC_NAME, this.createViaBedrockPacketCodec());
            }
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (CompressionReorderEvent.INSTANCE.equals(evt)) {
            final ChannelPipeline pipeline = ctx.pipeline();

            if (pipeline.names().indexOf(this.compressionCodecName()) > pipeline.names().indexOf(VIA_CODEC_NAME)) {
                pipeline.addAfter(this.compressionCodecName(), VIA_CODEC_NAME, pipeline.remove(VIA_CODEC_NAME));
            }
        }

        super.userEventTriggered(ctx, evt);
    }

    protected ChannelHandler createViaCodec() {
        return new ViaCodec(this.connection);
    }

    protected ChannelHandler createViaLegacyPreNettyLengthCodec() {
        return new PreNettyLengthCodec(this.connection);
    }

    protected ChannelHandler createViaBedrockDisconnectHandler() {
        return new DisconnectHandler();
    }

    protected ChannelHandler createViaBedrockRakNetMessageCodec() {
        return new RakNetMessageCodec();
    }

    protected ChannelHandler createViaBedrockBatchLengthCodec() {
        return new BatchLengthCodec();
    }

    protected ChannelHandler createViaBedrockPacketCodec() {
        return new PacketCodec();
    }

    protected void replaceLengthCodec(final ChannelHandlerContext ctx, final ChannelHandler handler) {
        ctx.pipeline().replace(this.lengthCodecName(), this.lengthCodecName(), handler);
    }

    protected abstract String compressionCodecName();

    protected abstract String packetCodecName();

    protected abstract String lengthCodecName();

}
