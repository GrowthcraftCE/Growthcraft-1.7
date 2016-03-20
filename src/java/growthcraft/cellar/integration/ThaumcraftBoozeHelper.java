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
package growthcraft.cellar.integration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.core.CoreRegistry;
import growthcraft.api.core.fluids.FluidTag;
import growthcraft.api.core.item.ItemKey;
import growthcraft.api.core.log.ILoggable;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;
import growthcraft.core.common.definition.BlockTypeDefinition;
import growthcraft.core.common.definition.ItemTypeDefinition;
import growthcraft.core.common.item.IFluidItem;
import growthcraft.core.integration.thaumcraft.AspectsHelper;
import growthcraft.core.util.FluidFactory;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.ThaumcraftApi;

import cpw.mods.fml.common.Optional;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.BlockFluidBase;

public class ThaumcraftBoozeHelper implements ILoggable
{
	// An AspectModifier is similar to a AspectList, however, it accepts and
	// retains negative Aspect counts
	public static class AspectModifier implements Iterable<Map.Entry<Aspect, Integer>>
	{
		private Map<Aspect, Integer> aspects = new HashMap<Aspect, Integer>();

		public AspectModifier() {}

		@Optional.Method(modid="Thaumcraft")
		public AspectModifier set(Aspect aspect, int i)
		{
			aspects.put(aspect, i);
			return this;
		}

		@Optional.Method(modid="Thaumcraft")
		public Iterator<Map.Entry<Aspect, Integer>> iterator()
		{
			return aspects.entrySet().iterator();
		}
	}

	private static ThaumcraftBoozeHelper INSTANCE;
	private ILogger logger = NullLogger.INSTANCE;
	private final Map<FluidTag, AspectModifier> tagToAspects;

	public ThaumcraftBoozeHelper()
	{
		this.tagToAspects = new HashMap<FluidTag, AspectModifier>();
		tagToAspects.put(BoozeTag.FERMENTED, new AspectModifier().set(Aspect.POISON, 1).set(Aspect.WATER, -1));
		tagToAspects.put(BoozeTag.EXTENDED, new AspectModifier().set(Aspect.ENERGY, 1).set(Aspect.WATER, -1));
		tagToAspects.put(BoozeTag.POTENT, new AspectModifier().set(Aspect.POISON, 1).set(Aspect.WATER, -1));
		tagToAspects.put(BoozeTag.HYPER_EXTENDED, new AspectModifier().set(Aspect.POISON, 1).set(Aspect.ENERGY, 1).set(Aspect.WATER, -2));
		tagToAspects.put(BoozeTag.DEADLY, new AspectModifier().set(Aspect.DEATH, 1));
		tagToAspects.put(BoozeTag.POISONED, new AspectModifier().set(Aspect.POISON, 3).set(Aspect.WATER, -3));
		tagToAspects.put(BoozeTag.CHILLED, new AspectModifier().set(Aspect.COLD, 1).set(Aspect.WATER, -1));
		tagToAspects.put(BoozeTag.INTOXICATED, new AspectModifier().set(Aspect.POISON, 2).set(Aspect.WATER, -3));
	}

	@Override
	public void setLogger(@Nonnull ILogger l)
	{
		this.logger = l;
	}

	@Optional.Method(modid="Thaumcraft")
	public AspectList setAspectsForFluid(Fluid fluid, AspectList aspects)
	{
		final Collection<FluidTag> tags = CoreRegistry.instance().fluidDictionary().getFluidTags(fluid);
		for (FluidTag tag : tags)
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

	@Optional.Method(modid="Thaumcraft")
	public AspectList setAspectsForFluidBucketContent(Fluid fluid, AspectList aspects)
	{
		return setAspectsForFluid(fluid, aspects.add(Aspect.WATER, 4));
	}

	@Optional.Method(modid="Thaumcraft")
	public AspectList setAspectsForFluidBottleContent(Fluid fluid, AspectList aspects)
	{
		return setAspectsForFluid(fluid, aspects.add(Aspect.WATER, 2));
	}

	@Optional.Method(modid="Thaumcraft")
	public AspectList setAspectsForFluidBucket(Fluid fluid, AspectList list)
	{
		list.add(Aspect.METAL, 3);
		return setAspectsForFluidBucketContent(fluid, list);
	}

	@Optional.Method(modid="Thaumcraft")
	public AspectList setAspectsForFluidBottle(Fluid fluid, AspectList list)
	{
		list.add(Aspect.CRYSTAL, 1);
		return setAspectsForFluidBottleContent(fluid, list);
	}

	@Optional.Method(modid="Thaumcraft")
	public void registerAspectsForBucket(ItemTypeDefinition<? extends IFluidItem> def, AspectList base)
	{
		final ItemStack stack = def.asStack(1, ItemKey.WILDCARD_VALUE);
		final Item item = stack.getItem();
		if (item instanceof IFluidItem)
		{
			final IFluidItem bucket = (IFluidItem)item;
			ThaumcraftApi.registerObjectTag(stack, setAspectsForFluidBucket(bucket.getFluid(stack), base.copy()));
		}
	}

	@Optional.Method(modid="Thaumcraft")
	public void registerAspectsForBuckets(ItemTypeDefinition<? extends IFluidItem>[] buckets, AspectList base)
	{
		for (ItemTypeDefinition<? extends IFluidItem> bucket : buckets) registerAspectsForBucket(bucket, base);
	}

	@Optional.Method(modid="Thaumcraft")
	public void registerAspectsForFluidBlock(BlockTypeDefinition<? extends BlockFluidBase> def, AspectList base)
	{
		final Block block = def.getBlock();
		if (block instanceof BlockFluidBase)
		{
			ThaumcraftApi.registerObjectTag(def.asStack(1, ItemKey.WILDCARD_VALUE), setAspectsForFluidBucket(((BlockFluidBase)block).getFluid(), base.copy()));
		}
	}

	@Optional.Method(modid="Thaumcraft")
	public void registerAspectsForFluidBlocks(BlockTypeDefinition<? extends BlockFluidBase>[] blocks, AspectList base)
	{
		for (BlockTypeDefinition<? extends BlockFluidBase> block : blocks) registerAspectsForFluidBlock(block, base);
	}

	@Optional.Method(modid="Thaumcraft")
	public void registerAspectsForBottleStack(ItemStack stack, AspectList base)
	{
		final Item item = stack.getItem();
		if (item instanceof IFluidItem)
		{
			final IFluidItem fluidItem = (IFluidItem)item;
			final List<ItemStack> subtypes = new ArrayList<ItemStack>();
			item.getSubItems(item, null, subtypes);
			for (ItemStack subStack : subtypes)
			{
				ThaumcraftApi.registerObjectTag(subStack, setAspectsForFluidBottle(fluidItem.getFluid(subStack), base.copy()));
			}
		}
	}

	@Optional.Method(modid="Thaumcraft")
	public void registerAspectsForBottle(ItemTypeDefinition<? extends IFluidItem> def, AspectList base)
	{
		final ItemStack stack = def.asStack(1, ItemKey.WILDCARD_VALUE);
		registerAspectsForBottleStack(stack, base);
	}

	@Optional.Method(modid="Thaumcraft")
	public void registerAspectsForFluidDetails(FluidFactory.FluidDetails details, AspectList base, Aspect... aspects)
	{
		if (details.bottle != null)
		{
			registerAspectsForBottle(details.bottle, base.copy());
		}

		if (details.foodBottle != null)
		{
			registerAspectsForBottle(details.foodBottle, base.copy());
		}

		if (details.bucket != null)
		{
			registerAspectsForBucket(details.bucket, AspectsHelper.scaleAspects(base.copy(), 3, aspects));
		}

		if (details.block != null)
		{
			registerAspectsForFluidBlock(details.block, AspectsHelper.scaleAspects(base.copy(), 3, aspects));
		}
	}

	@Optional.Method(modid="Thaumcraft")
	public static ThaumcraftBoozeHelper instance()
	{
		if (INSTANCE == null)
		{
			INSTANCE = new ThaumcraftBoozeHelper();
		}
		return INSTANCE;
	}
}
