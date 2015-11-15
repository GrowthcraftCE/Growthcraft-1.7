package growthcraft.cellar.common.tileentity;

import growthcraft.api.cellar.util.FluidUtils;
import growthcraft.core.common.inventory.GrcInternalInventory;

import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
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
	private static final int maxTankCap = 1000;

	protected int time;

	@Override
	protected CellarTank[] createTanks()
	{
		this.tankCaps = new int[] { maxTankCap };
		return new CellarTank[] { new CellarTank(tankCaps[0], this) };
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
		final int f = tanks[0].fill(resource, doFill);
		if (f > 0)
		{
			markForBlockUpdate();
		}
		return f;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		if (resource == null || !resource.isFluidEqual(tanks[0].getFluid()))
		{
			return null;
		}

		return drain(from, resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		final FluidStack d = tanks[0].drain(maxDrain, doDrain);
		if (d != null)
		{
			markForBlockUpdate();
		}
		return d;
	}

	@Override
	public void updateCellarDevice()
	{
		final FluidStack stack = getFluidStack(0);
		if (stack != null)
		{

		}
	}

	@Override
	public void receiveGUINetworkData(int id, int v)
	{
		switch (FermentJarDataId.fromInt(id))
		{
			case TIME:
				this.time = v;
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
		iCrafting.sendProgressBarUpdate(container, FermentJarDataId.TIME.ordinal(), time);
		final FluidStack fluid = getFluidStack(0);
		iCrafting.sendProgressBarUpdate(container, FermentJarDataId.TANK_FLUID_ID.ordinal(), fluid != null ? fluid.getFluidID() : 0);
		iCrafting.sendProgressBarUpdate(container, FermentJarDataId.TANK_FLUID_AMOUNT.ordinal(), fluid != null ? fluid.amount : 0);
	}
}
