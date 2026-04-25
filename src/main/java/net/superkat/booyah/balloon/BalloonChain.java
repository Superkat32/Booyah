package net.superkat.booyah.balloon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record BalloonChain(String id, Map<BlockPos, BalloonEntry> entries) {

//    private static final Codec<Map<BlockPos, BalloonEntry>> ENTRY_MAP_CODEC = Codec.unboundedMap(BlockPos.CODEC, BalloonEntry.CODEC);

    public static final Codec<BalloonChain> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.STRING.fieldOf("id").forGetter(chain -> chain.id),
//                    ENTRY_MAP_CODEC.fieldOf("entries").forGetter(chain -> chain.entries)
                    BalloonEntry.CODEC.listOf().fieldOf("entries").forGetter(chain -> chain.entries.values().stream().toList())
            ).apply(instance, BalloonChain::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, BalloonChain> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, BalloonChain::id,
            ByteBufCodecs.map(HashMap::new, BlockPos.STREAM_CODEC, BalloonEntry.STREAM_CODEC), BalloonChain::entries,
            BalloonChain::new
    );

    public BalloonChain(String id, List<BalloonEntry> entries) {
        HashMap<BlockPos, BalloonEntry> entriesMap = new HashMap<>();
        for (BalloonEntry entry : entries) {
            entriesMap.put(entry.pos(), entry);
        }
        this(id, entriesMap);
    }

    public void putEntry(BalloonEntry entry) {
        this.entries.put(entry.pos(), entry);
    }

    @Nullable
    public BalloonEntry getEntryForPos(BlockPos pos) {
        return this.entries.get(pos);
    }

}
