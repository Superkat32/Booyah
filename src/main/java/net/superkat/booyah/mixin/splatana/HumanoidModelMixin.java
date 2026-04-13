package net.superkat.booyah.mixin.splatana;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.superkat.booyah.item.BooyahItems;
import net.superkat.booyah.item.SplatanaAnimations;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin<T extends HumanoidRenderState> {

    @Shadow
    @Final
    public ModelPart rightArm;

    @Shadow
    @Final
    public ModelPart leftArm;

    @Shadow
    @Final
    public ModelPart head;

    @Inject(method = "poseRightArm", at = @At("HEAD"), cancellable = true)
    public void booyah$poseArmWithSplatana(T state, CallbackInfo ci) {
        if (state.rightHandItemStack.is(BooyahItems.SPLATANA_STAMPER)) {
            SplatanaAnimations.thirdPersonHold(this.rightArm, this.leftArm, this.head, true, state.getUseItemStackForArm(HumanoidArm.RIGHT), state);
            ci.cancel();
        }
    }

    @Inject(method = "setupAttackAnimation", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Ease;outQuart(F)F"), cancellable = true)
    public void booyah$setupSplatanaAttackAnimation(T state, CallbackInfo ci) {
        if (state.rightHandItemStack.is(BooyahItems.SPLATANA_STAMPER)) {
            SplatanaAnimations.thirdPersonSwing((HumanoidModel<T>)(Object)this, state);
            ci.cancel();
        }
    }

}
