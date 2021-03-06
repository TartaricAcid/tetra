package se.mickelus.tetra.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import se.mickelus.tetra.network.PacketHandler;

public class TetraBlock extends Block implements ITetraBlock {

    public TetraBlock(Material material) {
        super(material);
    }

    @Override
    public void clientPreInit() {
        Item item = Item.getItemFromBlock(this);
        if (item != Items.AIR) {
            ModelLoader.setCustomModelResourceLocation(item , 0, new ModelResourceLocation(getRegistryName(), "inventory"));
        }
    }

    @Override
    public void init(PacketHandler packetHandler) {

    }
}
