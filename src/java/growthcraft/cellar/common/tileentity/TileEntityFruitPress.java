package growthcraft.cellar.common.tileentity;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.common.Residue;
import growthcraft.api.cellar.pressing.PressingRegistry;
import growthcraft.api.cellar.util.FluidUtils;
import growthcraft.cellar.common.inventory.ContainerFruitPress;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.util.ItemUtils;

import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityFruitPress extends TileEntityCellarMachine
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

	public TileEntityFruitPress()
	{
		super();
		this.tankCaps = new int[] {maxCap};
		this.invSlots = new ItemStack[2];
		this.tanks = new CellarTank[] { new CellarTank(tankCaps[0], this) };
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
		final PressingRegistry pressing = CellarRegistry.instance().pressing();
		final int m = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord + 1, this.zCoord);

		if (m == 0 || m == 1) return false;
		if (invSlots[0] == null) return false;
		if (getFluidAmount(0) == this.maxCap) return false;
		if (!pressing.isPressingRecipe(this.invSlots[0])) return false;

		if (isFluidTankEmpty(0)) return true;

		final FluidStack stack = pressing.getPressingFluidStack(this.invSlots[0]);
		return stack.isFluidEqual(getFluidStack(0));
	}

	private void producePomace()
	{
		final Residue residue = CellarRegistry.instance().pressing().getPressingResidue(this.invSlots[0]);
		this.pomace = this.pomace + residue.pomaceRate;
		if (this.pomace >= 1.0F)
		{
			this.pomace = this.pomace - 1.0F;
			final ItemStack residueResult = ItemUtils.mergeStacks(this.invSlots[1], residue.residueItem);
			if (residueResult != null) invSlots[1] = residueResult;
		}
	}

	public void pressItem()
	{
		final PressingRegistry pressing = CellarRegistry.instance().pressing();
		producePomace();
		final FluidStack fluidstack = pressing.getPressingFluidStack(this.invSlots[0]);
		fluidstack.amount  = pressing.getPressingAmount(this.invSlots[0]);
		tanks[0].fill(fluidstack, true);

		invSlots[0] = ItemUtils.consumeStack(this.invSlots[0]);
	}

	public int getPressingTime()
	{
		return CellarRegistry.instance().pressing().getPressingTime(this.invSlots[0]);
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
	public void updateMachine()
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
			this.time = 0;
		}

		update = true;
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
	public void getGUINetworkData(int id, int v)
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

	public void sendGUINetworkData(ContainerFruitPress container, ICrafting iCrafting)
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
