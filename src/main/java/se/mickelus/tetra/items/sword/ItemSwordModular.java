package se.mickelus.tetra.items.sword;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import se.mickelus.tetra.DataHandler;
import se.mickelus.tetra.items.ItemModularHandheld;
import se.mickelus.tetra.module.ItemUpgradeRegistry;
import se.mickelus.tetra.module.schema.*;
import se.mickelus.tetra.network.PacketHandler;

import java.util.Map;

public class ItemSwordModular extends ItemModularHandheld {

    public final static String bladeKey = "sword/blade";
    public final static String hiltKey = "sword/hilt";

    public final static String guardKey = "sword/guard";
    public final static String pommelKey = "sword/pommel";
    public final static String fullerKey = "sword/fuller";


    static final String unlocalizedName = "sword_modular";

    public ItemSwordModular() {
        setUnlocalizedName(unlocalizedName);
        setRegistryName(unlocalizedName);
        setMaxStackSize(1);

        majorModuleNames = new String[]{"Blade", "Hilt"};
        majorModuleKeys = new String[]{bladeKey, hiltKey};
        minorModuleNames = new String[]{"Fuller", "Guard", "Pommel"};
        minorModuleKeys = new String[]{fullerKey, guardKey, pommelKey};

        new BladeModule(bladeKey);
        new ShortBladeModule(bladeKey);
        new HeavyBladeModule(bladeKey);
        new HiltModule(hiltKey);

        new MakeshiftGuardModule(guardKey);
        new DecorativePommelModule(pommelKey);
    }

    @Override
    public void init(PacketHandler packetHandler) {
        registerConfigSchema("sword/basic_blade");
        registerConfigSchema("sword/basic_blade_improvements");
        new BookEnchantSchema(BladeModule.instance);

        registerConfigSchema("sword/short_blade");
        registerConfigSchema("sword/short_blade_improvements");
        new BookEnchantSchema(ShortBladeModule.instance);

        registerConfigSchema("sword/heavy_blade");
        new BookEnchantSchema(HeavyBladeModule.instance);

        registerConfigSchema("sword/basic_hilt");
        new BookEnchantSchema(HiltModule.instance);

        new RepairSchema(this);

        ItemUpgradeRegistry.instance.registerPlaceholder(this::replaceSword);
    }

    private ItemStack replaceSword(ItemStack originalStack) {
        Item originalItem = originalStack.getItem();
        ItemStack newStack;

        if (!(originalItem instanceof ItemSword)) {
            return null;
        }

        newStack = createItemStack(((ItemSword) originalItem).getToolMaterialName());
        newStack.setItemDamage(originalStack.getItemDamage());
        transferEnchantments(originalStack, newStack);

        return newStack;
    }

    private ItemStack createItemStack(String material) {
        ItemStack itemStack = new ItemStack(this);
        itemStack.setTagCompound(new NBTTagCompound());

        ItemStack bladeMaterial = getStackFromMaterialString(material);

        BladeModule.instance.addModule(itemStack, new ItemStack[] {bladeMaterial}, false, null);
        HiltModule.instance.addModule(itemStack, new ItemStack[] {new ItemStack(Items.STICK)}, false, null);
        MakeshiftGuardModule.instance.addModule(itemStack, new ItemStack[] {bladeMaterial}, false, null);
        DecorativePommelModule.instance.addModule(itemStack, new ItemStack[] {bladeMaterial}, false, null);

        return itemStack;
    }

    private void transferEnchantments(ItemStack sourceStack, ItemStack modularStack) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(sourceStack);
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            String improvement = ItemUpgradeRegistry.instance.getImprovementFromEnchantment(entry.getKey());
            if (BladeModule.instance.acceptsImprovement(improvement) && HiltModule.instance.acceptsImprovement(improvement)) {
                BladeModule.instance.addImprovement(modularStack, improvement, (int) Math.ceil(entry.getValue() / 2f));
                HiltModule.instance.addImprovement(modularStack, improvement, (int) Math.floor(entry.getValue() / 2f));
            } else if (BladeModule.instance.acceptsImprovement(improvement)) {
                BladeModule.instance.addImprovement(modularStack, improvement, entry.getValue());
            } else if (HiltModule.instance.acceptsImprovement(improvement)) {
                HiltModule.instance.addImprovement(modularStack, improvement, entry.getValue());
            }
        }
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockState) {
        return blockState.getBlock() == Blocks.WEB;
    }
}
