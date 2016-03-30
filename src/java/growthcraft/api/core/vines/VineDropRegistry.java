/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015, 2016 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.api.core.vines;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;

public class VineDropRegistry implements IVineDropRegistry
{
	private final Set<IVineEntry> vines = new HashSet<IVineEntry>();
	private final List<VineDropEntry> vineDrops = new ArrayList<VineDropEntry>();
	private ILogger logger = NullLogger.INSTANCE;

	@Override
	public void setLogger(@Nonnull ILogger l)
	{
		this.logger = l;
	}

	/**
	 * @return vine drop list
	 */
	public List<VineDropEntry> getVineDropsList()
	{
		return vineDrops;
	}

	@Override
	public void addVineEntry(@Nonnull IVineEntry entry)
	{
		vines.add(entry);
	}

	@Override
	public void addVineEntry(@Nonnull Block block, int meta)
	{
		addVineEntry(new VineEntry(block, meta));
	}

	@Override
	public boolean isVine(@Nullable Block block, int meta)
	{
		for (IVineEntry entry : vines)
		{
			if (entry.matches(block, meta)) return true;
		}
		return false;
	}

	@Override
	public void addDropEntry(@Nonnull VineDropEntry entry)
	{
		vineDrops.add(entry);
	}

	/**
	 * Adds a drop to vines.
	 *
	 * @param item   - The item/block to be added.
	 * @param weight - Weight. Used for randoming. Higher numbers means lesser chance.
	 */
	@Override
	public void addDropEntry(@Nonnull ItemStack item, int weight)
	{
		if (weight <= 0)
		{
			logger.warn("Weight was set to 0 for item {%s}, please fix this by setting it to 1 or greater.", item);
			weight = 1;
		}
		addDropEntry(new VineDropEntry(item, weight));
	}

	/**
	 * @return true if their are any vine drops, false otherwise
	 */
	@Override
	public boolean hasVineDrops()
	{
		return !getVineDropsList().isEmpty();
	}

	/**
	 * @param world - The world
	 * @return itemstack or null
	 */
	@Override
	public ItemStack getVineDropItem(@Nonnull World world)
	{
		final List<VineDropEntry> vineEntries = getVineDropsList();
		if (vineEntries.isEmpty()) return null;

		final VineDropEntry entry = (VineDropEntry)WeightedRandom.getRandomItem(world.rand, vineEntries);
		if (entry == null || entry.getItemStack() == null) return null;

		return entry.getItemStack().copy();
	}
}
