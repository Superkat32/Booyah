package net.superkat.booyah.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
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

    public static final EntityType<Balloon> BALLOON_CHASE = register("balloon_chase",
            EntityType.Builder.of(Balloon::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(1f, 0.9f)
                    .clientTrackingRange(10)
                    .noSave()
    );

    public static void init() {
        FabricDefaultAttributeRegistry.register(BALLOON_CHASE, Balloon.createAttributes());
    }

    public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Booyah.id(id));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

}
