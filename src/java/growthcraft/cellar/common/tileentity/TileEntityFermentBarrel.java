package growthcraft.cellar.common.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.fermenting.FermentingRegistry;
import growthcraft.api.cellar.fermenting.FermentationResult;
import growthcraft.api.cellar.util.FluidUtils;
import growthcraft.cellar.common.inventory.ContainerFermentBarrel;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.util.ItemUtils;

import net.minecraft.inventory.ICrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityFermentBarrel extends TileEntityCellarMachine
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
	public final boolean canFormYeast = GrowthCraftCellar.getConfig().formYeastInBarrels;
	protected int time;
	protected int yeastTime;
	protected List<ItemStack> tempItemList = new ArrayList<ItemStack>();
	protected Random random = new Random();
	private int maxCap = GrowthCraftCellar.getConfig().fermentBarrelMaxCap;
	private int timemax = GrowthCraftCellar.getConfig().fermentSpeed;

	public TileEntityFermentBarrel()
	{
		super();
		this.tankCaps = new int[] {maxCap};
		this.invSlots = new ItemStack[2];
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

	private FermentationResult getFermentation()
	{
		return CellarRegistry.instance().fermenting().getFermentation(getFluidStack(0), invSlots[0]);
	}

	public int getTime()
	{
		return this.time;
	}

	public int getTimeMax()
	{
		final FermentationResult result = getFermentation();
		if (result != null)
		{
			return result.time;
		}
		return this.timemax;
	}

	public int getBoozeMeta()
	{
		return CellarRegistry.instance().booze().getBoozeIndex(getFluid(0));
	}

	private boolean canFerment()
	{
		if (invSlots[0] == null) return false;
		if (isFluidTankEmpty(0)) return false;
		return getFermentation() != null;
	}

	public void fermentItem()
	{
		final Item item = this.invSlots[0].getItem();
		final FluidStack fluidStack = getFluidStack(0);

		final FermentationResult result = getFermentation();
		if (result != null)
		{
			tanks[0].setFluid(result.asFluidStack(getFluidStack(0).amount));
		}

		invSlots[0] = ItemUtils.consumeStack(this.invSlots[0]);
	}

	public int getFermentProgressScaled(int scale)
	{
		if (this.canFerment())
		{
			return this.time * scale / getTimeMax();
		}

		return 0;
	}

	public boolean canProduceYeast()
	{
		if (!canFormYeast) return false;
		if (isFluidTankEmpty(0)) return false;

		return CellarRegistry.instance().booze().hasTags(getFluid(0), "young");
	}

	/**
	 * This is called to initialize the yeast slot, a random yeast type is
	 * chosen from the various biome types and set in the slot,
	 * any further yeast production will be of the same type.
	 */
	protected void initProduceYeast()
	{
		tempItemList.clear();
		final BiomeGenBase biome = worldObj.getBiomeGenForCoords(xCoord, zCoord);
		final FermentingRegistry reg = CellarRegistry.instance().fermenting();
		for (Type t : BiomeDictionary.getTypesForBiome(biome))
		{
			final List<ItemStack> yeastList = reg.getYeastListForBiomeType(t);
			if (yeastList != null)
			{
				tempItemList.addAll(yeastList);
			}
		}

		if (tempItemList.size() > 0)
		{
			invSlots[1] = tempItemList.get(random.nextInt(tempItemList.size())).copy();
		}
	}

	public void produceYeast()
	{
		if (invSlots[1] == null)
		{
			initProduceYeast();
		}
		else
		{
			final BiomeGenBase biome = worldObj.getBiomeGenForCoords(xCoord, zCoord);
			if (CellarRegistry.instance().fermenting().canYeastFormInBiome(invSlots[1], biome))
			{
				invSlots[1] = ItemUtils.increaseStack(invSlots[1]);
			}
		}
	}

	@Override
	public void updateMachine()
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
			if (canProduceYeast())
			{
				this.yeastTime++;
				if (yeastTime >= 1200)
				{
					this.yeastTime = 0;
					produceYeast();
					markForInventoryUpdate();
				}
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
		switch (FermentBarrelDataID.fromInt(id))
		{
			case TIME:
				time = v;
				break;
			case TANK_FLUID_ID:
				final FluidStack result = FluidUtils.replaceFluidStack(v, tanks[0].getFluid());
				if (result != null) tanks[0].setFluid(result);
				break;
			case TANK_FLUID_AMOUNT:
				tanks[0].setFluid(FluidUtils.updateFluidStackAmount(tanks[0].getFluid(), v));
				break;
			default:
				// should warn about invalid Data ID
				break;
		}
	}

	public void sendGUINetworkData(ContainerFermentBarrel container, ICrafting iCrafting)
	{
		iCrafting.sendProgressBarUpdate(container, FermentBarrelDataID.TIME.ordinal(), time);
		final FluidStack fluid = tanks[0].getFluid();
		iCrafting.sendProgressBarUpdate(container, FermentBarrelDataID.TANK_FLUID_ID.ordinal(), fluid != null ? fluid.getFluidID() : 0);
		iCrafting.sendProgressBarUpdate(container, FermentBarrelDataID.TANK_FLUID_AMOUNT.ordinal(), fluid != null ? fluid.amount : 0);
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
