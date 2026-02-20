package growthcraft.bees.common.inventory;

import growthcraft.api.bees.BeesRegistry;
import growthcraft.core.common.inventory.slot.SlotInput;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotBee extends SlotInput
{
	final ContainerBeeBox container;

	public SlotBee(ContainerBeeBox cont, IInventory inv, int x, int y, int z)
	{
		super(inv, x, y, z);
		this.container = cont;
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
