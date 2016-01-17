/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 IceDragon200
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
package growthcraft.cellar.integration;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.core.log.ILoggable;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;
import growthcraft.cellar.common.block.BlockFluidBooze;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.common.item.ItemBoozeBottle;
import growthcraft.cellar.common.item.ItemBucketBooze;
import growthcraft.core.common.definition.ItemDefinition;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspect;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

public class ThaumcraftBoozeHelper implements ILoggable
{
	// An AspectModifier is similar to a AspectList, however, it accepts and
	// retains negative Aspect counts
	public static class AspectModifier implements Iterable<Map.Entry<Aspect, Integer>>
	{
		private Map<Aspect, Integer> aspects = new HashMap<Aspect, Integer>();

		public AspectModifier() {}

		public AspectModifier set(Aspect aspect, int i)
		{
			aspects.put(aspect, i);
			return this;
		}

		public Iterator<Map.Entry<Aspect, Integer>> iterator()
		{
			return aspects.entrySet().iterator();
		}
	}

	private static ThaumcraftBoozeHelper INSTANCE;
	private ILogger logger = NullLogger.INSTANCE;
	private final Map<BoozeTag, AspectModifier> tagToAspects;

	public ThaumcraftBoozeHelper()
	{
		this.tagToAspects = new HashMap<BoozeTag, AspectModifier>();
		tagToAspects.put(BoozeTag.FERMENTED, new AspectModifier().set(Aspect.POISON, 1).set(Aspect.WATER, -1));
		tagToAspects.put(BoozeTag.EXTENDED, new AspectModifier().set(Aspect.ENERGY, 1).set(Aspect.WATER, -1));
		tagToAspects.put(BoozeTag.POTENT, new AspectModifier().set(Aspect.POISON, 1).set(Aspect.WATER, -1));
		tagToAspects.put(BoozeTag.HYPER_EXTENDED, new AspectModifier().set(Aspect.POISON, 1).set(Aspect.ENERGY, 1).set(Aspect.WATER, -2));
		tagToAspects.put(BoozeTag.DEADLY, new AspectModifier().set(Aspect.DEATH, 1));
		tagToAspects.put(BoozeTag.POISONED, new AspectModifier().set(Aspect.POISON, 3).set(Aspect.WATER, -3));
		tagToAspects.put(BoozeTag.CHILLED, new AspectModifier().set(Aspect.COLD, 1).set(Aspect.WATER, -1));
		tagToAspects.put(BoozeTag.INTOXICATED, new AspectModifier().set(Aspect.POISON, 2).set(Aspect.WATER, -3));
	}

	public void setLogger(ILogger l)
	{
		this.logger = l;
	}

	public AspectList setAspectsForBooze(Fluid booze, AspectList aspects)
	{
		final Collection<BoozeTag> tags = CellarRegistry.instance().booze().getTags(booze);
		for (BoozeTag tag : tags)
		{
			final AspectModifier mod = tagToAspects.get(tag);
			if (mod != null)
			{
				for (Map.Entry<Aspect, Integer> asp : mod)
				{
					aspects.add(asp.getKey(), asp.getValue());
				}
			}
			else
			{
				logger.error("AspectModifier %s not found!", tag);
			}
		}
		return aspects;
	}

	public AspectList setAspectsForBoozeBucketContent(Fluid booze, AspectList aspects)
	{
		return setAspectsForBooze(booze, aspects.add(Aspect.WATER, 4));
	}

	public AspectList setAspectsForBoozeBottleContent(Fluid booze, AspectList aspects)
	{
		return setAspectsForBooze(booze, aspects.add(Aspect.WATER, 2));
	}

	public AspectList setAspectsForBoozeBucket(Fluid booze, AspectList list)
	{
		list.add(Aspect.METAL, 3);
		return setAspectsForBoozeBucketContent(booze, list);
	}

	public AspectList setAspectsForBoozeBottle(Fluid booze, AspectList list)
	{
		list.add(Aspect.CRYSTAL, 1);
		return setAspectsForBoozeBottleContent(booze, list);
	}

	public void registerAspectsForBucket(ItemBucketBoozeDefinition def, AspectList base)
	{
		final ItemBucketBooze bucket = def.getItem();
		ThaumcraftApi.registerObjectTag(def.asStack(), setAspectsForBoozeBucket(bucket.getBooze(null), base.copy()));
	}

	public void registerAspectsForBuckets(ItemBucketBoozeDefinition[] buckets, AspectList base)
	{
		for (ItemBucketBoozeDefinition bucket : buckets) registerAspectsForBucket(bucket, base);
	}

	public void registerAspectsForFluidBlock(BlockBoozeDefinition def, AspectList base)
	{
		final BlockFluidBooze block = def.getBlock();
		ThaumcraftApi.registerObjectTag(def.asStack(), setAspectsForBoozeBucket(block.getFluid(), base.copy()));
	}

	public void registerAspectsForFluidBlocks(BlockBoozeDefinition[] blocks, AspectList base)
	{
		for (BlockBoozeDefinition block : blocks) registerAspectsForFluidBlock(block, base);
	}

	public void registerAspectsForBottleStack(ItemStack stack, AspectList base)
	{
		final ItemBoozeBottle bottle = (ItemBoozeBottle)stack.getItem();
		ThaumcraftApi.registerObjectTag(stack, setAspectsForBoozeBottle(bottle.getBoozeForStack(stack), base.copy()));
	}

	public void registerAspectsForBottle(ItemDefinition def, AspectList base)
	{
		if (def.getItem() instanceof ItemBoozeBottle)
		{
			final ItemBoozeBottle bottle = (ItemBoozeBottle)def.getItem();
			final List<ItemStack> subtypes = new ArrayList<ItemStack>();
			bottle.getSubItems(bottle, null, subtypes);
			for (ItemStack stack : subtypes)
			{
				ThaumcraftApi.registerObjectTag(stack, setAspectsForBoozeBottle(bottle.getBoozeForStack(stack), base.copy()));
			}
		}
	}

	public static ThaumcraftBoozeHelper instance()
	{
		if (INSTANCE == null)
		{
			INSTANCE = new ThaumcraftBoozeHelper();
		}
		return INSTANCE;
	}
}
