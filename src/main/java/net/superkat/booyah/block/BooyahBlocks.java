package net.superkat.booyah.block;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.superkat.booyah.Booyah;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BooyahBlocks {

    public static final Block BALLOON_CHASE_BLOCK = registerBlock(
            "balloon_chase_block",
            BalloonChaseBlock::new,
            BlockBehaviour.Properties.of()
                    .strength(-1.0F, 3600000.8F)
                    .mapColor(MapColor.NONE)
                    .noLootTable()
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .noTerrainParticles()
                    .pushReaction(PushReaction.BLOCK),
            true
    );

    public static final BlockEntityType<BalloonChaseBlockEntity> BALLOON_CHASE_BLOCK_ENTITY = registerBlockEntity(
            "balloon_chase_block_entity",
            BalloonChaseBlockEntity::new,
            BALLOON_CHASE_BLOCK
    );

    public static final Block BOOSTER_BLOCK = registerBlock(
            "booster_block",
            BoosterBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.FIRE)
                    .requiresCorrectToolForDrops()
                    .strength(5f, 6f)
                    .sound(SoundType.NETHERITE_BLOCK),
            true
    );

    public static final Block MAGICAL_BOX_BLOCK = registerBlock(
            "magical_box_block",
            MagicalBoxBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(2.5f)
                    .sound(SoundType.WOOD),
            true
    );

    public static final BlockEntityType<MagicBoxBlockEntity> MAGICAL_BOX_BLOCK_ENTITY = registerBlockEntity(
            "magical_box_block_entity",
            MagicBoxBlockEntity::new,
            MAGICAL_BOX_BLOCK
    );

    public static final Map<DyeColor, Block> CIRCLE_CARPET_BLOCKS = generateDyedItemSet(BooyahBlocks::registerCircleArrowCarpet);
//    public static final Block RED_CIRCLE_CARPET = registerCircleArrowCarpet("red", DyeColor.RED);

    public static void init() {
        // NO-OP
    }

    private static Map<DyeColor, Block> generateDyedItemSet(Function<DyeColor, Block> generator) {
        Map<DyeColor, Block> set = new HashMap<>();
        for (DyeColor dyeColor : DyeColor.values()) {
            Block block = generator.apply(dyeColor);
            set.put(dyeColor, block);
        }
        return set;
    }

    private static Block registerCircleArrowCarpet(DyeColor dyeColor) {
        return registerBlock(
                dyeColor.getName() + "_circle_carpet",
                CircleCarpetBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(dyeColor)
                        .strength(0.1f)
                        .sound(SoundType.WOOL)
                        .ignitedByLava(),
                true
        );
    }

    // Copy-pasted from docs
    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings, boolean shouldRegisterItem) {
        ResourceKey<Block> blockKey = keyOfBlock(name);
        Block block = blockFactory.apply(settings.setId(blockKey));
        if (shouldRegisterItem) {
            ResourceKey<Item> itemKey = keyOfItem(name);
            BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    private static ResourceKey<Block> keyOfBlock(String name) {
        return ResourceKey.create(Registries.BLOCK, Booyah.id(name));
    }

    private static ResourceKey<Item> keyOfItem(String name) {
        return ResourceKey.create(Registries.ITEM, Booyah.id(name));
    }

    private static Function<BlockState, MapColor> waterloggedMapColor(final MapColor mapColor) {
        return blockState -> blockState.getValue(BlockStateProperties.WATERLOGGED) ? MapColor.WATER : mapColor;
    }

    private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        Identifier id = Booyah.id(name);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }

}
