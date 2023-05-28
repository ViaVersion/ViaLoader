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
package net.raphimc.vialoader.commands.subs;

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
