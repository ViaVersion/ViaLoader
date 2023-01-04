package net.raphimc.viaprotocolhack.impl.viaversion;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.platform.ViaPlatformLoader;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;
import net.raphimc.viaprotocolhack.impl.providers.VPMovementTransmitterProvider;

public class VPLoader implements ViaPlatformLoader {

    @Override
    public void load() {
        Via.getManager().getProviders().use(MovementTransmitterProvider.class, new VPMovementTransmitterProvider());
    }

    @Override
    public void unload() {
    }

}
