package net.superkat.booyah.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.superkat.booyah.color.HSVColor;
import net.superkat.booyah.duck.balloon.BalloonBlockEditCapablePlayer;
import org.jspecify.annotations.Nullable;

import java.util.Random;

public class BalloonChaseBlock extends BaseEntityBlock {
    public static final MapCodec<BalloonChaseBlock> CODEC = simpleCodec(BalloonChaseBlock::new);
    public BalloonChaseBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player.canUseGameMasterBlocks()
            && level.getBlockEntity(pos) instanceof BalloonChaseBlockEntity blockEntity
            && player instanceof BalloonBlockEditCapablePlayer balloonPlayer
        ) {
            if (itemStack.is(BooyahBlocks.BALLOON_CHASE_BLOCK.asItem()) && !player.isShiftKeyDown()) {
                // Handle block connection (or open screen if this chainId is blank)
                if (balloonPlayer.booyah$isConnectingBalloonBlocks()) {
                    BlockPos initPos = balloonPlayer.booyah$getConnectingBalloonBlockPos();
                    if (!pos.equals(initPos) && level.getBlockEntity(initPos) instanceof BalloonChaseBlockEntity initBalloonBlock && initBalloonBlock.balloonEntry != null) {
                        blockEntity.updateBalloonEntry(
                                initBalloonBlock.balloonChainId, initBalloonBlock.balloonEntry.createNext(pos)
                        );
                    }
                    balloonPlayer.booyah$setConnectingBalloonBlocks(null);
                } else {
                    if (blockEntity.balloonChainId.isBlank()) {
                        balloonPlayer.booyah$openBalloonBlockEditScreen(blockEntity);
                    } else {
                        balloonPlayer.booyah$setConnectingBalloonBlocks(pos);
                    }
                }
            } else if (blockEntity.balloonEntry != null && blockEntity.balloonEntry.rewardItemOnPop() && !itemStack.isEmpty()) {
                // Set entry pop reward item if possible
                blockEntity.updatePopReward(itemStack);
            } else if (level.isClientSide()) {
                // Last attempt to open up screen on client
                balloonPlayer.booyah$openBalloonBlockEditScreen(blockEntity);
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
        boolean show = BooyahBlocks.BALLOON_CHASE_BLOCK != null && context.isHoldingItem(BooyahBlocks.BALLOON_CHASE_BLOCK.asItem());
        if (!show && context instanceof EntityCollisionContext entityContext && entityContext.getEntity() != null) {
            Entity entity = entityContext.getEntity();
            if (entity instanceof Player player) {
                show = player.canUseGameMasterBlocks() && entityContext.getEntity().distanceToSqr(pos.getCenter()) <= 8;
            }
        }

        return show ? Shapes.block() : Shapes.empty();
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

    public static HSVColor getRandomColorForIndex(int index) {
        Random random = new Random(index * 2048L + 1000);
        float hue = random.nextFloat();
        float saturation = 0.7f + random.nextFloat() * 0.3f;
        float value = 0.8f + random.nextFloat() * 0.2f;
        return new HSVColor(hue, saturation, value);
    }
}
