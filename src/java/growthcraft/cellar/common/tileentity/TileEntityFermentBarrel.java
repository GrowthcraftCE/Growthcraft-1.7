package growthcraft.cellar.common.tileentity;

import java.io.IOException;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.fermenting.IFermentationRecipe;
import growthcraft.api.core.definition.IMultiItemStacks;
import growthcraft.api.core.fluids.FluidTest;
import growthcraft.api.core.fluids.FluidUtils;
import growthcraft.api.core.nbt.INBTItemSerializable;
import growthcraft.api.core.nbt.NBTHelper;
import growthcraft.cellar.common.fluids.CellarTank;
import growthcraft.cellar.common.inventory.ContainerFermentBarrel;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.common.inventory.GrcInternalInventory;
import growthcraft.core.common.inventory.InventoryProcessor;
import growthcraft.core.common.tileentity.event.EventHandler;
import growthcraft.core.common.tileentity.feature.ITileProgressiveDevice;

import io.netty.buffer.ByteBuf;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class TileEntityFermentBarrel extends TileEntityCellarDevice implements ITileProgressiveDevice, INBTItemSerializable
{
	public static enum FermentBarrelDataID
	{
		TIME,
		TIME_MAX,
		UNKNOWN;

		public static final FermentBarrelDataID[] VALID = new FermentBarrelDataID[]
		{
			TIME,
			TIME_MAX
		};

		public static FermentBarrelDataID getByaOrdinal(int ord)
		{
			if (ord >= 0 && ord <= VALID.length) return VALID[ord];
			return UNKNOWN;
		}
	}

	// Constants
	private static final int[] accessableSlotIds = new int[] {0};

	// Other Vars.
	protected int time;
	private int timemax = GrowthCraftCellar.getConfig().fermentTime;
	private boolean shouldUseCachedRecipe = GrowthCraftCellar.getConfig().fermentBarrelUseCachedRecipe;
	private boolean recheckRecipe = true;
	private boolean lidOn = true;
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

	@Override
	public String getGuiID()
	{
		return "grccellar:ferment_barrel";
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerFermentBarrel(playerInventory, this);
	}

	protected void markForRecipeRecheck()
	{
		this.recheckRecipe = true;
	}

	/**
	 * @return time was reset, false otherwise
	 */
	protected boolean resetTime()
	{
		if (time != 0)
		{
			this.time = 0;
			return true;
		}
		return false;
	}

	private IFermentationRecipe loadRecipe()
	{
		return CellarRegistry.instance().fermenting().findRecipe(getFluidStack(0), getStackInSlot(0));
	}

	private IFermentationRecipe refreshRecipe()
	{
		final IFermentationRecipe recipe = loadRecipe();
		if (recipe != null && recipe != activeRecipe)
		{
			if (activeRecipe != null)
			{
				resetTime();
			}
			this.activeRecipe = recipe;
			markForInventoryUpdate();
		}
		else
		{
			if (activeRecipe != null)
			{
				this.activeRecipe = null;
				resetTime();
				markForInventoryUpdate();
			}
		}
		return activeRecipe;
	}

	private IFermentationRecipe getWorkingRecipe()
	{
		if (shouldUseCachedRecipe)
		{
			if (activeRecipe == null) refreshRecipe();
			return activeRecipe;
		}
		return loadRecipe();
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
			final IFermentationRecipe recipe = getWorkingRecipe();
			if (recipe != null)
			{
				final FluidStack outputFluidStack = recipe.getOutputFluidStack();
				if (outputFluidStack != null)
				{
					getFluidTank(0).setFluid(FluidUtils.exchangeFluid(getFluidStack(0), outputFluidStack.getFluid()));
				}
				final IMultiItemStacks fermenter = recipe.getFermentingItemStack();
				if (fermenter != null && !fermenter.isEmpty())
				{
					decrStackSize(0, fermenter.getStackSize());
				}
			}
		}
	}

	@Override
	public float getDeviceProgress()
	{
		final int tmx = getTimeMax();
		if (tmx > 0)
		{
			return (float)time / (float)tmx;
		}
		return 0.0f;
	}

	@Override
	public int getDeviceProgressScaled(int scale)
	{
		final int tmx = getTimeMax();
		if (tmx > 0)
		{
			return this.time * scale / tmx;
		}
		return 0;
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
				resetTime();
				markForInventoryUpdate();
			}
		}
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return accessableSlotIds;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack itemstack)
	{
		return index == 0;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, int side)
	{
		return InventoryProcessor.instance().canInsertItem(this, stack, index);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, int side)
	{
		return InventoryProcessor.instance().canExtractItem(this, stack, index);
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

	private void readFermentTimeFromNBT(NBTTagCompound nbt)
	{
		this.time = NBTHelper.getInteger(nbt, "time");
	}

	@Override
	public void readFromNBTForItem(NBTTagCompound nbt)
	{
		super.readFromNBTForItem(nbt);
		readFermentTimeFromNBT(nbt);
		if (nbt.hasKey("lid_on"))
		{
			this.lidOn = nbt.getBoolean("lid_on");
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		readFermentTimeFromNBT(nbt);
	}

	private void writeFermentTimeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("time", time);
	}

	@Override
	public void writeToNBTForItem(NBTTagCompound nbt)
	{
		super.writeToNBTForItem(nbt);
		writeFermentTimeToNBT(nbt);
		nbt.setBoolean("lid_on", lidOn);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		writeFermentTimeToNBT(nbt);
	}

	@Override
	public void receiveGUINetworkData(int id, int v)
	{
		super.receiveGUINetworkData(id, v);
		switch (FermentBarrelDataID.getByaOrdinal(id))
		{
			case TIME:
				this.time = v;
				break;
			case TIME_MAX:
				this.timemax = v;
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
		iCrafting.sendProgressBarUpdate(container, FermentBarrelDataID.TIME.ordinal(), time);
		iCrafting.sendProgressBarUpdate(container, FermentBarrelDataID.TIME_MAX.ordinal(), getTimeMax());
	}

	@EventHandler(type=EventHandler.EventType.NETWORK_READ)
	public boolean readFromStream_FermentBarrel(ByteBuf stream) throws IOException
	{
		this.time = stream.readInt();
		this.timemax = stream.readInt();
		return false;
	}

	@EventHandler(type=EventHandler.EventType.NETWORK_WRITE)
	public boolean writeToStream_FermentBarrel(ByteBuf stream) throws IOException
	{
		stream.writeInt(time);
		stream.writeInt(getTimeMax());
		return false;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		final FluidStack fluidStack = getFluidStack(0);
		if (fluidStack == null || fluidStack.getFluid() == null) return true;
		return FluidTest.fluidMatches(fluidStack, fluid);
	}

	@Override
	protected int doFill(ForgeDirection from, FluidStack resource, boolean shouldFill)
	{
		return fillFluidTank(0, resource, shouldFill);
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return FluidTest.fluidMatches(getFluidStack(0), fluid);
	}

	@Override
	protected FluidStack doDrain(ForgeDirection from, int maxDrain, boolean shouldDrain)
	{
		return drainFluidTank(0, maxDrain, shouldDrain);
	}

	@Override
	protected FluidStack doDrain(ForgeDirection from, FluidStack resource, boolean shouldDrain)
	{
		if (resource == null || !resource.isFluidEqual(getFluidStack(0)))
		{
			return null;
		}
		return doDrain(from, resource.amount, shouldDrain);
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
