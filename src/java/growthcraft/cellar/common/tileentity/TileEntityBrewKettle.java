package growthcraft.cellar.common.tileentity;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

import growthcraft.cellar.common.fluids.CellarTank;
import growthcraft.cellar.common.inventory.ContainerBrewKettle;
import growthcraft.cellar.common.tileentity.device.BrewKettle;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.common.inventory.GrcInternalInventory;
import growthcraft.core.common.tileentity.event.TileEventHandler;
import growthcraft.core.common.tileentity.feature.ITileHeatedDevice;
import growthcraft.core.common.tileentity.feature.ITileProgressiveDevice;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class TileEntityBrewKettle extends TileEntityCellarDevice implements ITileHeatedDevice, ITileProgressiveDevice
{
	public static enum BrewKettleDataID
	{
		TIME,
		TIME_MAX,
		HEAT_AMOUNT,
		UNKNOWN;

		public static final BrewKettleDataID[] VALUES = new BrewKettleDataID[]
		{
			TIME,
			TIME_MAX,
			HEAT_AMOUNT
		};

		public static BrewKettleDataID getByOrdinal(int ord)
		{
			if (ord >= 0 && ord < VALUES.length) return VALUES[ord];
			return UNKNOWN;
		}
	}

	private static final int[] rawSlotIDs = new int[] {0, 1};
	private static final int[] residueSlotIDs = new int[] {0};

	private BrewKettle brewKettle = new BrewKettle(this, 0, 1, 0, 1);

	@Override
	protected FluidTank[] createTanks()
	{
		final int maxCap = GrowthCraftCellar.getConfig().brewKettleMaxCap;
		return new FluidTank[] {
			new CellarTank(maxCap, this),
			new CellarTank(maxCap, this)
		};
	}

	@Override
	protected GrcInternalInventory createInventory()
	{
		return new GrcInternalInventory(this, 2);
	}

	@Override
	protected void markFluidDirty()
	{
		// Brew Kettles need to update their rendering state when a fluid
		// changes, most of the other cellar blocks are unaffected by this
		markForUpdate();
	}

	@Override
	public String getDefaultInventoryName()
	{
		return "container.grc.brewKettle";
	}

	@Override
	public String getGuiID()
	{
		return "grccellar:brew_kettle";
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerBrewKettle(playerInventory, this);
	}

	@Override
	public float getDeviceProgress()
	{
		return brewKettle.getProgress();
	}

	@Override
	public int getDeviceProgressScaled(int scale)
	{
		return brewKettle.getProgressScaled(scale);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (!worldObj.isRemote)
		{
			brewKettle.update();
		}
	}

	@Override
	public int getHeatScaled(int range)
	{
		return (int)(MathHelper.clamp_float(brewKettle.getHeatMultiplier(), 0.0f, 1.0f) * range);
	}

	@Override
	public boolean isHeated()
	{
		return brewKettle.isHeated();
	}

	@Override
	public float getHeatMultiplier()
	{
		return brewKettle.getHeatMultiplier();
	}

	public boolean canBrew()
	{
		return brewKettle.canBrew();
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		// 0 = raw
		// 1 = residue
		return side == 0 ? rawSlotIDs : residueSlotIDs;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, int side)
	{
		return side != 0 || index == 1;
	}

	@TileEventHandler(event=TileEventHandler.EventType.NBT_READ)
	public void readFromNBT_BrewKettle(NBTTagCompound nbt)
	{
		if (nbt.hasKey("time"))
		{
			// Pre 2.5
			brewKettle.setTime(nbt.getShort("time"));
			brewKettle.setGrain(nbt.getFloat("grain"));
		}
		else
		{
			brewKettle.readFromNBT(nbt, "brew_kettle");
		}
	}

	@TileEventHandler(event=TileEventHandler.EventType.NBT_WRITE)
	public void writeToNBT_BrewKettle(NBTTagCompound nbt)
	{
		brewKettle.writeToNBT(nbt, "brew_kettle");
	}

	@TileEventHandler(event=TileEventHandler.EventType.NETWORK_READ)
	public boolean readFromStream_BrewKettle(ByteBuf stream) throws IOException
	{
		brewKettle.readFromStream(stream);
		return false;
	}

	@TileEventHandler(event=TileEventHandler.EventType.NETWORK_WRITE)
	public boolean writeToStream_BrewKettle(ByteBuf stream) throws IOException
	{
		brewKettle.writeToStream(stream);
		return false;
	}

	/************
	 * PACKETS
	 ************/

	/**
	 * @param id - data id
	 * @param v - value
	 */
	@Override
	public void receiveGUINetworkData(int id, int v)
	{
		super.receiveGUINetworkData(id, v);
		final BrewKettleDataID dataId = BrewKettleDataID.getByOrdinal(id);
		switch (dataId)
		{
			case TIME:
				brewKettle.setTime(v);
				break;
			case TIME_MAX:
				brewKettle.setTimeMax(v);
				break;
			case HEAT_AMOUNT:
				brewKettle.setHeatMultiplier((float)v / (float)0x7FFF);
				break;
			default:
				break;
		}
	}

	@Override
	public void sendGUINetworkData(Container container, ICrafting iCrafting)
	{
		super.sendGUINetworkData(container, iCrafting);
		iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.TIME.ordinal(), (int)brewKettle.getTime());
		iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.TIME_MAX.ordinal(), (int)brewKettle.getTimeMax());
		iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.HEAT_AMOUNT.ordinal(), (int)(brewKettle.getHeatMultiplier() * 0x7FFF));
	}

	@Override
	protected int doFill(ForgeDirection from, FluidStack resource, boolean shouldFill)
	{
		return fillFluidTank(0, resource, shouldFill);
	}

	@Override
	protected FluidStack doDrain(ForgeDirection from, int maxDrain, boolean shouldDrain)
	{
		return drainFluidTank(1, maxDrain, shouldDrain);
	}

	@Override
	protected FluidStack doDrain(ForgeDirection from, FluidStack stack, boolean shouldDrain)
	{
		if (stack == null || !stack.isFluidEqual(getFluidStack(1)))
		{
			return null;
		}
		return doDrain(from, stack.amount, shouldDrain);
	}

	public void switchTanks()
	{
		FluidStack f0 = null;
		FluidStack f1 = null;
		if (this.getFluidStack(0) != null)
		{
			f0 = this.getFluidStack(0).copy();
		}
		if (this.getFluidStack(1) != null)
		{
			f1 = this.getFluidStack(1).copy();
		}
		this.clearTank(0);
		this.clearTank(1);
		this.getFluidTank(0).fill(f1, true);
		this.getFluidTank(1).fill(f0, true);
		markForUpdate();
	}
}
