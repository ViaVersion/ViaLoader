package net.raphimc.viaprotocolhack.commands;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import com.viaversion.viaversion.api.connection.UserConnection;

import java.util.UUID;

public class UserCommandSender implements ViaCommandSender {

    private final UserConnection user;

    public UserCommandSender(final UserConnection user) {
        this.user = user;
    }

    @Override
    public boolean hasPermission(String s) {
        return true;
    }

    @Override
    public void sendMessage(String s) {
        Via.getPlatform().sendMessage(this.getUUID(), s);
    }

    @Override
    public UUID getUUID() {
        return this.user.getProtocolInfo().getUuid();
    }

    @Override
    public String getName() {
        return this.user.getProtocolInfo().getUsername();
    }

}
