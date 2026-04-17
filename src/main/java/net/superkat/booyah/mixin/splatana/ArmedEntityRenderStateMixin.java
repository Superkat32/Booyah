package net.superkat.booyah.mixin.splatana;

import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.entity.LivingEntity;
import net.superkat.booyah.duck.splatana.SplatanaPlayer;
import net.superkat.booyah.item.client.SplatanaAnimations;
import net.superkat.booyah.render.data.SplatanaWeaponRenderData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmedEntityRenderState.class)
public class ArmedEntityRenderStateMixin {

    @Inject(method = "extractArmedEntityRenderState", at = @At("TAIL"))
    private static void booyah$extractSplatanaWeaponRenderData(LivingEntity entity, ArmedEntityRenderState state, ItemModelResolver itemModelResolver, float partialTicks, CallbackInfo ci) {
        if (entity instanceof SplatanaPlayer player) {
            state.setData(SplatanaAnimations.SPLATANA_RENDER_DATA, new SplatanaWeaponRenderData(
                    player.booyah$getSplatanaAttackAnim(partialTicks), player.booyah$reverseSplatanaSwing()
            ));
        }
    }

}
