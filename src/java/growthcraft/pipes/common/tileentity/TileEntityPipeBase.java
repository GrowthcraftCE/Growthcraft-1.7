package growthcraft.pipes.common.tileentity;

import growthcraft.api.core.GrcColour;
import growthcraft.pipes.common.block.IPipeBlock;
import growthcraft.pipes.util.PipeFlag;
import growthcraft.pipes.util.PipeType;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityPipeBase extends TileEntity implements IFluidHandler, IPipeTile, IColourableTile
{
	public static enum UsageState
	{
		UNUSABLE,
		USABLE;
	}

	public static enum TransferState
	{
		IDLE,
		INPUT,
		OUTPUT;
	}

	public static enum EndType
	{
		NONE,
		PIPE,
		BUS;
	}

	public static class PipeFluidTank extends FluidTank
	{
		public PipeFluidTank(int capacity)
		{
			super(capacity);
		}

		public boolean isFull()
		{
			return getFluidAmount() >= getCapacity();
		}

		public boolean hasFluid()
		{
			return getFluidAmount() > 0;
		}

		public boolean isEmpty()
		{
			return getFluidAmount() == 0;
		}

		public int getFreeSpace()
		{
			return getCapacity() - getFluidAmount();
		}

		public int getDrainAmount()
		{
			return Math.min(getFluidAmount(), 10);
		}

		public int getFillAmount()
		{
			return Math.min(getFreeSpace(), 10);
		}
	}

	public static class PipeSection
	{
		public TransferState transferState = TransferState.IDLE;
		public UsageState usageState = UsageState.UNUSABLE;
		public EndType endType = EndType.NONE;
		public int feedFlag;
	}

	public static class PipeBuffer
	{
		public TileEntity te;

		public void clear()
		{
			this.te = null;
		}
	}

	public PipeSection[] pipeSections = new PipeSection[7];
	public PipeBuffer[] pipeBuffers = new PipeBuffer[ForgeDirection.VALID_DIRECTIONS.length];
	private GrcColour colour = GrcColour.Transparent;
	private PipeFluidTank fluidTank = new PipeFluidTank(FluidContainerRegistry.BUCKET_VOLUME / 4);
	private int pipeRenderState = PipeFlag.PIPE_CORE;
	private boolean dirty = true;
	private boolean needsUpdate = true;
	private PipeType pipeType = PipeType.UNKNOWN;

	public TileEntityPipeBase()
	{
		for (int i = 0; i < pipeSections.length; ++i)
		{
			pipeSections[i] = new PipeSection();
		}
		for (int i = 0; i < pipeBuffers.length; ++i)
		{
			pipeBuffers[i] = new PipeBuffer();
		}
	}

	private void markAsDirty()
	{
		this.dirty = true;
	}

	@Override
	public void setColour(GrcColour kolour)
	{
		this.colour = kolour;
		markAsDirty();
	}

	@Override
	public GrcColour getColour()
	{
		return colour;
	}

	public void onNeighbourChanged()
	{
		markAsDirty();
	}

	@Override
	public void invalidate()
	{
		super.invalidate();
		markAsDirty();
	}

	public boolean isVacuumPipe()
	{
		return pipeType == PipeType.VACUUM;
	}

	public int getPipeCoreState()
	{
		if (isVacuumPipe())
		{
			return PipeFlag.PIPE_VACUUM_CORE;
		}
		return PipeFlag.PIPE_CORE;
	}

	public void refreshCache()
	{
		needsUpdate = true;
		final Block block = worldObj.getBlock(xCoord, yCoord, zCoord);
		if (block instanceof IPipeBlock)
		{
			pipeType = ((IPipeBlock)block).getPipeType();
		}
		else
		{
			invalidate();
			return;
		}
		pipeRenderState = getPipeCoreState();
		for (int i = 0; i < pipeBuffers.length; ++i)
		{
			final ForgeDirection dir = ForgeDirection.getOrientation(i);
			final TileEntity te = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
			pipeBuffers[i].te = te;
			pipeSections[i].usageState = UsageState.UNUSABLE;
			pipeSections[i].endType = EndType.NONE;
			boolean valid = true;
			// if the other pipe is a ColourableTile
			if (te instanceof IColourableTile)
			{
				final IColourableTile colouredTile = (IColourableTile)te;
				valid = colour.matches(colouredTile.getColour());
			}

			if (valid)
			{
				if (te instanceof IPipeTile)
				{
					pipeRenderState |= 1 << i;
					pipeSections[i].usageState = UsageState.USABLE;
					pipeSections[i].endType = EndType.PIPE;
				}
				else if (te instanceof IFluidHandler)
				{
					pipeRenderState |= 1 << (6 + i);
					pipeSections[i].usageState = UsageState.USABLE;
					pipeSections[i].endType = EndType.BUS;
				}
			}
		}
	}

	private void transferFluid(IFluidHandler dest, PipeFluidTank src, ForgeDirection dir)
	{
		final FluidStack drained = fluidTank.drain(fluidTank.getDrainAmount(), true);
		if (drained != null)
		{
			final int filled = dest.fill(dir, drained, true);
			final int diff = drained.amount - filled;
			if (diff > 0)
			{
				src.fill(new FluidStack(drained.getFluid(), diff), true);
			}
		}
	}

	private void transferFluid(PipeFluidTank dest, IFluidHandler src, ForgeDirection dir)
	{
		final FluidStack drained = src.drain(dir, dest.getFillAmount(), true);
		if (drained != null)
		{
			final int filled = dest.fill(drained, true);
			final int diff = drained.amount - filled;
			if (diff > 0)
			{
				src.fill(dir, new FluidStack(drained.getFluid(), diff), true);
			}
		}
	}

	@Override
	public void updateEntity()
	{
		if (!worldObj.isRemote)
		{
			if (dirty)
			{
				dirty = false;
				refreshCache();
				worldObj.notifyBlockChange(this.xCoord, this.yCoord, this.zCoord, worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
			}
		}

		final PipeSection coreSection = pipeSections[6];
		coreSection.feedFlag = 0;
		for (int i = 0; i < pipeBuffers.length; ++i)
		{
			final ForgeDirection dir = ForgeDirection.getOrientation(i);
			final ForgeDirection oppdir = dir.getOpposite();
			final PipeSection section = pipeSections[i];
			if (section.usageState == UsageState.USABLE)
			{
				final TileEntity te = pipeBuffers[i].te;
				if (section.transferState == TransferState.OUTPUT)
				{
					if (isVacuumPipe())
					{
						if (te instanceof IPipeTile)
						{
							if (fluidTank.getFluidAmount() > 0)
							{
								final IFluidHandler fluidHandler = (IFluidHandler)te;
								transferFluid(fluidHandler, fluidTank, oppdir);
							}
						}
					}
					else
					{
						if (te instanceof IFluidHandler)
						{
							if (fluidTank.getFluidAmount() > 0)
							{
								final IFluidHandler fluidHandler = (IFluidHandler)te;
								transferFluid(fluidHandler, fluidTank, oppdir);
							}
						}
					}
					section.transferState = TransferState.IDLE;
				}
				else
				{
					if (section.transferState == TransferState.INPUT)
					{
						coreSection.feedFlag = 1 << i;
						section.transferState = TransferState.IDLE;
					}
					if (isVacuumPipe())
					{
						if (te instanceof IFluidHandler && !(te instanceof IPipeTile))
						{
							final IFluidHandler fluidHandler = (IFluidHandler)te;
							if (!fluidTank.isFull())
							{
								transferFluid(fluidTank, fluidHandler, oppdir);
								section.transferState = TransferState.INPUT;
							}
						}
					}
				}
			}
		}

		if (fluidTank.getFluidAmount() > 0)
		{
			int dist = 0;
			for (int i = 0; i < pipeBuffers.length; ++i)
			{
				final int flag = 1 << i;
				if ((coreSection.feedFlag & flag) == flag) continue;
				if (pipeSections[i].usageState == UsageState.USABLE)
					dist += 1;
			}

			if (dist != 0)
			{
				for (int i = 0; i < pipeBuffers.length; ++i)
				{
					final int flag = 1 << i;
					if ((coreSection.feedFlag & flag) == flag) continue;
					if (pipeSections[i].usageState == UsageState.USABLE)
					{
						final PipeSection section = pipeSections[i];
						section.transferState = TransferState.OUTPUT;
					}
				}
			}
		}

		if (needsUpdate)
		{
			needsUpdate = false;
			this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		}
	}

	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return true;
	}

	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		final int amount = fluidTank.fill(resource, doFill);
		if (doFill)
		{
			if (from != ForgeDirection.UNKNOWN)
			{
				pipeSections[from.ordinal()].transferState = TransferState.INPUT;
			}
		}
		return amount;
	}

	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return true;
	}

	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		final FluidStack stack = fluidTank.drain(maxDrain, doDrain);
		if (doDrain)
		{
			if (from != ForgeDirection.UNKNOWN)
			{
				pipeSections[from.ordinal()].transferState = TransferState.OUTPUT;
			}
		}
		return stack;
	}

	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		if (resource == null || !resource.isFluidEqual(fluidTank.getFluid()))
		{
			return null;
		}
		return drain(from, resource.amount, doDrain);
	}

	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return new FluidTankInfo[]{ fluidTank.getInfo() };
	}

	public int getPipeRenderState()
	{
		return pipeRenderState;
	}

	public void readTankNBT(NBTTagCompound tag)
	{
		if (tag.hasKey("tank"))
		{
			fluidTank.readFromNBT(tag.getCompoundTag("tank"));
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		this.colour = GrcColour.toColour(tag.getInteger("colour"));
		this.pipeRenderState = tag.getInteger("pipe_render_state");
		readTankNBT(tag);
	}

	public void writeTankNBT(NBTTagCompound tag)
	{
		final NBTTagCompound tankTag = new NBTTagCompound();
		fluidTank.writeToNBT(tankTag);
		tag.setTag("tank", tankTag);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tag.setInteger("colour", colour.ordinal());
		tag.setInteger("pipe_render_state", getPipeRenderState());
		writeTankNBT(tag);
	}

	/************
	 * PACKETS
	 ************/
	@Override
	public Packet getDescriptionPacket()
	{
		final NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
	{
		this.readFromNBT(packet.func_148857_g());
		this.worldObj.func_147479_m(this.xCoord, this.yCoord, this.zCoord);
	}
}
