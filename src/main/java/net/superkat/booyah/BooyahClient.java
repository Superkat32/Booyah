package net.superkat.booyah;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.superkat.booyah.block.client.BooyahBlocksClient;
import net.superkat.booyah.entity.client.BooyahEntitiesClient;
import net.superkat.booyah.item.client.BalloonConnectionTintSource;
import net.superkat.booyah.item.client.SplatanaCharge;
import net.superkat.booyah.network.BooyahClientNetworkHandler;
import net.superkat.booyah.particles.BooyahParticlesClient;
import net.superkat.booyah.render.BooyahRenderer;

public class BooyahClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ConditionalItemModelProperties.ID_MAPPER.put(Booyah.id("stamper"), SplatanaCharge.CODEC);
        ItemTintSources.ID_MAPPER.put(Booyah.id("balloon_chase_connection_colors"), BalloonConnectionTintSource.CODEC);
        BooyahRenderer.init();
        BooyahBlocksClient.init();
        BooyahClientNetworkHandler.init();
        BooyahParticlesClient.init();
        BooyahEntitiesClient.init();
    }

}
