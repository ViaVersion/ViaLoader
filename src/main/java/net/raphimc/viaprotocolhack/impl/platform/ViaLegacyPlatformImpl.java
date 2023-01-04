package net.raphimc.viaprotocolhack.impl.platform;

import com.viaversion.viaversion.api.Via;
import net.raphimc.vialegacy.platform.ViaLegacyPlatform;
import net.raphimc.viaprotocolhack.util.JLoggerToSLF4J;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.logging.Logger;

public class ViaLegacyPlatformImpl implements ViaLegacyPlatform {

    private static final Logger LOGGER = new JLoggerToSLF4J(LoggerFactory.getLogger("ViaLegacy"));

    public ViaLegacyPlatformImpl() {
        this.init(this.getDataFolder());
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    public File getDataFolder() {
        return Via.getPlatform().getDataFolder();
    }

}
