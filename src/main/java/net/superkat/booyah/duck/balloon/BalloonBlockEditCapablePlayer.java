package net.superkat.booyah.duck.balloon;

import net.minecraft.core.BlockPos;
import net.superkat.booyah.block.BalloonChaseBlockEntity;

// golly I'm good at naming stuff
public interface BalloonBlockEditCapablePlayer {

    void booyah$openBalloonBlockEditScreen(BalloonChaseBlockEntity blockEntity);

    boolean booyah$isConnectingBalloonBlocks();
    BlockPos booyah$getConnectingBalloonBlockPos();
    void booyah$setConnectingBalloonBlocks(BlockPos initPos);

}
