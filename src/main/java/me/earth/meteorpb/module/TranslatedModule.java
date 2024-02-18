package me.earth.meteorpb.module;

import me.earth.meteorpb.settings.KeyBindTranslation;
import me.earth.meteorpb.settings.SettingTranslation;
import me.earth.pingbypass.api.input.Bind;
import me.earth.pingbypass.api.traits.Nameable;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.misc.Keybind;

public class TranslatedModule extends Module {
    public final me.earth.pingbypass.api.module.Module pingbypassModule;

    public TranslatedModule(me.earth.pingbypass.api.module.Module pingbypassModule) {
        super(fromPingBypassCategory(pingbypassModule.getCategory()), pingbypassModule.getName(), pingbypassModule.getDescription());
        this.pingbypassModule = pingbypassModule;
        // TODO: use toggleOnBindRelease and chatFeedback (for Notifications), once we have that functionality in PingBypass!
        for (me.earth.pingbypass.api.setting.Setting<?> setting : pingbypassModule) {
            if ("Enabled".equals(setting.getName())) {
                continue;
            }

            if ("Bind".equals(setting.getName())) {
                if (setting.getValue() instanceof Bind bind) {
                    keybind.set(KeyBindTranslation.getMeteorBind(bind));
                }

                continue;
            }

            var translated = SettingTranslation.translate(pingbypassModule.getPingBypass(), setting);
            if (translated != null) {
                settings.getDefaultGroup().add(translated);
            }
        }
    }

    @Override
    public void toggle() {
        pingbypassModule.toggle();
    }

    @Override
    public boolean isActive() {
        return pingbypassModule.isEnabled();
    }

    public me.earth.pingbypass.api.module.Module getPingbypassModule() {
        return pingbypassModule;
    }

    public static Category fromPingBypassCategory(Nameable pingbypassCategory) {
        for (Category category : Modules.loopCategories()) {
            if (category.name.equalsIgnoreCase(pingbypassCategory.getName())) {
                return category;
            }
        }

        return Categories.Misc;
    }

}
