package net.superkat.booyah.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.superkat.booyah.duck.balloon.BalloonBlockEditCapablePlayer;
import org.jspecify.annotations.Nullable;

public class BalloonChaseBlock extends BaseEntityBlock {
    public static final MapCodec<BalloonChaseBlock> CODEC = simpleCodec(BalloonChaseBlock::new);
    public BalloonChaseBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player.canUseGameMasterBlocks()
            && level.getBlockEntity(pos) instanceof BalloonChaseBlockEntity balloonChaseBlockEntity
            && player instanceof BalloonBlockEditCapablePlayer balloonPlayer
        ) {

            if (itemStack.is(BooyahBlocks.BALLOON_CHASE_BLOCK.asItem())) {
                if (balloonPlayer.booyah$isConnectingBalloonBlocks()) {
                    BlockPos initPos = balloonPlayer.booyah$getConnectingBalloonBlockPos();
                    if (!pos.equals(initPos) && level.getBlockEntity(initPos) instanceof BalloonChaseBlockEntity initBalloonBlock && initBalloonBlock.balloonEntry != null) {
                        balloonChaseBlockEntity.updateBalloonEntry(
                                initBalloonBlock.balloonChainId, initBalloonBlock.balloonEntry.createNext(pos)
                        );
                    }
                    balloonPlayer.booyah$setConnectingBalloonBlocks(null);
                } else {
                    if (balloonChaseBlockEntity.balloonChainId.isBlank()) {
                        balloonPlayer.booyah$openBalloonBlockEditScreen(balloonChaseBlockEntity);
                    } else {
                        balloonPlayer.booyah$setConnectingBalloonBlocks(pos);
                    }
                }
            } else if (level.isClientSide()) {
                balloonPlayer.booyah$openBalloonBlockEditScreen(balloonChaseBlockEntity);
            }

            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new BalloonChaseBlockEntity(worldPosition, blockState);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return context.isHoldingItem(BooyahBlocks.BALLOON_CHASE_BLOCK.asItem()) ? Shapes.block() : Shapes.empty();
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1f;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
