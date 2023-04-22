/*
 * This file is part of ViaProtocolHack - https://github.com/RaphiMC/ViaProtocolHack
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
package net.raphimc.viaprotocolhack.impl.providers;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_8.ServerboundPackets1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.MovementTracker;

public class VPHMovementTransmitterProvider extends MovementTransmitterProvider {

    @Override
    public Object getFlyingPacket() {
        return null;
    }

    @Override
    public Object getGroundPacket() {
        return null;
    }

    @Override
    public void sendPlayer(UserConnection user) {
        if (user.getProtocolInfo().getState() != State.PLAY) {
            return;
        }
        if (user.getEntityTracker(Protocol1_9To1_8.class).clientEntityId() == -1) {
            return;
        }

        final MovementTracker movementTracker = user.get(MovementTracker.class);
        movementTracker.incrementIdlePacket();
        final boolean onGround = movementTracker.isGround();

        user.getChannel().eventLoop().submit(() -> {
            try {
                final PacketWrapper playerMovement = PacketWrapper.create(ServerboundPackets1_8.PLAYER_MOVEMENT, user);
                playerMovement.write(Type.BOOLEAN, onGround); // on ground
                playerMovement.sendToServer(Protocol1_9To1_8.class);
            } catch (Throwable ignored) {
            }
        });
    }

}
