package growthcraft.cellar.common.tileentity;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.fermenting.IFermentationRecipe;
import growthcraft.api.core.fluids.FluidUtils;
import growthcraft.cellar.common.fluids.CellarTank;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.common.inventory.GrcInternalInventory;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
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
		TIME_MAX,
		TANK_FLUID_ID,
		TANK_FLUID_AMOUNT,
		UNKNOWN;

		public static final FermentBarrelDataID[] VALID = new FermentBarrelDataID[] { TIME, TIME_MAX, TANK_FLUID_ID, TANK_FLUID_AMOUNT };

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
	private boolean recheckRecipe = true;
	private IFermentationRecipe activeRecipe;

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

	protected void markForRecipeRecheck()
	{
		this.recheckRecipe = true;
	}

	private IFermentationRecipe getWorkingRecipe()
	{
		return activeRecipe;
	}

	public int getTime()
	{
		return this.time;
	}

	public int getTimeMax()
	{
		// if this is the server, just return the recipe time
		// clients will have their timemax synced from the server in the gui
		if (!worldObj.isRemote)
		{
			final IFermentationRecipe result = getWorkingRecipe();
			if (result != null)
			{
				return result.getTime();
			}
		}
		return this.timemax;
	}

	private void resetTime()
	{
		this.time = 0;
	}

	private boolean canFerment()
	{
		if (getStackInSlot(0) == null) return false;
		if (isFluidTankEmpty(0)) return false;
		return getWorkingRecipe() != null;
	}

	public void fermentItem()
	{
		final ItemStack fermentItem = getStackInSlot(0);
		if (fermentItem != null)
		{
			final Item item = fermentItem.getItem();
			final FluidStack fluidStack = getFluidStack(0);

			final IFermentationRecipe recipe = getWorkingRecipe();
			if (recipe != null)
			{
				final FluidStack outputFluidStack = recipe.getOutputFluidStack();
				if (outputFluidStack != null)
				{
					getFluidTank(0).setFluid(FluidUtils.exchangeFluid(getFluidStack(0), outputFluidStack.getFluid()));
				}
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
		final int tmx = getTimeMax();
		if (tmx > 0)
		{
			return this.time * scale / tmx;
		}
		return 0;
	}

	private void refreshRecipe()
	{
		final IFermentationRecipe recipe = CellarRegistry.instance().fermenting().findRecipe(getFluidStack(0), getStackInSlot(0));
		if (recipe != null && recipe != activeRecipe)
		{
			if (activeRecipe != null)
			{
				resetTime();
			}
			activeRecipe = recipe;
		}
		else
		{
			if (activeRecipe != null)
			{
				this.activeRecipe = null;
				resetTime();
			}
		}
	}

	@Override
	protected void updateDevice()
	{
		if (recheckRecipe)
		{
			this.recheckRecipe = false;
			refreshRecipe();
		}

		if (canFerment())
		{
			this.time++;

			if (time >= getTimeMax())
			{
				resetTime();
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

	@Override
	public void receiveGUINetworkData(int id, int v)
	{
		switch (FermentBarrelDataID.fromInt(id))
		{
			case TIME:
				this.time = v;
				break;
			case TIME_MAX:
				this.timemax = v;
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
		iCrafting.sendProgressBarUpdate(container, FermentBarrelDataID.TIME_MAX.ordinal(), getTimeMax());
		final FluidStack fluid = getFluidStack(0);
		iCrafting.sendProgressBarUpdate(container, FermentBarrelDataID.TANK_FLUID_ID.ordinal(), fluid != null ? fluid.getFluidID() : 0);
		iCrafting.sendProgressBarUpdate(container, FermentBarrelDataID.TANK_FLUID_AMOUNT.ordinal(), fluid != null ? fluid.amount : 0);
	}

	@Override
	protected int doFill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		return fillFluidTank(0, resource, doFill);
	}

	@Override
	protected FluidStack doDrain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return drainFluidTank(0, maxDrain, doDrain);
	}

	@Override
	protected FluidStack doDrain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		if (resource == null || !resource.isFluidEqual(getFluidStack(0)))
		{
			return null;
		}
		return doDrain(from, resource.amount, doDrain);
	}

	@Override
	protected void markForFluidUpdate()
	{
		super.markForFluidUpdate();
		markForRecipeRecheck();
	}


	@Override
	public void onInventoryChanged(IInventory inv, int index)
	{
		super.onInventoryChanged(inv, index);
		markForRecipeRecheck();
	}
}
