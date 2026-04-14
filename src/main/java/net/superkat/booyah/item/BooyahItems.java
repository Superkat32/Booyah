package net.superkat.booyah.item;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.Weapon;
import net.superkat.booyah.Booyah;

import java.util.List;
import java.util.function.Function;

public class BooyahItems {

    public static final Item SPLATANA_STAMPER = register("splatana_stamper", Item::new,
            new Item.Properties()
                    .rarity(Rarity.RARE)
                    .attributes(SplatanaItem.createAttributes())
                    .component(DataComponents.WEAPON, new Weapon(1))
                    .component(DataComponents.MINIMUM_ATTACK_CHARGE, 1f)
                    .component(DataComponents.TOOL, new Tool(List.of(), 1f, 2, false))
                    .stacksTo(1)
    );

    public static void init() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
                .register((tab) -> tab.accept(SPLATANA_STAMPER));
    }

    public static boolean isSplatana(ItemStack item) {
        return item.is(SPLATANA_STAMPER);
    }

    private static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Booyah.id(name));
        T item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

}
