package growthcraft.bees.common.inventory;

import growthcraft.api.bees.BeesRegistry;
import growthcraft.bees.common.tileentity.TileEntityBeeBox;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBeeBox extends Container
{
	public static class SlotId
	{
		public static final int BEE = 0;
		public static final int HONEY_COMB_START = 1;
		public static final int HONEY_COMB_END = HONEY_COMB_START + 27;
		public static final int PLAYER_BACKPACK_START = HONEY_COMB_END;
		public static final int PLAYER_BACKPACK_END = PLAYER_BACKPACK_START + 27;
		public static final int PLAYER_INVENTORY_START = PLAYER_BACKPACK_END;
		public static final int PLAYER_INVENTORY_END = PLAYER_INVENTORY_START + 9;

		private SlotId() {}
	}

	private TileEntityBeeBox te;

	public ContainerBeeBox(InventoryPlayer player, TileEntityBeeBox beeBox)
	{
		// Slot Indexes:
		//0            bee
		//1 - 27 (28)  honeycomb
		//28 - 54 (55) player.inv.backpack
		//55 - 63 (64) player.inv.hotbar

		this.te = beeBox;
		this.addSlotToContainer(new SlotBee(this, te, SlotId.BEE, 80, 18));
		int i;

		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new SlotHoneyComb(this, te,
						j + i * 9 + SlotId.HONEY_COMB_START,
						8 + j * 18,
						50 + i * 18));
			}
		}

		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(player,
						j + i * 9 + 9,
						8 + j * 18,
						118 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(player,
					i,
					8 + i * 18,
					176));
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

			if (index == SlotId.BEE)
			{
				if (!this.mergeItemStack(stack, SlotId.PLAYER_BACKPACK_START, SlotId.PLAYER_BACKPACK_END, false))
				{
					return null;
				}

				slot.onSlotChange(stack, itemstack);
			}
			else if (index >= SlotId.HONEY_COMB_START && index < SlotId.HONEY_COMB_END)
			{
				if (!this.mergeItemStack(stack, SlotId.PLAYER_BACKPACK_START, SlotId.PLAYER_BACKPACK_END, true))
				{
					return null;
				}

				slot.onSlotChange(stack, itemstack);
			}
			else
			{
				if (BeesRegistry.instance().isItemBee(stack))
				{
					if (!this.mergeItemStack(stack, SlotId.BEE, SlotId.BEE + 1, false))
					{
						return null;
					}
				}
				else if (BeesRegistry.instance().isItemHoneyComb(stack))
				{
					if (!this.mergeHoneyStack(stack, SlotId.HONEY_COMB_START, SlotId.HONEY_COMB_END, false))
					{
						return null;
					}
				}
				else if (index >= SlotId.PLAYER_BACKPACK_START && index < SlotId.PLAYER_BACKPACK_END)
				{
					if (!this.mergeItemStack(stack, SlotId.PLAYER_INVENTORY_START, SlotId.PLAYER_INVENTORY_END, false))
					{
						return null;
					}
				}
				else if (index >= SlotId.PLAYER_INVENTORY_START && index < SlotId.PLAYER_INVENTORY_END)
				{
					if (!this.mergeItemStack(stack, SlotId.PLAYER_BACKPACK_START, SlotId.PLAYER_BACKPACK_END, false))
					{
						return null;
					}
				}
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

	private boolean mergeHoneyStack(ItemStack stack0, int index0, int index1, boolean flag)
	{
		boolean ret = false;
		int i = index0;

		if (flag)
		{
			i = index1 - 1;
		}

		Slot slot;
		ItemStack stack1;

		if (stack0.stackSize > 0)
		{
			if (flag)
			{
				i = index1 - 1;
			}
			else
			{
				i = index0;
			}

			while (!flag && i < index1 || flag && i >= index0)
			{
				slot = (Slot)this.inventorySlots.get(i);
				stack1 = slot.getStack();

				if (stack1 == null)
				{
					slot.putStack(new ItemStack(stack0.getItem(), 1, stack0.getItemDamage()));
					--stack0.stackSize;
					slot.onSlotChanged();
					ret = true;

					if (stack0.stackSize <= 0)
					{
						break;
					}
					//stack0.stackSize = 0;
					//break;
				}

				if (flag)
				{
					--i;
				}
				else
				{
					++i;
				}
			}
		}

		return ret;
	}
}
