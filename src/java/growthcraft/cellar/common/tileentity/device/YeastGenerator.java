package growthcraft.cellar.common.tileentity.device;

import java.util.ArrayList;
import java.util.List;

import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.yeast.IYeastRegistry;
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

	/**
	 * How many fluid units are consumed per yeast gen?
	 *
	 * @param c - fluid consumption in milli-buckets
	 */
	public YeastGenerator setConsumption(int c)
	{
		this.consumption = c;
		return this;
	}

	/**
	 * Returns the current biome of the Yeast Generator's parent TileEntity.
	 *
	 * @return biome
	 */
	public BiomeGenBase getCurrentBiome()
	{
		return getWorld().getBiomeGenForCoords(parent.xCoord, parent.zCoord);
	}

	/**
	 * Determines if the given item stack can be replicated as a yeast item
	 *
	 * @param stack - item stack to test
	 * @return true, it can be replicated, false otherwise
	 */
	public boolean canReplicateYeast(ItemStack stack)
	{
		// prevent production if the stack size is currently maxed
		if (stack.stackSize >= stack.getMaxStackSize()) return false;
		// prevent item pointless ticking with invalid items
		if (!CellarRegistry.instance().yeast().isYeast(stack)) return false;
		return true;
	}

	/**
	 * Determines if the jar can produce any yeast
	 *
	 * @return true, the generator can produce yeast, false otherwise
	 */
	public boolean canProduceYeast()
	{
		if (parent.getFluidAmount(0) < consumption) return false;
		final ItemStack yeastItem = getInventory().getStackInSlot(invSlot);
		// we can ignore null items, this will fallback to the initProduceYeast
		if (yeastItem != null)
		{
			if (!canReplicateYeast(yeastItem)) return false;
		}
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
		final IYeastRegistry reg = CellarRegistry.instance().yeast();
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
			// ensure that the item is indeed some form of yeast, prevents item duplication
			// while canProduceYeast will prevent invalid items from popping up
			// produceYeast is public, and can be called outside the update
			// logic to force yeast generation, as such, this must ensure
			// item correctness
			if (canReplicateYeast(contents))
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
