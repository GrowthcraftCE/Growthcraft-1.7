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

import growthcraft.api.cellar.CellarRegistry;
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

public class ThaumcraftBoozeHelper
{
	private ThaumcraftBoozeHelper() {}

	public static AspectList setAspectsForBoozeBucketContent(Fluid booze, AspectList aspects)
	{
		final Collection<String> tags = CellarRegistry.instance().booze().getTags(booze);
		if (tags != null)
		{
			// Fermented booze always contains poison
			if (tags.contains("fermented"))
			{
				// Potent has a balance of water and poison
				if (tags.contains("potent"))
				{
					aspects.add(Aspect.WATER, 2).add(Aspect.POISON, 2);
				}
				// extended has less poison, and an energy
				else if (tags.contains("extended"))
				{
					aspects.add(Aspect.WATER, 2).add(Aspect.POISON, 1).add(Aspect.ENERGY, 1);
				}
				// hyper extended is pretty much potented + extended
				else if (tags.contains("hyper-extended"))
				{
					aspects.add(Aspect.WATER, 1).add(Aspect.POISON, 2).add(Aspect.ENERGY, 1);
				}
				// intoxicated is heavily poisoned
				else if (tags.contains("intoxicated"))
				{
					aspects.add(Aspect.WATER, 1).add(Aspect.POISON, 3);
				}
				// poisoned, is well, POISONED.
				else if (tags.contains("poisoned"))
				{
					aspects.add(Aspect.POISON, 4);
				}
				// otherwise it has just a bit of poison
				else
				{
					aspects.add(Aspect.WATER, 3).add(Aspect.POISON, 1);
				}
			}
			else
			{
				aspects.add(Aspect.WATER, 4);
			}
		}
		return aspects;
	}

	public static AspectList setAspectsForBoozeBottleContent(Fluid booze, AspectList aspects)
	{
		final Collection<String> tags = CellarRegistry.instance().booze().getTags(booze);
		if (tags != null)
		{
			// Fermented booze always contains poison
			if (tags.contains("fermented"))
			{
				// Potent has a balance of water and poison
				if (tags.contains("potent"))
				{
					aspects.add(Aspect.WATER, 1).add(Aspect.POISON, 2);
				}
				// extended has less poison, and an energy
				else if (tags.contains("extended"))
				{
					aspects.add(Aspect.WATER, 1).add(Aspect.POISON, 1).add(Aspect.ENERGY, 1);
				}
				// hyper extended is pretty much potented + extended
				else if (tags.contains("hyper-extended"))
				{
					aspects.add(Aspect.WATER, 1).add(Aspect.POISON, 2).add(Aspect.ENERGY, 1);
				}
				// intoxicated is heavily poisoned
				else if (tags.contains("intoxicated"))
				{
					aspects.add(Aspect.WATER, 1).add(Aspect.POISON, 3);
				}
				// poisoned, is well, POISONED.
				else if (tags.contains("poisoned"))
				{
					aspects.add(Aspect.POISON, 4);
				}
				// otherwise it has just a bit of poison
				else
				{
					aspects.add(Aspect.WATER, 1).add(Aspect.POISON, 1);
				}
			}
			else
			{
				aspects.add(Aspect.WATER, 1);
			}
		}
		return aspects;
	}

	public static AspectList setAspectsForBoozeBucket(Fluid booze, AspectList list)
	{
		list.add(Aspect.METAL, 3);
		return setAspectsForBoozeBucketContent(booze, list);
	}

	public static AspectList setAspectsForBoozeBottle(Fluid booze, AspectList list)
	{
		list.add(Aspect.CRYSTAL, 1);
		return setAspectsForBoozeBottleContent(booze, list);
	}

	public static void registerAspectsForBucket(ItemBucketBoozeDefinition def, AspectList base)
	{
		final ItemBucketBooze bucket = def.getItem();
		ThaumcraftApi.registerObjectTag(def.asStack(), setAspectsForBoozeBucket(bucket.getBooze(), base.copy()));
	}

	public static void registerAspectsForBuckets(ItemBucketBoozeDefinition[] buckets, AspectList base)
	{
		for (ItemBucketBoozeDefinition bucket : buckets) registerAspectsForBucket(bucket, base);
	}

	public static void registerAspectsForFluidBlock(BlockBoozeDefinition def, AspectList base)
	{
		final BlockFluidBooze block = def.getBlock();
		ThaumcraftApi.registerObjectTag(def.asStack(), setAspectsForBoozeBucket(block.getFluid(), base.copy()));
	}

	public static void registerAspectsForFluidBlocks(BlockBoozeDefinition[] blocks, AspectList base)
	{
		for (BlockBoozeDefinition block : blocks) registerAspectsForFluidBlock(block, base);
	}

	public static void registerAspectsForBottleStack(ItemStack stack, AspectList base)
	{
		final ItemBoozeBottle bottle = (ItemBoozeBottle)stack.getItem();
		ThaumcraftApi.registerObjectTag(stack, setAspectsForBoozeBottle(bottle.getBoozeForStack(stack), base.copy()));
	}

	public static void registerAspectsForBottle(ItemDefinition def, AspectList base)
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
}
