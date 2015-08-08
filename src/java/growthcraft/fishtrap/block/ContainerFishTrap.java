package growthcraft.fishtrap.block;

import growthcraft.fishtrap.entity.TileEntityFishTrap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFishTrap extends Container
{
	private TileEntityFishTrap te;

	public ContainerFishTrap(InventoryPlayer player, TileEntityFishTrap te)
	{
		this.te = te;
		te.openInventory();
		byte b0 = 51;
		int i;

		for (i = 0; i < te.getSizeInventory(); ++i)
		{
			this.addSlotToContainer(new Slot(te, i, 44 + i * 18, 20));
		}

		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(player, j + i * 9 + 9, 8 + j * 18, i * 18 + b0));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(player, i, 8 + i * 18, 58 + b0));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return this.te.isUseableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < this.te.getSizeInventory())
			{
				if (!this.mergeItemStack(itemstack1, this.te.getSizeInventory(), this.inventorySlots.size(), true))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 0, this.te.getSizeInventory(), false))
			{
				return null;
			}

			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack)null);
			}
			else
			{
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}
}
