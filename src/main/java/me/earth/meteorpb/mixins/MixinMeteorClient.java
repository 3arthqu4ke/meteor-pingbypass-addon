package me.earth.meteorpb.mixins;

import me.earth.meteorpb.EntryPoint;
import meteordevelopment.meteorclient.MeteorClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * To make this both a PingBypass and Meteor plugin we use mixins create our entry point.
 */
@Pseudo
@Mixin(value = MeteorClient.class, remap = false)
public class MixinMeteorClient {
    @Inject(method = "onInitializeClient", at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/systems/modules/Modules;sortModules()V"))
    private void initHook(CallbackInfo ci) {
        EntryPoint.init();
    }

}
