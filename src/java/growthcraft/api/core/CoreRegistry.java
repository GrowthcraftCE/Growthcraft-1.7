package growthcraft.api.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;

public class CoreRegistry 
{
	/**
	 * Gwafu:
	 * 
	 * Yes, it's the same functons/methods as Forge's tall grass hook.
	 */

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
		this.vineList.add(new VineEntry(item, weight));
	}

	/**
	 * STUFF
	 */
	class VineEntry extends WeightedRandom.Item
	{
		public final ItemStack vine;
		public VineEntry(ItemStack vine, int weight)
		{
			super(weight);
			this.vine = vine;
		}
	}

	public ItemStack getVineList(World world)
	{
		VineEntry entry = (VineEntry)WeightedRandom.getRandomItem(world.rand, this.vineList);
		if (entry == null || entry.vine == null)
		{
			return null;
		}
		return entry.vine.copy();
	}
	
	public List<VineEntry> getList(){ return vineList; }

	final List<VineEntry> vineList = new ArrayList<VineEntry>();
}
