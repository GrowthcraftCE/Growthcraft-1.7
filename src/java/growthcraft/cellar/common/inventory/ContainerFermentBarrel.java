package growthcraft.cellar.common.inventory;

import growthcraft.cellar.common.tileentity.TileEntityFermentBarrel;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFermentBarrel extends Container
{
	private TileEntityFermentBarrel te;

	public ContainerFermentBarrel(InventoryPlayer player, TileEntityFermentBarrel fermentBarrel)
	{
		// Slot Indexes:
		//0            raw
		//1 - 27 (28)  player.inv.backpack
		//28 - 36 (37) player.inv.hotbar

		this.te = fermentBarrel;
		this.addSlotToContainer(new Slot(te, 0, 43, 53));
		this.addSlotToContainer(new Slot(te, 1, 153, 35));
		int i;

		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				final int slotIndex = j + i * 9 + 9;
				final int x = 8 + j * 18;
				final int y = 84 + i * 18;
				this.addSlotToContainer(new Slot(player, slotIndex, x, y));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(player, i, 8 + i * 18, 142));
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
		final Slot slot = (Slot)this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			final ItemStack stack = slot.getStack();
			itemstack = stack.copy();

			if (index > 1)
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

	// crafters
	@Override
	public void addCraftingToCrafters(ICrafting iCrafting)
	{
		super.addCraftingToCrafters(iCrafting);
		te.sendGUINetworkData(this, iCrafting);
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		for (int i = 0; i < crafters.size(); i++)
		{
			te.sendGUINetworkData(this, (ICrafting) crafters.get(i));
		}
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int v)
	{
		te.getGUINetworkData(id, v);
	}
}
