package growthcraft.cellar.common.inventory;

import growthcraft.cellar.common.tileentity.TileEntityFermentJar;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFermentJar extends CellarContainer
{
	public ContainerFermentJar(InventoryPlayer player, TileEntityFermentJar fermentJar)
	{
		super(fermentJar);
		// Slot Indexes:
		//0            output
		//1 - 27 (28)  player.inv.backpack
		//28 - 36 (37) player.inv.hotbar

		addSlotToContainer(new Slot(fermentJar, 0, 80, 35));

		bindPlayerInventory(player, 8, 84);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index)
	{
		ItemStack itemstack = null;
		final Slot slot = (Slot)this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			final ItemStack stack = slot.getStack();
			itemstack = stack.copy();

			if (index > 0)
			{
				if (!this.mergeItemStack(stack, 0, 1, false))
				{
					return null;
				}
				else if (index >= 1 && index < 28)
				{
					if (!this.mergeItemStack(stack, 28, 37, false))
					{
						return null;
					}
				}
				else if (index >= 28 && index < 37 && !this.mergeItemStack(stack, 1, 28, false))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(stack, 1, 37, false))
			{
				return null;
			}

			if (stack.stackSize == 0)
			{
				slot.putStack((ItemStack)null);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (stack.stackSize == itemstack.stackSize)
			{
				return null;
			}

			slot.onPickupFromSlot(player, stack);
		}

		return itemstack;
	}
}
