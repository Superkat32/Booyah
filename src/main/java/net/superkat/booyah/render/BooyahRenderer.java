package net.superkat.booyah.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.CommonColors;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.superkat.booyah.render.data.BooyahRenderData;
import org.joml.Matrix4f;

public class BooyahRenderer {

    public static final RenderStateDataKey<BooyahRenderData> BOOYAH_RENDER_DATA = RenderStateDataKey.create(() -> "Booyah \"Booyah!\" render data");

    public static void init() {
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
                if (booyahTicks < 2) { // Fade in
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
