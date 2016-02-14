package growthcraft.api.core;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.ILoggable;
import growthcraft.api.core.log.NullLogger;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;

public class VineDropRegistry implements ILoggable
{
	public static class VineEntry extends WeightedRandom.Item
	{
		private final ItemStack item;

		public VineEntry(ItemStack v, int weight)
		{
			super(weight);
			this.item = v;
		}

		public ItemStack getItemStack()
		{
			return item;
		}
	}

	private final List<VineEntry> vineList = new ArrayList<VineEntry>();
	private ILogger logger = NullLogger.INSTANCE;

	@Override
	public void setLogger(@Nonnull ILogger l)
	{
		this.logger = l;
	}

	/**
	 * @return vine drop list
	 */
	public List<VineEntry> getList()
	{
		return vineList;
	}

	/**
	 * Adds a drop to vines.
	 *
	 * @param item   - The item/block to be added.
	 * @param weight - Weight. Used for randoming. Higher numbers means lesser chance.
	 */
	public void addEntry(@Nonnull ItemStack item, int weight)
	{
		if (weight <= 0)
		{
			logger.warn("RARITY/WEIGHT WAS SET TO 0 FOR ITEM: %s, THE WORLD IS CRUMBLING, WHAT HAVE YOU DONE ~ IceDragon. Go set it to 1 or something.", item.getUnlocalizedName());
			weight = 1;
		}
		this.vineList.add(new VineEntry(item, weight));
	}

	/**
	 * @return true if their are any vine drops, false otherwise
	 */
	public boolean hasVineDrops()
	{
		return !getList().isEmpty();
	}

	/**
	 * @param world - The world
	 * @return itemstack or null
	 */
	public ItemStack getVineDropItem(@Nonnull World world)
	{
		final List<VineEntry> vineEntries = getList();
		if (vineEntries.isEmpty()) return null;

		final VineEntry entry = (VineEntry)WeightedRandom.getRandomItem(world.rand, vineEntries);
		if (entry == null || entry.getItemStack() == null) return null;

		return entry.getItemStack().copy();
	}
}
