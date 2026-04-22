package net.superkat.booyah.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.superkat.booyah.Booyah;
import net.superkat.booyah.entity.SplatanaSwipe;
import net.superkat.booyah.entity.client.model.SplatanaSwipeModel;
import net.superkat.booyah.entity.client.state.SplatanaSwipeRenderState;

@Environment(EnvType.CLIENT)
public class SplatanaSwipeRenderer extends EntityRenderer<SplatanaSwipe, SplatanaSwipeRenderState> {
    public static final Identifier TEXTURE = Booyah.id("textures/entity/splatana/swipe.png");
    protected final SplatanaSwipeModel model;

    public SplatanaSwipeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SplatanaSwipeModel(context.bakeLayer(SplatanaSwipeModel.LAYER_LOCATION));
    }

    @Override
    public void submit(SplatanaSwipeRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.scale(1.75f, 1.75f, 1.75f);
        poseStack.translate(0, -1.35f, 0);
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot + 180f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot));
//        poseStack.translate(0.0F, 0.15F, 0.0F);
//        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0F));
//        poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot));
//        submitNodeCollector.submitModel(this.model, state, poseStack, );
        submitNodeCollector.submitModel(this.model, state, poseStack, this.model.renderType(TEXTURE), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public SplatanaSwipeRenderState createRenderState() {
        return new SplatanaSwipeRenderState();
    }

    @Override
    public void extractRenderState(SplatanaSwipe entity, SplatanaSwipeRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.yRot = entity.getYRot(partialTicks);
    }
}
