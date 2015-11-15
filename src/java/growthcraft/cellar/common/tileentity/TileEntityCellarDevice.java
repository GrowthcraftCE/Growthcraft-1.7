package growthcraft.cellar.common.tileentity;

import java.util.Random;

import growthcraft.core.common.inventory.GrcInternalInventory;
import growthcraft.core.common.inventory.IInventoryWatcher;
import growthcraft.core.common.tileentity.ICustomDisplayName;
import growthcraft.core.common.tileentity.IGuiNetworkSync;
import growthcraft.core.util.ItemUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public abstract class TileEntityCellarDevice extends TileEntity implements ISidedInventory, IFluidHandler, ICustomDisplayName, IInventoryWatcher, IGuiNetworkSync
{
	protected String name;
	protected GrcInternalInventory inventory;
	protected CellarTank[] tanks;
	protected int[] tankCaps;
	protected boolean needInventoryUpdate;
	protected boolean needBlockUpdate;
	protected Random random = new Random();

	public TileEntityCellarDevice()
	{
		super();
		this.inventory = createInventory();
		this.tanks = createTanks();
	}

	public void onInventoryChanged(IInventory inv, int index)
	{
		markDirty();
	}

	public void onItemDiscarded(IInventory inv, ItemStack stack, int index)
	{
		ItemUtils.spawnItemStack(worldObj, xCoord, yCoord, zCoord, stack, random);
	}

	protected abstract GrcInternalInventory createInventory();
	protected abstract CellarTank[] createTanks();
	public abstract String getDefaultInventoryName();
	public abstract void updateCellarDevice();
	public abstract void sendGUINetworkData(Container container, ICrafting icrafting);
	public abstract void receiveGUINetworkData(int id, int value);

	// Call this when you modified the inventory, or your not sure what
	// kind of update you require
	protected void markForInventoryUpdate()
	{
		needInventoryUpdate = true;
	}

	// Call this ONLY when you absolutely need to update the block's state
	// @eg rendering
	protected void markForBlockUpdate()
	{
		needBlockUpdate = true;
	}

	// Call this when you modify a fluid tank outside of its usual methods
	protected void markForFluidUpdate()
	{
		//
	}

	protected void checkUpdateFlags()
	{
		if (needInventoryUpdate)
		{
			needInventoryUpdate = false;
			this.markDirty();
		}
		if (needBlockUpdate)
		{
			needBlockUpdate = false;
			this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		}
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		checkUpdateFlags();

		if (!this.worldObj.isRemote)
		{
			updateCellarDevice();
		}
	}

	@Override
	public String getInventoryName()
	{
		return this.hasCustomInventoryName() ? this.name : getDefaultInventoryName();
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

	@Override
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

	@Override
	public ItemStack decrStackSize(int index, int par2)
	{
		return inventory.decrStackSize(index, par2);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index)
	{
		return inventory.getStackInSlotOnClosing(index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack itemstack)
	{
		inventory.setInventorySlotContents(index, itemstack);
	}

	@Override
	public int getInventoryStackLimit()
	{
		return inventory.getInventoryStackLimit();
	}

	@Override
	public int getSizeInventory()
	{
		return inventory.getSizeInventory();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		if (this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this)
		{
			return false;
		}
		return player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(){}

	@Override
	public void closeInventory(){}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack itemstack)
	{
		return inventory.isItemValidForSlot(index, itemstack);
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, int side)
	{
		return isItemValidForSlot(index, stack);
	}

	@Override
	public abstract int[] getAccessibleSlotsFromSide(int side);

	@Override
	public abstract boolean canExtractItem(int index, ItemStack stack, int side);

	/**
	 * @param nbt - nbt data to load
	 */
	protected void readInventorySlotsFromNBT(NBTTagCompound nbt)
	{
		inventory.readFromNBT(nbt, "items");
	}

	protected void readTanksFromNBT(NBTTagCompound nbt)
	{
		for (int i = 0; i < tanks.length; i++)
		{
			tanks[i] = new CellarTank(tankCaps[i], this);
			if (nbt.hasKey("Tank" + i))
			{
				tanks[i].readFromNBT(nbt.getCompoundTag("Tank" + i));
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		readInventorySlotsFromNBT(nbt);
		readTanksFromNBT(nbt);

		if (nbt.hasKey("name"))
		{
			this.name = nbt.getString("name");
		}
	}

	protected void writeTanksToNBT(NBTTagCompound nbt)
	{
		for (int i = 0; i < tanks.length; i++)
		{
			final NBTTagCompound tag = new NBTTagCompound();
			tanks[i].writeToNBT(tag);
			nbt.setTag("Tank" + i, tag);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		inventory.writeToNBT(nbt, "items");

		// TANKS
		writeTanksToNBT(nbt);

		// NAME
		if (this.hasCustomInventoryName())
		{
			nbt.setString("name", this.name);
		}
	}

	@Override
	public Packet getDescriptionPacket()
	{
		final NBTTagCompound nbt = new NBTTagCompound();
		writeTanksToNBT(nbt);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 64, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
	{
		readTanksFromNBT(packet.func_148857_g());
		this.worldObj.func_147479_m(this.xCoord, this.yCoord, this.zCoord);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		final FluidTankInfo[] tankInfos = new FluidTankInfo[tanks.length];
		for (int i = 0; i < tanks.length; ++i)
		{
			tankInfos[i] = tanks[i].getInfo();
		}
		return tankInfos;
	}

	public int getFluidAmountScaled(int scalar, int slot)
	{
		return this.getFluidAmount(slot) * scalar / tanks[slot].getCapacity();
	}

	public boolean isFluidTankFilled(int slot)
	{
		return this.getFluidAmount(slot) > 0;
	}

	public boolean isFluidTankFull(int slot)
	{
		return this.getFluidAmount(slot) >= tanks[slot].getCapacity();
	}

	public boolean isFluidTankEmpty(int slot)
	{
		return this.getFluidAmount(slot) <= 0;
	}

	public int getFluidAmount(int slot)
	{
		return tanks[slot].getFluidAmount();
	}

	public CellarTank getFluidTank(int slot)
	{
		return tanks[slot];
	}

	public FluidStack getFluidStack(int slot)
	{
		return tanks[slot].getFluid();
	}

	public Fluid getFluid(int slot)
	{
		return getFluidStack(slot).getFluid();
	}

	public void clearTank(int slot)
	{
		tanks[slot].setFluid(null);

		markForFluidUpdate();
	}
}
