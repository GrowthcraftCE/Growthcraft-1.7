/*
 * The MIT License (MIT)
 *
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
package growthcraft.core.common.tileentity;

import java.io.IOException;
import java.util.Random;

import growthcraft.api.core.fluids.FluidTest;
import growthcraft.core.common.inventory.GrcInternalInventory;
import growthcraft.core.common.tileentity.device.FluidTanks;
import growthcraft.core.common.tileentity.device.IFluidTanks;
import growthcraft.core.common.tileentity.event.EventHandler;
import growthcraft.core.util.ItemUtils;

import io.netty.buffer.ByteBuf;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;

/**
 * Only use this class if you know what you're doing
 * This class implements both the IIventory and IFluidHandler interfaces, however
 * it doesn't explicitly say so.
 */
public abstract class GrcTileEntityFullBase extends GrcTileEntityBase
{
	protected String inventoryName;
	protected GrcInternalInventory inventory;
	protected boolean needInventoryUpdate;
	protected Random random = new Random();
	private FluidTanks tanks;

	public GrcTileEntityFullBase()
	{
		super();

		if (this instanceof IInventory)
			this.inventory = createInventory();

		if (this instanceof IFluidTanks)
			this.tanks = new FluidTanks(createTanks());
	}

	protected GrcInternalInventory createInventory()
	{
		return null;
	}

	public String getDefaultInventoryName()
	{
		return "grc.inventory.name";
	}

	// Call this when you have modified the inventory, or you're not sure what
	// kind of update you require
	public void markForInventoryUpdate()
	{
		needInventoryUpdate = true;
	}

	public void onInventoryChanged(IInventory inv, int index)
	{
		markForInventoryUpdate();
	}

	public void onItemDiscarded(IInventory inv, ItemStack stack, int index, int discardedAmount)
	{
		final ItemStack discarded = stack.copy();
		discarded.stackSize = discardedAmount;
		ItemUtils.spawnItemStack(worldObj, xCoord, yCoord, zCoord, discarded, random);
	}

	protected void checkUpdateFlags()
	{
		if (needInventoryUpdate)
		{
			needInventoryUpdate = false;
			markDirty();
		}
	}

	public String getInventoryName()
	{
		return hasCustomInventoryName() ? inventoryName : getDefaultInventoryName();
	}

	public boolean hasCustomInventoryName()
	{
		return inventoryName != null && inventoryName.length() > 0;
	}

	public void setGuiDisplayName(String string)
	{
		this.inventoryName = string;
	}

	public ItemStack getStackInSlot(int index)
	{
		return inventory.getStackInSlot(index);
	}

	public ItemStack tryMergeItemIntoSlot(ItemStack itemstack, int index)
	{
		final ItemStack result = ItemUtils.mergeStacksBang(getStackInSlot(index), itemstack);
		if (result != null)
		{
			inventory.setInventorySlotContents(index, result);
		}
		return result;
	}

	// Attempts to merge the given itemstack into the main slot
	public ItemStack tryMergeItemIntoMainSlot(ItemStack itemstack)
	{
		return tryMergeItemIntoSlot(itemstack, 0);
	}

	public ItemStack decrStackSize(int index, int par2)
	{
		return inventory.decrStackSize(index, par2);
	}

	public ItemStack getStackInSlotOnClosing(int index)
	{
		return inventory.getStackInSlotOnClosing(index);
	}

	public void setInventorySlotContents(int index, ItemStack itemstack)
	{
		inventory.setInventorySlotContents(index, itemstack);
	}

	public int getInventoryStackLimit()
	{
		return inventory.getInventoryStackLimit();
	}

	public int getSizeInventory()
	{
		return inventory.getSizeInventory();
	}

	public boolean isUseableByPlayer(EntityPlayer player)
	{
		if (worldObj.getTileEntity(xCoord, yCoord, zCoord) != this)
		{
			return false;
		}
		return player.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64.0D;
	}

	public void openInventory() {}

	public void closeInventory() {}

	public boolean isItemValidForSlot(int index, ItemStack itemstack)
	{
		return inventory.isItemValidForSlot(index, itemstack);
	}

	public boolean canInsertItem(int index, ItemStack stack, int side)
	{
		return isItemValidForSlot(index, stack);
	}

	public int[] getAccessibleSlotsFromSide(int side)
	{
		return new int[]{};
	}

	public boolean canExtractItem(int index, ItemStack stack, int side)
	{
		return true;
	}

	protected FluidTank[] createTanks()
	{
		return new FluidTank[] {};
	}

	protected void updateDevice()
	{
	}

	// Call this when you modify a fluid tank outside of its usual methods
	protected void markForFluidUpdate()
	{
		//
	}

	public void updateEntity()
	{
		super.updateEntity();

		checkUpdateFlags();

		if (!worldObj.isRemote)
		{
			updateDevice();
		}
	}

	private void readInventoryFromNBT(NBTTagCompound nbt)
	{
		if (inventory != null)
		{
			if (nbt.hasKey("items"))
			{
				inventory.readFromNBT(nbt, "items");
			}
			else if (nbt.hasKey("inventory"))
			{
				inventory.readFromNBT(nbt, "inventory");
			}
		}
	}

	private void readInventoryNameFromNBT(NBTTagCompound nbt)
	{
		if (inventory != null)
		{
			if (nbt.hasKey("name"))
			{
				this.inventoryName = nbt.getString("name");
			}
			else if (nbt.hasKey("inventory_name"))
			{
				this.inventoryName = nbt.getString("inventory_name");
			}
		}
	}

	protected void readTanksFromNBT(NBTTagCompound nbt)
	{
		if (tanks != null)
			tanks.readFromNBT(nbt);
	}

	@Override
	public void readFromNBTForItem(NBTTagCompound nbt)
	{
		readTanksFromNBT(nbt);
		readInventoryFromNBT(nbt);
		// Do not reload the inventory name from NBT, allow the ItemStack to do that
		//readInventoryNameFromNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		readTanksFromNBT(nbt);
		readInventoryFromNBT(nbt);
		readInventoryNameFromNBT(nbt);
	}

	private void writeInventoryToNBT(NBTTagCompound nbt)
	{
		if (inventory != null)
		{
			inventory.writeToNBT(nbt, "inventory");
			// NAME
			if (hasCustomInventoryName())
			{
				nbt.setString("inventory_name", inventoryName);
			}
			nbt.setInteger("inventory_tile_version", 3);
		}
	}

	private void writeTanksToNBT(NBTTagCompound nbt)
	{
		if (tanks != null)
			tanks.writeToNBT(nbt);
	}

	@Override
	public void writeToNBTForItem(NBTTagCompound nbt)
	{
		super.writeToNBTForItem(nbt);
		writeInventoryToNBT(nbt);
		writeTanksToNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		writeInventoryToNBT(nbt);
		writeTanksToNBT(nbt);
	}

	@EventHandler(type=EventHandler.EventType.NETWORK_READ)
	public boolean readFromStream_FluidTanks(ByteBuf stream) throws IOException
	{
		if (tanks != null)
			tanks.readFromStream(stream);
		return true;
	}

	@EventHandler(type=EventHandler.EventType.NETWORK_WRITE)
	public boolean writeToStream_FluidTanks(ByteBuf stream) throws IOException
	{
		if (tanks != null)
			tanks.writeToStream(stream);
		return false;
	}

	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return true;
	}

	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return true;
	}

	protected FluidStack doDrain(ForgeDirection dir, int amount, boolean shouldDrain)
	{
		return null;
	}

	protected FluidStack doDrain(ForgeDirection dir, FluidStack stack, boolean shouldDrain)
	{
		return null;
	}

	public FluidStack drain(ForgeDirection dir, int amount, boolean shouldDrain)
	{
		final FluidStack result = doDrain(dir, amount, shouldDrain);
		if (shouldDrain && FluidTest.isValid(result)) markForFluidUpdate();
		return result;
	}

	public FluidStack drain(ForgeDirection dir, FluidStack stack, boolean shouldDrain)
	{
		if (!FluidTest.isValid(stack)) return null;
		final FluidStack result = doDrain(dir, stack, shouldDrain);
		if (shouldDrain && FluidTest.isValid(result)) markForFluidUpdate();
		return result;
	}

	protected int doFill(ForgeDirection dir, FluidStack stack, boolean shouldFill)
	{
		return 0;
	}

	public int fill(ForgeDirection dir, FluidStack stack, boolean shouldFill)
	{
		final int result = doFill(dir, stack, shouldFill);
		if (shouldFill && result != 0) markForFluidUpdate();
		return result;
	}

	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return tanks.getTankInfo(from);
	}

	public IFluidTanks getTanks()
	{
		return tanks;
	}

	public int getTankCount()
	{
		return tanks.getTankCount();
	}

	public FluidTank[] getFluidTanks()
	{
		return tanks.getFluidTanks();
	}

	public int getFluidAmountScaled(int scalar, int slot)
	{
		return tanks.getFluidAmountScaled(scalar, slot);
	}

	public float getFluidAmountRate(int slot)
	{
		return tanks.getFluidAmountRate(slot);
	}

	public boolean isFluidTankFilled(int slot)
	{
		return tanks.isFluidTankFilled(slot);
	}

	public boolean isFluidTankFull(int slot)
	{
		return tanks.isFluidTankFull(slot);
	}

	public boolean isFluidTankEmpty(int slot)
	{
		return tanks.isFluidTankEmpty(slot);
	}

	public int getFluidAmount(int slot)
	{
		return tanks.getFluidAmount(slot);
	}

	public FluidTank getFluidTank(int slot)
	{
		return tanks.getFluidTank(slot);
	}

	public FluidStack getFluidStack(int slot)
	{
		return tanks.getFluidStack(slot);
	}

	public FluidStack drainFluidTank(int slot, int amount, boolean doDrain)
	{
		final FluidStack result = tanks.drainFluidTank(slot, amount, doDrain);
		if (result != null && result.amount != 0) markForFluidUpdate();
		return result;
	}

	public int fillFluidTank(int slot, FluidStack fluid, boolean doFill)
	{
		final int result = tanks.fillFluidTank(slot, fluid, doFill);
		if (result != 0) markForFluidUpdate();
		return result;
	}

	public void setFluidStack(int slot, FluidStack stack)
	{
		tanks.setFluidStack(slot, stack);
		markForFluidUpdate();
	}

	public Fluid getFluid(int slot)
	{
		return tanks.getFluid(slot);
	}

	public void clearTank(int slot)
	{
		tanks.clearTank(slot);
		markForFluidUpdate();
	}
}
