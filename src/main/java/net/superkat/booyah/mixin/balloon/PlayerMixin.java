package net.superkat.booyah.mixin.balloon;

import net.minecraft.world.entity.player.Player;
import net.superkat.booyah.block.BalloonChaseBlockEntity;
import net.superkat.booyah.duck.balloon.BalloonBlockEditCapablePlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Player.class)
public class PlayerMixin implements BalloonBlockEditCapablePlayer {
    @Override
    public void booyah$openBalloonBlockEditScreen(BalloonChaseBlockEntity blockEntity) {
        // NO-OP - Client only!
    }
}
