package net.superkat.booyah.item.client;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.superkat.booyah.duck.splatana.SplatanaPlayer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record SplatanaCharge() implements ConditionalItemModelProperty {
    public static final MapCodec<SplatanaCharge> CODEC = MapCodec.unit(new SplatanaCharge());

    @Override
    public boolean get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
        if (owner instanceof SplatanaPlayer splatanaPlayer && splatanaPlayer.booyah$isSplatanaSwinging()) {
            return owner.getItemHeldByArm(owner.getMainArm()) == itemStack;
        }
        return false;
    }

    @Override
    public @NonNull MapCodec<? extends ConditionalItemModelProperty> type() {
        return CODEC;
    }
}
