package growthcraft.cellar.common.tileentity;

import java.util.Arrays;
import java.util.List;

import growthcraft.api.cellar.brewing.BrewingResult;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.common.Residue;
import growthcraft.api.cellar.heatsource.IHeatSourceBlock;
import growthcraft.api.cellar.util.FluidUtils;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.util.ItemUtils;
import growthcraft.core.common.inventory.GrcInternalInventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class TileEntityBrewKettle extends TileEntityCellarDevice
{
	public static enum BrewKettleDataID
	{
		TIME,
		TIME_MAX,
		TANK1_FLUID_ID,
		TANK1_FLUID_AMOUNT,
		TANK2_FLUID_ID,
		TANK2_FLUID_AMOUNT;

		public static final List<BrewKettleDataID> VALUES = Arrays.asList(values());
	}

	private static final int[] rawSlotIDs = new int[] {0, 1};
	private static final int[] residueSlotIDs = new int[] {0};

	// Other Vars.
	protected float residue;
	protected double time;

	@Override
	protected FluidTank[] createTanks()
	{
		final int maxCap = GrowthCraftCellar.getConfig().brewKettleMaxCap;
		return new FluidTank[] {
			new CellarTank(maxCap, this),
			new CellarTank(maxCap, this)
		};
	}

	@Override
	protected GrcInternalInventory createInventory()
	{
		return new GrcInternalInventory(this, 2);
	}

	protected void markForFluidUpdate()
	{
		// Brew Kettles need to update their rendering state when a fluid
		// changes, most of the other cellar blocks are unaffected by this
		markForBlockUpdate();
	}

	@Override
	public String getDefaultInventoryName()
	{
		return "container.grc.brewKettle";
	}

	protected boolean resetTime()
	{
		if (this.time != 0)
		{
			this.time = 0.0;
			return true;
		}
		return false;
	}

	public boolean hasFire()
	{
		final Block block = this.worldObj.getBlock(xCoord, yCoord - 1, zCoord);
		final int meta = worldObj.getBlockMetadata(xCoord, yCoord - 1, zCoord);
		return CellarRegistry.instance().heatSource().isBlockHeatSource(block, meta);
	}

	private BrewingResult getBrewingResult()
	{
		return CellarRegistry.instance().brewing().getBrewingResult(getFluidStack(0), getStackInSlot(0));
	}

	public boolean canBrew()
	{
		if (!hasFire()) return false;

		final BrewingResult result = getBrewingResult();
		if (result == null) return false;


		final ItemStack expected = result.getInputItemStack();
		if (expected != null)
		{
			final ItemStack brewingItem = getStackInSlot(0);
			if (brewingItem == null) return false;
			if (isFluidTankFull(1)) return false;
			if (!expected.isItemEqual(brewingItem)) return false;
			if (brewingItem.stackSize < expected.stackSize) return false;
		}

		final FluidStack inputFluid = result.getInputFluidStack();
		if (inputFluid != null)
		{
			final FluidStack currentInputFluid = getFluidStack(0);
			if (!inputFluid.isFluidEqual(currentInputFluid)) return false;
			if (currentInputFluid.amount < inputFluid.amount) return false;
		}

		if (isFluidTankEmpty(1)) return true;

		final FluidStack outputFluid = result.asFluidStack();
		final FluidStack currentOutputFluid = getFluidStack(1);
		if (!outputFluid.isFluidEqual(currentOutputFluid)) return false;

		return true;
	}

	public float getHeatMultiplier()
	{
		final Block block = worldObj.getBlock(xCoord, yCoord - 1, zCoord);
		final int meta = worldObj.getBlockMetadata(xCoord, yCoord - 1, zCoord);
		final IHeatSourceBlock heatSource = CellarRegistry.instance().heatSource().getHeatSource(block, meta);
		if (heatSource != null)
		{
			return heatSource.getHeat(worldObj, xCoord, yCoord - 1, zCoord);
		}
		return 0.0f;
	}

	private void produceGrain(BrewingResult result)
	{
		final Residue res = result.getResidue();
		this.residue = this.residue + res.pomaceRate;
		if (this.residue >= 1.0F)
		{
			this.residue = this.residue - 1.0F;

			final ItemStack residueResult = ItemUtils.mergeStacks(getStackInSlot(1), res.residueItem);
			if (residueResult != null) setInventorySlotContents(1, residueResult);
		}
	}

	public void brewItem()
	{
		final ItemStack brewingItem = getStackInSlot(0);
		final BrewingResult result = getBrewingResult();
		if (result == null) return;
		// set spent grain
		produceGrain(result);
		final FluidStack inputStack = result.getInputFluidStack();
		final FluidStack resultStack = result.asFluidStack();
		getFluidTank(1).fill(resultStack, true);
		getFluidTank(0).drain(inputStack.amount, true);

		decrStackSize(0, 1);

		markForBlockUpdate();
	}

	public int getBrewingTime()
	{
		final BrewingResult result = getBrewingResult();
		if (result != null) return result.getTime();
		return 0;
	}

	/************
	 * UPDATE
	 ************/
	@Override
	protected void updateDevice()
	{
		if (this.canBrew())
		{
			final float multiplier = getHeatMultiplier();
			this.time += multiplier * 1;

			if ((int)time >= getBrewingTime())
			{
				resetTime();
				this.brewItem();
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

	@SideOnly(Side.CLIENT)
	public int getBrewProgressScaled(int range)
	{
		if (this.canBrew())
		{
			return (int)time * range / getBrewingTime();
		}
		return 0;
	}

	@SideOnly(Side.CLIENT)
	public int getHeatScaled(int range)
	{
		return (int)(MathHelper.clamp_float(getHeatMultiplier(), 0.0f, 1.0f) * range);
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
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		this.time = nbt.getShort("time");
		this.residue = nbt.getFloat("grain");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		nbt.setShort("time", (short)this.time);
		nbt.setFloat("grain", this.residue);
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
		final BrewKettleDataID dataId = BrewKettleDataID.VALUES.get(id);
		switch (dataId)
		{
			case TIME:
				time = v;
				break;
			case TIME_MAX:
				// time = v;
				break;
			case TANK1_FLUID_ID:
			{
				final FluidStack result = FluidUtils.replaceFluidStack(v, getFluidStack(0));
				if (result != null) getFluidTank(0).setFluid(result);
			}	break;
			case TANK1_FLUID_AMOUNT:
				getFluidTank(0).setFluid(FluidUtils.updateFluidStackAmount(getFluidStack(0), v));
				break;
			case TANK2_FLUID_ID:
			{
				final FluidStack result = FluidUtils.replaceFluidStack(v, getFluidStack(1));
				if (result != null) getFluidTank(1).setFluid(result);
			}	break;
			case TANK2_FLUID_AMOUNT:
				getFluidTank(1).setFluid(FluidUtils.updateFluidStackAmount(getFluidStack(1), v));
				break;
			default:
				break;
		}
	}

	@Override
	public void sendGUINetworkData(Container container, ICrafting iCrafting)
	{
		iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.TIME.ordinal(), (int)time);
		//iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.TIME_MAX.ordinal(), (int)time);
		FluidStack fluid = getFluidStack(0);
		iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.TANK1_FLUID_ID.ordinal(), fluid != null ? fluid.getFluidID() : 0);
		iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.TANK1_FLUID_AMOUNT.ordinal(), fluid != null ? fluid.amount : 0);
		fluid = getFluidStack(1);
		iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.TANK2_FLUID_ID.ordinal(), fluid != null ? fluid.getFluidID() : 0);
		iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.TANK2_FLUID_AMOUNT.ordinal(), fluid != null ? fluid.amount : 0);
	}

	/************
	 * FLUID
	 ************/
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		final int f = getFluidTank(0).fill(resource, doFill);
		if (doFill && f > 0)
		{
			markForBlockUpdate();
		}
		return f;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		final FluidStack d = getFluidTank(1).drain(maxDrain, doDrain);
		if (doDrain && d != null && d.amount > 0)
		{
			markForBlockUpdate();
		}
		return d;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		if (resource == null || !resource.isFluidEqual(getFluidStack(1)))
		{
			return null;
		}

		return drain(from, resource.amount, doDrain);
	}

	public void switchTanks()
	{
		FluidStack f0 = null;
		FluidStack f1 = null;
		if (this.getFluidStack(0) != null)
		{
			f0 = this.getFluidStack(0).copy();
		}
		if (this.getFluidStack(1) != null)
		{
			f1 = this.getFluidStack(1).copy();
		}
		this.clearTank(0);
		this.clearTank(1);
		this.getFluidTank(0).fill(f1, true);
		this.getFluidTank(1).fill(f0, true);

		markForBlockUpdate();
	}
}
