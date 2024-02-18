package me.earth.meteorpb.settings;

import me.earth.meteorpb.registry.RegistryTranslation;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.module.Module;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.screens.settings.LeftRightListSettingScreen;
import meteordevelopment.meteorclient.gui.themes.meteor.widgets.WMeteorLabel;
import meteordevelopment.meteorclient.gui.utils.SettingsWidgetFactory;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.gui.widgets.containers.WContainer;
import meteordevelopment.meteorclient.gui.widgets.containers.WHorizontalList;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import meteordevelopment.meteorclient.settings.IVisible;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static meteordevelopment.meteorclient.MeteorClient.mc;

/**
 * A {@link meteordevelopment.meteorclient.settings.ModuleListSetting}
 */
public class PbModuleListSetting extends Setting<List<Module>> {
    private static List<String> suggestions;

    static {
        SettingsWidgetFactory.registerCustomFactory(PbModuleListSetting.class, (theme) -> (table, s) -> {
            var setting = (PbModuleListSetting) s;
            WHorizontalList c2 = table.add(theme.horizontalList()).expandCellX().widget();
            c2.spacing *= 2;
            WButton button = c2.add(theme.button("Select")).expandCellX().widget();
            button.action = () -> mc.setScreen(new PbModuleListSettingScreen(setting.pingBypass, theme, setting));

            c2.add(new WSelectedCountLabel(setting).color(theme.textSecondaryColor()));
            reset(theme, table, setting, null);
        });
    }

    private final PingBypass pingBypass;

    public PbModuleListSetting(PingBypass pingBypass, String name, String description, List<Module> defaultValue, Consumer<List<Module>> onChanged, Consumer<Setting<List<Module>>> onModuleActivated, IVisible visible) {
        super(name, description, defaultValue, onChanged, onModuleActivated, visible);
        this.pingBypass = pingBypass;
    }

    @Override
    public void resetImpl() {
        value = new ArrayList<>(defaultValue);
    }

    @Override
    protected List<Module> parseImpl(String str) {
        String[] values = str.split(",");
        List<Module> modules = new ArrayList<>(values.length);

        try {
            for (String value : values) {
                pingBypass.getModuleManager().getByName(value.trim()).ifPresent(modules::add);
            }
        } catch (Exception ignored) {
        }

        return modules;
    }

    @Override
    protected boolean isValueValid(List<Module> value) {
        return true;
    }

    @Override
    public List<String> getSuggestions() {
        if (suggestions == null) {
            suggestions = new ArrayList<>(Modules.get().getAll().size());
            for (Module module : pingBypass.getModuleManager()) suggestions.add(module.getName());
        }

        return suggestions;
    }

    @Override
    public NbtCompound save(NbtCompound tag) {
        NbtList modulesTag = new NbtList();
        for (Module module : get()) modulesTag.add(NbtString.of(module.getName()));
        tag.put("modules", modulesTag);

        return tag;
    }

    @Override
    public List<Module> load(NbtCompound tag) {
        get().clear();

        NbtList valueTag = tag.getList("modules", 8);
        for (NbtElement tagI : valueTag) {
            pingBypass.getModuleManager().getByName(tagI.asString()).ifPresent(module -> get().add(module));
        }

        return get();
    }

    public static class Builder extends SettingBuilder<Builder, List<Module>, PbModuleListSetting> {
        private final PingBypass pingBypass;

        public Builder(PingBypass pingBypass) {
            super(new ArrayList<>(0));
            this.pingBypass = pingBypass;
        }

        @SafeVarargs
        public final Builder defaultValue(Class<? extends Module>... defaults) {
            List<Module> modules = new ArrayList<>();

            for (Class<? extends Module> klass : defaults) {
                pingBypass.getModuleManager().getByClass(klass).ifPresent(modules::add);
            }

            return defaultValue(modules);
        }

        @Override
        public PbModuleListSetting build() {
            return new PbModuleListSetting(pingBypass, name, description, defaultValue, onChanged, onModuleActivated, visible);
        }
    }

    public static class PbModuleListSettingScreen extends LeftRightListSettingScreen<Module> {
        public PbModuleListSettingScreen(PingBypass pingBypass, GuiTheme theme, Setting<List<Module>> setting) {
            super(theme, "Select Modules", setting, setting.get(), RegistryTranslation.fromPingBypassRegistry("modules", pingBypass.getModuleManager()));
        }

        @Override
        protected WWidget getValueWidget(Module value) {
            return theme.label(getValueName(value));
        }

        @Override
        protected String getValueName(Module value) {
            return value.getName();
        }
    }

    private static void reset(GuiTheme theme, WContainer c, Setting<?> setting, Runnable action) {
        WButton reset = c.add(theme.button(GuiRenderer.RESET)).widget();
        reset.action = () -> {
            setting.reset();
            if (action != null) action.run();
        };
    }

    // Stolen from DefaultSettingsWidgetFactory
    private static class WSelectedCountLabel extends WMeteorLabel {
        private final Setting<?> setting;
        private int lastSize = -1;

        public WSelectedCountLabel(Setting<?> setting) {
            super("", false);

            this.setting = setting;
        }

        @Override
        protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
            int size = getSize(setting);

            if (size != lastSize) {
                set("(" + size + " selected)");
                lastSize = size;
            }

            super.onRender(renderer, mouseX, mouseY, delta);
        }

        public static int getSize(Setting<?> setting) {
            if (setting.get() instanceof Collection<?> collection) return collection.size();
            if (setting.get() instanceof Map<?, ?> map) return map.size();

            return -1;
        }
    }

}

