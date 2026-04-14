package net.superkat.booyah.mixin.splatana;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.superkat.booyah.item.BooyahItems;
import net.superkat.booyah.item.SplatanaAnimations;
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
    public abstract ModelPart getArm(HumanoidArm arm);

    @Inject(method = "poseRightArm", at = @At("HEAD"), cancellable = true)
    public void booyah$poseArmWithSplatana(T state, CallbackInfo ci) {
        if (BooyahItems.isSplatana(state.rightHandItemStack)) {
            SplatanaAnimations.thirdPersonHold(this.rightArm, this.leftArm, this.head, true, state.getUseItemStackForArm(HumanoidArm.RIGHT), state);
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

//    @WrapMethod(method = "translateToHand(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;Lnet/minecraft/world/entity/HumanoidArm;Lcom/mojang/blaze3d/vertex/PoseStack;)V")
//    public void booyah$onlyTranslateSplatanaModelInsteadOfTranslateAndRotate(HumanoidRenderState state, HumanoidArm arm, PoseStack poseStack, Operation<Void> original) {
//        SplatanaWeaponRenderData splatanaData = state.getData(SplatanaAnimations.SPLATANA_RENDER_DATA);
//        if (BooyahItems.isSplatana(state.rightHandItemStack) && splatanaData != null && splatanaData.swingAnim() > 0) {
//            translateModelOnly(this.root, poseStack);
//            translateModelOnly(this.getArm(arm), poseStack);
//        } else {
//            original.call(state, arm, poseStack);
//        }
//    }

//    @Unique
//    private void translateModelOnly(ModelPart part, PoseStack poseStack) {
//        poseStack.translate(part.x / 16.0F, part.y / 16.0F, part.z / 16.0F);
////        if (part.xRot != 0.0F || part.yRot != 0.0F || part.zRot != 0.0F) {
////            poseStack.mulPose(new Quaternionf().rotationZYX(part.zRot, part.yRot, part.xRot));
////        }
//
//        if (part.xScale != 1.0F || part.yScale != 1.0F || part.zScale != 1.0F) {
//            poseStack.scale(part.xScale, part.yScale, part.zScale);
//        }
//    }

//    @Inject(method = "setupAttackAnimation", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Ease;outQuart(F)F"), cancellable = true)
//    public void booyah$setupSplatanaAttackAnimation(T state, CallbackInfo ci) {
//        if (BooyahItems.isSplatana(state.rightHandItemStack)) {
//            SplatanaAnimations.thirdPersonSwing((HumanoidModel<T>)(Object)this, state);
//            ci.cancel();
//        }
//    }

}
