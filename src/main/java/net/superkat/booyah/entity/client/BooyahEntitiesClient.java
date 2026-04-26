package net.superkat.booyah.entity.client;

import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.superkat.booyah.entity.BooyahEntities;
import net.superkat.booyah.entity.client.model.BalloonModel;
import net.superkat.booyah.entity.client.model.SplatanaSwipeModel;
import net.superkat.booyah.entity.client.renderer.BalloonRenderer;
import net.superkat.booyah.entity.client.renderer.SplatanaSwipeRenderer;

public class BooyahEntitiesClient {

    public static void init() {
        ModelLayerRegistry.registerModelLayer(SplatanaSwipeModel.LAYER_LOCATION, SplatanaSwipeModel::createBodyLayer);
        EntityRenderers.register(BooyahEntities.SPLATANA_SWIPE, SplatanaSwipeRenderer::new);

        ModelLayerRegistry.registerModelLayer(BalloonModel.LAYER_LOCATION, BalloonModel::createBodyLayer);
        EntityRenderers.register(BooyahEntities.BALLOON_CHASE, BalloonRenderer::new);
    }

}
