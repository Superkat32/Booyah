package net.superkat.booyah;

import net.fabricmc.api.ClientModInitializer;
import net.superkat.booyah.network.BooyahClientNetworkHandler;
import net.superkat.booyah.particles.BooyahParticlesClient;
import net.superkat.booyah.render.BooyahRenderer;

public class BooyahClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BooyahRenderer.init();
        BooyahClientNetworkHandler.init();
        BooyahParticlesClient.init();
    }

}
