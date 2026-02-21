package growthcraft.cellar.common.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.core.common.inventory.slot.SlotInput;

public class SlotInputPressing extends SlotInput {

    public SlotInputPressing(IInventory inv, int x, int y, int z) {
        super(inv, x, y, z);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return CellarRegistry.instance()
            .pressing()
            .hasPressingRecipe(stack);
    }
}
