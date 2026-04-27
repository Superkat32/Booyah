package net.superkat.booyah.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.superkat.booyah.Booyah;
import net.superkat.booyah.entity.Balloon;
import net.superkat.booyah.entity.client.model.BalloonModel;
import net.superkat.booyah.entity.client.state.BalloonRenderState;
import org.jspecify.annotations.NonNull;

public class BalloonRenderer extends LivingEntityRenderer<Balloon, BalloonRenderState, BalloonModel> {
    public static final Identifier TEXTURE = Booyah.id("textures/entity/balloon/blue.png");

    public BalloonRenderer(EntityRendererProvider.Context context) {
        super(context, new BalloonModel(context.bakeLayer(BalloonModel.LAYER_LOCATION)), 0.4f);
    }

    @Override
    public void submit(BalloonRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0, -1.2f, 0);
        super.submit(state, poseStack, submitNodeCollector, camera);
        poseStack.popPose();
    }

    @Override
    public void extractRenderState(Balloon entity, BalloonRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.bodyRot = entity.getYRot();
        state.idleAnimationState.copyFrom(entity.idleAnimationState);
    }

    @Override
    protected boolean shouldShowName(Balloon entity, double distanceToCameraSq) {
        return entity.isCustomNameVisible();
    }

    @Override
    public @NonNull Identifier getTextureLocation(BalloonRenderState state) {
        return TEXTURE;
    }

    @Override
    public BalloonRenderState createRenderState() {
        return new BalloonRenderState();
    }
}
