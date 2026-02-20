package growthcraft.api.fishtrap;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nonnull;

import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;
import growthcraft.api.core.log.ILoggable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;

public class FishTrapRegistry implements ILoggable
{
	private static final FishTrapRegistry instance = new FishTrapRegistry();
	private final BaitRegistry baits = new BaitRegistry();
	private final Set<CatchGroupEntry> catchGroups = new HashSet<CatchGroupEntry>();
	private final Map<String, List<FishTrapEntry>> entriesByGroup = new HashMap<String, List<FishTrapEntry>>();
	private ILogger logger = NullLogger.INSTANCE;

	public static final FishTrapRegistry instance()
	{
		return instance;
	}

	@Override
	public void setLogger(@Nonnull ILogger l)
	{
		this.logger = l;
	}

	public void addBait(Object stack, BaitRegistry.BaitHandle handle)
	{
		logger.debug("Adding FishTrap Bait `%s`", stack);
		baits.add(stack, handle);
	}

	public void addBait(Object stack, float base, float mul)
	{
		addBait(stack, new BaitRegistry.BaitHandle(base, mul));
	}

	public BaitRegistry.BaitHandle findBait(ItemStack stack)
	{
		return baits.findHandle(stack);
	}

	public void addCatchGroup(String name, int weight)
	{
		logger.debug("Adding Catch Group group=`%s` weight=%d", name, weight);
		catchGroups.add(new CatchGroupEntry(name, weight));
	}

	public Collection<CatchGroupEntry> getCatchGroups()
	{
		return catchGroups;
	}

	public String getRandomCatchGroup(Random random)
	{
		final CatchGroupEntry entry = (CatchGroupEntry)WeightedRandom.getRandomItem(random, getCatchGroups());
		if (entry != null) return entry.getName();
		return null;
	}

	public void addCatchToGroup(FishTrapEntry entry, String group)
	{
		if (!entriesByGroup.containsKey(group))
		{
			entriesByGroup.put(group, new LinkedList<FishTrapEntry>());
		}
		entriesByGroup.get(group).add(entry);
	}

	private ItemStack getRandomCatchFromList(Random random, List<FishTrapEntry> list)
	{
		if (list.isEmpty()) return null;
		return ((FishTrapEntry)WeightedRandom.getRandomItem(random, list)).getFishable(random);
	}

	public ItemStack getRandomCatchFromGroup(Random random, String group)
	{
		final List<FishTrapEntry> list = entriesByGroup.get(group);
		if (list != null)
		{
			return getRandomCatchFromList(random, list);
		}
		return null;
	}

	/**
	 * Use addCatchToGroup instead
	 */
	@Deprecated
	public void addTrapFish(FishTrapEntry entry)
	{
		addCatchToGroup(entry, "fish");
	}

	@Deprecated
	public void addTrapTreasure(FishTrapEntry entry)
	{
		addCatchToGroup(entry, "treasure");
	}

	@Deprecated
	public void addTrapJunk(FishTrapEntry entry)
	{
		addCatchToGroup(entry, "junk");
	}

	@Deprecated
	public ItemStack getFishList(World world)
	{
		return getRandomCatchFromGroup(world.rand, "fish");
	}

	@Deprecated
	public ItemStack getTreasureList(World world)
	{
		return getRandomCatchFromGroup(world.rand, "treasure");
	}

	@Deprecated
	public ItemStack getJunkList(World world)
	{
		return getRandomCatchFromGroup(world.rand, "junk");
	}
}
