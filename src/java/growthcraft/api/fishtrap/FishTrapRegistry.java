package growthcraft.api.fishtrap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;

public class FishTrapRegistry
{
	private static final FishTrapRegistry instance = new FishTrapRegistry();
	public static final FishTrapRegistry instance()
	{
		return instance;
	}

	//////////////////////////////////////////////////////////////////////
	// FISH TRAP /////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	/**
	 * addTrapFish() // addTrapTreasure() // addTrapJunk()
	 *
	 * Example:
	 * FishTrapRegistry.instance().addTrapTreasure(new FishTrapEntry(new ItemStack(Item.bow), 1).setDamage(0.25F).setEnchantable());
	 *
	 * new FishTrapEntry(new ItemStack(Item.bow), 1)
	 * -- ItemStack represents the...ItemStack. k
	 * -- 1 represents the weight of the ItemStack when randomly picked. Higher number means better chances being randomly picked.
	 *
	 * .setDamage(0.25F)
	 * -- Float param is the damage factor of the ItemStack. It will serve as the base factor for the ItemStack's damage.
	 *
	 * .setEnchantable()
	 * -- Call if the ItemStack can be enchanted.
	 *
	 * @param entry   - The entry to be added.
	 */
	public void addTrapFish(FishTrapEntry entry)
	{
		this.fishList.add(entry);
	}

	public void addTrapTreasure(FishTrapEntry entry)
	{
		this.treasureList.add(entry);
	}

	public void addTrapJunk(FishTrapEntry entry)
	{
		this.junkList.add(entry);
	}

	/**
	 * STUFF
	 */
	private ItemStack getFishableEntry(Random random, List list)
	{
		return ((FishTrapEntry)WeightedRandom.getRandomItem(random, list)).getFishable(random);
	}

	public ItemStack getFishList(World world)
	{
		return this.getFishableEntry(world.rand, this.fishList);
	}

	public ItemStack getTreasureList(World world)
	{
		return this.getFishableEntry(world.rand, this.treasureList);
	}

	public ItemStack getJunkList(World world)
	{
		return this.getFishableEntry(world.rand, this.junkList);
	}

	final List<FishTrapEntry> fishList = new ArrayList<FishTrapEntry>();
	final List<FishTrapEntry> treasureList = new ArrayList<FishTrapEntry>();
	final List<FishTrapEntry> junkList = new ArrayList<FishTrapEntry>();
}
