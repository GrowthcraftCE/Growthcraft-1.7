package growthcraft.cellar.common.tileentity;

import growthcraft.api.core.fluids.FluidUtils;
import growthcraft.cellar.common.fluids.CellarTank;
import growthcraft.cellar.common.tileentity.device.FruitPress;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.common.inventory.GrcInternalInventory;
import growthcraft.core.common.tileentity.ITileProgressiveDevice;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class TileEntityFruitPress extends TileEntityCellarDevice implements ITileProgressiveDevice
{
	public static class FruitPressDataID
	{
		public static final int TIME = 0;
		public static final int TIME_MAX = 1;
		public static final int TANK_FLUID_ID = 2;
		public static final int TANK_FLUID_AMOUNT = 3;

		private FruitPressDataID() {}
	}

	private static final int[] allSlotIds = new int[] {0, 1};
	private static final int[] residueSlotIds = new int[] {0};
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

	@Override
	public float getDeviceProgress()
	{
		return fruitPress.getProgress();
	}

	@Override
	public int getDeviceProgressScaled(int scale)
	{
		return fruitPress.getProgressScaled(scale);
	}

	@Override
	protected void updateDevice()
	{
		fruitPress.update();
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		// 0 = raw item
		// 1 = residue
		return allSlotIds;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, int side)
	{
		// allow the insertion of a raw item from ANY side
		if (index == 0) return true;
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, int side)
	{
		// if this is the raw item slow
		if (index == 0)
		{
			// only allow extraction from the top or bottom
			if (side == 0 || side == 1) return true;
		}
		// else this is the residue slot
		else
		{
			// if its the side, the item can be safely extracted
			if (side > 1) return true;
		}
		return false;
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
		super.receiveGUINetworkData(id, v);
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
		super.sendGUINetworkData(container, iCrafting);
		iCrafting.sendProgressBarUpdate(container, FruitPressDataID.TIME, fruitPress.getTime());
		iCrafting.sendProgressBarUpdate(container, FruitPressDataID.TIME_MAX, fruitPress.getTimeMax());
		final FluidStack fluid = getFluidStack(0);
		iCrafting.sendProgressBarUpdate(container, FruitPressDataID.TANK_FLUID_ID, fluid != null ? fluid.getFluidID() : 0);
		iCrafting.sendProgressBarUpdate(container, FruitPressDataID.TANK_FLUID_AMOUNT, fluid != null ? fluid.amount : 0);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return false;
	}

	@Override
	protected int doFill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		return 0;
	}

	@Override
	protected FluidStack doDrain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return drainFluidTank(0, maxDrain, doDrain);
	}

	@Override
	protected FluidStack doDrain(ForgeDirection from, FluidStack stack, boolean doDrain)
	{
		if (stack == null || !stack.isFluidEqual(getFluidStack(0)))
		{
			return null;
		}
		return doDrain(from, stack.amount, doDrain);
	}
}
