package net.superkat.booyah.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.CommonColors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.superkat.booyah.particles.zoom.ZoomParticleOptions;
import org.jspecify.annotations.Nullable;

public class BoosterBlock extends Block {
    public static final MapCodec<BoosterBlock> CODEC = simpleCodec(BoosterBlock::new);
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    public static final BooleanProperty ELEVATED = BooleanProperty.create("booster_elevated");
    public static final VoxelShape SHAPE = Block.column(14, 0, 1);
    public static final VoxelShape BOOST_CHECK_SHAPE = Block.column(14, 0, 3);

    public BoosterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(ELEVATED, false)
        );
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }


    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getEntityInsideCollisionShape(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
        return BOOST_CHECK_SHAPE;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        entity.setDeltaMovement(entity.getDeltaMovement().scale(0.5f));
        Vec3 baseVelocity = state.getValue(FACING).getOpposite().getUnitVec3().scale(1f);
        entity.push(baseVelocity.add(0, 0.25f, 0));
        level.playSound(null, pos, SoundEvents.BREEZE_CHARGE, SoundSource.BLOCKS, 0.75f, 1.25f);
        if (level.isClientSide()) {
            for (int i = 0; i < 16; i++) {
                float x = (float) entity.getRandomX(2);
                float y = (float) entity.getRandomY(0.5) + 1f;
                float z = (float) entity.getRandomZ(2);
                float velX = (float) baseVelocity.x;
                float velY = (float) baseVelocity.y;
                float velZ = (float) baseVelocity.z;
                level.addParticle(new ZoomParticleOptions(CommonColors.WHITE, state.getValue(FACING).getOpposite().toYRot() + 90, 0, 0), x, y, z, velX, velY, velZ);
            }
        }
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ELEVATED);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
