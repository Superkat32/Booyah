package net.superkat.booyah.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.superkat.booyah.balloon.BalloonChain;
import net.superkat.booyah.balloon.BalloonChainManager;
import net.superkat.booyah.balloon.BalloonEntry;
import org.jspecify.annotations.Nullable;

public class BalloonChaseBlockEntity extends BlockEntity {
    public String balloonChainId = "";
    @Nullable
    public BalloonEntry balloonEntry = null;

    public BalloonChaseBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BooyahBlocks.BALLOON_CHASE_BLOCK_ENTITY, worldPosition, blockState);
    }

    // Intended for server only
    public void updateBalloonEntry(String chainId, int entryIndex) {
        if (this.level == null) return;
        BalloonChainManager chainManager = BalloonChainManager.get(this.level);
        BalloonChain chain = chainManager.getOrCreateChain(chainId);
        BalloonEntry entry = new BalloonEntry(this.getBlockPos(), entryIndex);
        chain.putEntry(entry);

        this.balloonEntry = entry;
        this.balloonChainId = chain.id();
        this.setChanged();
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        this.balloonChainId = input.getStringOr("chain_id", "");
        this.balloonEntry = input.read("entry", BalloonEntry.CODEC).orElse(null);
        super.loadAdditional(input);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        output.putString("chain_id", this.balloonChainId);
        if (this.balloonEntry != null) output.store("entry", BalloonEntry.CODEC, this.balloonEntry);
        super.saveAdditional(output);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithFullMetadata(registries);
    }

    @Override
    public @Nullable ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public String getChainId() {
        return this.balloonChainId;
    }

    public String getChainIndex() {
        return this.balloonEntry == null ? "0" : String.valueOf(this.balloonEntry.index());
    }
}
