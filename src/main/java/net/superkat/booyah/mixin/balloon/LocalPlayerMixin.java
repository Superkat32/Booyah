package net.superkat.booyah.mixin.balloon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.superkat.booyah.block.BalloonChaseBlockEntity;
import net.superkat.booyah.block.client.screen.BalloonChaseEditBlockScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin extends PlayerMixin {

    @Shadow
    @Final
    protected Minecraft minecraft;

    @Override
    public void booyah$openBalloonBlockEditScreen(BalloonChaseBlockEntity blockEntity) {
        this.minecraft.setScreen(new BalloonChaseEditBlockScreen(blockEntity));
    }
}
