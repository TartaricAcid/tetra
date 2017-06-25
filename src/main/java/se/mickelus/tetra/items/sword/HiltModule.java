package se.mickelus.tetra.items.sword;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import se.mickelus.tetra.module.ItemModuleMajor;
import se.mickelus.tetra.module.ItemUpgradeRegistry;

public class HiltModule extends ItemModuleMajor {

    public static final String key = "basic_hilt";

    public static HiltModule instance;

    public HiltModule() {
        instance = this;
        ItemUpgradeRegistry.instance.registerModule(key, this);
    }

    @Override
    public String getName(ItemStack stack) {
        return "Wooden hilt";
    }

    @Override
    public void addModule(ItemStack targetStack, ItemStack[] materials) {
        NBTTagCompound tag = targetStack.getTagCompound();

        tag.setString(ItemSwordModular.hiltKey, key);
    }

    @Override
    public ItemStack[] removeModule(ItemStack targetStack, ItemStack[] tools) {
        return new ItemStack[0];
    }
}