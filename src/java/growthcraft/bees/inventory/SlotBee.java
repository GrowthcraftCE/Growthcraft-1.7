package growthcraft.bees.inventory;

import growthcraft.api.bees.BeesRegistry;
import growthcraft.bees.block.ContainerBeeBox;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBee extends Slot
{
	final ContainerBeeBox con;

	public SlotBee(ContainerBeeBox cont, IInventory inv, int x, int y, int z)
	{
		super(inv, x, y, z);
		this.con = cont;
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return stack != null ? BeesRegistry.instance().isItemBee(stack) : false;
	}

	@Override
	public int getSlotStackLimit()
	{
		return 64;
	}
}
