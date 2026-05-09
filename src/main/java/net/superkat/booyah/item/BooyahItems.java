package net.superkat.booyah.item;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.UseEffects;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.level.block.Block;
import net.superkat.booyah.Booyah;
import net.superkat.booyah.block.BooyahBlocks;
import net.superkat.booyah.item.color.SplatanaColorSet;
import net.superkat.booyah.item.color.SplatanaColors;
import net.superkat.booyah.item.component.SplatanaComponent;

import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class BooyahItems {

    public static final TagKey<Item> SPLATANA_TAG = TagKey.create(Registries.ITEM, Booyah.id("splatana_item"));

    public static final DataComponentType<SplatanaComponent> SPLATANA_COMPONENT = register(
            "splatana_color_component",
            builder -> builder.persistent(SplatanaComponent.CODEC).networkSynchronized(SplatanaComponent.STREAM_CODEC).cacheEncoding()
    );

    public static final Item STAMPER_WHITE = registerStamper("white", SplatanaColors.WHITE);
    public static final Item STAMPER_LIGHT_GRAY = registerStamper("light_gray", SplatanaColors.LIGHT_GRAY);
    public static final Item STAMPER_GRAY = registerStamper("gray", SplatanaColors.GRAY);
    public static final Item STAMPER_BLACK = registerStamper("black", SplatanaColors.BLACK);
    public static final Item STAMPER_BROWN = registerStamper("brown", SplatanaColors.BROWN);
    public static final Item STAMPER_RED = registerStamper("red", SplatanaColors.RED);
    public static final Item STAMPER_ORANGE = registerStamper("orange", SplatanaColors.ORANGE);
    public static final Item STAMPER_YELLOW = registerStamper("yellow", SplatanaColors.YELLOW);
    public static final Item STAMPER_LIME = registerStamper("lime", SplatanaColors.LIME);
    public static final Item STAMPER_GREEN = registerStamper("green", SplatanaColors.GREEN);
    public static final Item STAMPER_CYAN = registerStamper("cyan", SplatanaColors.CYAN);
    public static final Item STAMPER_LIGHT_BLUE = registerStamper("light_blue", SplatanaColors.LIGHT_BLUE);
    public static final Item STAMPER_BLUE = registerStamper("blue", SplatanaColors.BLUE);
    public static final Item STAMPER_PURPLE = registerStamper("purple", SplatanaColors.PURPLE);
    public static final Item STAMPER_MAGENTA = registerStamper("magenta", SplatanaColors.MAGENTA);
    public static final Item STAMPER_PINK = registerStamper("pink", SplatanaColors.PINK);
    public static final Item STAMPER_TRANS = registerStamper("trans", SplatanaColors.TRANS);

    public static void init() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
                .register((tab) -> {
                    tab.accept(STAMPER_WHITE);
                    tab.accept(STAMPER_LIGHT_GRAY);
                    tab.accept(STAMPER_GRAY);
                    tab.accept(STAMPER_BLACK);
                    tab.accept(STAMPER_BROWN);
                    tab.accept(STAMPER_RED);
                    tab.accept(STAMPER_ORANGE);
                    tab.accept(STAMPER_YELLOW);
                    tab.accept(STAMPER_LIME);
                    tab.accept(STAMPER_GREEN);
                    tab.accept(STAMPER_CYAN);
                    tab.accept(STAMPER_LIGHT_BLUE);
                    tab.accept(STAMPER_BLUE);
                    tab.accept(STAMPER_PURPLE);
                    tab.accept(STAMPER_MAGENTA);
                    tab.accept(STAMPER_PINK);
                    tab.accept(STAMPER_TRANS);
                });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.OP_BLOCKS)
                .register(tab -> {
                    tab.accept(BooyahBlocks.BALLOON_CHASE_BLOCK.asItem());
                });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS)
                .register(tab -> {
                    tab.accept(BooyahBlocks.BOOSTER_BLOCK.asItem());
                });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS)
                .register(tab -> {
                    tab.accept(BooyahBlocks.MAGICAL_BOX_BLOCK.asItem());
                });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COLORED_BLOCKS)
                .register(tab -> {
                    for (Block carpet : BooyahBlocks.CIRCLE_CARPET_BLOCKS.values()) {
                        tab.accept(carpet.asItem());
                    }
                });
    }

    public static boolean isSplatana(ItemStack item) {
        return item.is(SPLATANA_TAG);
    }

    private static SplatanaItem registerStamper(String name, SplatanaColorSet colorSet) {
        return registerSplatana("stamper_" + name, new SplatanaComponent(colorSet, 1f));
    }

    private static SplatanaItem registerSplatana(String name, SplatanaComponent splatanaComponent) {
        return register("splatana_" + name, SplatanaItem::new,
                new Item.Properties()
                        .rarity(Rarity.EPIC)
                        .attributes(SplatanaItem.createAttributes())
                        .component(DataComponents.WEAPON, new Weapon(1))
                        .component(DataComponents.MINIMUM_ATTACK_CHARGE, 1f)
                        .component(DataComponents.TOOL, new Tool(List.of(), 1f, 2, false))
                        .component(DataComponents.USE_EFFECTS, new UseEffects(true, true, 0.25f))
                        .component(SPLATANA_COMPONENT, splatanaComponent)
                        .stacksTo(1)
                );
    }

    private static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Booyah.id(name));
        T item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> DataComponentType<T> register(final String id, final UnaryOperator<DataComponentType.Builder<T>> builder) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Booyah.id(id), ((DataComponentType.Builder)builder.apply(DataComponentType.builder())).build());
    }

}
