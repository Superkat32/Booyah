package net.superkat.booyah.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
    public void updateBalloonEntry(String chainId, BalloonEntry entry) {
        if (this.level == null || !(this.level instanceof ServerLevel serverLevel) || chainId.isBlank()) return;
        BalloonChainManager chainManager = BalloonChainManager.get(this.level);
        BalloonChain chain = chainManager.getOrCreateChain(chainId);
        chain.putEntry(serverLevel, entry);

        // Remove from old chain if chainId was updated
        if (!this.balloonChainId.isBlank() && !this.balloonChainId.equals(chainId)
                && chainManager.getChain(chainId) != null && this.balloonEntry != null) {
            chainManager.removeEntry(this.level, this.balloonChainId, this.balloonEntry);
        }

        this.balloonEntry = entry;
        this.balloonChainId = chain.id();
        this.setChanged();
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    // Also server only I think
    public void updatePopReward(ItemStack popReward) {
        if (this.balloonEntry == null) return;
        this.updateBalloonEntry(this.balloonChainId, this.balloonEntry.updatePopReward(popReward));
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        if (!this.level.isClientSide() && this.balloonEntry != null) { // Try to let NBT copied blocks work
            this.balloonEntry = this.balloonEntry.updatePos(this.getBlockPos());
            BalloonChainManager.get(this.level).addEntry(this.level, this.balloonChainId, this.balloonEntry);
        }
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        if (!this.level.isClientSide()) {
            BalloonChainManager.get(this.level).removeEntry(level, this.balloonChainId, this.balloonEntry);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        this.balloonChainId = input.getStringOr("chain_id", "");
        this.balloonEntry = input.read("entry", BalloonEntry.CODEC).orElse(null);
        super.loadAdditional(input);

        if (this.level != null && this.balloonEntry != null) { // Try to let NBT copied blocks work
            this.updateBalloonEntry(this.balloonChainId, this.balloonEntry.updatePos(this.getBlockPos()));
//            this.balloonEntry = this.balloonEntry.updatePos(this.getBlockPos());
        }
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

    public String getChainIndexString() {
        return this.balloonEntry == null ? "0" : String.valueOf(this.balloonEntry.index());
    }

    public String getSpawnDelayString() {
        return this.balloonEntry == null ? "0" : String.valueOf(this.balloonEntry.spawnDelayTicks());
    }

    public String getFloatAwayString() {
        return this.balloonEntry == null ? "300" : String.valueOf(this.balloonEntry.floatAwayTicks());
    }

    public String getYawString() {
        return this.balloonEntry == null ? "0.0" : String.valueOf(this.balloonEntry.balloonYaw());
    }
}
