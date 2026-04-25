package net.superkat.booyah.block.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

@Environment(EnvType.CLIENT)
public class BalloonChaseBlockRenderState extends BlockEntityRenderState {
    public boolean render = false;
    public String chainId = "";
    public String entryIndex = "";
}
