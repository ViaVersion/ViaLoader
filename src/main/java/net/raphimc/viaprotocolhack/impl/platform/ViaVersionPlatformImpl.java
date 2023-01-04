package net.raphimc.viaprotocolhack.impl.platform;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import com.viaversion.viaversion.api.configuration.ConfigurationProvider;
import com.viaversion.viaversion.api.configuration.ViaVersionConfig;
import com.viaversion.viaversion.api.platform.PlatformTask;
import com.viaversion.viaversion.api.platform.ViaPlatform;
import com.viaversion.viaversion.configuration.AbstractViaConfig;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.util.VersionInfo;
import io.netty.channel.EventLoop;
import net.raphimc.viaprotocolhack.commands.UserCommandSender;
import net.raphimc.viaprotocolhack.impl.viaversion.VPApiBase;
import net.raphimc.viaprotocolhack.impl.viaversion.VPViaConfig;
import net.raphimc.viaprotocolhack.util.*;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class ViaVersionPlatformImpl implements ViaPlatform<UUID> {

    private static final ExecutorService ASYNC_EXECUTOR;
    private static final EventLoop EVENT_LOOP;
    private static final Logger LOGGER = new JLoggerToSLF4J(LoggerFactory.getLogger("ViaVersion"));

    private final File dataFolder;
    private final AbstractViaConfig config;
    private final ViaAPI<UUID> api;

    static {
        final ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("ViaProtocolHack-#%d").setDaemon(true).build();
        ASYNC_EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), factory);
        EVENT_LOOP = new DefaultEventLoop(factory);
    }

    public ViaVersionPlatformImpl(final File rootFolder) {
        this.dataFolder = new File(rootFolder, "ViaProtocolHack");
        this.config = this.createConfig();
        this.api = this.createApi();
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    public String getPlatformName() {
        return "ViaProtocolHack";
    }

    @Override
    public String getPlatformVersion() {
        return "1.3.3.7";
    }

    @Override
    public String getPluginVersion() {
        return VersionInfo.VERSION;
    }

    @Override
    public FutureTaskId runAsync(Runnable runnable) {
        return new FutureTaskId(CompletableFuture
                .runAsync(runnable, ASYNC_EXECUTOR)
                .exceptionally(throwable -> {
                    if (!(throwable instanceof CancellationException)) {
                        throwable.printStackTrace();
                    }
                    return null;
                })
        );
    }

    @Override
    public FutureTaskId runSync(Runnable runnable) {
        return new FutureTaskId(EVENT_LOOP
                .submit(runnable)
                .addListener(future -> {
                    if (!future.isCancelled() && future.cause() != null) {
                        future.cause().printStackTrace();
                    }
                })
        );
    }

    @Override
    public PlatformTask<Future<?>> runSync(Runnable runnable, long ticks) {
        return new FutureTaskId(EVENT_LOOP
                .schedule(() -> runSync(runnable), ticks * 50, TimeUnit.MILLISECONDS)
                .addListener(future -> {
                    if (!future.isCancelled() && future.cause() != null) {
                        future.cause().printStackTrace();
                    }
                })
        );
    }

    @Override
    public PlatformTask<Future<?>> runRepeatingSync(Runnable runnable, long ticks) {
        return new FutureTaskId(EVENT_LOOP
                .scheduleAtFixedRate(runnable, 0, ticks * 50, TimeUnit.MILLISECONDS)
                .addListener(future -> {
                    if (!future.isCancelled() && future.cause() != null) {
                        future.cause().printStackTrace();
                    }
                })
        );
    }

    @Override
    public ViaCommandSender[] getOnlinePlayers() {
        return Via.getManager().getConnectionManager().getConnectedClients().values().stream().map(UserCommandSender::new).toArray(ViaCommandSender[]::new);
    }

    @Override
    public void sendMessage(UUID uuid, String msg) {
        if (uuid == null) {
            this.getLogger().info(msg);
        } else {
            this.getLogger().info("[" + uuid + "] " + msg);
        }
    }

    @Override
    public boolean kickPlayer(UUID uuid, String s) {
        return false;
    }

    @Override
    public boolean isPluginEnabled() {
        return true;
    }

    @Override
    public boolean isOldClientsAllowed() {
        return true;
    }

    @Override
    public boolean hasPlugin(String s) {
        return s.equalsIgnoreCase("ViaVersion") || s.equalsIgnoreCase("ViaBackwards") || s.equalsIgnoreCase("ViaRewind");
    }

    @Override
    public boolean isProxy() {
        return true;
    }

    @Override
    public ViaAPI<UUID> getApi() {
        return this.api;
    }

    @Override
    public ViaVersionConfig getConf() {
        return this.config;
    }

    @Override
    public ConfigurationProvider getConfigurationProvider() {
        return this.config;
    }

    @Override
    public File getDataFolder() {
        return this.dataFolder;
    }

    @Override
    public void onReload() {
    }

    @Override
    public JsonObject getDump() {
        return new JsonObject();
    }

    protected AbstractViaConfig createConfig() {
        final AbstractViaConfig config = new VPViaConfig(new File(this.dataFolder, "viaversion.yml"));
        config.reloadConfig();
        return config;
    }

    protected ViaAPI<UUID> createApi() {
        return new VPApiBase();
    }

}
