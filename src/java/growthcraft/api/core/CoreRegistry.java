package growthcraft.api.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;

import org.apache.logging.log4j.Level;
import cpw.mods.fml.common.FMLLog;

public class CoreRegistry
{
	class VineEntry extends WeightedRandom.Item
	{
		public final ItemStack vine;
		public VineEntry(ItemStack vine, int weight)
		{
			super(weight);
			this.vine = vine;
		}
	}
	/**
	 * Gwafu:
	 *
	 * Yes, it's the same functons/methods as Forge's tall grass hook.
	 */

	/**
	 * @return vine drop list
	 */
	public List<VineEntry> getList(){ return vineList; }

	private final List<VineEntry> vineList = new ArrayList<VineEntry>();

	private static final CoreRegistry instance = new CoreRegistry();
	public static final CoreRegistry instance()
	{
		return instance;
	}

	///////////////////////////////////////////////////////////////////////
	// VINE DROPS /////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	/**
	 * addVineDrop()
	 * Adds a drop to vines.
	 *
	 * @param item   - The item/block to be added.
	 * @param weight - Weight. Used for randoming. Higher numbers means lesser chance.
	 */
	public void addVineDrop(ItemStack item, int weight)
	{
		if (weight <= 0)
		{
			FMLLog.log("Growthcraft", Level.WARN,
			 	"RARITY/WEIGHT WAS SET TO 0 FOR ITEM: " +
			 	item.getUnlocalizedName() +
			 	", THE WORLD IS CRUMBLING, WHAT HAVE YOU DONE ~ IceDragon. Go set it to 1 or something.");
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
	public ItemStack getVineDropItem(World world)
	{
		final List<VineEntry> vineEntries = getList();
		if (vineEntries.isEmpty()) return null;

		VineEntry entry = (VineEntry)WeightedRandom.getRandomItem(world.rand, vineEntries);
		if (entry == null || entry.vine == null) return null;

		return entry.vine.copy();
	}
}
