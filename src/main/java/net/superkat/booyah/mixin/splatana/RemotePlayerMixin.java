package net.superkat.booyah.mixin.splatana;

import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.LivingEntity;
import net.superkat.booyah.item.SplatanaManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RemotePlayer.class)
public class RemotePlayerMixin {

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/RemotePlayer;updateSwingTime()V"))
    public void booyah$updatePlayerSwingTimeAfterPossibleLithiumCancelButRemotelyThisTime(CallbackInfo ci) {
        SplatanaManager.updateSplatanaPlayer((LivingEntity) (Object) this);
    }

}
