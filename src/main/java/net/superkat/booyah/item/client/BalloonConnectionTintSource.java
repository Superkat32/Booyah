package net.superkat.booyah.item.client;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.CommonColors;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.superkat.booyah.block.BalloonChaseBlockEntity;
import net.superkat.booyah.color.HSVColor;
import net.superkat.booyah.duck.balloon.BalloonBlockEditCapablePlayer;
import org.jspecify.annotations.Nullable;

import java.util.Random;

public record BalloonConnectionTintSource(int color) implements ItemTintSource {
    public static final MapCodec<BalloonConnectionTintSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(source -> source.color)
            ).apply(instance, BalloonConnectionTintSource::new)
    );

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        if (level != null) {
            if (owner instanceof BalloonBlockEditCapablePlayer player && player.booyah$isConnectingBalloonBlocks()) {
                if (level.getBlockEntity(player.booyah$getConnectingBalloonBlockPos()) instanceof BalloonChaseBlockEntity balloonChaseBlockEntity) {
                    Random random = new Random(balloonChaseBlockEntity.balloonEntry == null ? 0 : balloonChaseBlockEntity.balloonEntry.index() * 2048L + 1000);
                    float hue = random.nextFloat();
                    float saturation = 0.7f + random.nextFloat() * 0.3f;
                    float value = 0.8f + random.nextFloat() * 0.2f;
                    return new HSVColor(hue, saturation, value).getARGB();
                } else {
                    return CommonColors.BLACK;
                }
            }
        }
        return CommonColors.WHITE;
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return CODEC;
    }
}
