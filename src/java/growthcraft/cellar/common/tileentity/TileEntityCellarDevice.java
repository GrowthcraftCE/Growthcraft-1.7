package growthcraft.cellar.common.tileentity;

import java.io.IOException;

import growthcraft.core.common.tileentity.event.EventHandler;
import growthcraft.core.common.tileentity.IGuiNetworkSync;
import growthcraft.core.common.tileentity.GrcBaseInventoryTile;
import growthcraft.core.util.StreamUtils;

import io.netty.buffer.ByteBuf;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public abstract class TileEntityCellarDevice extends GrcBaseInventoryTile implements IFluidHandler, IGuiNetworkSync
{
	private CellarTank[] tanks;

	public TileEntityCellarDevice()
	{
		super();

		this.tanks = createTanks();
	}

	protected abstract CellarTank[] createTanks();
	public abstract void updateCellarDevice();
	public abstract void sendGUINetworkData(Container container, ICrafting icrafting);
	public abstract void receiveGUINetworkData(int id, int value);

	// Call this when you modify a fluid tank outside of its usual methods
	protected void markForFluidUpdate()
	{
		//
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			updateCellarDevice();
		}
	}

	protected void readTanksFromNBT(NBTTagCompound nbt)
	{
		for (int i = 0; i < tanks.length; i++)
		{
			tanks[i].setFluid(null);
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
		readTanksFromNBT(nbt);
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
		// TANKS
		writeTanksToNBT(nbt);
	}

	protected void readTanksFromStream(ByteBuf stream)
	{
		for (int i = 0; i < tanks.length; i++)
		{
			StreamUtils.readFluidTank(stream, tanks[i]);
		}
	}

	@EventHandler(type=EventHandler.EventType.NETWORK_READ)
	public boolean readFromStream_FluidTanks(ByteBuf stream) throws IOException
	{
		readTanksFromStream(stream);
		return true;
	}

	protected void writeTanksToStream(ByteBuf stream)
	{
		for (int i = 0; i < tanks.length; i++)
		{
			StreamUtils.writeFluidTank(stream, tanks[i]);
		}
	}

	@EventHandler(type=EventHandler.EventType.NETWORK_WRITE)
	public void writeToStream_FluidTanks(ByteBuf stream) throws IOException
	{
		writeTanksToStream(stream);
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
		final int cap = tanks[slot].getCapacity();
		if (cap <= 0) return 0;
		return this.getFluidAmount(slot) * scalar / cap;
	}

	public float getFluidAmountRate(int slot)
	{
		final int cap = tanks[slot].getCapacity();
		if (cap <= 0) return 0;
		return (float)this.getFluidAmount(slot) / (float)cap;
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
		final FluidStack stack = getFluidStack(slot);
		if (stack == null) return null;
		return stack.getFluid();
	}

	public void clearTank(int slot)
	{
		tanks[slot].setFluid(null);

		markForFluidUpdate();
	}
}
