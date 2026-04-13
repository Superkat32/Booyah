package net.superkat.booyah.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Ease;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

public class SplatanaAnimations {

    public static <T extends HumanoidRenderState> void thirdPersonHold(
            ModelPart rightArm, ModelPart leftArm, ModelPart head, boolean holdingInRightArm, ItemStack item, T state
    ) {
        ModelPart holdingArm = holdingInRightArm ? rightArm : leftArm;
        ModelPart otherArm = holdingInRightArm ? leftArm : rightArm;

        // Change animation slightly when walking (hold up more)
        float walkingExtra = state.walkAnimationSpeed * 25 + Mth.cos(state.walkAnimationPos * 0.6662F) * 5;

        if (state.attackTime > 0) return;

        // Horizontal rotation
        holdingArm.yRot = (holdingInRightArm ? -0.6F : 0.6F) + head.yRot;
        otherArm.yRot = (holdingInRightArm ? 0.6F : -0.6F) + head.yRot;

        // Vertical rotation
        holdingArm.xRot = (float) (head.xRot - Math.toRadians(45 + walkingExtra));
        otherArm.xRot = (float) (head.xRot - Math.toRadians(55 + walkingExtra));

        // The other horizontal rotation I suppose
        holdingArm.zRot = (float) Math.toRadians(15);
    }

    public static <T extends HumanoidRenderState> void thirdPersonSwing(HumanoidModel<T> model, T state) {
        HumanoidArm arm = state.attackArm;
        ModelPart mainArm = model.getArm(arm);
        boolean swappedHands = state.attackTime > 0.5f;

        float armRotX = 0f + Ease.inOutQuint(state.attackTime) * -50f;
        float armRotY = 0f + Ease.inOutQuint(state.attackTime) * -60f;
        float armRotZ = 15f + Ease.inOutQuint(state.attackTime) * -15f;

//        float armRotX = 0f;
//        float armRotY = 0f;
//        float armRotZ = 15f;
//        if (swappedHands) {
//            armRotX = -70f;
//            armRotY = -60f;
//            armRotZ = 0f;
//        }
        mainArm.xRot = (float) Math.toRadians(armRotX);
        mainArm.yRot = (float) Math.toRadians(armRotY);
        mainArm.zRot = (float) Math.toRadians(armRotZ);

        ModelPart otherArm = model.getArm(arm == HumanoidArm.RIGHT ? HumanoidArm.LEFT : HumanoidArm.RIGHT);
        float otherArmRotX = -65f + Ease.inOutQuint(state.attackTime) * 85f;
        float otherArmRotY = 65f + Ease.inOutQuint(state.attackTime) * -80f;
        float otherArmRotZ = 0 + Ease.inOutQuint(state.attackTime) * -15f;

//        float otherArmRotX = -65f;
//        float otherArmRotY = 65f;
//        float otherArmRotZ = 0f;
//        if (swappedHands) {
//            otherArmRotX = 25f;
//            otherArmRotY = -15f;
//            otherArmRotZ = -15f;
//        }
        otherArm.xRot = (float) Math.toRadians(otherArmRotX);
        otherArm.yRot = (float) Math.toRadians(otherArmRotY);
        otherArm.zRot = (float) Math.toRadians(otherArmRotZ);

    }

    public static <S extends ArmedEntityRenderState> void thirdPersonItemSwing(S state, PoseStack poseStack) {
        if (state.attackTime <= 0) return;
        boolean swappedHands = state.attackTime <= 0.5f;

        float splatanaRotX = 0f + (Ease.inOutQuint(state.attackTime) * 15f);
        float splatanaRotY = -85f;
        float splatanaRotZ = 170f - (Ease.inOutQuint(state.attackTime) * 270f);

        float pivotY = -0.15f + (Ease.inOutQuint(state.attackTime) * -0.15f);

        poseStack.rotateAround(Axis.ZN.rotationDegrees(splatanaRotZ), 0, pivotY, 0);
        poseStack.rotateAround(Axis.YN.rotationDegrees(splatanaRotY), 0, pivotY, 0);
        poseStack.rotateAround(Axis.ZN.rotationDegrees(splatanaRotX), 0, pivotY, 0);
    }

}
