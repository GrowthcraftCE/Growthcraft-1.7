package growthcraft.bees.common.inventory;

import growthcraft.api.bees.BeesRegistry;
import growthcraft.core.common.inventory.slot.SlotInput;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotHoneyComb extends SlotInput
{
	final ContainerBeeBox container;

	public SlotHoneyComb(ContainerBeeBox cont, IInventory inv, int x, int y, int z)
	{
		super(inv, x, y, z);
		this.container = cont;
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		// Only if the item is some kind of honey comb
		return BeesRegistry.instance().isItemHoneyComb(stack);
	}

	@Override
	public int getSlotStackLimit()
	{
		// 1 comb per slot
		return 1;
	}
}
