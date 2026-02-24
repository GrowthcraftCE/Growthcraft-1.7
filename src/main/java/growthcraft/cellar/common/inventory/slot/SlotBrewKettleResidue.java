package growthcraft.cellar.common.inventory.slot;

import growthcraft.core.common.inventory.slot.GrcSlot;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotBrewKettleResidue extends GrcSlot
{
	public SlotBrewKettleResidue(IInventory inv, int x, int y, int z)
	{
		super(inv, x, y, z);
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return true;
	}
}
