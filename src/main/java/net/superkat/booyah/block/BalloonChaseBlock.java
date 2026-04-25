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
        if (player.canUseGameMasterBlocks() && itemStack.is(BooyahBlocks.BALLOON_CHASE_BLOCK.asItem())
            && level.getBlockEntity(pos) instanceof BalloonChaseBlockEntity balloonChaseBlockEntity) {

            if (level.isClientSide()) {
                ((BalloonBlockEditCapablePlayer) player).booyah$openBalloonBlockEditScreen(balloonChaseBlockEntity);
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
        return (BooyahBlocks.BALLOON_CHASE_BLOCK != null && context.isHoldingItem(BooyahBlocks.BALLOON_CHASE_BLOCK.asItem())) ? Shapes.block() : Shapes.empty();
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
