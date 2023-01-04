package net.raphimc.viaprotocolhack;

import com.viaversion.viabackwards.api.ViaBackwardsPlatform;
import com.viaversion.viaversion.ViaManagerImpl;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import com.viaversion.viaversion.api.platform.*;
import com.viaversion.viaversion.commands.ViaCommandHandler;
import com.viaversion.viaversion.protocol.ProtocolManagerImpl;
import de.gerrygames.viarewind.api.ViaRewindPlatform;
import net.raphimc.vialegacy.platform.ViaLegacyPlatform;
import net.raphimc.viaprotocolhack.impl.platform.ViaVersionPlatformImpl;
import net.raphimc.viaprotocolhack.impl.viaversion.*;
import net.raphimc.viaprotocolhack.util.JLoggerToSLF4J;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;
import java.util.logging.Logger;

public class ViaProtocolHack {

    public static final String VERSION = "${version}";

    private static final Logger LOGGER = new JLoggerToSLF4J(LoggerFactory.getLogger("ViaProtocolHack"));

    @SuppressWarnings("ReassignedVariable")
    public static void init(ViaPlatform<?> platform, ViaPlatformLoader loader, ViaInjector injector, ViaCommandHandler commandHandler, Supplier<ViaBackwardsPlatform> viaBackwardsPlatformSupplier, Supplier<ViaRewindPlatform> viaRewindPlatformSupplier, Supplier<ViaLegacyPlatform> viaLegacyPlatformSupplier) {
        if (platform == null) platform = new ViaVersionPlatformImpl(null);
        if (loader == null) loader = new VPLoader();
        if (injector == null) injector = new VPInjector();
        if (commandHandler == null) commandHandler = new VPCommandHandler();

        Via.init(ViaManagerImpl.builder()
                .platform(platform)
                .loader(loader)
                .injector(injector)
                .commandHandler(commandHandler)
                .build());
        MappingDataLoader.enableMappingsCache();

        final Supplier<ViaBackwardsPlatform> finalViaBackwardsPlatformSupplier = viaBackwardsPlatformSupplier;
        final Supplier<ViaRewindPlatform> finalViaRewindPlatformSupplier = viaRewindPlatformSupplier;
        final Supplier<ViaLegacyPlatform> finalViaLegacyPlatformSupplier = viaLegacyPlatformSupplier;
        Via.getManager().addEnableListener(() -> {
            if (finalViaBackwardsPlatformSupplier != null) {
                try {
                    finalViaBackwardsPlatformSupplier.get();
                } catch (Throwable e) {
                    LOGGER.severe("ViaBackwards failed to load: " + e.getMessage());
                }
            } else {
                LOGGER.info("ViaBackwards is not loaded.");
            }
            if (finalViaRewindPlatformSupplier != null) {
                try {
                    finalViaRewindPlatformSupplier.get();
                } catch (Throwable e) {
                    LOGGER.severe("ViaRewind failed to load: " + e.getMessage());
                }
            } else {
                LOGGER.info("ViaRewind is not loaded.");
            }
            if (finalViaLegacyPlatformSupplier != null) {
                try {
                    finalViaLegacyPlatformSupplier.get();
                } catch (Throwable e) {
                    LOGGER.severe("ViaLegacy failed to load: " + e.getMessage());
                }
            } else {
                LOGGER.info("ViaLegacy is not loaded.");
            }
        });

        ((ViaManagerImpl) Via.getManager()).init();
        Via.getManager().getProtocolManager().setMaxProtocolPathSize(Integer.MAX_VALUE);
        Via.getManager().getProtocolManager().setMaxPathDeltaIncrease(-1);
        ((ProtocolManagerImpl) Via.getManager().getProtocolManager()).refreshVersions();
    }

}
