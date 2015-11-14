package growthcraft.cellar.common.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class SlotBrewKettleResidue extends Slot
{
	public SlotBrewKettleResidue(Container cont, IInventory inv, int x, int y, int z)
	{
		super(inv, x, y, z);
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return true;
	}
}
