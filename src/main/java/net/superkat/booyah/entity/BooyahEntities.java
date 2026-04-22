package net.superkat.booyah.entity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.superkat.booyah.Booyah;

public class BooyahEntities {

    public static final EntityType<SplatanaSwipe> SPLATANA_SWIPE = register("splatana_swipe",
            EntityType.Builder.<SplatanaSwipe>of(SplatanaSwipe::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(1f, 0.5f)
                    .clientTrackingRange(4)
                    .updateInterval(20)
    );

    public static void init() {

    }

    public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Booyah.id(id));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

}
