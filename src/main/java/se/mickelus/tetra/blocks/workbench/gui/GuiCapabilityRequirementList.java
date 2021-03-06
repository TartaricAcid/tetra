package se.mickelus.tetra.blocks.workbench.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import se.mickelus.tetra.capabilities.Capability;
import se.mickelus.tetra.gui.GuiElement;
import se.mickelus.tetra.module.schema.UpgradeSchema;

public class GuiCapabilityRequirementList extends GuiElement {

    private GuiCapabilityRequirement hammerIndicator;

    public GuiCapabilityRequirementList(int x, int y) {
        super(x, y, 54, 18);

        hammerIndicator = new GuiCapabilityRequirement(38, 0, Capability.hammer);
        addChild(hammerIndicator);
    }

    public void update(EntityPlayer player, UpgradeSchema schema, ItemStack targetStack, ItemStack[] materials) {
        setVisible(schema.isMaterialsValid(targetStack, materials));
        hammerIndicator.update(player, schema, targetStack, materials);
    }
}
