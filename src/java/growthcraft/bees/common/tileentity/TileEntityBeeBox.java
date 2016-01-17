package growthcraft.bees.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import growthcraft.api.bees.BeesRegistry;
import growthcraft.bees.common.inventory.ContainerBeeBox;
import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.common.inventory.GrcInternalInventory;
import growthcraft.core.common.tileentity.GrcTileEntityInventoryBase;
import growthcraft.core.util.ItemUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityBeeBox extends GrcTileEntityInventoryBase
{
	public static enum HoneyCombExpect
	{
		ANY,
		EMPTY,
		FILLED;
	}

	private static final int beeBoxVersion = 2;
	private static final int[] beeSlotIds = new int[] {0};
	private static final int[] honeyCombSlotIds = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27};

	// Temp variable used by BlockBeeBox for storing flower lists
	@SuppressWarnings({"rawtypes", "unchecked"})
	public ArrayList<List> flowerList = new ArrayList<List>();

	private int time;

	@Override
	public String getDefaultInventoryName()
	{
		return "container.grc.beeBox";
	}

	@Override
	protected GrcInternalInventory createInventory()
	{
		return new GrcInternalInventory(this, 28);
	}

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
		final ItemStack slotItem = getStackInSlot(index);
		if (slotItem == null) return false;
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

	public int countCombsOfType(HoneyCombExpect type)
	{
		int count = 0;
		for (int i = 1; i < getSizeInventory(); ++i)
		{
			if (getStackInSlot(i) != null)
			{
				if (slotHasHoneyComb(i, type))
				{
					count++;
				}
			}
		}
		return count;
	}

	//counts filled honeycombs only
	public int countHoney()
	{
		return countCombsOfType(HoneyCombExpect.FILLED);
	}

	public int countEmptyCombs()
	{
		return countCombsOfType(HoneyCombExpect.ANY);
	}

	//counts both empty and filled honeycombs
	public int countCombs()
	{
		return countCombsOfType(HoneyCombExpect.ANY);
	}

	public int getHoneyCombMax()
	{
		return honeyCombSlotIds.length;
	}

	public int countBees()
	{
		final ItemStack stack = getStackInSlot(ContainerBeeBox.SlotId.BEE);
		if (stack == null) return 0;
		return stack.stackSize;
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

	public void decreaseHoney(int count)
	{
		for (int i = 1; i < getSizeInventory(); ++i)
		{
			if (count <= 0) break;
			if (slotHasHoneyComb(i, HoneyCombExpect.FILLED))
			{
				final ItemStack stack = getStackInSlot(i);
				final ItemStack result = BeesRegistry.instance().getEmptyHoneyComb(stack);
				setInventorySlotContents(i, result != null ? result.copy() : null);
				count--;
			}
		}
	}

	public ItemStack getBeeStack()
	{
		return getStackInSlot(ContainerBeeBox.SlotId.BEE);
	}

	private void setBeeStack(ItemStack itemstack)
	{
		setInventorySlotContents(ContainerBeeBox.SlotId.BEE, itemstack);
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

	public void spawnHoneyCombs(int n)
	{
		for (int i = 1; i < getSizeInventory(); ++i)
		{
			if (n <= 0) break;
			final ItemStack stack = getStackInSlot(i);
			if (stack == null)
			{
				setInventorySlotContents(i, GrowthCraftBees.honeyCombEmpty.asStack());
				n--;
			}
		}
	}

	public void spawnHoneyComb()
	{
		spawnHoneyCombs(1);
	}

	public void fillHoneyCombs(int n)
	{
		for (int i = 1; i < getSizeInventory(); ++i)
		{
			if (n <= 0) break;
			final ItemStack stack = getStackInSlot(i);
			if (stack != null && slotHasEmptyComb(i))
			{
				final ItemStack resultStack = BeesRegistry.instance().getFilledHoneyComb(stack).copy();
				setInventorySlotContents(i, resultStack);
				n--;
			}
		}
	}

	public void fillHoneyComb()
	{
		fillHoneyCombs(1);
	}

	/************
	 * NBT
	 ************/
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.time = nbt.getShort("time");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setShort("time", (short)this.time);
		nbt.setInteger("BeeBox.version", beeBoxVersion);
	}

	/************
	 * HOPPER
	 ************/
	@Override
	public boolean isItemValidForSlot(int index, ItemStack itemstack)
	{
		if (index == ContainerBeeBox.SlotId.BEE)
		{
			return BeesRegistry.instance().isItemBee(itemstack);
		}
		else
		{
			return BeesRegistry.instance().isItemHoneyComb(itemstack);
		}
	}

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
