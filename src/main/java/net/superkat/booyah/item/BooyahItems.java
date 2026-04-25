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
    public static final Item STAMPER_PURPLE = registerStamper("purple", SplatanaColors.PURPLE);

    public static void init() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
                .register((tab) -> {
                    tab.accept(STAMPER_WHITE);
                    tab.accept(STAMPER_PURPLE);
                });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.OP_BLOCKS)
                .register(tab -> {
                    tab.accept(BooyahBlocks.BALLOON_CHASE_BLOCK.asItem());
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
