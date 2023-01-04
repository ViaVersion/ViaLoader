package net.raphimc.viaprotocolhack.impl.viaversion;


import com.google.common.collect.Lists;
import com.viaversion.viaversion.configuration.AbstractViaConfig;

import java.io.File;
import java.util.*;

public class VPViaConfig extends AbstractViaConfig {

    protected final List<String> UNSUPPORTED = Lists.newArrayList(
            "checkforupdates", "bungee-ping-interval", "bungee-ping-save", "bungee-servers",
            "velocity-ping-interval", "velocity-ping-save", "velocity-servers",
            "block-protocols", "block-disconnect-msg", "reload-disconnect-msg",
            "max-pps", "max-pps-kick-msg", "tracking-period", "tracking-warning-pps", "tracking-max-warnings", "tracking-max-kick-msg",
            "blockconnection-method", "anti-xray-patch", "quick-move-action-fix", "item-cache",
            "change-1_9-hitbox", "change-1_14-hitbox",
            "use-new-deathmessages", "nms-player-ticking"
    );

    public VPViaConfig(final File configFile) {
        super(configFile);
    }

    @Override
    protected void handleConfig(Map<String, Object> config) {
    }

    @Override
    public List<String> getUnsupportedOptions() {
        return Collections.unmodifiableList(UNSUPPORTED);
    }

    @Override
    public boolean isCheckForUpdates() {
        return false;
    }

    @Override
    public String getBlockConnectionMethod() {
        return "packet";
    }

    @Override
    public boolean isAntiXRay() {
        return false;
    }

    @Override
    public boolean is1_12QuickMoveActionFix() {
        return false;
    }

    @Override
    public boolean isItemCache() {
        return false;
    }

    @Override
    public boolean is1_9HitboxFix() {
        return false;
    }

    @Override
    public boolean is1_14HitboxFix() {
        return false;
    }

    @Override
    public boolean isShowNewDeathMessages() {
        return false;
    }

    @Override
    public boolean isNMSPlayerTicking() {
        return false;
    }

}
