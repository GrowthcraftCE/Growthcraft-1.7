package growthcraft.cellar.common.tileentity;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.common.Residue;
import growthcraft.api.cellar.pressing.PressingRegistry;
import growthcraft.api.cellar.util.FluidUtils;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.util.ItemUtils;
import growthcraft.core.common.inventory.GrcInternalInventory;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityFruitPress extends TileEntityCellarDevice
{
	public static class FruitPressDataID
	{
		public static final int TIME = 0;
		public static final int TANK_FLUID_ID = 1;
		public static final int TANK_FLUID_AMOUNT = 2;

		private FruitPressDataID() {}
	}

	private static final int[] rawSlotIDs = new int[] {0, 1};
	private static final int[] residueSlotIDs = new int[] {0};

	protected float pomace;
	protected int time;

	private final int maxCap = GrowthCraftCellar.getConfig().fruitPressMaxCap;

	@Override
	protected CellarTank[] createTanks()
	{
		this.tankCaps = new int[] {maxCap};
		return new CellarTank[] { new CellarTank(tankCaps[0], this) };
	}

	@Override
	protected GrcInternalInventory createInventory()
	{
		return new GrcInternalInventory(this, 2);
	}

	protected boolean resetTime()
	{
		if (this.time != 0)
		{
			this.time = 0;
			return true;
		}
		return false;
	}

	@Override
	public String getDefaultInventoryName()
	{
		return "container.grc.fruitPress";
	}

	private void debugMsg()
	{
		if (this.worldObj.isRemote)
		{
			System.out.println("CLIENT: " + getFluidAmount(0));
		}
		if (!this.worldObj.isRemote)
		{
			System.out.println("SERVER: " + getFluidAmount(0));
		}
	}

	private boolean canPress()
	{
		final ItemStack primarySlotItem = getStackInSlot(0);

		if (primarySlotItem == null) return false;

		final int m = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord + 1, this.zCoord);
		if (m == 0 || m == 1) return false;

		if (getFluidAmount(0) == this.maxCap) return false;

		final PressingRegistry pressing = CellarRegistry.instance().pressing();
		if (!pressing.isPressingRecipe(primarySlotItem)) return false;

		if (isFluidTankEmpty(0)) return true;

		final FluidStack stack = pressing.getPressingFluidStack(primarySlotItem);
		return stack.isFluidEqual(getFluidStack(0));
	}

	private void producePomace()
	{
		final Residue residue = CellarRegistry.instance().pressing().getPressingResidue(getStackInSlot(0));
		this.pomace = this.pomace + residue.pomaceRate;
		if (this.pomace >= 1.0F)
		{
			this.pomace = this.pomace - 1.0F;
			final ItemStack residueResult = ItemUtils.mergeStacks(getStackInSlot(1), residue.residueItem);
			if (residueResult != null) setInventorySlotContents(1, residueResult);
		}
	}

	public void pressItem()
	{
		final PressingRegistry pressing = CellarRegistry.instance().pressing();
		final ItemStack pressingItem = getStackInSlot(0);
		producePomace();
		final FluidStack fluidstack = pressing.getPressingFluidStack(pressingItem);
		fluidstack.amount  = pressing.getPressingAmount(pressingItem);
		tanks[0].fill(fluidstack, true);

		decrStackSize(0, 1);
	}

	public int getPressingTime()
	{
		return CellarRegistry.instance().pressing().getPressingTime(getStackInSlot(0));
	}

	public int getPressProgressScaled(int par1)
	{
		if (this.canPress())
		{
			return this.time * par1 / getPressingTime();
		}

		return 0;
	}

	/************
	 * UPDATE
	 ************/
	@Override
	public void updateCellarDevice()
	{
		if (this.canPress())
		{
			++this.time;

			if (this.time >= getPressingTime())
			{
				this.time = 0;
				this.pressItem();
			}
		}
		else
		{
			if (resetTime())
			{
				markForInventoryUpdate();
			}
		}

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
			this.tanks[0] = new CellarTank(this.maxCap, this);
			this.tanks[0].readFromNBT(nbt.getCompoundTag("Tank"));
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
		this.time = nbt.getShort("time");
		this.pomace = nbt.getFloat("pomace");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setShort("time", (short)this.time);
		nbt.setFloat("pomace", this.pomace);
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
				time = v;
				break;
			case FruitPressDataID.TANK_FLUID_ID:
				final FluidStack result = FluidUtils.replaceFluidStack(v, tanks[0].getFluid());
				if (result != null) tanks[0].setFluid(result);
				break;
			case FruitPressDataID.TANK_FLUID_AMOUNT:
				tanks[0].setFluid(FluidUtils.updateFluidStackAmount(tanks[0].getFluid(), v));
				break;
			default:
				// should warn about invalid Data ID
				break;
		}
	}

	@Override
	public void sendGUINetworkData(Container container, ICrafting iCrafting)
	{
		iCrafting.sendProgressBarUpdate(container, FruitPressDataID.TIME, time);
		final FluidStack fluid = tanks[0].getFluid();
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
		return tanks[0].drain(maxDrain, doDrain);
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
}
