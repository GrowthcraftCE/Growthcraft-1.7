package growthcraft.cellar.common.tileentity;

import java.io.IOException;

import growthcraft.api.cellar.util.FluidUtils;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.common.inventory.GrcInternalInventory;
import growthcraft.core.common.tileentity.event.EventHandler;
import growthcraft.cellar.common.tileentity.device.YeastGenerator;

import io.netty.buffer.ByteBuf;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityFermentJar extends TileEntityCellarDevice
{
	public static enum FermentJarDataId
	{
		TIME,
		TANK_FLUID_ID,
		TANK_FLUID_AMOUNT,
		UNKNOWN;

		public static final FermentJarDataId[] VALID = new FermentJarDataId[] { TIME, TANK_FLUID_ID, TANK_FLUID_AMOUNT };

		public static FermentJarDataId fromInt(int i)
		{
			if (i >= 0 && i <= VALID.length) return VALID[i];
			return UNKNOWN;
		}
	}

	private static final int[] accessibleSlots = new int[] { 0 };
	private YeastGenerator yeastGen;

	public TileEntityFermentJar()
	{
		super();
		this.yeastGen = new YeastGenerator(this, 0, 0);
		this.yeastGen.setTimeMax(GrowthCraftCellar.getConfig().fermentJarTimeMax);
		this.yeastGen.setConsumption(GrowthCraftCellar.getConfig().fermentJarConsumption);
	}

	@Override
	protected CellarTank[] createTanks()
	{
		final int maxTankCap = GrowthCraftCellar.getConfig().fermentJarMaxCap;
		return new CellarTank[] { new CellarTank(maxTankCap, this) };
	}

	@Override
	protected GrcInternalInventory createInventory()
	{
		return new GrcInternalInventory(this, 1);
	}

	@Override
	public String getDefaultInventoryName()
	{
		return "container.grc.fermentJar";
	}

	protected void markForFluidUpdate()
	{
		// Ferment Jars need to update their rendering state when a fluid
		// changes, most of the other cellar blocks are unaffected by this
		markForBlockUpdate();
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return accessibleSlots;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, int side)
	{
		return index == 0;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, int side)
	{
		return false;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		final int f = getFluidTank(0).fill(resource, doFill);
		if (f > 0 && doFill)
		{
			markForBlockUpdate();
		}
		return f;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		final FluidStack d = getFluidTank(0).drain(maxDrain, doDrain);
		if (d != null && doDrain)
		{
			markForBlockUpdate();
		}
		return d;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		if (resource == null || !resource.isFluidEqual(getFluidTank(0).getFluid()))
		{
			return null;
		}

		return drain(from, resource.amount, doDrain);
	}

	@Override
	public void updateCellarDevice()
	{
		yeastGen.update();
	}

	@Override
	public void receiveGUINetworkData(int id, int v)
	{
		switch (FermentJarDataId.fromInt(id))
		{
			case TIME:
				yeastGen.setTime(v);
				break;
			case TANK_FLUID_ID:
				final FluidStack result = FluidUtils.replaceFluidStack(v, getFluidStack(0));
				if (result != null) getFluidTank(0).setFluid(result);
				break;
			case TANK_FLUID_AMOUNT:
				getFluidTank(0).setFluid(FluidUtils.updateFluidStackAmount(getFluidStack(0), v));
				break;
			default:
				// should warn about invalid Data ID
				break;
		}
	}

	@Override
	public void sendGUINetworkData(Container container, ICrafting iCrafting)
	{
		iCrafting.sendProgressBarUpdate(container, FermentJarDataId.TIME.ordinal(), yeastGen.getTime());
		final FluidStack fluid = getFluidStack(0);
		iCrafting.sendProgressBarUpdate(container, FermentJarDataId.TANK_FLUID_ID.ordinal(), fluid != null ? fluid.getFluidID() : 0);
		iCrafting.sendProgressBarUpdate(container, FermentJarDataId.TANK_FLUID_AMOUNT.ordinal(), fluid != null ? fluid.amount : 0);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		yeastGen.readFromNBT(nbt, "yeastgen");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		yeastGen.writeToNBT(nbt, "yeastgen");
	}

	@EventHandler(type=EventHandler.EventType.NETWORK_READ)
	public boolean readFromStream_YeastGen(ByteBuf stream) throws IOException
	{
		yeastGen.readFromStream(stream);
		return false;
	}

	@EventHandler(type=EventHandler.EventType.NETWORK_WRITE)
	public void writeToStream_YeastGen(ByteBuf stream) throws IOException
	{
		yeastGen.writeToStream(stream);
	}

	public int getFermentProgressScaled(int scale)
	{
		return yeastGen.getProgressScaled(scale);
	}
}
