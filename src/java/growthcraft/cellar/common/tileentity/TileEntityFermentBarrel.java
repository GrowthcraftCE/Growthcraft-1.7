package growthcraft.cellar.common.tileentity;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.fermenting.FermentationRecipe;
import growthcraft.api.cellar.util.FluidUtils;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.common.inventory.GrcInternalInventory;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class TileEntityFermentBarrel extends TileEntityCellarDevice
{
	public static enum FermentBarrelDataID
	{
		TIME,
		TANK_FLUID_ID,
		TANK_FLUID_AMOUNT,
		UNKNOWN;

		public static final FermentBarrelDataID[] VALID = new FermentBarrelDataID[] { TIME, TANK_FLUID_ID, TANK_FLUID_AMOUNT };

		public static FermentBarrelDataID fromInt(int i)
		{
			if (i >= 0 && i <= VALID.length) return VALID[i];
			return UNKNOWN;
		}
	}

	// Constants
	private static final int[] accessableSlotIds = new int[] {0};

	// Other Vars.
	protected int time;
	private int timemax = GrowthCraftCellar.getConfig().fermentTime;

	@Override
	protected FluidTank[] createTanks()
	{
		return new FluidTank[] { new CellarTank(GrowthCraftCellar.getConfig().fermentBarrelMaxCap, this) };
	}

	@Override
	protected GrcInternalInventory createInventory()
	{
		return new GrcInternalInventory(this, 2);
	}

	@Override
	public String getDefaultInventoryName()
	{
		return "container.grc.fermentBarrel";
	}

	private FermentationRecipe getFermentation()
	{
		return CellarRegistry.instance().fermenting().getFermentationRecipe(getFluidStack(0), getStackInSlot(0));
	}

	public int getTime()
	{
		return this.time;
	}

	public int getTimeMax()
	{
		final FermentationRecipe result = getFermentation();
		if (result != null)
		{
			return result.getTime();
		}
		return this.timemax;
	}

	private boolean canFerment()
	{
		if (getStackInSlot(0) == null) return false;
		if (isFluidTankEmpty(0)) return false;
		return getFermentation() != null;
	}

	public void fermentItem()
	{
		final ItemStack fermentItem = getStackInSlot(0);
		if (fermentItem != null)
		{
			final Item item = fermentItem.getItem();
			final FluidStack fluidStack = getFluidStack(0);

			final FermentationRecipe recipe = getFermentation();
			if (recipe != null)
			{
				getFluidTank(0).setFluid(recipe.asFluidStack(getFluidStack(0).amount));
				final ItemStack fermenter = recipe.getFermentingItemStack();
				if (fermenter != null)
				{
					decrStackSize(0, fermenter.stackSize);
				}
			}
		}
	}

	public int getFermentProgressScaled(int scale)
	{
		if (this.canFerment())
		{
			return this.time * scale / getTimeMax();
		}

		return 0;
	}

	@Override
	protected void updateDevice()
	{
		if (canFerment())
		{
			this.time++;

			if (time >= getTimeMax())
			{
				this.time = 0;
				fermentItem();
				markForInventoryUpdate();
			}
		}
		else
		{
			if (time != 0)
			{
				this.time = 0;
				markForInventoryUpdate();
			}
		}
	}

	/************
	 * INVENTORY
	 ************/
	@Override
	public boolean isItemValidForSlot(int index, ItemStack itemstack)
	{
		return true;
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
	@Override
	public void receiveGUINetworkData(int id, int v)
	{
		switch (FermentBarrelDataID.fromInt(id))
		{
			case TIME:
				time = v;
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
		iCrafting.sendProgressBarUpdate(container, FermentBarrelDataID.TIME.ordinal(), time);
		final FluidStack fluid = getFluidStack(0);
		iCrafting.sendProgressBarUpdate(container, FermentBarrelDataID.TANK_FLUID_ID.ordinal(), fluid != null ? fluid.getFluidID() : 0);
		iCrafting.sendProgressBarUpdate(container, FermentBarrelDataID.TANK_FLUID_AMOUNT.ordinal(), fluid != null ? fluid.amount : 0);
	}

	/************
	 * FLUID
	 ************/
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		return getFluidTank(0).fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return getFluidTank(0).drain(maxDrain, doDrain);
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
}
