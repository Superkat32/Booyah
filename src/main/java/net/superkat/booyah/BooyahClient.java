package net.superkat.booyah;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.superkat.booyah.entity.client.BooyahEntitiesClient;
import net.superkat.booyah.item.client.SplatanaCharge;
import net.superkat.booyah.network.BooyahClientNetworkHandler;
import net.superkat.booyah.particles.BooyahParticlesClient;
import net.superkat.booyah.render.BooyahRenderer;

public class BooyahClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ConditionalItemModelProperties.ID_MAPPER.put(Booyah.id("stamper"), SplatanaCharge.CODEC);
        BooyahRenderer.init();
        BooyahClientNetworkHandler.init();
        BooyahParticlesClient.init();
        BooyahEntitiesClient.init();
    }

}
