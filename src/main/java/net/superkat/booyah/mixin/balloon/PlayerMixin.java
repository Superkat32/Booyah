package net.superkat.booyah.mixin.balloon;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.superkat.booyah.block.BalloonChaseBlockEntity;
import net.superkat.booyah.block.BooyahBlocks;
import net.superkat.booyah.duck.balloon.BalloonBlockEditCapablePlayer;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin implements BalloonBlockEditCapablePlayer {
    @Unique
    @Nullable
    public BlockPos booyah$initConnectingBalloonBlockPos = null;

    @Inject(method = "tick", at = @At(value = "TAIL"))
    public void booyah$updatePlayerBalloonBlockConnection(CallbackInfo ci) {
        Player self = (Player) (Object) this;
        if (this.booyah$isConnectingBalloonBlocks()
                && !self.getMainHandItem().is(BooyahBlocks.BALLOON_CHASE_BLOCK.asItem())
                && !self.getOffhandItem().is(BooyahBlocks.BALLOON_CHASE_BLOCK.asItem())
        ) {
            this.booyah$initConnectingBalloonBlockPos = null;
        }
    }

    @Override
    public void booyah$openBalloonBlockEditScreen(BalloonChaseBlockEntity blockEntity) {
        // NO-OP - Client only!
    }

    @Override
    public boolean booyah$isConnectingBalloonBlocks() {
        return this.booyah$initConnectingBalloonBlockPos != null;
    }

    @Override
    public BlockPos booyah$getConnectingBalloonBlockPos() {
        return this.booyah$initConnectingBalloonBlockPos;
    }

    @Override
    public void booyah$setConnectingBalloonBlocks(BlockPos initPos) {
        this.booyah$initConnectingBalloonBlockPos = initPos;
    }
}
