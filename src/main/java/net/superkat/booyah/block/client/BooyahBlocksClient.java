package net.superkat.booyah.block.client;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.superkat.booyah.block.BooyahBlocks;
import net.superkat.booyah.block.client.renderer.BalloonChaseBlockRenderer;

public class BooyahBlocksClient {

    public static void init() {
        BlockEntityRenderers.register(BooyahBlocks.BALLOON_CHASE_BLOCK_ENTITY, BalloonChaseBlockRenderer::new);
    }

}
