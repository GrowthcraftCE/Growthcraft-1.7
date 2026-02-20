/*
 * The MIT License (MIT)
 *
 * Copyright (c) < 2014, Gwafu
 * Copyright (c) 2015, 2016 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.fishtrap.common.tileentity;

import growthcraft.api.core.nbt.NBTType;
import growthcraft.api.fishtrap.BaitRegistry.BaitHandle;
import growthcraft.api.fishtrap.FishTrapRegistry;
import growthcraft.core.common.inventory.GrcInternalInventory;
import growthcraft.core.common.inventory.InventoryProcessor;
import growthcraft.core.common.inventory.InventorySlice;
import growthcraft.core.common.tileentity.feature.IInteractionObject;
import growthcraft.core.common.tileentity.GrcTileInventoryBase;
import growthcraft.fishtrap.common.inventory.ContainerFishTrap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TileEntityFishTrap extends GrcTileInventoryBase implements IInteractionObject
{
	private static final int[] TRAP_SLOTS = new int[] {0,1,2,3,4,5};
	private static final int[] BAIT_SLOTS = new int[] {6};
	private static final int[] ACCESSIBLE_SLOTS = new int[] {0,1,2,3,4,5,6};
	public InventorySlice trapInventory;
	public InventorySlice baitInventory;

	public TileEntityFishTrap()
	{
		super();
		this.trapInventory = new InventorySlice(this, TRAP_SLOTS);
		this.baitInventory = new InventorySlice(this, BAIT_SLOTS);
	}

	@Override
	public String getGuiID()
	{
		return "grcfishtrap:fish_trap";
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerFishTrap(playerInventory, this);
	}

	@Override
	public GrcInternalInventory createInventory()
	{
		return new GrcInternalInventory(this, 7);
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int facing)
	{
		// only insert into the bait slot
		if (slot == 6) {
			return super.canInsertItem(slot, stack, facing);
		} else {
			return false;
		}
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int facing)
	{
		// only extract from the loot/trap slots
		if (slot < 6) {
			return super.canExtractItem(slot, stack, facing);
		} else {
			return false;
		}
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int facing)
	{
		return ACCESSIBLE_SLOTS;
	}

	/**
	 * Called after a successful catch, this will remove an item from the stack
	 * of provided bait
	 */
	public void consumeBait()
	{
		baitInventory.decrStackSize(0, 1);
	}

	/**
	 * Called by the block to modify the catch rate
	 *
	 * @param f the current catch rate
	 * @return result the modified catch rate
	 */
	public float applyBaitModifier(float f)
	{
		float result = f;
		final ItemStack bait = baitInventory.getStackInSlot(0);
		if (bait != null)
		{
			final BaitHandle handle = FishTrapRegistry.instance().findBait(bait);
			if (handle != null)
			{
				result += handle.baseRate;
				result *= handle.multiplier;
			}
		}
		return result;
	}

	public int getBaitInventoryOffset()
	{
		return BAIT_SLOTS[0];
	}

	public int getTrapInventorySize()
	{
		return TRAP_SLOTS.length;
	}

	public int getBaitInventorySize()
	{
		return BAIT_SLOTS.length;
	}

	/**
	 * The fishtrap does not update on it's own, instead it relies on the
	 * block for processing.
	 */
	@Override
	public boolean canUpdate()
	{
		return false;
	}

	/**
	 * @param stack the stack to add to the invetory
	 * @return true, the stack was added, false otherwise
	 */
	public boolean addStack(ItemStack stack)
	{
		return InventoryProcessor.instance().mergeWithSlots(trapInventory, stack);
	}

	@Override
	public String getDefaultInventoryName()
	{
		return "container.grc.fishTrap";
	}

	@Override
	protected void readInventoryFromNBT(NBTTagCompound nbt)
	{
		// Backwards compatability
		if (nbt.hasKey("items"))
		{
			inventory.clear();
			final NBTTagList tags = nbt.getTagList("items", NBTType.COMPOUND.id);
			for (int i = 0; i < tags.tagCount(); ++i)
			{
				final NBTTagCompound item = tags.getCompoundTagAt(i);
				final byte b0 = item.getByte("Slot");
				if (b0 >= 0 && b0 < trapInventory.getSizeInventory())
				{
					final ItemStack stack = ItemStack.loadItemStackFromNBT(item);
					InventoryProcessor.instance().mergeWithSlot(trapInventory, stack, (int)b0);
				}
			}
		}
		else
		{
			super.readInventoryFromNBT(nbt);
		}
	}
}
