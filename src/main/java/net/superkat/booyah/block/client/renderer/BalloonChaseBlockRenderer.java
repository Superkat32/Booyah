package net.superkat.booyah.block.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;
import net.minecraft.world.phys.Vec3;
import net.superkat.booyah.block.BalloonChaseBlockEntity;
import net.superkat.booyah.block.BooyahBlocks;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class BalloonChaseBlockRenderer implements BlockEntityRenderer<BalloonChaseBlockEntity, BalloonChaseBlockRenderState> {
    public final Font font;
    public BalloonChaseBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.font = context.font();
    }

    @Override
    public void submit(BalloonChaseBlockRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (!state.render || state.chainId == null) return;
        poseStack.pushPose();

        poseStack.translate(0.5, 1, 0.5);
        poseStack.mulPose(Axis.XP.rotationDegrees(90));
        poseStack.scale(1 / 18f, 1 / 18f, 1 / 18f);

        poseStack.pushPose();
        poseStack.scale(0.75f, 0.75f, 0.75f);
        poseStack.translate(0, -4, 0);
        Component idText = Component.literal(state.chainId);
        submitNodeCollector.submitText(
                poseStack, -font.width(idText) / 2f, -4f,
                idText.getVisualOrderText(), true, Font.DisplayMode.SEE_THROUGH,
                state.lightCoords, CommonColors.WHITE, 0, 0
        );
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.translate(0, 8, 0);
        Component indexText = Component.literal(state.entryIndex);
        submitNodeCollector.submitText(
                poseStack, -font.width(indexText) / 2f, -4f,
                indexText.getVisualOrderText(), true, Font.DisplayMode.SEE_THROUGH,
                state.lightCoords, CommonColors.WHITE, 0, 0
        );
        poseStack.popPose();

        poseStack.popPose();
    }

    @Override
    public void extractRenderState(BalloonChaseBlockEntity blockEntity, BalloonChaseBlockRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        if (Minecraft.getInstance().player != null
                && Minecraft.getInstance().player.getMainHandItem().is(BooyahBlocks.BALLOON_CHASE_BLOCK.asItem())
        ) {
            state.render = true;
            state.chainId = blockEntity.balloonChainId.isBlank() ? "Empty!" : blockEntity.balloonChainId;
            state.entryIndex = blockEntity.balloonEntry == null ? "" : String.valueOf(blockEntity.balloonEntry.index());
        }
    }

    @Override
    public BalloonChaseBlockRenderState createRenderState() {
        return new BalloonChaseBlockRenderState();
    }
}
