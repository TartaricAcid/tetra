package se.mickelus.tetra.items.toolbelt.gui;

import net.minecraft.util.ResourceLocation;
import se.mickelus.tetra.TetraMod;
import se.mickelus.tetra.gui.*;

public class GuiPotionsBackdrop extends GuiElement {
    private static final ResourceLocation texture = new ResourceLocation(TetraMod.MOD_ID, "textures/gui/toolbelt-inventory.png");
    public GuiPotionsBackdrop(int x, int y, int numSlots) {
        super(x, y, numSlots * 17 - 9, 28);

        setAttachmentPoint(GuiAttachment.topCenter);
        setAttachmentAnchor(GuiAttachment.topCenter);

        // background rects
        addChild(new GuiRect(0, 3, width, 22, 0xff000000));
        addChild(new GuiRect(0, 4, width, 1, GuiColors.muted));
        addChild(new GuiRect(0, 23, width, 1, GuiColors.normal));

        // left cap
        GuiTexture leftCap = new GuiTexture(0, 0, 16, 28, 64, 0, texture);
        leftCap.setAttachmentPoint(GuiAttachment.topRight);
        addChild(leftCap);

        GuiTexture rightCap = new GuiTexture(0, 0, 16, 28, 80, 0, texture);
        rightCap.setAttachmentPoint(GuiAttachment.topLeft);
        rightCap.setAttachmentAnchor(GuiAttachment.topRight);
        addChild(rightCap);
    }
}
