package net.superkat.booyah.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.superkat.booyah.item.color.SplatanaColorSet;

public record SplatanaComponent(SplatanaColorSet colorSet, float dashAmount) {

    public static final Codec<SplatanaComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    SplatanaColorSet.CODEC.fieldOf("color_set").forGetter(SplatanaComponent::colorSet),
                    Codec.FLOAT.fieldOf("dash_amount").forGetter(SplatanaComponent::dashAmount)
            ).apply(instance, SplatanaComponent::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SplatanaComponent> STREAM_CODEC = StreamCodec.composite(
            SplatanaColorSet.STREAM_CODEC, SplatanaComponent::colorSet,
            ByteBufCodecs.FLOAT, SplatanaComponent::dashAmount,
            SplatanaComponent::new
    );

}
