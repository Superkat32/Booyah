package net.superkat.booyah.item.client;

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
import net.minecraft.world.item.ItemStack;
import net.superkat.booyah.item.BooyahItems;
import net.superkat.booyah.render.data.SplatanaWeaponRenderData;

// This whole class is a nightmare of nonsense
public class SplatanaAnimations {

    public static final RenderStateDataKey<SplatanaWeaponRenderData> SPLATANA_RENDER_DATA = RenderStateDataKey.create(() -> "Splatana swing & charge animation render data");

    public static <T extends ArmedEntityRenderState> HumanoidArm getArmToTranslateSplatanaTo(T state, HumanoidArm original) {
        SplatanaWeaponRenderData splatanaData = state.getData(SPLATANA_RENDER_DATA);
        if (!BooyahItems.isSplatana(state.rightHandItemStack) || splatanaData == null || splatanaData.swingAnim() == 0) return original;

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

        float bodyRotY = 0f;
//        float modelRotY = 0f;
        if (attackTime > 0) {
            float progress = Ease.inOutExpo(attackTime);
            float preSwapProgress = Ease.inExpo(swappedHands ? 1 : (attackTime) * 2);
            float postSwapProgress = Ease.outExpo(swappedHands ? (Math.max(0, attackTime - 0.5f)) * 2 : 0);

            mainArmRotX = -45f;
            mainArmRotY = 0 + (progress * -45f);
            mainArmRotZ = 10f + (progress * -40f);
            otherArmRotX = -35f + (progress * 95f);
            otherArmRotY = 45f + (preSwapProgress * -45f);
            otherArmRotZ = 35f + (progress * -70f) + (-45f * (swappedHands ? 1 - postSwapProgress : preSwapProgress));

            bodyRotY = (25f + (progress * -50f)) * (state.isCrouching ? 0.25f : 1);
//            modelRotY = state.isCrouching ? 0 : 5f + (progress * -10f);
        } else {
            mainArmRotX = -45f;
            mainArmRotY = -45f;
            mainArmRotZ = -30f;
            otherArmRotX = 60f;
            otherArmRotZ = -35f;

            bodyRotY = -25f * (state.isCrouching ? 0.25f : 1);
//            modelRotY = state.isCrouching ? 0 : -5f;
        }

        if (reversed) {
            mainArmRotY *= -1;
            mainArmRotZ *= -1;
            otherArmRotY *= -1;
            otherArmRotZ *= -1;
            bodyRotY *= -1;
//            modelRotY *= -1f;
        }

        mainArm.xRot = radians(mainArmRotX);
        mainArm.yRot = radians(mainArmRotY);
        mainArm.zRot = radians(mainArmRotZ);
        otherArm.xRot = radians(otherArmRotX);
        otherArm.yRot = radians(otherArmRotY);
        otherArm.zRot = radians(otherArmRotZ);

        model.body.yRot += radians(bodyRotY);
//        model.root().yRot += radians(modelRotY);
//        model.head.yRot -= radians(modelRotY);
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

        float splatanaRotX = 0;
        float splatanaRotY = 0;
        float splatanaRotZ = 0;
        float pivotX = 0;
        float pivotY = 0;
        float pivotZ = 0;
        float undoArmX = swappedHands ? otherArm.xRot : mainArm.xRot;
        float undoArmY = swappedHands ? otherArm.zRot : mainArm.zRot;
        float undoArmZ = swappedHands ? otherArm.yRot : mainArm.yRot;

        if (attackTime > 0) {
            float progress = Ease.inOutExpo(attackTime);
            float preSwapProgress = Ease.inExpo(swappedHands ? 0 : (attackTime) * 2);
            float postSwapProgress = Ease.outExpo(swappedHands ? (Math.max(0, attackTime - 0.5f)) * 2 : 0);
//            splatanaRotX = 0; // forget rot x this nimble gimble gooble gabble lock nonsesne is asbolute trash
            splatanaRotY = 90;
            splatanaRotZ = -170 + (progress * 260);
            pivotX = 0.05f + (progress * -0.05f);
            pivotY = -0.15f + (progress * -0.1f);
            pivotZ = 0 + (progress * 0.1f) + (-0.1f * preSwapProgress);
            if (reversed) {
                pivotX = 0 + (progress * 0.05f);
                pivotY = -0.25f + (progress * 0.1f);
                pivotZ = 0.1f + (progress * -0.1f) + (0.1f * preSwapProgress);
            }
        } else {
            splatanaRotY = 90;
            splatanaRotZ = 90;
            pivotY = -0.25f;
            pivotZ = 0.1f;
            if (reversed) {
                pivotX = 0.05f;
                pivotY = -0.15f;
                pivotZ = 0;
            }
            undoArmX = otherArm.xRot;
            undoArmY = otherArm.zRot;
            undoArmZ = otherArm.yRot;
        }

        if (reversed) {
            splatanaRotY *= -1;
            splatanaRotZ *= -1;
        }

        // Undo arm rotations
        poseStack.rotateAround(Axis.XP.rotation(undoArmX), 0, 0, 0);
        poseStack.rotateAround(Axis.YP.rotation(undoArmY), 0, 0, 0);
        poseStack.rotateAround(Axis.ZP.rotation(undoArmZ), 0, 0, 0);

//        poseStack.rotateAround(Axis.XP.rotationDegrees(splatanaRotX), pivotX, pivotY, pivotZ);
        poseStack.rotateAround(Axis.ZP.rotationDegrees(splatanaRotZ), pivotX, pivotY, 0);
        poseStack.rotateAround(Axis.YP.rotationDegrees(splatanaRotY), pivotZ, 0, 0);
    }

    private static HumanoidArm swapArm(HumanoidArm arm) {
        return (arm == HumanoidArm.RIGHT ? HumanoidArm.LEFT : HumanoidArm.RIGHT);
    }

    private static float radians(float degrees) {
        return (float) Math.toRadians(degrees);
    }
}
