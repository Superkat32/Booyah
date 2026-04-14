package net.superkat.booyah.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Ease;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.superkat.booyah.duck.splatana.SplatanaPlayer;
import net.superkat.booyah.render.data.SplatanaWeaponRenderData;

// This whole class is a nightmare of nonsense
public class SplatanaAnimations {

    public static final RenderStateDataKey<SplatanaWeaponRenderData> SPLATANA_RENDER_DATA = RenderStateDataKey.create(() -> "Splatana swing & charge animation render data");

    public static void updateSplatanaSwingTime(LivingEntity player, int currentSwingDuration) {
        SplatanaPlayer splatanaPlayer = (SplatanaPlayer) player;
        if (currentSwingDuration > 0) {
            currentSwingDuration += 10;
        }

        int swingTime = splatanaPlayer.booyah$getSplatanaSwingTime();

        if (splatanaPlayer.booyah$isSplatanaSwinging()) {
            if (swingTime == 0) {
                if (splatanaPlayer.booyah$firstSwing()) {
                    splatanaPlayer.booyah$setFirstSwing(false);
                } else {
                    splatanaPlayer.booyah$setReverseSplatanaSwing(!splatanaPlayer.booyah$reverseSplatanaSwing());
                }
            }
            swingTime++;
            if (swingTime >= currentSwingDuration) {
                swingTime = 0;
                splatanaPlayer.booyah$setIsSplatanaSwinging(false);
            }
        } else {
            splatanaPlayer.booyah$setReverseSplatanaSwing(false);
            splatanaPlayer.booyah$setFirstSwing(true);
            swingTime = 0;
        }

        splatanaPlayer.booyah$setSplatanaSwingTime(swingTime);
        splatanaPlayer.booyah$setSplatanaAttackAnim((float) swingTime / currentSwingDuration);
    }

    public static <T extends ArmedEntityRenderState> HumanoidArm getArmToTranslateSplatanaTo(T state, HumanoidArm original) {
        SplatanaWeaponRenderData splatanaData = state.getData(SPLATANA_RENDER_DATA);
        if (!BooyahItems.isSplatana(state.rightHandItemStack) || splatanaData == null) return original;

        boolean swap = splatanaData.reverseSwing();
        if (state.attackTime > 0.5 || (state.attackTime <= 0 && splatanaData.swingAnim() >= 0.35)) {
            swap = !swap;
        }

        if (swap) {
            return original.getOpposite();
        }
        return original;
    }

    // Arm animation for holding the Splatana, but not actively using it (e.g. swinging or charging)
    public static <T extends HumanoidRenderState> void thirdPersonHold(
            ModelPart rightArm, ModelPart leftArm, ModelPart head, boolean holdingInRightArm, ItemStack item, T state
    ) {
        // use >= 0 because swingTime gets set to -1 on swing because that's how Mojang does it ¯\_(ツ)_/¯
        if (state.getData(SPLATANA_RENDER_DATA) != null && state.getData(SPLATANA_RENDER_DATA).swingAnim() > 0) return;
        ModelPart holdingArm = holdingInRightArm ? rightArm : leftArm;
        ModelPart otherArm = holdingInRightArm ? leftArm : rightArm;

        // Change animation slightly when walking (hold up more)
        float walkingExtra = state.walkAnimationSpeed * 35 + Mth.cos(state.walkAnimationPos * 0.6662F) * 5;

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

    // Arm animation for swinging the Splatana horizontally (left-clicked)
    public static <T extends HumanoidRenderState> void thirdPersonSwing(HumanoidModel<T> model, T state) {
        SplatanaWeaponRenderData splatanaData = state.getData(SPLATANA_RENDER_DATA);
        if (splatanaData == null || splatanaData.swingAnim() <= 0) return;
        boolean reversed = splatanaData.reverseSwing();
        HumanoidArm arm = reversed ? swapArm(state.attackArm) : state.attackArm;
        ModelPart mainArm = model.getArm(arm);
        ModelPart otherArm = model.getArm(arm == HumanoidArm.RIGHT ? HumanoidArm.LEFT : HumanoidArm.RIGHT);

        float animTime = splatanaData.swingAnim(); // Full animation (including draw back)
        float attackTime = state.attackTime; // Swing only animation (only swing left/right)
        boolean swappedHands = attackTime > 0.5f;

        float mainArmRotX = 0f;
        float mainArmRotY = 0f;
        float mainArmRotZ = 0f;
        float otherArmRotX = 0f;
        float otherArmRotY = 0f;
        float otherArmRotZ = 0f;

        if (attackTime > 0) { // Attack anim
            float progress = Ease.inOutQuint(attackTime);
            mainArmRotX = -55f + (progress * -5f);
            mainArmRotY = 0 + (progress * -55f);
            mainArmRotZ = 0 + (progress * 5f);
            otherArmRotX = -35f + (progress * 90f);
            otherArmRotY = 55f + (progress * -40f);
            otherArmRotZ = 25f + (progress * -50f);
//            mainArmRotX = -55f;
//            mainArmRotY = 0f;
//            mainArmRotZ = 0f;
//            otherArmRotX = -35f;
//            otherArmRotY = 55f;
//            otherArmRotZ = 25f;
//            if (swappedHands) {
//                mainArmRotX = -85f;
//                mainArmRotY = -55f;
//                mainArmRotZ = 25f;
//                otherArmRotX = 55f;
//                otherArmRotY = 15f;
//                otherArmRotZ = -10f;
//            }
        } else { // Return to idle anim
            mainArmRotX = -60;
            mainArmRotY = -55f;
            mainArmRotZ = 5;
            otherArmRotX = 55f;
            otherArmRotY = 15f;
            otherArmRotZ = -25f;
        }

        if (reversed) {
//            mainArmRotX *= -1;
            mainArmRotY *= -1;
            mainArmRotZ *= -1;
//            otherArmRotX *= -1;
            otherArmRotY *= -1;
            otherArmRotZ *= -1;
        }

        mainArm.xRot = radians(mainArmRotX);
        mainArm.yRot = radians(mainArmRotY);
        mainArm.zRot = radians(mainArmRotZ);
        otherArm.xRot = radians(otherArmRotX);
        otherArm.yRot = radians(otherArmRotY);
        otherArm.zRot = radians(otherArmRotZ);

////        float armRotX = 0f + Ease.inOutQuint(swingAnim) * -50f;
////        float armRotY = 0f + Ease.inOutQuint(swingAnim) * -60f;
////        float armRotZ = 15f + Ease.inOutQuint(swingAnim) * -15f;
//
//        float armRotX = 0f;
//        float armRotY = 0f;
//        float armRotZ = 15f;
//        if (swappedHands) {
//            armRotX = -70f;
//            armRotY = -60f;
//            armRotZ = 0f;
//        }
//        mainArm.xRot = (float) Math.toRadians(armRotX);
//        mainArm.yRot = (float) Math.toRadians(armRotY);
//        mainArm.zRot = (float) Math.toRadians(armRotZ);
//
//        ModelPart otherArm = model.getArm(arm == HumanoidArm.RIGHT ? HumanoidArm.LEFT : HumanoidArm.RIGHT);
////        float otherArmRotX = -65f + Ease.inOutQuint(swingAnim) * 85f;
////        float otherArmRotY = 65f + Ease.inOutQuint(swingAnim) * -80f;
////        float otherArmRotZ = 0 + Ease.inOutQuint(swingAnim) * -15f;
//
//        float otherArmRotX = -65f;
//        float otherArmRotY = 65f;
//        float otherArmRotZ = 0f;
//        if (swappedHands) {
//            otherArmRotX = 25f;
//            otherArmRotY = -15f;
//            otherArmRotZ = -15f;
//        }
//        otherArm.xRot = (float) Math.toRadians(otherArmRotX);
//        otherArm.yRot = (float) Math.toRadians(otherArmRotY);
//        otherArm.zRot = (float) Math.toRadians(otherArmRotZ);
    }

    // Item model animation for swinging the Splatana horizontally - Only affects the item model, not the arm!
    public static <T extends HumanoidRenderState, S extends ArmedEntityRenderState> void thirdPersonItemSwing(HumanoidModel<T> model, S state, PoseStack poseStack) {
        SplatanaWeaponRenderData splatanaData = state.getData(SPLATANA_RENDER_DATA);
        if (splatanaData == null || splatanaData.swingAnim() <= 0) return;

        boolean reversed = splatanaData.reverseSwing();
        HumanoidArm arm = reversed ? state.attackArm.getOpposite() : state.attackArm;
        ModelPart mainArm = model.getArm(arm);
        ModelPart otherArm = model.getArm(arm == HumanoidArm.RIGHT ? HumanoidArm.LEFT : HumanoidArm.RIGHT);

        float animTime = splatanaData.swingAnim(); // Full animation (including draw back after attack cooldown ends)
        float attackTime = state.attackTime; // Swing only animation (only swing left/right with attack cooldown)
        boolean swappedHands = attackTime > 0.5f;

        float splatanaRotX;
        float splatanaRotY;
        float splatanaRotZ;
        float pivotX = 0;
        float pivotY = swappedHands ? -0.3f : -0.15f;
        float pivotZ = 0;
        if (attackTime > 0) { // Swing anim
            float progress = Ease.inOutQuint(attackTime);
            splatanaRotX = progress * 270;
            splatanaRotY = (float) (90f - Math.toDegrees(mainArm.zRot));
            splatanaRotZ = (float) (180f + Math.toDegrees(mainArm.xRot));
            if (swappedHands) {
                splatanaRotY = (float) (90f - Math.toDegrees(otherArm.zRot));
                splatanaRotZ = (float) (180f + Math.toDegrees(otherArm.xRot));
            }
        } else { // Return to idle anim
            splatanaRotX = 270f;
            splatanaRotY = (float) (90f - Math.toDegrees(otherArm.zRot));
            splatanaRotZ = (float) (180f + Math.toDegrees(otherArm.xRot));
            pivotY = reversed ? -0.15f : -0.3f;
        }

        if (reversed) {
            splatanaRotX *= -1;
            if (swappedHands) pivotY += 0.15f;
        }

        poseStack.rotateAround(Axis.YN.rotationDegrees(splatanaRotY), pivotX, pivotY, pivotZ);
        poseStack.rotateAround(Axis.ZN.rotationDegrees(splatanaRotZ), pivotX, pivotY, pivotZ);
        poseStack.rotateAround(Axis.XN.rotationDegrees(splatanaRotX), pivotX, pivotY, pivotZ);

//        poseStack.rotateAround(Axis.ZN.rotationDegrees(splatanaRotZ), 0, pivotY, 0);
//        poseStack.rotateAround(Axis.YN.rotationDegrees(splatanaRotY), 0, pivotY, 0);
//        poseStack.rotateAround(Axis.ZN.rotationDegrees(splatanaRotX), 0, pivotY, 0);



//        if (state.attackTime <= 0) return;
//        SplatanaWeaponRenderData splatanaData = state.getData(SPLATANA_RENDER_DATA);
//        if (splatanaData == null || splatanaData.swingAnim() <= 0) return;
//        float swingAnim = state.attackTime;
//        boolean swappedHands = state.attackTime <= 0.5f;

//        float splatanaRotX = 0f + (Ease.inOutQuint(swingAnim) * 15f);
//        float splatanaRotY = -85f;
//        float splatanaRotZ = 170f - (Ease.inOutQuint(swingAnim) * 270f);
//
//        float pivotY = -0.15f + (Ease.inOutQuint(swingAnim) * -0.15f);
//
//        poseStack.rotateAround(Axis.ZN.rotationDegrees(splatanaRotZ), 0, pivotY, 0);
//        poseStack.rotateAround(Axis.YN.rotationDegrees(splatanaRotY), 0, pivotY, 0);
//        poseStack.rotateAround(Axis.ZN.rotationDegrees(splatanaRotX), 0, pivotY, 0);
    }

    private static HumanoidArm swapArm(HumanoidArm arm) {
        return (arm == HumanoidArm.RIGHT ? HumanoidArm.LEFT : HumanoidArm.RIGHT);
    }

    private static float radians(float degrees) {
        return (float) Math.toRadians(degrees);
    }
}
