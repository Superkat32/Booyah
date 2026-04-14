package net.superkat.booyah.mixin.splatana;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec2;
import net.superkat.booyah.duck.splatana.SplatanaPlayer;
import net.superkat.booyah.item.SplatanaItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Inject(method = "modifyInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isMovingSlowly()Z"), cancellable = true)
    public void booyah$slowPlayerWhileSwingingSplatana(Vec2 input, CallbackInfoReturnable<Vec2> cir) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        if (player instanceof SplatanaPlayer splatanaPlayer) {
            if (splatanaPlayer.booyah$isSplatanaSwinging()) {
                cir.setReturnValue(input.scale(SplatanaItem.PLAYER_SPEED_SWING_REDUCER_AMOUNT));
            }
        }
    }

}
