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
package growthcraft.grapes.integration;

import growthcraft.grapes.GrowthCraftGrapes;
import growthcraft.cellar.integration.ThaumcraftBoozeHelper;
import growthcraft.core.integration.ThaumcraftModuleBase;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspect;

import cpw.mods.fml.common.event.FMLInterModComms;

public class ThaumcraftModule extends ThaumcraftModuleBase
{
	public ThaumcraftModule()
	{
		super(GrowthCraftGrapes.MOD_ID);
	}

	@Override
	protected void integrate()
	{
		FMLInterModComms.sendMessage("Thaumcraft", "harvestStandardCrop", GrowthCraftGrapes.grapeBlock.asStack());

		ThaumcraftApi.registerObjectTag(GrowthCraftGrapes.grapeSeeds.asStack(), new AspectList().add(Aspect.PLANT, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftGrapes.grapes.asStack(), new AspectList().add(Aspect.CROP, 1).add(Aspect.HUNGER, 1));

		ThaumcraftBoozeHelper.registerAspectsForBottleStack(GrowthCraftGrapes.grapeWine.asStack(1, 0), new AspectList());
		ThaumcraftBoozeHelper.registerAspectsForBottleStack(GrowthCraftGrapes.grapeWine.asStack(1, 1), new AspectList().add(Aspect.ARMOR, 1));
		ThaumcraftBoozeHelper.registerAspectsForBottleStack(GrowthCraftGrapes.grapeWine.asStack(1, 2), new AspectList().add(Aspect.ARMOR, 2));
		ThaumcraftBoozeHelper.registerAspectsForBottleStack(GrowthCraftGrapes.grapeWine.asStack(1, 3), new AspectList().add(Aspect.ARMOR, 1));

		ThaumcraftBoozeHelper.registerAspectsForBucket(GrowthCraftGrapes.grapeWineBuckets[0], new AspectList());
		ThaumcraftBoozeHelper.registerAspectsForBucket(GrowthCraftGrapes.grapeWineBuckets[1], new AspectList().add(Aspect.ARMOR, 3));
		ThaumcraftBoozeHelper.registerAspectsForBucket(GrowthCraftGrapes.grapeWineBuckets[2], new AspectList().add(Aspect.ARMOR, 6));
		ThaumcraftBoozeHelper.registerAspectsForBucket(GrowthCraftGrapes.grapeWineBuckets[3], new AspectList().add(Aspect.ARMOR, 3));

		ThaumcraftBoozeHelper.registerAspectsForFluidBlock(GrowthCraftGrapes.grapeWineFluids[0], new AspectList());
		ThaumcraftBoozeHelper.registerAspectsForFluidBlock(GrowthCraftGrapes.grapeWineFluids[1], new AspectList().add(Aspect.ARMOR, 3));
		ThaumcraftBoozeHelper.registerAspectsForFluidBlock(GrowthCraftGrapes.grapeWineFluids[2], new AspectList().add(Aspect.ARMOR, 6));
		ThaumcraftBoozeHelper.registerAspectsForFluidBlock(GrowthCraftGrapes.grapeWineFluids[3], new AspectList().add(Aspect.ARMOR, 3));
	}
}
