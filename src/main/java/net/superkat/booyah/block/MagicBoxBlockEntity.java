package net.superkat.booyah.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.superkat.booyah.block.inventory.MagicalBoxMenu;
import org.jspecify.annotations.NonNull;

public class MagicBoxBlockEntity extends RandomizableContainerBlockEntity {
    public NonNullList<ItemStack> stockItems = NonNullList.withSize(27, ItemStack.EMPTY);
    public NonNullList<ItemStack> inventoryItems = NonNullList.withSize(27, ItemStack.EMPTY);
    public MagicBoxBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BooyahBlocks.MAGICAL_BOX_BLOCK_ENTITY, worldPosition, blockState);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (!this.trySaveLootTable(output)){
            ContainerHelper.saveAllItems(output, this.stockItems);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.inventoryItems = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        this.stockItems = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(input)){
            ContainerHelper.loadAllItems(input, this.inventoryItems);
            ContainerHelper.loadAllItems(input, this.stockItems);
        }
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return MagicalBoxMenu.create(containerId, inventory, this);
    }

    @Override
    public void startOpen(ContainerUser containerUser) {
        super.startOpen(containerUser);
        containerUser.getLivingEntity().level().playSound(null, this.getBlockPos(), SoundEvents.ENDER_CHEST_OPEN, SoundSource.BLOCKS, 0.75f, 1f);
    }

    @Override
    public void stopOpen(ContainerUser containerUser) {
        if (containerUser.getLivingEntity() instanceof Player player) {
            player.level().playSound(null, this.getBlockPos(), SoundEvents.ENDER_CHEST_CLOSE, SoundSource.BLOCKS, 0.75f, 1f);

            if (player.isCreative()) {
                for (int i = 0; i < this.inventoryItems.size(); i++) {
                    this.stockItems.set(i, this.inventoryItems.get(i).copy());
                }
            } else {
                for (int i = 0; i < this.stockItems.size(); i++) {
                    this.inventoryItems.set(i, this.stockItems.get(i).copy());
                }
                if (player.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.WITCH, this.getBlockPos().getX() + 0.5f, this.getBlockPos().getY() + 1f, this.getBlockPos().getZ() + 0.5f, 8, 0.35f, 0.25f, 0.35f, 0);
                }
            }
        }
    }

    @Override
    protected @NonNull Component getDefaultName() {
        return Component.literal("Magical Box");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return inventoryItems;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.inventoryItems = items;
    }

    @Override
    public int getContainerSize() {
        return 27;
    }
}
