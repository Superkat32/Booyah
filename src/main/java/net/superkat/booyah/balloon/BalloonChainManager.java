package net.superkat.booyah.balloon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.superkat.booyah.Booyah;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BalloonChainManager {
    public static final Codec<BalloonChainManager> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    BalloonChain.CODEC.listOf().fieldOf("chains").forGetter(manager -> manager.chains.values().stream().toList())
            ).apply(instance, BalloonChainManager::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, BalloonChainManager> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, BalloonChain.STREAM_CODEC), manager -> manager.chains,
            BalloonChainManager::new
    );

    public static final AttachmentType<BalloonChainManager> LEVEL_ATTACHMENT = AttachmentRegistry.create(
            Booyah.id("balloon_chain_manager_attachment"), builder -> builder
                    .initializer(BalloonChainManager::new)
    );

    public static void init() {
        ServerTickEvents.END_LEVEL_TICK.register(serverLevel -> {
            BalloonChainManager.get(serverLevel).tick(serverLevel);
        });
    }

    public Map<String, BalloonChain> chains;

    public BalloonChainManager(Map<String, BalloonChain> chains) {
        this.chains = new HashMap<>(chains);
    }

    public BalloonChainManager() {
        this(new HashMap<>());
    }

    public BalloonChainManager(List<BalloonChain> chains) {
        HashMap<String, BalloonChain> chainMap = new HashMap<>();
        for (BalloonChain chain : chains) {
            chainMap.put(chain.id(), chain);
        }
        this(chainMap);
    }

    public void tick(ServerLevel level) {
        for (BalloonChain chain : chains.values()) {
            chain.tick(level);
        }
    }

    public BalloonChain getOrCreateChain(String id) {
        if (this.hasChain(id)) {
            return this.chains.get(id);
        }
        BalloonChain chain = new BalloonChain(id, new HashMap<>());
        this.chains.put(id, chain);
        return chain;
    }

    @Nullable
    public BalloonChain getChain(String id) {
        if (this.hasChain(id)) return this.chains.get(id);
        return null;
    }

    public void addEntry(Level level, String chainId, BalloonEntry entry) {
        if (entry == null || !(level instanceof ServerLevel serverLevel)) return;
        BalloonChain chain = this.getOrCreateChain(chainId);
        chain.putEntry(serverLevel, entry);
    }

    public void removeEntry(Level level, String chainId, BalloonEntry entry) {
        if (entry == null) return;
        BalloonChain chain = this.getOrCreateChain(chainId);
        chain.removeEntry(level, entry);

        if (chain.entries().isEmpty()) {
            this.chains.remove(chainId);
        }
    }

    public boolean hasChain(String id) {
        return this.chains.containsKey(id);
    }

    public static BalloonChainManager get(Level level) {
        return level.getAttachedOrCreate(LEVEL_ATTACHMENT, BalloonChainManager::new);
    }

}
