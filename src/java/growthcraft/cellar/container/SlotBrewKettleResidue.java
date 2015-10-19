package growthcraft.cellar.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBrewKettleResidue extends Slot
{
	final ContainerBrewKettle con;

	public SlotBrewKettleResidue(ContainerBrewKettle cont, IInventory inv, int x, int y, int z)
	{
		super(inv, x, y, z);
		this.con = cont;
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return true;
	}
}
