package growthcraft.cellar.common.tileentity;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.util.FluidUtils;
import growthcraft.cellar.common.inventory.ContainerFermentBarrel;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.util.ItemUtils;

import net.minecraft.init.Items;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityFermentBarrel extends TileEntityCellarMachine
{
	public static class FermentBarrelDataID
	{
		public static final int TIME = 0;
		public static final int TANK_FLUID_ID = 1;
		public static final int TANK_FLUID_AMOUNT = 2;

		private FermentBarrelDataID() {}
	}

	// Constants
	private static final int[] accessableSlotIds = new int[] {0};

	// Other Vars.
	protected int time;
	private int maxCap = GrowthCraftCellar.getConfig().fermentBarrelMaxCap;
	private int timemax = GrowthCraftCellar.getConfig().fermentSpeed;

	public TileEntityFermentBarrel()
	{
		super();
		this.tankCaps = new int[] {maxCap};
		this.invSlots = new ItemStack[1];
		this.tanks = new CellarTank[] { new CellarTank(tankCaps[0], this) };
	}

	@Override
	public String getDefaultInventoryName()
	{
		return "container.grc.fermentBarrel";
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

	private boolean canFerment()
	{
		if (invSlots[0] == null) return false;
		if (isFluidTankEmpty(0)) return false;
		if (!CellarRegistry.instance().booze().isFluidBooze(getFluid(0)))return false;

		final Item item = this.invSlots[0].getItem();
		final int meta = CellarRegistry.instance().booze().getBoozeIndex(getFluid(0));

		if (meta == 3)
		{
			return  item == Items.glowstone_dust;
		}
		else if (meta == 2)
		{
			return item == Items.redstone;
		}
		else if (meta == 1)
		{
			return item == Items.redstone || item == Items.glowstone_dust;
		}
		else if (meta == 0)
		{
			return item == Items.nether_wart;
		}

		return false;
	}

	public void fermentItem()
	{
		final Item item = this.invSlots[0].getItem();
		final int meta = CellarRegistry.instance().booze().getBoozeIndex(getFluid(0));
		final Fluid[] fluidArray = CellarRegistry.instance().booze().getBoozeArray(getFluid(0));

		if (meta == 0 && item == Items.nether_wart)
		{
			tanks[0].setFluid(new FluidStack(fluidArray[1], getFluidStack(0).amount, getFluidStack(0).tag));
		}
		else if ((meta == 1 || meta == 3) && item == Items.glowstone_dust)
		{
			tanks[0].setFluid(new FluidStack(fluidArray[2], getFluidStack(0).amount, getFluidStack(0).tag));
		}
		else if ((meta == 1 || meta == 2) && item == Items.redstone)
		{
			tanks[0].setFluid(new FluidStack(fluidArray[3], getFluidStack(0).amount, getFluidStack(0).tag));
		}

		invSlots[0] = ItemUtils.consumeStack(this.invSlots[0]);
	}

	public int getFermentProgressScaled(int par1)
	{
		if (this.canFerment())
		{
			return this.time * par1 / this.timemax;
		}

		return 0;
	}

	public int getTime()
	{
		return this.time;
	}

	public int getTimeMax()
	{
		return this.timemax;
	}

	public int getBoozeMeta()
	{
		return CellarRegistry.instance().booze().getBoozeIndex(this.getFluid(0));
	}

	@Override
	public void updateMachine()
	{
		if (canFerment())
		{
			++this.time;

			if (this.time == this.timemax)
			{
				this.time = 0;
				this.fermentItem();
			}
		}
		else
		{
			this.time = 0;
		}

		update = true;
	}

	/************
	 * INVENTORY
	 ************/
	@Override
	public boolean isItemValidForSlot(int index, ItemStack itemstack)
	{
		final Item item = itemstack.getItem();
		return index == 0 ? item == Items.nether_wart || item == Items.redstone || item == Items.glowstone_dust : false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return accessableSlotIds;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, int side)
	{
		return true;
	}

	/************
	 * NBT
	 ************/
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
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setShort("time", (short)this.time);
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
			case FermentBarrelDataID.TIME:
				time = v;
				break;
			case FermentBarrelDataID.TANK_FLUID_ID:
				final FluidStack result = FluidUtils.replaceFluidStack(v, tanks[0].getFluid());
				if (result != null) tanks[0].setFluid(result);
				break;
			case FermentBarrelDataID.TANK_FLUID_AMOUNT:
				tanks[0].setFluid(FluidUtils.updateFluidStackAmount(tanks[0].getFluid(), v));
				break;
			default:
				// should warn about invalid Data ID
				break;
		}
	}

	public void sendGUINetworkData(ContainerFermentBarrel container, ICrafting iCrafting)
	{
		iCrafting.sendProgressBarUpdate(container, FermentBarrelDataID.TIME, time);
		final FluidStack fluid = tanks[0].getFluid();
		iCrafting.sendProgressBarUpdate(container, FermentBarrelDataID.TANK_FLUID_ID, fluid != null ? fluid.getFluidID() : 0);
		iCrafting.sendProgressBarUpdate(container, FermentBarrelDataID.TANK_FLUID_AMOUNT, fluid != null ? fluid.amount : 0);
	}

	/************
	 * FLUID
	 ************/
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		return tanks[0].fill(resource, doFill);
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
		return d;
	}
}
