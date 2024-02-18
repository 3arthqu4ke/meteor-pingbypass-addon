package me.earth.meteorpb.settings;

import me.earth.meteorpb.AddOn;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.input.Bind;
import me.earth.pingbypass.api.setting.impl.SettingUtil;
import me.earth.pingbypass.api.setting.impl.types.NumberSetting;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class SettingTranslation {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> @Nullable Setting<T> translate(PingBypass pingBypass, me.earth.pingbypass.api.setting.Setting<T> setting) {
        try {
            Setting result;
            if (setting instanceof NumberSetting<?> numberSetting && (setting.getDefaultValue() instanceof Double || setting.getDefaultValue() instanceof Float)) {
                DoubleSetting.Builder builder = translate(new DoubleSetting.Builder(), (me.earth.pingbypass.api.setting.Setting<Double>) setting,
                                                          ((Number) setting.getDefaultValue()).doubleValue(), v -> SettingUtil.setValueUnchecked(setting, getCorrectValue(v, setting)));
                builder.min(numberSetting.getMin().doubleValue());
                builder.max(numberSetting.getMax().doubleValue());
                result = builder.build();
                result.set(((Number) setting.getValue()).doubleValue());
            } else if (setting instanceof NumberSetting<?> numberSetting
                && (setting.getDefaultValue() instanceof Byte
                || setting.getDefaultValue() instanceof Short
                || setting.getDefaultValue() instanceof Integer
                || setting.getDefaultValue() instanceof Long)) {
                IntSetting.Builder builder = translate(new IntSetting.Builder(), (me.earth.pingbypass.api.setting.Setting<Integer>) setting,
                                                       ((Number) setting.getDefaultValue()).intValue(), v -> SettingUtil.setValueUnchecked(setting, getCorrectValue(v, setting)));
                builder.min(numberSetting.getMin().intValue());
                builder.max(numberSetting.getMax().intValue());
                result = builder.build();
                result.set(((Number) setting.getValue()).intValue());
            } else if (setting.getDefaultValue() instanceof Boolean) {
                result = translate(new BoolSetting.Builder(), (me.earth.pingbypass.api.setting.Setting<Boolean>) setting).build();
                result.set(setting.getValue());
            } else if (setting.getDefaultValue() instanceof String) {
                result = translate(new StringSetting.Builder(), (me.earth.pingbypass.api.setting.Setting<String>) setting).build();
                result.set(setting.getValue());
            } else if (setting.getDefaultValue() instanceof Bind) {
                result = translate(
                    new KeybindSetting.Builder(),
                    (me.earth.pingbypass.api.setting.Setting<Keybind>) setting,
                    KeyBindTranslation.getMeteorBind((Bind) setting.getDefaultValue()),
                    bind -> SettingUtil.setValueUnchecked(setting, KeyBindTranslation.getPbBind(pingBypass, bind))
                ).build();
                result.set(KeyBindTranslation.getMeteorBind((Bind) setting.getValue()));
            } else if (setting.getDefaultValue() instanceof Enum<?>) {
                result = translate(new EnumSetting.Builder(), setting).build();
                result.set(setting.getValue());
            } else {
                result = new Setting(setting.getName(), setting.getDescription(), setting.getDefaultValue(), v -> {
                }, s -> {
                }, setting::isVisible) {
                    @Override
                    protected Object parseImpl(String str) {
                        return null;
                    }

                    @Override
                    protected boolean isValueValid(Object value) {
                        return false;
                    }

                    @Override
                    protected NbtCompound save(NbtCompound tag) {
                        return null;
                    }

                    @Override
                    protected Object load(NbtCompound tag) {
                        return null;
                    }
                };
            }

            return result;
        } catch (Exception e) {
            AddOn.LOG.error("Failed to translate setting " + setting.getName() + " - " + setting.getDescription() + " - " + setting.getDefaultValue(), e);
            return null;
        }
    }

    private static <V, S, B extends Setting.SettingBuilder<B, V, S>> B translate(B builder, me.earth.pingbypass.api.setting.Setting<V> setting) {
        return translate(builder, setting, setting.getDefaultValue(), setting::setValue);
    }

    private static <V, S, B extends Setting.SettingBuilder<B, V, S>> B translate(B builder, me.earth.pingbypass.api.setting.Setting<V> setting,
                                                                                 V defaultValue, Consumer<V> onChanged) {
        // we initially set the value of the setting, at that time we don't want to trigger onChanged
        boolean[] hasChanged = new boolean[]{false};
        return builder
            .name(setting.getName())
            .description(setting.getDescription())
            .defaultValue(defaultValue)
            .onModuleActivated(v -> {
            })
            .onChanged(v -> {
                if (hasChanged[0]) {
                    try {
                        onChanged.accept(v);
                    } catch (Exception e) {
                        AddOn.LOG.error("Failed to set value of " + setting + " to " + v, e);
                    }
                } else {
                    hasChanged[0] = true;
                }
            })
            .visible(setting::isVisible);
    }

    private static Number getCorrectValue(Number number, me.earth.pingbypass.api.setting.Setting<?> setting) {
        if (setting.getDefaultValue() instanceof Float) {
            return number.floatValue();
        } else if (setting.getDefaultValue() instanceof Double) {
            return number.doubleValue();
        } else if (setting.getDefaultValue() instanceof Byte) {
            return number.byteValue();
        } else if (setting.getDefaultValue() instanceof Short) {
            return number.shortValue();
        } else if (setting.getDefaultValue() instanceof Integer) {
            return number.intValue();
        } else if (setting.getDefaultValue() instanceof Long) {
            return number.longValue();
        }

        throw new IllegalStateException("Failed to get correct value for number type " + number);
    }

}
