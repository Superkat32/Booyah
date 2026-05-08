package net.superkat.booyah.block.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.CommonColors;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import net.superkat.booyah.Booyah;
import net.superkat.booyah.block.BalloonChaseBlock;
import net.superkat.booyah.block.BalloonChaseBlockEntity;
import net.superkat.booyah.block.BooyahBlocks;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class BalloonChaseBlockRenderer implements BlockEntityRenderer<BalloonChaseBlockEntity, BalloonChaseBlockRenderState> {
    public static final Identifier ICON_TEXTURE = Booyah.id("textures/entity/balloon/icon.png");
    public static final Identifier ARROW_TEXTURE = Booyah.id("textures/entity/balloon/arrow.png");

    public final Font font;
    public final ItemModelResolver itemModelResolver;
    public BalloonChaseBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.font = context.font();
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public void submit(BalloonChaseBlockRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (!state.render || state.chainId == null) return;
        // Icon Rendering
        poseStack.pushPose();
        poseStack.rotateAround(camera.orientation, 0.5f, 0.5f, 0.5f);
        submitNodeCollector.submitCustomGeometry(
                poseStack,
                RenderTypes.entityTranslucent(ICON_TEXTURE),
                (pose, buffer) ->
                        renderQuad(pose, buffer, state.color, 0, 1, 0, 0, 1, 0, 0f, 1f, 0f, 1f)
        );
        // Pop Reward Rendering
        if (state.rewardForPop && state.popRewardItemRenderState != null) {
            poseStack.pushPose();
            poseStack.translate(0.5f, 0.65f, 0.1f);
            poseStack.scale(0.25f, 0.25f, 0.25f);
            state.popRewardItemRenderState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
        poseStack.popPose();

        // Info Rendering
        poseStack.pushPose();

        poseStack.translate(0.5, 1, 0.5);
        poseStack.mulPose(Axis.XP.rotationDegrees(90));
        poseStack.scale(1 / 18f, 1 / 18f, 1 / 18f);

        // Chain ID
        poseStack.pushPose();
        poseStack.scale(0.75f, 0.75f, 0.75f);
        poseStack.translate(0, -4, 0);
        Component idText = Component.literal(state.chainId);
        submitNodeCollector.submitText(
                poseStack, -font.width(idText) / 2f, -4f,
                idText.getVisualOrderText(), false, Font.DisplayMode.POLYGON_OFFSET,
                state.lightCoords, CommonColors.WHITE, 0, CommonColors.BLACK
        );
        poseStack.popPose();

        // Chain Index
        poseStack.pushPose();
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.translate(0, 8, 0);
        Component indexText = Component.literal(state.entryIndex);
        submitNodeCollector.submitText(
                poseStack, -font.width(indexText) / 2f, -4f,
                indexText.getVisualOrderText(), false, Font.DisplayMode.POLYGON_OFFSET,
                state.lightCoords, CommonColors.WHITE, 0, CommonColors.BLACK
        );
        poseStack.popPose();

        // Arrow
        poseStack.pushPose();
        poseStack.scale(4f, 4f, 4f);
        poseStack.translate(0, 1f, 0.1f);
        poseStack.rotateAround(Axis.ZP.rotationDegrees(state.balloonSpawnYaw), 0, -1f, 0);
        poseStack.mulPose(Axis.ZP.rotationDegrees(45));
        submitNodeCollector.submitCustomGeometry(
                poseStack,
                RenderTypes.entityTranslucent(ARROW_TEXTURE),
                (pose, buffer) ->
                        renderQuad(pose, buffer, CommonColors.WHITE, 0, 1, 0, 0, 1, 0, 0f, 1f, 0f, 1f)
        );
        poseStack.popPose();

        poseStack.popPose();
    }

    @Override
    public void extractRenderState(BalloonChaseBlockEntity blockEntity, BalloonChaseBlockRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && (
                player.getMainHandItem().is(BooyahBlocks.BALLOON_CHASE_BLOCK.asItem())
                || player.getOffhandItem().is(BooyahBlocks.BALLOON_CHASE_BLOCK.asItem())
                || (player.canUseGameMasterBlocks() && player.distanceToSqr(blockEntity.getBlockPos().getCenter()) <= 8))
        ) {
            state.render = true;
            state.chainId = blockEntity.balloonChainId.isBlank() ? "Empty!" : blockEntity.balloonChainId;
            state.entryIndex = blockEntity.balloonEntry == null ? "" : String.valueOf(blockEntity.balloonEntry.index());
            state.balloonSpawnYaw = blockEntity.balloonEntry == null ? 0 : blockEntity.balloonEntry.balloonYaw();

            if (blockEntity.balloonEntry != null) {
//                Random random = new Random(blockEntity.balloonEntry.index() * 2048L + 1000);
//                float hue = random.nextFloat();
//                float saturation = 0.7f + random.nextFloat() * 0.3f;
//                float value = 0.8f + random.nextFloat() * 0.2f;
//                state.color = new HSVColor(hue, saturation, value).getARGB();

                state.color = BalloonChaseBlock.getRandomColorForIndex(blockEntity.balloonEntry.index()).getARGB();

                state.rewardForPop = blockEntity.balloonEntry.rewardItemOnPop();
                if (state.rewardForPop) {
                    ItemStackRenderState itemStackRenderState = new ItemStackRenderState();
                    this.itemModelResolver.updateForTopItem(itemStackRenderState, blockEntity.balloonEntry.popReward(), ItemDisplayContext.ON_SHELF, blockEntity.getLevel(), null, 0);
                    state.popRewardItemRenderState = itemStackRenderState;
                }
            }
        }
    }

    @Override
    public BalloonChaseBlockRenderState createRenderState() {
        return new BalloonChaseBlockRenderState();
    }

    private static void renderQuad(
            final PoseStack.Pose pose,
            final VertexConsumer builder,
            final int color,
            final int beamStart,
            final int beamEnd,
            final float wnx,
            final float wnz,
            final float enx,
            final float enz,
            final float uu1,
            final float uu2,
            final float vv1,
            final float vv2
    ) {
        addVertex(pose, builder, color, beamEnd, wnx, wnz, uu2, vv1);
        addVertex(pose, builder, color, beamStart, wnx, wnz, uu2, vv2);
        addVertex(pose, builder, color, beamStart, enx, enz, uu1, vv2);
        addVertex(pose, builder, color, beamEnd, enx, enz, uu1, vv1);
    }

    private static void addVertex(
            final PoseStack.Pose pose, final VertexConsumer builder, final int color, final int y, final float x, final float z, final float u, final float v
    ) {
        builder.addVertex(pose, x, (float)y, z)
                .setColor(color)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(15728880)
                .setNormal(pose, 1.0F, 1.0F, 1.0F);
    }
}
