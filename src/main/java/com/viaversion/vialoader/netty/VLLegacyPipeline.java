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
import net.raphimc.viabedrock.netty.BatchLengthCodec;
import net.raphimc.viabedrock.netty.PacketCodec;
import net.raphimc.vialegacy.netty.PreNettyLengthPrepender;
import net.raphimc.vialegacy.netty.PreNettyLengthRemover;

public abstract class VLLegacyPipeline extends ChannelInboundHandlerAdapter {

    public static final String VIA_DECODER_NAME = "via-decoder";
    public static final String VIA_ENCODER_NAME = "via-encoder";

    public static final String VIALEGACY_PRE_NETTY_LENGTH_PREPENDER_NAME = "vialegacy-pre-netty-length-prepender";
    public static final String VIALEGACY_PRE_NETTY_LENGTH_REMOVER_NAME = "vialegacy-pre-netty-length-remover";

    public static final String VIABEDROCK_DISCONNECT_HANDLER_NAME = "viabedrock-disconnect-handler";
    public static final String VIABEDROCK_RAKNET_MESSAGE_CODEC_NAME = "viabedrock-raknet-message-codec";
    public static final String VIABEDROCK_PACKET_CODEC_NAME = "viabedrock-packet-codec";

    protected final UserConnection connection;
    protected final ProtocolVersion version;

    public VLLegacyPipeline(final UserConnection connection) {
        this(connection, Via.getManager().getProviders().get(VersionProvider.class).getServerProtocol(connection));
    }

    public VLLegacyPipeline(final UserConnection connection, final ProtocolVersion version) {
        this.connection = connection;
        this.version = version;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        ctx.pipeline().addBefore(this.packetDecoderName(), VIA_DECODER_NAME, this.createViaDecoder());
        ctx.pipeline().addBefore(this.packetEncoderName(), VIA_ENCODER_NAME, this.createViaEncoder());

        if (this.connection.isClientSide()) {
            final ProtocolVersion r1_6_4 = ProtocolVersion.getProtocol(VersionType.RELEASE_INITIAL, 78);
            if (ProtocolVersion.isRegistered(r1_6_4.getVersionType(), r1_6_4.getOriginalVersion()) && this.version.olderThanOrEqualTo(r1_6_4)) {
                ctx.pipeline().addBefore(this.lengthSplitterName(), VIALEGACY_PRE_NETTY_LENGTH_PREPENDER_NAME, this.createViaLegacyPreNettyLengthPrepender());
                ctx.pipeline().addBefore(this.lengthPrependerName(), VIALEGACY_PRE_NETTY_LENGTH_REMOVER_NAME, this.createViaLegacyPreNettyLengthRemover());
            } else if (this.version.getName().startsWith("Bedrock")) {
                ctx.pipeline().addBefore(this.lengthSplitterName(), VIABEDROCK_DISCONNECT_HANDLER_NAME, this.createViaBedrockDisconnectHandler());
                ctx.pipeline().addBefore(this.lengthSplitterName(), VIABEDROCK_RAKNET_MESSAGE_CODEC_NAME, this.createViaBedrockRakNetMessageCodec());
                this.replaceLengthSplitter(ctx, this.createViaBedrockBatchLengthCodec());
                ctx.pipeline().remove(this.lengthPrependerName());
                ctx.pipeline().addBefore(VIA_DECODER_NAME, VIABEDROCK_PACKET_CODEC_NAME, this.createViaBedrockPacketCodec());
            }
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (CompressionReorderEvent.INSTANCE.equals(evt)) {
            final int decoderIndex = ctx.pipeline().names().indexOf(this.decompressName());
            if (decoderIndex == -1) return;

            if (decoderIndex > ctx.pipeline().names().indexOf(VIA_DECODER_NAME)) {
                final ChannelHandler decoder = ctx.pipeline().get(VIA_DECODER_NAME);
                final ChannelHandler encoder = ctx.pipeline().get(VIA_ENCODER_NAME);

                ctx.pipeline().remove(decoder);
                ctx.pipeline().remove(encoder);

                ctx.pipeline().addAfter(this.decompressName(), VIA_DECODER_NAME, decoder);
                ctx.pipeline().addAfter(this.compressName(), VIA_ENCODER_NAME, encoder);
            }
        }

        super.userEventTriggered(ctx, evt);
    }

    protected ChannelHandler createViaDecoder() {
        return new ViaDecoder(this.connection);
    }

    protected ChannelHandler createViaEncoder() {
        return new ViaEncoder(this.connection);
    }

    protected ChannelHandler createViaLegacyPreNettyLengthPrepender() {
        return new PreNettyLengthPrepender(this.connection);
    }

    protected ChannelHandler createViaLegacyPreNettyLengthRemover() {
        return new PreNettyLengthRemover(this.connection);
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

    protected void replaceLengthSplitter(final ChannelHandlerContext ctx, final ChannelHandler handler) {
        ctx.pipeline().replace(this.lengthSplitterName(), this.lengthSplitterName(), handler);
    }

    protected abstract String decompressName();

    protected abstract String compressName();

    protected abstract String packetDecoderName();

    protected abstract String packetEncoderName();

    protected abstract String lengthSplitterName();

    protected abstract String lengthPrependerName();

}
