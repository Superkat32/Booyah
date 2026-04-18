package net.superkat.booyah.mixin.splatana;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import net.superkat.booyah.item.BooyahItems;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow
    @Nullable
    public LocalPlayer player;

    @Shadow
    public int missTime;

    @Shadow
    @Nullable
    public HitResult hitResult;

    @Shadow
    @Nullable
    public MultiPlayerGameMode gameMode;

//    @Inject(method = "startAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/HitResult;getType()Lnet/minecraft/world/phys/HitResult$Type;"), cancellable = true)
//    public void booyah$preventSplatanaFromHittingBlock(CallbackInfoReturnable<Boolean> cir) {
//        if (this.hitResult.getType() == HitResult.Type.BLOCK && BooyahItems.isSplatana(this.player.getMainHandItem())) {
//            // Act as if MISS was the case instead of BLOCK
//            if (this.gameMode.hasMissTime()) {
//                this.missTime = 10;
//            }
//
//            this.player.resetAttackStrengthTicker();
//            this.player.swing(InteractionHand.MAIN_HAND);
//            cir.setReturnValue(true);
//            cir.cancel();
//        }
//    }

    @Shadow
    @Nullable
    public ClientLevel level;

    @Inject(method = "continueAttack(Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"), cancellable = true)
    private void booyah$preventSplatanasFromMiningAndBreakingEverything(boolean down, CallbackInfo ci) {
        ItemStack heldItem = this.player.getItemInHand(InteractionHand.MAIN_HAND);
        if(BooyahItems.isSplatana(heldItem)) {
            ci.cancel();
        }
    }

    @WrapMethod(method = "startAttack()Z")
    public boolean booyah$overrideStartAttackForSplatana(Operation<Boolean> original) {
        ItemStack heldItem = this.player.getItemInHand(InteractionHand.MAIN_HAND);
        if (this.player != null && this.hitResult != null
                && this.hitResult.getType() == HitResult.Type.BLOCK && BooyahItems.isSplatana(heldItem)
        ) {
            boolean callOriginal = false;
            if (this.missTime > 0 || this.player.isHandsBusy()) callOriginal = true;
            if (this.gameMode.isSpectator()) callOriginal = true;

            if (!heldItem.isItemEnabled(this.level.enabledFeatures())) callOriginal = true;
            if (this.player.cannotAttackWithItem(heldItem, 0)) callOriginal = true;
            if (!callOriginal) {
                // Act as if MISS was the case instead of BLOCK, but return true instead of false
                if (this.gameMode.hasMissTime()) {
                    this.missTime = 10;
                }

                this.player.resetAttackStrengthTicker();
                this.player.swing(InteractionHand.MAIN_HAND);
                return true;
            }
        }
        return original.call();
    }

}
