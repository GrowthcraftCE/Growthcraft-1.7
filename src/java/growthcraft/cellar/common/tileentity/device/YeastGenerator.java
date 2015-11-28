package growthcraft.cellar.common.tileentity.device;

import java.util.ArrayList;
import java.util.List;

import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.fermenting.FermentingRegistry;
import growthcraft.cellar.common.tileentity.TileEntityCellarDevice;
import growthcraft.core.util.ItemUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeDictionary;

public class YeastGenerator extends DeviceProgressive
{
	protected int consumption = 1200 / 16;
	protected int fluidSlot;
	protected int invSlot;
	protected List<ItemStack> tempItemList = new ArrayList<ItemStack>();

	/**
	 * @param te - parent tile entity
	 * @param fs - fluid slot id to use in parent
	 *             Fluid will be used from this slot
	 * @param is - inventory slot id to use in parent
	 *             Yeast will be generated into this slot
	 */
	public YeastGenerator(TileEntityCellarDevice te, int fs, int is)
	{
		super(te);
		this.fluidSlot = fs;
		this.invSlot = is;
		setTimeMax(1200);
	}

	public YeastGenerator setConsumption(int t)
	{
		this.consumption = t;
		return this;
	}

	public BiomeGenBase getCurrentBiome()
	{
		return getWorld().getBiomeGenForCoords(parent.xCoord, parent.zCoord);
	}

	public boolean canProduceYeast()
	{
		if (parent.getFluidAmount(0) < consumption) return false;

		return CellarRegistry.instance().booze().hasTags(parent.getFluid(fluidSlot), BoozeTag.YOUNG);
	}

	public void consumeFluid()
	{
		parent.getFluidTank(fluidSlot).drain(consumption, true);
		parent.markForBlockUpdate();
	}

	/**
	 * This is called to initialize the yeast slot, a random yeast type is
	 * chosen from the various biome types and set in the slot,
	 * any further yeast production will be of the same type.
	 */
	protected void initProduceYeast()
	{
		tempItemList.clear();
		final BiomeGenBase biome = getCurrentBiome();
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
			final ItemStack result = tempItemList.get(random.nextInt(tempItemList.size())).copy();
			getInventory().setInventorySlotContents(invSlot, result);
			consumeFluid();
		}
	}

	public void produceYeast()
	{
		if (getInventory().getStackInSlot(invSlot) == null)
		{
			initProduceYeast();
		}
		else
		{
			final ItemStack contents = getInventory().getStackInSlot(invSlot);
			if (CellarRegistry.instance().fermenting().canYeastFormInBiome(contents, getCurrentBiome()))
			{
				getInventory().setInventorySlotContents(invSlot, ItemUtils.increaseStack(contents));
				consumeFluid();
			}
		}
	}

	public void update()
	{
		if (canProduceYeast())
		{
			increaseTime();
			if (time >= timeMax)
			{
				resetTime();
				produceYeast();
				markForInventoryUpdate();
			}
		}
		else
		{
			if (resetTime()) markForInventoryUpdate();
		}
	}
}
