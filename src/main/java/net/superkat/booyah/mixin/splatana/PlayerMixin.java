package net.superkat.booyah.mixin.splatana;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.superkat.booyah.item.SplatanaManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;updateSwingTime()V"))
    public void booyah$updatePlayerSwingTimeAfterPossibleLithiumCancel(CallbackInfo ci) {
        // Called on server and client
        SplatanaManager.updateSplatanaPlayer((LivingEntity) (Object) this);
    }


}
