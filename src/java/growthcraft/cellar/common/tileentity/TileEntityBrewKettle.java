package growthcraft.cellar.common.tileentity;

import java.util.Arrays;
import java.util.List;
import java.io.IOException;

import io.netty.buffer.ByteBuf;

import growthcraft.api.core.util.FluidUtils;
import growthcraft.cellar.common.fluids.CellarTank;
import growthcraft.cellar.common.tileentity.device.BrewKettle;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.common.inventory.GrcInternalInventory;
import growthcraft.core.common.tileentity.event.EventHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class TileEntityBrewKettle extends TileEntityCellarDevice
{
	public static enum BrewKettleDataID
	{
		TIME,
		TIME_MAX,
		TANK1_FLUID_ID,
		TANK1_FLUID_AMOUNT,
		TANK2_FLUID_ID,
		TANK2_FLUID_AMOUNT;

		public static final List<BrewKettleDataID> VALUES = Arrays.asList(values());
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

	protected void markForFluidUpdate()
	{
		// Brew Kettles need to update their rendering state when a fluid
		// changes, most of the other cellar blocks are unaffected by this
		markForBlockUpdate();
	}

	@Override
	public String getDefaultInventoryName()
	{
		return "container.grc.brewKettle";
	}

	/************
	 * UPDATE
	 ************/
	@Override
	protected void updateDevice()
	{
		brewKettle.update();
	}

	@SideOnly(Side.CLIENT)
	public int getBrewProgressScaled(int range)
	{
		return (int)(brewKettle.getProgress() * range);
	}

	@SideOnly(Side.CLIENT)
	public int getHeatScaled(int range)
	{
		return (int)(MathHelper.clamp_float(brewKettle.getHeatMultiplier(), 0.0f, 1.0f) * range);
	}

	public boolean hasHeat()
	{
		return brewKettle.hasHeat();
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

	/************
	 * NBT
	 ************/
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

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

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		brewKettle.writeToNBT(nbt, "brew_kettle");
	}

	@EventHandler(type=EventHandler.EventType.NETWORK_READ)
	public boolean readFromStream_BrewKettle(ByteBuf stream) throws IOException
	{
		brewKettle.readFromStream(stream);
		return false;
	}

	@EventHandler(type=EventHandler.EventType.NETWORK_WRITE)
	public void writeToStream_BrewKettle(ByteBuf stream) throws IOException
	{
		brewKettle.writeToStream(stream);
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
		final BrewKettleDataID dataId = BrewKettleDataID.VALUES.get(id);
		switch (dataId)
		{
			case TIME:
				brewKettle.setTime(v);
				break;
			case TIME_MAX:
				brewKettle.setTimeMax(v);
				break;
			case TANK1_FLUID_ID:
			{
				final FluidStack result = FluidUtils.replaceFluidStack(v, getFluidStack(0));
				if (result != null) getFluidTank(0).setFluid(result);
			}	break;
			case TANK1_FLUID_AMOUNT:
				getFluidTank(0).setFluid(FluidUtils.updateFluidStackAmount(getFluidStack(0), v));
				break;
			case TANK2_FLUID_ID:
			{
				final FluidStack result = FluidUtils.replaceFluidStack(v, getFluidStack(1));
				if (result != null) getFluidTank(1).setFluid(result);
			}	break;
			case TANK2_FLUID_AMOUNT:
				getFluidTank(1).setFluid(FluidUtils.updateFluidStackAmount(getFluidStack(1), v));
				break;
			default:
				break;
		}
	}

	@Override
	public void sendGUINetworkData(Container container, ICrafting iCrafting)
	{
		iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.TIME.ordinal(), (int)brewKettle.getTime());
		iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.TIME_MAX.ordinal(), (int)brewKettle.getTimeMax());
		FluidStack fluid = getFluidStack(0);
		iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.TANK1_FLUID_ID.ordinal(), fluid != null ? fluid.getFluidID() : 0);
		iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.TANK1_FLUID_AMOUNT.ordinal(), fluid != null ? fluid.amount : 0);
		fluid = getFluidStack(1);
		iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.TANK2_FLUID_ID.ordinal(), fluid != null ? fluid.getFluidID() : 0);
		iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.TANK2_FLUID_AMOUNT.ordinal(), fluid != null ? fluid.amount : 0);
	}

	@Override
	protected int doFill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		return fillFluidTank(0, resource, doFill);
	}

	@Override
	protected FluidStack doDrain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return drainFluidTank(1, maxDrain, doDrain);
	}

	@Override
	protected FluidStack doDrain(ForgeDirection from, FluidStack stack, boolean doDrain)
	{
		if (stack == null || !stack.isFluidEqual(getFluidStack(1)))
		{
			return null;
		}
		return doDrain(from, stack.amount, doDrain);
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

		markForBlockUpdate();
	}
}
