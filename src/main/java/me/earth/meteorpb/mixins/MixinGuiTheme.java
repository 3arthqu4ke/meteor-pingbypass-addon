package me.earth.meteorpb.mixins;

import meteordevelopment.meteorclient.gui.GuiTheme;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * <a href=https://github.com/MeteorDevelopment/meteor-client/pull/4412>https://github.com/MeteorDevelopment/meteor-client/pull/4412</a>.
 */
@Pseudo
@Mixin(value = GuiTheme.class, remap = false)
public class MixinGuiTheme {
    @Redirect(
        method = "dropdown(Ljava/lang/Enum;)Lmeteordevelopment/meteorclient/gui/widgets/input/WDropdown;",
        at = @At(value = "INVOKE", target = "Ljava/lang/Object;getClass()Ljava/lang/Class;"),
        require = 0)
    private Class<?> getClassHook(Object value) {
        return ((Enum<?>) value).getDeclaringClass();
    }

}
