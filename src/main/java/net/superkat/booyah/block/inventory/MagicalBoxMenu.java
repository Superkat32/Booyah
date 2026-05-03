package net.superkat.booyah.block.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.MenuType;
import net.superkat.booyah.block.MagicBoxBlockEntity;
import org.jspecify.annotations.Nullable;

public class MagicalBoxMenu extends ChestMenu {
    public static MagicalBoxMenu create(int containerId, Inventory inventory, Container container) {
        return new MagicalBoxMenu(MenuType.GENERIC_9x3, containerId, inventory, container, 3);
    }

    @Nullable
    public MagicBoxBlockEntity magicBoxEntity;

    public MagicalBoxMenu(MenuType<?> menuType, int containerId, Inventory inventory, Container container, int rows) {
        super(menuType, containerId, inventory, container, rows);

        if (container instanceof MagicBoxBlockEntity magicBoxBlockEntity) {
            this.magicBoxEntity = magicBoxBlockEntity;
        }
    }

    @Override
    public void clicked(int slotIndex, int buttonNum, ContainerInput containerInput, Player player) {
        if (player.isCreative() // Creative override
                || (slotIndex >= 26 && containerInput != ContainerInput.QUICK_MOVE) // Don't quick move from inventory
                || (slotIndex < 26 && containerInput == ContainerInput.QUICK_MOVE) // Quick move into inventory
                || (slotIndex < 26 && getCarried().isEmpty()) // Pick up item
        ) { // Normal actions in creative or on normal inventory
            super.clicked(slotIndex, buttonNum, containerInput, player);
        } else { // Prevent dropping items in the magic box
            // are these even needed? I don't think so, but it works and I'm so tired of working on this stupid box
            // I don't want to touch it and break it
            if (slotIndex < 26 && (containerInput == ContainerInput.PICKUP || containerInput == ContainerInput.SWAP) && !getCarried().isEmpty()) {
                return;
            } else if (slotIndex >= 26) {
                return;
            }
        }
    }
}
