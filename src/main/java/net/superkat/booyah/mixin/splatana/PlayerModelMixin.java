package net.superkat.booyah.mixin.splatana;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.superkat.booyah.item.BooyahItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerModel.class)
public class PlayerModelMixin {

    @ModifyVariable(method = "translateToHand(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lnet/minecraft/world/entity/HumanoidArm;Lcom/mojang/blaze3d/vertex/PoseStack;)V", at = @At("HEAD"), name = "arm", argsOnly = true)
    private HumanoidArm booyah$modifyTranslatedArmForSplatanaSwingAnimation(HumanoidArm arm, @Local(argsOnly = true, name = "state") AvatarRenderState state) {
        if (arm == HumanoidArm.RIGHT && state.rightHandItemStack.is(BooyahItems.SPLATANA_STAMPER) && state.attackTime > 0.5) {
            return HumanoidArm.LEFT;
        }
        return arm;
    }

}
