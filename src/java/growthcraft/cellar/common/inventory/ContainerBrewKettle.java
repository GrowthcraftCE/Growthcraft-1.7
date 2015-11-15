package growthcraft.cellar.common.inventory;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.common.tileentity.TileEntityBrewKettle;
import growthcraft.cellar.common.inventory.slot.SlotBrewKettleResidue;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBrewKettle extends CellarContainer
{
	public ContainerBrewKettle(InventoryPlayer player, TileEntityBrewKettle brewKettle)
	{
		super(brewKettle);
		// Slot Indexes:
		//0            raw
		//1            residue
		//2 - 28 (29)  player.inv.backpack
		//29 - 37 (38) player.inv.hotbar

		addSlotToContainer(new Slot(brewKettle, 0, 80, 35));
		addSlotToContainer(new SlotBrewKettleResidue(this, brewKettle, 1, 141, 17));

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

			if (index == 1)
			{
				if (!this.mergeItemStack(stack, 2, 38, true))
				{
					return null;
				}

				slot.onSlotChange(stack, itemstack);
			}
			else if (index != 0)
			{
				if (CellarRegistry.instance().brewing().isItemBrewingIngredient(stack))
				{
					if (!this.mergeItemStack(stack, 0, 1, false))
					{
						return null;
					}
				}
				else if (index >= 2 && index < 29)
				{
					if (!this.mergeItemStack(stack, 29, 38, false))
					{
						return null;
					}
				}
				else if (index >= 29 && index < 38 && !this.mergeItemStack(stack, 2, 29, false))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(stack, 2, 38, false))
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
