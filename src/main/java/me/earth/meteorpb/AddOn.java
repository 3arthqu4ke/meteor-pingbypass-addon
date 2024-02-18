package me.earth.meteorpb;

import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Actual entry point is {@link EntryPoint}
 */
public class AddOn extends MeteorAddon {
    public static final AtomicBoolean IS_LOADED_AS_ADDON = new AtomicBoolean();
    public static final HudGroup HUD_GROUP = new HudGroup("PingBypass");
    public static final String PACKAGE = AddOn.class.getPackageName();
    public static final Logger LOG = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        LOG.info("Found Meteor PingBypass Add-On.");
        IS_LOADED_AS_ADDON.set(true);
        // Actual initialization happens in EntryPoint
    }

    @Override
    public String getPackage() {
        return PACKAGE;
    }

}
