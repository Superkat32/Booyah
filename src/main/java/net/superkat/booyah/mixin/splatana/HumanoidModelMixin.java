package net.superkat.booyah.mixin.splatana;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.superkat.booyah.item.BooyahItems;
import net.superkat.booyah.item.client.SplatanaAnimations;
import net.superkat.booyah.render.data.SplatanaWeaponRenderData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends HumanoidRenderState> {

    @Shadow
    @Final
    public ModelPart rightArm;

    @Shadow
    @Final
    public ModelPart leftArm;

    @Shadow
    @Final
    public ModelPart head;

    @Shadow
    @Final
    public ModelPart body;

    @Shadow
    @Final
    public ModelPart leftLeg;

    @Shadow
    @Final
    public ModelPart rightLeg;

    @Inject(method = "poseRightArm", at = @At("HEAD"), cancellable = true)
    public void booyah$poseRightArmWithSplatana(T state, CallbackInfo ci) {
        if (BooyahItems.isSplatana(state.rightHandItemStack)) {
            SplatanaAnimations.thirdPersonHold(this.rightArm, this.leftArm, this.body, this.head, this.rightLeg, this.leftLeg, true, state.getUseItemStackForArm(HumanoidArm.RIGHT), state);
            ci.cancel();
        }
    }

    @Inject(method = "poseLeftArm", at = @At("HEAD"), cancellable = true)
    public void booyah$cancelPoseLeftArmWithSplatana(T state, CallbackInfo ci) {
        if (BooyahItems.isSplatana(state.rightHandItemStack) && state.ticksUsingItem > 0) {
            ci.cancel();
        }
    }

    @WrapMethod(method = "setupAttackAnimation")
    public void booyah$useCustomSplatanaAttackAnimation(T state, Operation<Void> original) {
        SplatanaWeaponRenderData splatanaData = state.getData(SplatanaAnimations.SPLATANA_RENDER_DATA);
        if (BooyahItems.isSplatana(state.rightHandItemStack) && splatanaData != null && splatanaData.swingAnim() > 0) {
            //noinspection unchecked
            SplatanaAnimations.thirdPersonSwing((HumanoidModel<T>)(Object)this, state);
        } else {
            original.call(state);
        }
    }

}
