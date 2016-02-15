package growthcraft.bees.common.inventory;

import growthcraft.api.bees.BeesRegistry;
import growthcraft.core.common.inventory.slot.GrcSlot;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotHoneyComb extends GrcSlot
{
	final ContainerBeeBox con;

	public SlotHoneyComb(ContainerBeeBox cont, IInventory inv, int x, int y, int z)
	{
		super(inv, x, y, z);
		this.con = cont;
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return BeesRegistry.instance().isItemHoneyComb(stack);
	}

	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}
}
