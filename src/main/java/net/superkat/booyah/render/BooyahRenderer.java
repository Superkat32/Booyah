package net.superkat.booyah.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.CommonColors;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.superkat.booyah.Booyah;
import net.superkat.booyah.duck.comm.BooyahablePlayer;
import net.superkat.booyah.render.data.BooyahRenderData;
import org.joml.Matrix4f;

public class BooyahRenderer {

    public static final RenderStateDataKey<BooyahRenderData> BOOYAH_RENDER_DATA = RenderStateDataKey.create(() -> "Booyah \"Booyah!\" render data");
    public static final Identifier BOOYAH_HUD_ID = Booyah.id("booyah_hud_element");

    public static void init() {

        HudElementRegistry.attachElementBefore(VanillaHudElements.TITLE_AND_SUBTITLE, BOOYAH_HUD_ID, (graphics, deltaTracker) -> {
            if (Minecraft.getInstance().player == null || !(Minecraft.getInstance().player instanceof BooyahablePlayer booyahablePlayer)) return;
            if (!Minecraft.getInstance().options.getCameraType().isFirstPerson()) return;
            if (booyahablePlayer.booyah$booyahTicks() <= 0) return;
            graphics.pose().pushMatrix();
            graphics.pose().translate(graphics.guiWidth() / 2f, graphics.guiHeight() - 68f);

            float scale = 1f + Mth.sin((Minecraft.getInstance().player.tickCount - booyahablePlayer.booyah$tickCountOfBooyah()) * 0.5) * 0.1f;
            graphics.pose().scale(scale, scale);

            float alpha = 1f;
            int booyahTicks = booyahablePlayer.booyah$booyahTicks();
            if (booyahTicks <= 2) { // Fade in
                alpha = booyahTicks / 2f;
            } else if (booyahTicks >= 60 - 5) { // Fade out
                alpha = (60 - booyahTicks) / 5f;
            }
            int color = ARGB.color(alpha, CommonColors.WHITE);

            Component text = Component.literal("Booyah!");
            int width = Minecraft.getInstance().font.width(text);
            graphics.textWithBackdrop(Minecraft.getInstance().font, text, -width / 2, 8, width, color);
            graphics.pose().popMatrix();
//            Vec3 attachmentPoint = player.isLocalPlayer() ? new Vec3(0, entity.getBbHeight(), 0) : state.nameTagAttachment;
//            state.setData(BooyahRenderer.BOOYAH_RENDER_DATA, new BooyahRenderData(
//                    attachmentPoint, player.isLocalPlayer() ? 0 : -10, booyahablePlayer.booyah$tickCountOfBooyah(), booyahablePlayer.booyah$booyahTicks())
//            );

        });

        // World rendering
        LevelRenderEvents.AFTER_TRANSLUCENT_FEATURES.register(context -> {
            for (EntityRenderState entityRenderState : context.levelState().entityRenderStates) {
                BooyahRenderData booyahRenderData = entityRenderState.getData(BOOYAH_RENDER_DATA);
                if (booyahRenderData == null || booyahRenderData.booyahTicks() <= 0) continue;

                Minecraft minecraft = Minecraft.getInstance();
                CameraRenderState camera = context.levelState().cameraRenderState;
                PoseStack poseStack = context.poseStack();
                Vec3 nameTagAttachment = booyahRenderData.booyahTagAttachment();
                Component text = Component.literal("Booyah!");

                poseStack.pushPose();
                poseStack.translate(entityRenderState.x - camera.pos.x, entityRenderState.y - camera.pos.y, entityRenderState.z - camera.pos.z);
                poseStack.translate(nameTagAttachment.x, nameTagAttachment.y + 0.5, nameTagAttachment.z);
                poseStack.mulPose(camera.orientation);
                poseStack.scale(0.025f, -0.025f, 0.025f);

                float scale = 1f + Mth.sin((entityRenderState.ageInTicks - booyahRenderData.tickCountOfBooyah()) * 0.5) * 0.1f;
                poseStack.scale(scale, scale, scale);

                Matrix4f pose = new Matrix4f(poseStack.last().pose());
                float x = -minecraft.font.width(text) / 2f;

                float alpha = 1f;
                int booyahTicks = booyahRenderData.booyahTicks();
                if (booyahTicks <= 2) { // Fade in
                    alpha = booyahTicks / 2f;
                } else if (booyahTicks >= 60 - 5) { // Fade out
                    alpha = (60 - booyahTicks) / 5f;
                }
                int color = ARGB.color(alpha, CommonColors.WHITE);

                minecraft.font.drawInBatch(
                        text, x, booyahRenderData.yOffset(),
                        color, true,
                        pose, context.bufferSource(), Font.DisplayMode.NORMAL,
                        0, LightCoordsUtil.lightCoordsWithEmission(entityRenderState.lightCoords, 2)
                );
                poseStack.popPose();
            }
        });
    }

}
