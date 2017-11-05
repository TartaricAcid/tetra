package se.mickelus.tetra.module;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import se.mickelus.tetra.items.ItemModular;
import se.mickelus.tetra.items.sword.ItemSwordModular;

public class BasicSchema implements UpgradeSchema {

    private static String nameSuffix = ".name";
    private static String descriptionSuffix = ".description";
    private static String slotSuffix = ".slot1";

    private String key;

    private ItemModule module;

    public BasicSchema(String key, ItemModule module) {
        this.key = key;
        this.module = module;

        ItemUpgradeRegistry.instance.registerSchema(this);
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getName() {
        return I18n.format(key + nameSuffix);
    }

    @Override
    public String getDescription() {
        return I18n.format(key + descriptionSuffix);
        //return "Replace the current sword blade with the given blade, or reforge it using raw materials.";
    }

    @Override
    public int getNumMaterialSlots() {
        return 1;
    }

    @Override
    public String getSlotName(final int index) {
        return I18n.format(key + slotSuffix);
    }

    @Override
    public boolean slotAcceptsMaterial(final ItemStack itemStack, final int index, final ItemStack materialStack) {
        if (index == 0) {
            return module.slotAcceptsMaterial(itemStack, materialStack);
        }
        return true;
    }

    @Override
    public boolean canUpgrade(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemSwordModular;
    }

    @Override
    public boolean canApplyUpgrade(ItemStack itemStack, ItemStack[] materials) {
        return isMaterialsValid(itemStack, materials) && isIntegrityViolation(itemStack, materials);
    }

    @Override
    public boolean isMaterialsValid(ItemStack itemStack, ItemStack[] materials) {
        return module.canApplyUpgrade(itemStack, materials);
    }

    @Override
    public boolean isIntegrityViolation(ItemStack itemStack, final ItemStack[] materials) {
        ItemStack upgradedStack = applyUpgrade(itemStack, materials, false);
        return ItemModular.getIntegrityGain(upgradedStack) + ItemModular.getIntegrityCost(upgradedStack) >= 0;
    }

    @Override
    public ItemStack applyUpgrade(final ItemStack itemStack, final ItemStack[] materials, boolean consumeMaterials) {
        ItemStack upgradedStack = itemStack.copy();

        module.addModule(upgradedStack, materials, consumeMaterials);

        return upgradedStack;
    }
}