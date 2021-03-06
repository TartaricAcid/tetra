package se.mickelus.tetra.items.toolbelt;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import se.mickelus.tetra.items.toolbelt.inventory.*;

public class UtilToolbelt {
    public static void equipItemFromToolbelt(EntityPlayer player, ToolbeltSlotType slotType, int index, EnumHand hand) {
        InventoryToolbelt inventory = null;
        ItemStack toolbeltStack = findToolbelt(player);

        switch (slotType) {
            case quickslot:
                inventory = new InventoryQuickslot(toolbeltStack);
                break;
            case potion:
                inventory = new InventoryPotions(toolbeltStack);
                break;
            case quiver:
                inventory = new InventoryQuiver(toolbeltStack);
                break;
            case storage:
                inventory = new InventoryStorage(toolbeltStack);
                break;
        }

        if (inventory.getSizeInventory() <= index || inventory.getStackInSlot(index).isEmpty()) {
            return;
        }

        ItemStack heldItemStack = player.getHeldItem(hand);

        player.setHeldItem(hand, inventory.takeItemStack(index));

        if (!heldItemStack.isEmpty()) {
            storeItemInToolbelt(toolbeltStack, heldItemStack);
        }
    }

    /**
     * Attempts to store the given players offhand or mainhand item in the toolbelt. Attempts to grab the offhand item
     * first and grabs the mainhand item if the offhand is empty.
     * @param player A player
     * @return false if the toolbelt is full, otherwise true
     */
    public static boolean storeItemInToolbelt(EntityPlayer player) {
        ItemStack toolbeltStack = findToolbelt(player);
        ItemStack itemStack = player.getHeldItem(EnumHand.OFF_HAND);
        EnumHand sourceHand = EnumHand.OFF_HAND;

        if (itemStack.isEmpty()) {
            itemStack = player.getHeldItem(EnumHand.MAIN_HAND);
            sourceHand = EnumHand.MAIN_HAND;
        }

        if (toolbeltStack.isEmpty() || itemStack.isEmpty() || itemStack.getItem() == ItemToolbeltModular.instance) {
            return true;
        }

        if (storeItemInToolbelt(toolbeltStack, itemStack)) {
            player.setHeldItem(sourceHand, ItemStack.EMPTY);
            return true;
        }
        return false;
    }

    public static boolean storeItemInToolbelt(ItemStack toolbeltStack, ItemStack itemStack) {
        if (new InventoryPotions(toolbeltStack).storeItemInInventory(itemStack)) {
            return true;
        }

        if (new InventoryQuiver(toolbeltStack).storeItemInInventory(itemStack)) {
            return true;
        }

        if (new InventoryQuickslot(toolbeltStack).storeItemInInventory(itemStack)) {
            return true;
        }

        if (new InventoryStorage(toolbeltStack).storeItemInInventory(itemStack)) {
            return true;
        }

        return false;
    }

    /**
     * Attempts to find the first itemstack containing a toolbelt in the given players inventory.
     * todo: add baubles support
     * @param player A player
     * @return A toolbelt itemstack, or an empty itemstack if the player has to toolbelt
     */
    public static ItemStack findToolbelt(EntityPlayer player) {
        InventoryPlayer inventoryPlayer = player.inventory;
        for (int i = 0; i < inventoryPlayer.mainInventory.size(); ++i) {
            ItemStack itemStack = inventoryPlayer.getStackInSlot(i);
            if (ItemToolbeltModular.instance.equals(itemStack.getItem())) {
                return itemStack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static void emptyOverflowSlots(ItemStack itemStack, EntityPlayer player) {
        new InventoryQuickslot(itemStack).emptyOverflowSlots(player);
        new InventoryPotions(itemStack).emptyOverflowSlots(player);
        new InventoryStorage(itemStack).emptyOverflowSlots(player);
        new InventoryQuiver(itemStack).emptyOverflowSlots(player);
    }
}
