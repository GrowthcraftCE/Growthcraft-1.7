package growthcraft.cellar.common.tileentity;

import growthcraft.api.cellar.brewing.BrewingRegistry;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.common.Residue;
import growthcraft.api.cellar.util.FluidUtils;
import growthcraft.cellar.common.inventory.ContainerBrewKettle;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.util.ItemUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityBrewKettle extends TileEntityCellarMachine
{
	public static class BrewKettleDataID
	{
		public static final int TIME = 0;
		public static final int TANK1_FLUID_ID = 1;
		public static final int TANK1_FLUID_AMOUNT = 2;
		public static final int TANK2_FLUID_ID = 3;
		public static final int TANK2_FLUID_AMOUNT = 4;

		private BrewKettleDataID() {}
	}

	private static final int[] rawSlotIDs = new int[] {0, 1};
	private static final int[] residueSlotIDs = new int[] {0};

	// Other Vars.
	protected float residue;
	protected double time;

	private int maxCap = GrowthCraftCellar.getConfig().brewKettleMaxCap;

	public TileEntityBrewKettle()
	{
		super();
		this.tankCaps = new int[] {maxCap, maxCap};
		this.invSlots = new ItemStack[2];
		this.tanks = new CellarTank[] {
			new CellarTank(tankCaps[0], this),
			new CellarTank(tankCaps[1], this)
		};
	}

	@Override
	public String getDefaultInventoryName()
	{
		return "container.grc.brewKettle";
	}

	private void resetTime()
	{
		this.time = 0.0;
	}

	@Override
	protected void sendUpdate()
	{
		this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
	}

	public boolean hasFire()
	{
		final Block block = this.worldObj.getBlock(this.xCoord, this.yCoord - 1, this.zCoord);
		final int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord - 1, this.zCoord);
		return CellarRegistry.instance().heatSource().isBlockHeatSource(block, meta);
	}

	public boolean canBrew()
	{
		if (!hasFire()) return false;
		if (this.invSlots[0] == null) return false;
		if (this.isFluidTankFull(1)) return false;
		if (!CellarRegistry.instance().brewing().isBrewingRecipe(getFluidStack(0), this.invSlots[0])) return false;
		if (this.isFluidTankEmpty(1)) return true;

		final FluidStack stack = CellarRegistry.instance().brewing().getBrewingFluidStack(getFluidStack(0), this.invSlots[0]);
		return stack.isFluidEqual(getFluidStack(1));
	}

	public float getHeatMultiplier()
	{
		final Block block = this.worldObj.getBlock(this.xCoord, this.yCoord - 1, this.zCoord);
		final int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord - 1, this.zCoord);
		return CellarRegistry.instance().heatSource().getHeatMultiplier(block, meta);
	}

	private void produceGrain()
	{
		final Residue res = CellarRegistry.instance().brewing().getBrewingResidue(getFluidStack(0), this.invSlots[0]);
		this.residue = this.residue + res.pomaceRate;
		if (this.residue >= 1.0F)
		{
			this.residue = this.residue - 1.0F;

			final ItemStack residueResult = ItemUtils.mergeStacks(this.invSlots[1], res.residueItem);
			if (residueResult != null) invSlots[1] = residueResult;
		}
	}

	public void brewItem()
	{
		final BrewingRegistry brewing = CellarRegistry.instance().brewing();
		// set spent grain
		produceGrain();

		final FluidStack fluidstack = brewing.getBrewingFluidStack(getFluidStack(0), this.invSlots[0]);
		final int amount = brewing.getBrewingAmount(getFluidStack(0), this.invSlots[0]);
		fluidstack.amount = amount;
		tanks[1].fill(fluidstack, true);
		tanks[0].drain(amount, true);

		this.invSlots[0] = ItemUtils.consumeStack(this.invSlots[0]);

		sendUpdate();
	}

	public int getBrewingTime()
	{
		return CellarRegistry.instance().brewing().getBrewingTime(getFluidStack(0), this.invSlots[0]);
	}

	/************
	 * UPDATE
	 ************/
	@Override
	public void updateMachine()
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
			resetTime();
		}

		markForUpdate();
	}

	private void debugMsg()
	{
		if (this.worldObj.isRemote)
		{
			System.out.println("CLIENT: " + getFluidAmount(0) + " " + getFluidAmount(1));
		}
		if (!this.worldObj.isRemote)
		{
			System.out.println("SERVER: " + getFluidAmount(0) + " " + getFluidAmount(1));
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
	public void getGUINetworkData(int id, int v)
	{
		switch (id)
		{
			case BrewKettleDataID.TIME:
				time = v;
				break;
			case BrewKettleDataID.TANK1_FLUID_ID:
			{
				final FluidStack result = FluidUtils.replaceFluidStack(v, tanks[0].getFluid());
				if (result != null) tanks[0].setFluid(result);
			}	break;
			case BrewKettleDataID.TANK1_FLUID_AMOUNT:
				tanks[0].setFluid(FluidUtils.updateFluidStackAmount(tanks[0].getFluid(), v));
				break;
			case BrewKettleDataID.TANK2_FLUID_ID:
			{
				final FluidStack result = FluidUtils.replaceFluidStack(v, tanks[1].getFluid());
				if (result != null) tanks[1].setFluid(result);
			}	break;
			case BrewKettleDataID.TANK2_FLUID_AMOUNT:
				tanks[1].setFluid(FluidUtils.updateFluidStackAmount(tanks[1].getFluid(), v));
				break;
			default:
				break;
		}
	}

	public void sendGUINetworkData(ContainerBrewKettle container, ICrafting iCrafting)
	{
		iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.TIME, (int)time);
		FluidStack fluid = tanks[0].getFluid();
		iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.TANK1_FLUID_ID, fluid != null ? fluid.getFluidID() : 0);
		iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.TANK1_FLUID_AMOUNT, fluid != null ? fluid.amount : 0);
		fluid = tanks[1].getFluid();
		iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.TANK2_FLUID_ID, fluid != null ? fluid.getFluidID() : 0);
		iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.TANK2_FLUID_AMOUNT, fluid != null ? fluid.amount : 0);
	}

	/************
	 * FLUID
	 ************/
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		final int f = tanks[0].fill(resource, doFill);
		if (f > 0)
		{
			sendUpdate();
		}
		return f;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		if (resource == null || !resource.isFluidEqual(tanks[1].getFluid()))
		{
			return null;
		}

		return drain(from, resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		final FluidStack d = tanks[1].drain(maxDrain, doDrain);
		if (d != null)
		{
			sendUpdate();
		}
		return d;
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

		sendUpdate();
	}
}
