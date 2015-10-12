package growthcraft.cellar.container;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.tileentity.TileEntityFruitPress;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFruitPress extends Container
{
	private TileEntityFruitPress te;

	public ContainerFruitPress(InventoryPlayer player, TileEntityFruitPress fruitPress)
	{
		// Slot Indexes:
		//0            raw
		//1            residue
		//2 - 28 (29)  player.inv.backpack
		//29 - 37 (38) player.inv.hotbar

		this.te = fruitPress;
		this.addSlotToContainer(new Slot(te, 0, 45, 35));
		this.addSlotToContainer(new SlotFruitPressResidue(this, te, 1, 116, 17));
		int i;

		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(player, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
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

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int v)
	{
		te.getGUINetworkData(id, v);
	}
}
