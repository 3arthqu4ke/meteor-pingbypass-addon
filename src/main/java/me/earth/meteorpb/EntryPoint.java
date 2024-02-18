package me.earth.meteorpb;

import me.earth.meteorpb.gui.PbTab;
import me.earth.meteorpb.hud.PingBypassHudElement;
import me.earth.meteorpb.module.ModuleBindListener;
import me.earth.pingbypass.PingBypassApi;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.gui.tabs.Tabs;
import meteordevelopment.meteorclient.systems.hud.Hud;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @see me.earth.meteorpb.mixins.MixinMeteorClient
 */
public class EntryPoint {
    private static final AtomicBoolean INITIALIZED = new AtomicBoolean();

    public static synchronized void init() {
        if (!INITIALIZED.getAndSet(true)) {
            AddOn.LOG.info("Initializing Meteor PingBypass Add-On.");

            try {
                if (PingBypassApi.instances().findAny().isEmpty()) {
                    AddOn.LOG.info("Failed to find any PingBypass instances!");
                }

                if (!AddOn.IS_LOADED_AS_ADDON.get()) {
                    MeteorClient.EVENT_BUS.registerLambdaFactory(AddOn.PACKAGE, (look, c) -> (MethodHandles.Lookup) look.invoke(null, c, MethodHandles.lookup()));
                }

                PingBypassApi.instances().forEach(pb -> {
                    MeteorClient.EVENT_BUS.subscribe(new ModuleBindListener(pb));
                    Hud.get().register(PingBypassHudElement.getInfo(pb));
                    Tabs.add(new PbTab(pb));
                });
            } catch (NoClassDefFoundError e) {
                AddOn.LOG.info("Failed to find PingBypass: " + e.getMessage());
            }
        }
    }

}
