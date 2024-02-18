package me.earth.meteorpb.module;

import me.earth.meteorpb.settings.KeyBindTranslation;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.input.Bind;
import meteordevelopment.meteorclient.events.meteor.ModuleBindChangedEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

public class ModuleBindListener {
    private final PingBypass pingBypass;

    public ModuleBindListener(PingBypass pingBypass) {
        this.pingBypass = pingBypass;
    }

    @EventHandler
    private void onModuleBound(ModuleBindChangedEvent moduleBindChangedEvent) {
        Module module = moduleBindChangedEvent.module;
        if (module instanceof TranslatedModule translatedModule) {
            translatedModule.getPingbypassModule()
                            .getSetting("Bind", Bind.class)
                            .ifPresent(setting -> setting.setValue(KeyBindTranslation.getPbBind(pingBypass, module.keybind)));
        }
    }

}
