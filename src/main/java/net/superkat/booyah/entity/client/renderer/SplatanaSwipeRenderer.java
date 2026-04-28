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
    public static final Identifier TEXTURE = Booyah.id("textures/entity/splatana/slash.png");
    protected final SplatanaSwipeModel model;

    public SplatanaSwipeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SplatanaSwipeModel(context.bakeLayer(SplatanaSwipeModel.LAYER_LOCATION));
    }

    @Override
    public void submit(SplatanaSwipeRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();

//        poseStack.scale(1.5f, 1.5f, 1.5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot + 180f));
        poseStack.mulPose(Axis.XP.rotationDegrees(state.xRot));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.zRot));
        poseStack.translate(0, -1.35f - (state.zRot == 90 ? 0.15f : 0), 0);
        // Main
        submitNodeCollector.submitModel(
                this.model, state, poseStack, this.model.renderType(TEXTURE), state.lightCoords,
                OverlayTexture.NO_OVERLAY, state.mainColor, null, state.outlineColor, null
        );

        poseStack.scale(1, 1.5f, 1f);
        poseStack.translate(0, -0.5f, 0);
        // Alt 1
        poseStack.pushPose();
        poseStack.translate(state.extraModelAX, state.extraModelAY, 0.1f);
        submitNodeCollector.submitModel(
                this.model, state, poseStack, this.model.renderType(TEXTURE), state.lightCoords,
                OverlayTexture.NO_OVERLAY, state.extraColorA, null, state.outlineColor, null
        );
        poseStack.popPose();

        // Alt 2
        poseStack.pushPose();
        poseStack.translate(state.extraModelBX, state.extraModelBY, 0.15f);
        submitNodeCollector.submitModel(
                this.model, state, poseStack, this.model.renderType(TEXTURE), state.lightCoords,
                OverlayTexture.NO_OVERLAY, state.extraColorB, null, state.outlineColor, null
        );
        poseStack.popPose();

        // Alt 3
        poseStack.pushPose();
        poseStack.translate(state.extraModelCX, state.extraModelCY, 0.05f);
        submitNodeCollector.submitModel(
                this.model, state, poseStack, this.model.renderType(TEXTURE), state.lightCoords,
                OverlayTexture.NO_OVERLAY, state.extraColorC, null, state.outlineColor, null
        );
        poseStack.popPose();

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
        state.zRot = entity.getEntityData().get(SplatanaSwipe.ROT_Z);
        state.mainColor = entity.getEntityData().get(SplatanaSwipe.COLOR_ID);

        state.extraModelAX = entity.extraAnimA.getX(partialTicks);
        state.extraModelAY = entity.extraAnimA.getY(partialTicks);
        state.extraColorA = entity.getEntityData().get(SplatanaSwipe.ALT_COLOR_A_ID);

        state.extraModelBX = entity.extraAnimB.getX(partialTicks);
        state.extraModelBY = entity.extraAnimB.getY(partialTicks);
        state.extraColorB = entity.getEntityData().get(SplatanaSwipe.ALT_COLOR_B_ID);

        state.extraModelCX = entity.extraAnimC.getX(partialTicks);
        state.extraModelCY = entity.extraAnimC.getY(partialTicks);
        state.extraColorC = entity.getEntityData().get(SplatanaSwipe.ALT_COLOR_C_ID);
    }
}
