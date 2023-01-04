package net.raphimc.viaprotocolhack.commands.subs;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import com.viaversion.viaversion.api.command.ViaSubCommand;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

import java.util.Set;

public class ConnectionsSubCommand extends ViaSubCommand {

    @Override
    public String name() {
        return "connections";
    }

    @Override
    public String description() {
        return "Shows a list of all connections";
    }

    @Override
    public boolean execute(ViaCommandSender viaCommandSender, String[] strings) {
        final Set<UserConnection> users = Via.getManager().getConnectionManager().getConnections();
        sendMessage(viaCommandSender, "&a--- Connected users ---");
        for (UserConnection user : users) {
            sendMessage(viaCommandSender, "&7[&6" + user.getProtocolInfo().getUsername() + "&7] UUID: &5" + user.getProtocolInfo().getUuid() + " &7Client-Protocol: &5" + ProtocolVersion.getProtocol(user.getProtocolInfo().getProtocolVersion()).getName() + " &7Server-Protocol: &5" + ProtocolVersion.getProtocol(user.getProtocolInfo().getServerProtocolVersion()).getName());
        }
        return true;
    }

}
