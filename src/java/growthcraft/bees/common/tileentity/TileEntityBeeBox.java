package growthcraft.bees.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import growthcraft.api.bees.BeesRegistry;
import growthcraft.bees.GrowthCraftBees;
import growthcraft.bees.common.inventory.ContainerBeeBox;
import growthcraft.core.util.ItemUtils;
import growthcraft.core.util.NBTHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBeeBox extends TileEntity implements ISidedInventory
{
	public static enum HoneyCombExpect
	{
		ANY,
		EMPTY,
		FILLED;
	}

	private static final int[] beeSlotIds = new int[] {0};
	private static final int[] honeyCombSlotIds = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9 , 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27};

	// Temp variable used by BlockBeeBox for storing flower lists
	@SuppressWarnings({"rawtypes", "unchecked"})
	public ArrayList<List> flowerList = new ArrayList<List>();

	// Constants
	private ItemStack[] invSlots   = new ItemStack[28];

	// Other Vars.
	private String   name;
	private int time;

	/************
	 * UPDATE
	 ************/
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			--this.time;
			if (this.time <= 0)
			{
				this.time = 0;
			}
		}
	}

	public boolean hasBonus()
	{
		return this.time > 0;
	}

	public void setTime(int v)
	{
		this.time = v;
	}

	public boolean slotHasHoneyComb(int index, HoneyCombExpect expects)
	{
		final ItemStack slotItem = this.invSlots[index];
		switch (expects)
		{
			case EMPTY:
				return BeesRegistry.instance().isItemEmptyHoneyComb(slotItem);
			case FILLED:
				return BeesRegistry.instance().isItemFilledHoneyComb(slotItem);
			default:
				return BeesRegistry.instance().isItemHoneyComb(slotItem);
		}
	}

	public boolean slotHasEmptyComb(int index)
	{
		return slotHasHoneyComb(index, HoneyCombExpect.EMPTY);
	}

	//counts filled honeycombs only
	public int countHoney()
	{
		int count = 0;
		for (int i = 1; i < this.invSlots.length; ++i)
		{
			if (this.invSlots[i] != null)
			{
				if (slotHasHoneyComb(i, HoneyCombExpect.FILLED))
				{
					count++;
				}
			}
		}
		return count;
	}

	//counts both empty and filled honeycombs
	public int countCombs()
	{
		int count = 0;
		for (int i = 1; i < this.invSlots.length; ++i)
		{
			if (this.invSlots[i] != null)
			{
				if (slotHasHoneyComb(i, HoneyCombExpect.ANY))
				{
					count++;
				}
			}
		}
		return count;
	}

	public int countBees()
	{
		if (this.invSlots[0] == null)
		{
			return 0;
		}
		return this.invSlots[0].stackSize;
	}

	public boolean hasBees()
	{
		return countBees() != 0;
	}

	public boolean hasMaxBees()
	{
		// in the case they use a mod that overrides the maximum item stack
		return countBees() >= 64;
	}

	public boolean isHoneyEnough()
	{
		return countHoney() >= 6;
	}

	public void decreaseHoney()
	{
		int count = 0;
		for (int i = 1; i < this.invSlots.length; ++i)
		{
			if (this.invSlots[i] != null)
			{
				if (slotHasHoneyComb(i, HoneyCombExpect.FILLED))
				{
					this.invSlots[i].setItemDamage(0);
					count++;
					if (count >= 6)
					{
						break;
					}
				}
			}
		}
	}

	public ItemStack getBeeStack()
	{
		return invSlots[ContainerBeeBox.SlotId.BEE];
	}

	private void setBeeStack(ItemStack itemstack)
	{
		invSlots[ContainerBeeBox.SlotId.BEE] = itemstack;
	}

	public void spawnBee()
	{
		final ItemStack beestack = getBeeStack();
		if (beestack == null)
		{
			setBeeStack(GrowthCraftBees.bee.asStack());
		}
		else
		{
			setBeeStack(ItemUtils.increaseStack(beestack));
		}
	}

	public void spawnHoneyComb()
	{
		for (int i = 1; i < this.invSlots.length; ++i)
		{
			if (this.invSlots[i] == null)
			{
				this.invSlots[i] = GrowthCraftBees.honeyCombEmpty.asStack();
				break;
			}
		}
	}

	public void fillHoneyComb()
	{
		for (int i = 1; i < this.invSlots.length; ++i)
		{
			if (this.invSlots[i] != null && slotHasEmptyComb(i))
			{
				this.invSlots[i] = BeesRegistry.instance().getFilledHoneyComb(invSlots[i]).copy();
				break;
			}
		}
	}

	/************
	 * INVENTORY
	 ************/
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return this.invSlots[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int par2)
	{
		if (this.invSlots[index] != null)
		{
			ItemStack itemstack;

			if (this.invSlots[index].stackSize <= par2)
			{
				itemstack = this.invSlots[index];
				this.invSlots[index] = null;
				return itemstack;
			}
			else
			{
				itemstack = this.invSlots[index].splitStack(par2);

				if (this.invSlots[index].stackSize == 0)
				{
					this.invSlots[index] = null;
				}

				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index)
	{
		if (this.invSlots[index] != null)
		{
			final ItemStack itemstack = this.invSlots[index];
			this.invSlots[index] = null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack itemstack)
	{
		this.invSlots[index] = itemstack;

		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
		{
			itemstack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public int getSizeInventory()
	{
		return this.invSlots.length;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		if (this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this)
		{
			return false;
		}
		else
		{
			return player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
		}
	}

	@Override
	public void openInventory(){}

	@Override
	public void closeInventory(){}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack itemstack)
	{
		if (index == 0)
		{
			return BeesRegistry.instance().isItemBee(itemstack);
		}
		else
		{
			return BeesRegistry.instance().isItemHoneyComb(itemstack);
		}
	}

	/************
	 * NBT
	 ************/

	/**
	 * @param nbt - nbt data to load
	 */
	protected void readInventorySlotsFromNBT(NBTTagCompound nbt)
	{
		this.invSlots = ItemUtils.clearInventorySlots(invSlots, getSizeInventory());
		NBTHelper.readInventorySlotsFromNBT(invSlots, nbt.getTagList("items", NBTHelper.NBTType.COMPOUND));
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		readInventorySlotsFromNBT(nbt);
		this.time = nbt.getShort("time");
		if (nbt.hasKey("name"))
		{
			this.name = nbt.getString("name");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setTag("items", NBTHelper.writeInventorySlotsToNBT(invSlots));
		nbt.setShort("time", (short)this.time);
		if (this.hasCustomInventoryName())
		{
			nbt.setString("name", this.name);
		}
	}

	/************
	 * NAMES
	 ************/
	@Override
	public String getInventoryName()
	{
		return this.hasCustomInventoryName() ? this.name : "container.grc.beeBox";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return this.name != null && this.name.length() > 0;
	}

	public void setGuiDisplayName(String string)
	{
		this.name = string;
	}

	/************
	 * HOPPER
	 ************/
	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return side == 1 ? beeSlotIds : honeyCombSlotIds;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, int side)
	{
		return this.isItemValidForSlot(index, stack);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, int side)
	{
		return true;
	}
}
