package growthcraft.cellar.common.inventory;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.common.tileentity.TileEntityFruitPress;
import growthcraft.cellar.common.inventory.slot.SlotFruitPressResidue;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFruitPress extends CellarContainer
{
	public ContainerFruitPress(InventoryPlayer player, TileEntityFruitPress fruitPress)
	{
		super(fruitPress);
		// Slot Indexes:
		//0            raw
		//1            residue
		//2 - 28 (29)  player.inv.backpack
		//29 - 37 (38) player.inv.hotbar

		addSlotToContainer(new Slot(fruitPress, 0, 45, 35));
		addSlotToContainer(new SlotFruitPressResidue(this, fruitPress, 1, 116, 17));

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
				if (CellarRegistry.instance().pressing().isPressingRecipe(stack))
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
