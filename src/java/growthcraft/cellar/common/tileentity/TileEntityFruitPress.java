package growthcraft.cellar.common.tileentity;

import growthcraft.api.cellar.util.FluidUtils;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.common.tileentity.device.FruitPress;
import growthcraft.core.common.inventory.GrcInternalInventory;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class TileEntityFruitPress extends TileEntityCellarDevice
{
	public static class FruitPressDataID
	{
		public static final int TIME = 0;
		public static final int TIME_MAX = 1;
		public static final int TANK_FLUID_ID = 2;
		public static final int TANK_FLUID_AMOUNT = 3;

		private FruitPressDataID() {}
	}

	private static final int[] rawSlotIDs = new int[] {0, 1};
	private static final int[] residueSlotIDs = new int[] {0};
	private FruitPress fruitPress = new FruitPress(this, 0, 0, 1);

	@Override
	protected FluidTank[] createTanks()
	{
		final int maxCap = GrowthCraftCellar.getConfig().fruitPressMaxCap;
		return new FluidTank[] { new CellarTank(maxCap, this) };
	}

	@Override
	protected GrcInternalInventory createInventory()
	{
		return new GrcInternalInventory(this, 2);
	}

	@Override
	public String getDefaultInventoryName()
	{
		return "container.grc.fruitPress";
	}

	public int getPressProgressScaled(int par1)
	{
		return fruitPress.getProgressScaled(par1);
	}

	/************
	 * UPDATE
	 ************/
	@Override
	protected void updateDevice()
	{
		fruitPress.update();
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

	/**
	 * @param nbt - nbt data to load
	 */
	@Override
	protected void readTanksFromNBT(NBTTagCompound nbt)
	{
		if (nbt.hasKey("Tank"))
		{
			getFluidTank(0).readFromNBT(nbt.getCompoundTag("Tank"));
		}
		else
		{
			super.readTanksFromNBT(nbt);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		if (nbt.getInteger("FruitPress_version") > 0)
		{
			fruitPress.readFromNBT(nbt, "fruit_press");
		}
		else
		{
			fruitPress.readFromNBT(nbt);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		fruitPress.writeToNBT(nbt, "fruit_press");
		nbt.setInteger("FruitPress_version", 2);
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
		switch (id)
		{
			case FruitPressDataID.TIME:
				fruitPress.setTime(v);
				break;
			case FruitPressDataID.TIME_MAX:
				fruitPress.setTimeMax(v);
				break;
			case FruitPressDataID.TANK_FLUID_ID:
				final FluidStack result = FluidUtils.replaceFluidStack(v, getFluidStack(0));
				if (result != null) getFluidTank(0).setFluid(result);
				break;
			case FruitPressDataID.TANK_FLUID_AMOUNT:
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
		iCrafting.sendProgressBarUpdate(container, FruitPressDataID.TIME, fruitPress.getTime());
		iCrafting.sendProgressBarUpdate(container, FruitPressDataID.TIME_MAX, fruitPress.getTimeMax());
		final FluidStack fluid = getFluidStack(0);
		iCrafting.sendProgressBarUpdate(container, FruitPressDataID.TANK_FLUID_ID, fluid != null ? fluid.getFluidID() : 0);
		iCrafting.sendProgressBarUpdate(container, FruitPressDataID.TANK_FLUID_AMOUNT, fluid != null ? fluid.amount : 0);
	}

	/************
	 * FLUID
	 ************/
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return false;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return getFluidTank(0).drain(maxDrain, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		if (resource == null || !resource.isFluidEqual(getFluidStack(0)))
		{
			return null;
		}

		return drain(from, resource.amount, doDrain);
	}
}
