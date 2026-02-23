/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 IceDragon200
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
package growthcraft.milk.integration;

import growthcraft.api.core.item.ItemKey;
import growthcraft.cellar.integration.ThaumcraftBoozeHelper;
import growthcraft.core.integration.thaumcraft.AspectsHelper;
import growthcraft.core.integration.ThaumcraftModuleBase;
import growthcraft.milk.GrowthCraftMilk;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspect;

import cpw.mods.fml.common.Optional;

public class ThaumcraftModule extends ThaumcraftModuleBase
{
	public ThaumcraftModule()
	{
		super(GrowthCraftMilk.MOD_ID);
	}

	@Override
	@Optional.Method(modid="Thaumcraft")
	protected void integrate()
	{
		/**
		 * Blocks
		 */
		ThaumcraftApi.registerObjectTag(GrowthCraftMilk.blocks.butterChurn.asStack(1, ItemKey.WILDCARD_VALUE), new AspectList().add(Aspect.TREE, 4).add(Aspect.TOOL, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftMilk.blocks.cheeseBlock.asStack(1, ItemKey.WILDCARD_VALUE), new AspectList().add(Aspect.SLIME, 2));
		ThaumcraftApi.registerObjectTag(GrowthCraftMilk.blocks.cheesePress.asStack(1, ItemKey.WILDCARD_VALUE), new AspectList().add(Aspect.TREE, 4).add(Aspect.METAL, 5).add(Aspect.TOOL, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftMilk.blocks.cheeseVat.asStack(1, ItemKey.WILDCARD_VALUE), new AspectList().add(Aspect.METAL, 7).add(Aspect.TOOL, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftMilk.blocks.hangingCurds.asStack(1, ItemKey.WILDCARD_VALUE), new AspectList().add(Aspect.SLIME, 2));
		ThaumcraftApi.registerObjectTag(GrowthCraftMilk.blocks.pancheon.asStack(1, ItemKey.WILDCARD_VALUE), new AspectList().add(Aspect.EARTH, 2).add(Aspect.TOOL, 1).add(Aspect.WATER, 1));
		if (GrowthCraftMilk.blocks.thistle != null)
		{
			ThaumcraftApi.registerObjectTag(GrowthCraftMilk.blocks.thistle.asStack(1, ItemKey.WILDCARD_VALUE), new AspectList().add(Aspect.PLANT, 1));
		}

		/**
		 * Items
		 */
		ThaumcraftApi.registerObjectTag(GrowthCraftMilk.items.butter.asStack(1, ItemKey.WILDCARD_VALUE), new AspectList().add(Aspect.SLIME, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftMilk.items.cheese.asStack(1, ItemKey.WILDCARD_VALUE), new AspectList().add(Aspect.SLIME, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftMilk.items.cheeseCloth.asStack(1, ItemKey.WILDCARD_VALUE), new AspectList().add(Aspect.SLIME, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftMilk.items.iceCream.asStack(1, ItemKey.WILDCARD_VALUE), new AspectList().add(Aspect.COLD, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftMilk.items.starterCulture.asStack(1, ItemKey.WILDCARD_VALUE), new AspectList().add(Aspect.ENERGY, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftMilk.items.stomach.asStack(1, ItemKey.WILDCARD_VALUE), new AspectList().add(Aspect.FLESH, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftMilk.items.yogurt.asStack(1, ItemKey.WILDCARD_VALUE), new AspectList().add(Aspect.SLIME, 1));

		/**
		 * Booze - Kumis
		 */
		final AspectList[] common = new AspectList[]
		{
			new AspectList().add(Aspect.HEAL, 1),
			new AspectList().add(Aspect.HEAL, 2),
			new AspectList().add(Aspect.HEAL, 1),
			new AspectList().add(Aspect.HEAL, 2),
			new AspectList().add(Aspect.HEAL, 2).add(Aspect.POISON, 1),
			new AspectList().add(Aspect.POISON, 3)
		};

		for (int i = 0; i < common.length; ++i)
		{
			final AspectList list = common[i];
			ThaumcraftBoozeHelper.instance().registerAspectsForBottleStack(GrowthCraftMilk.fluids.kumisBottle.asStack(1, i), list.copy());
			ThaumcraftBoozeHelper.instance().registerAspectsForBucket(GrowthCraftMilk.fluids.kumisFluidBuckets[i], AspectsHelper.scaleAspects(list.copy(), 3, Aspect.HEAL));
			ThaumcraftBoozeHelper.instance().registerAspectsForFluidBlock(GrowthCraftMilk.fluids.kumisFluidBlocks[i], AspectsHelper.scaleAspects(list.copy(), 3, Aspect.HEAL));
		}

		/**
		 * Other Fluids
		 */
		ThaumcraftBoozeHelper.instance().registerAspectsForFluidDetails(GrowthCraftMilk.fluids.butterMilk, new AspectList());
		ThaumcraftBoozeHelper.instance().registerAspectsForFluidDetails(GrowthCraftMilk.fluids.cream, new AspectList());
		ThaumcraftBoozeHelper.instance().registerAspectsForFluidDetails(GrowthCraftMilk.fluids.milk, new AspectList());
		ThaumcraftBoozeHelper.instance().registerAspectsForFluidDetails(GrowthCraftMilk.fluids.curds, new AspectList());
		ThaumcraftBoozeHelper.instance().registerAspectsForFluidDetails(GrowthCraftMilk.fluids.rennet, new AspectList());
		ThaumcraftBoozeHelper.instance().registerAspectsForFluidDetails(GrowthCraftMilk.fluids.skimMilk, new AspectList());
		ThaumcraftBoozeHelper.instance().registerAspectsForFluidDetails(GrowthCraftMilk.fluids.whey, new AspectList());
		ThaumcraftBoozeHelper.instance().registerAspectsForFluidDetails(GrowthCraftMilk.fluids.pasteurizedMilk, new AspectList());
	}
}
