package net.superkat.booyah.block.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.util.CommonColors;

@Environment(EnvType.CLIENT)
public class BalloonChaseBlockRenderState extends BlockEntityRenderState {
    public boolean render = false;
    public int color = CommonColors.WHITE;
    public String chainId = "";
    public String entryIndex = "";
    public float balloonSpawnYaw = 0;
}
