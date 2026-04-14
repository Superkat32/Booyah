package net.superkat.booyah.mixin.comm;

import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.superkat.booyah.duck.comm.BooyahablePlayer;
import net.superkat.booyah.render.BooyahRenderer;
import net.superkat.booyah.render.data.BooyahRenderData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity> {

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("TAIL"))
    public void booyah$extractBooyahRenderData(AvatarlikeEntity entity, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
        if (!(entity instanceof Player player)) return;
        BooyahablePlayer booyahablePlayer = (BooyahablePlayer) player;

        Vec3 attachmentPoint = player.isLocalPlayer() ? new Vec3(0, entity.getBbHeight(), 0) : state.nameTagAttachment;
        state.setData(BooyahRenderer.BOOYAH_RENDER_DATA, new BooyahRenderData(
                attachmentPoint, player.isLocalPlayer() ? 0 : -10, booyahablePlayer.booyah$tickCountOfBooyah(), booyahablePlayer.booyah$booyahTicks())
        );
    }

}
