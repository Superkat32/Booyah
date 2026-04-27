package net.superkat.booyah.balloon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.Level;
import net.superkat.booyah.entity.Balloon;
import net.superkat.booyah.entity.BooyahEntities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class BalloonChain {

    public static final Codec<BalloonChain> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.STRING.fieldOf("id").forGetter(chain -> chain.id),
                    BalloonEntry.CODEC.listOf().fieldOf("entries").forGetter(chain -> chain.entries.values().stream().toList())
            ).apply(instance, BalloonChain::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, BalloonChain> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, BalloonChain::id,
            ByteBufCodecs.map(HashMap::new, BlockPos.STREAM_CODEC, BalloonEntry.STREAM_CODEC), BalloonChain::entries,
            BalloonChain::new
    );

    public final String id;
    public final Map<BlockPos, BalloonEntry> entries;

    public int ticks = 0;
    public int balloonSpawnDelayTicks = 0;
    public int balloonWaveTicks = 0;

    public boolean chasing = false;
    public boolean balloonSpawned = false;
    public Map<BlockPos, UUID> balloonUuids = new HashMap<>();
    public boolean waveFailed = false;

    public int startingIndex = 0;
    public int endingIndex = 0;
    public int currentIndex = 0;
    public List<Integer> knownEntryIndexes = new ArrayList<>();

    public BalloonChain(String id, Map<BlockPos, BalloonEntry> entries) {
        this.id = id;
        this.entries = entries;
    }

    public BalloonChain(String id, List<BalloonEntry> entries) {
        HashMap<BlockPos, BalloonEntry> entriesMap = new HashMap<>();
        for (BalloonEntry entry : entries) {
            entriesMap.put(entry.pos(), entry);
        }
        this(id, entriesMap);
    }

    public void tick(ServerLevel level) {
        if (this.entries.isEmpty() || this.knownEntryIndexes.isEmpty()) return;

        this.ticks++;
        if (this.chasing) {
            this.balloonWaveTicks++;
        }

        if (!this.balloonSpawned && !this.chasing && this.currentIndex == this.startingIndex) {
            spawnBalloons(level, this.startingIndex);
        } else if (this.waveFailed) {
            this.waveFailed = false;
            this.reset(level);
        }
    }

    public void spawnNextWave(ServerLevel level) {
        this.balloonWaveTicks = 0;
        if (this.currentIndex == this.endingIndex) {
            this.onComplete(level);
        } else {
            List<Integer> uniqueKnownIndexes = this.knownEntryIndexes.stream().distinct().toList();
            // Wow Kat! *you are so funny* - Could you say that with a little more gusto? - BALALALA
            int currentIndexIndex = uniqueKnownIndexes.indexOf(this.currentIndex);
            this.currentIndex = uniqueKnownIndexes.get(currentIndexIndex + 1);
            this.spawnBalloons(level, this.currentIndex);
            this.chasing = true;
        }
    }

    public void onComplete(ServerLevel level) {
        this.chasing = false;
        this.currentIndex = this.startingIndex;
        this.balloonSpawned = false;
    }

    public void spawnBalloons(ServerLevel level, int index) {
        List<BalloonEntry> entriesForIndex = this.entries.values().stream().filter(entry -> entry.index() == index).toList();
        for (BalloonEntry entry : entriesForIndex) {
            BlockPos pos = entry.pos();
            Balloon balloon = BooyahEntities.BALLOON_CHASE.create(level, EntitySpawnReason.MOB_SUMMONED);
            int ticksUntilFloatAway = index == this.startingIndex ? -1 : 300;
//            int ticksUntilFloatAway = index == this.startingIndex ? -1 : 100;
            if (balloon == null) continue;

            // Move pos up if either of the 2 blocks beneath aren't air
            if (!level.getBlockState(pos.below()).isAir() || !level.getBlockState(pos.below(2)).isAir()) {
                pos = pos.above(2);
            }

            balloon.setPos(pos.getCenter());
            balloon.setOnGround(false);
            balloon.snapTo(pos.getBottomCenter(), 0, 0);
            level.addFreshEntity(balloon);
            balloon.setChain(this);
            balloon.setChainPos(entry.pos());
            balloon.setTicksUntilFloatAway(ticksUntilFloatAway);
            this.balloonUuids.put(entry.pos(), balloon.getUUID());
        }

        this.balloonSpawned = true;
    }

    // Server only - used when a player pops a balloon, not when it despawns
    public void onBalloonPop(Balloon balloon) {
        this.balloonUuids.remove(balloon.getChainPos());
        if (this.balloonUuids.isEmpty()) {
            this.spawnNextWave((ServerLevel) balloon.level());
        }
    }

    public void onBalloonDespawn(Balloon balloon) {
        this.balloonUuids.remove(balloon.getChainPos());
        // FIXME - Breaking a balloon block while the next index has multiple positions causes a reset loop
        //  of failures and sadness and heart break and total destruction and a lot of particles
        this.waveFailed = true;
    }

    public void reset(ServerLevel level) {
        for (UUID balloonUuid : balloonUuids.values()) {
            Entity balloon = level.getEntity(balloonUuid);
            if (balloon == null || balloon.isRemoved()) continue;
            balloon.remove(Entity.RemovalReason.DISCARDED);
        }
        this.chasing = false;
        this.currentIndex = this.startingIndex;
        this.balloonSpawned = false;
    }

    public void putEntry(BalloonEntry entry) {
        this.entries.put(entry.pos(), entry);
        this.knownEntryIndexes = new ArrayList<>(this.entries.values().stream().map(BalloonEntry::index).sorted().toList());

        this.startingIndex = knownEntryIndexes.getFirst();
        this.endingIndex = knownEntryIndexes.getLast();
        if (!this.chasing) this.currentIndex = this.startingIndex;
    }

    public void removeEntry(Level level, BalloonEntry entry) {
        if (this.balloonUuids.containsKey(entry.pos()) && level.getEntity(this.balloonUuids.get(entry.pos())) != null) {
            level.getEntity(this.balloonUuids.get(entry.pos())).remove(Entity.RemovalReason.DISCARDED);
        }
        this.entries.remove(entry.pos());
        this.knownEntryIndexes.remove(Integer.valueOf(entry.index()));

        if (!this.knownEntryIndexes.isEmpty()) {
            this.startingIndex = knownEntryIndexes.getFirst();
            this.endingIndex = knownEntryIndexes.getLast();
        } else {
            this.startingIndex = 0;
            this.endingIndex = 0;
        }
        if (!this.chasing) this.currentIndex = this.startingIndex;
    }

    public String id() {
        return id;
    }

    public Map<BlockPos, BalloonEntry> entries() {
        return entries;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (BalloonChain) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.entries, that.entries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entries);
    }

    @Override
    public String toString() {
        return "BalloonChain[" +
                "id=" + id + ", " +
                "entries=" + entries + ']';
    }


}
