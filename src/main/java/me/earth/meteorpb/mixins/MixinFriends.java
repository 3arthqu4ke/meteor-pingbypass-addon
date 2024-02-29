package me.earth.meteorpb.mixins;

import me.earth.pingbypass.PingBypassApi;
import me.earth.pingbypass.api.players.PlayerInfo;
import meteordevelopment.meteorclient.systems.friends.Friend;
import meteordevelopment.meteorclient.systems.friends.Friends;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Pseudo
@Mixin(value = Friends.class, remap = false)
public class MixinFriends {
    @Inject(method = "get(Ljava/lang/String;)Lmeteordevelopment/meteorclient/systems/friends/Friend;", at = @At("HEAD"), cancellable = true, require = 0)
    private void getHook(String name, CallbackInfoReturnable<Friend> cir) {
        Optional<PlayerInfo> info = PingBypassApi.instances().map(pb -> pb.getFriendManager().getByName(name)).filter(Optional::isPresent).map(Optional::get).findFirst();
        info.ifPresent(playerInfo -> cir.setReturnValue(new Friend(playerInfo.name(), playerInfo.uuid())));
    }

}
