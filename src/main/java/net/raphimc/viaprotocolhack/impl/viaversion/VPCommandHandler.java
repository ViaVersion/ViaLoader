package net.raphimc.viaprotocolhack.impl.viaversion;

import com.viaversion.viaversion.commands.ViaCommandHandler;
import net.raphimc.viaprotocolhack.commands.subs.ConnectionsSubCommand;
import net.raphimc.viaprotocolhack.commands.subs.LeakDetectSubCommand;

public class VPCommandHandler extends ViaCommandHandler {

    public VPCommandHandler() {
        this.registerSubCommand(new LeakDetectSubCommand());
        this.registerSubCommand(new ConnectionsSubCommand());
    }

}
