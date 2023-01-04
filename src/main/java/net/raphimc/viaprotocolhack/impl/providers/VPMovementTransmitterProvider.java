package net.raphimc.viaprotocolhack.impl.providers;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_8.ServerboundPackets1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.MovementTracker;

public class VPMovementTransmitterProvider extends MovementTransmitterProvider {

    @Override
    public Object getFlyingPacket() {
        return null;
    }

    @Override
    public Object getGroundPacket() {
        return null;
    }

    @Override
    public void sendPlayer(UserConnection userConnection) {
        if (userConnection.getProtocolInfo().getState() == State.PLAY) {
            if (userConnection.getEntityTracker(Protocol1_9To1_8.class).clientEntityId() == -1) return;

            final MovementTracker tracker = userConnection.get(MovementTracker.class);
            tracker.incrementIdlePacket();

            final PacketWrapper wrapper = PacketWrapper.create(ServerboundPackets1_8.PLAYER_MOVEMENT, userConnection);
            wrapper.write(Type.BOOLEAN, tracker.isGround());

            userConnection.getChannel().eventLoop().submit(() -> {
                try {
                    wrapper.sendToServer(Protocol1_9To1_8.class);
                } catch (Throwable ignored) {
                }
            });
        }
    }

}
