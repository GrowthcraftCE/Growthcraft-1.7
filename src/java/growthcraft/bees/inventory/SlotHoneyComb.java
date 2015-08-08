package growthcraft.bees.inventory;

import growthcraft.bees.GrowthCraftBees;
import growthcraft.bees.block.ContainerBeeBox;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotHoneyComb extends Slot
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
		return stack != null ? stack.getItem() == GrowthCraftBees.honeyComb : false;
	}

	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}
}
