package net.raphimc.viaprotocolhack.impl.viaversion;

import com.viaversion.viaversion.api.platform.ViaInjector;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.libs.fastutil.ints.IntLinkedOpenHashSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSortedSet;
import com.viaversion.viaversion.libs.gson.JsonObject;
import net.raphimc.viaprotocolhack.netty.ViaPipeline;

public class VPInjector implements ViaInjector {

    @Override
    public void inject() {
    }

    @Override
    public void uninject() {
    }

    @Override
    public int getServerProtocolVersion() {
        return this.getServerProtocolVersions().firstInt();
    }

    @Override
    public IntSortedSet getServerProtocolVersions() {
        final IntSortedSet versions = new IntLinkedOpenHashSet();
        for (ProtocolVersion protocol : ProtocolVersion.getProtocols()) {
            if (protocol.getOriginalVersion() >= 4) { // >=1.7.2
                versions.add(protocol.getOriginalVersion());
            }
        }
        return versions;
    }

    @Override
    public String getEncoderName() {
        return ViaPipeline.HANDLER_ENCODER_NAME;
    }

    @Override
    public String getDecoderName() {
        return ViaPipeline.HANDLER_DECODER_NAME;
    }

    @Override
    public JsonObject getDump() {
        return new JsonObject();
    }

}
