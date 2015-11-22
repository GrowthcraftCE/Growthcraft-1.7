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
package growthcraft.rice.integration;

import growthcraft.rice.GrowthCraftRice;
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
		super(GrowthCraftRice.MOD_ID);
	}

	@Override
	protected void integrate()
	{
		FMLInterModComms.sendMessage("Thaumcraft", "harvestStandardCrop", GrowthCraftRice.riceBlock.asStack(1, 7));

		ThaumcraftApi.registerObjectTag(GrowthCraftRice.rice.asStack(), new AspectList().add(Aspect.CROP, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftRice.riceBall.asStack(), new AspectList().add(Aspect.ENTROPY, 1).add(Aspect.PLANT, 1).add(Aspect.CRAFT, 1).add(Aspect.HUNGER, 1));

		ThaumcraftBoozeHelper.registerAspectsForBottleStack(GrowthCraftRice.booze.riceSake.asStack(1, 0), new AspectList());
		ThaumcraftBoozeHelper.registerAspectsForBottleStack(GrowthCraftRice.booze.riceSake.asStack(1, 1), new AspectList().add(Aspect.FLIGHT, 1));
		ThaumcraftBoozeHelper.registerAspectsForBottleStack(GrowthCraftRice.booze.riceSake.asStack(1, 2), new AspectList().add(Aspect.FLIGHT, 2));
		ThaumcraftBoozeHelper.registerAspectsForBottleStack(GrowthCraftRice.booze.riceSake.asStack(1, 3), new AspectList().add(Aspect.FLIGHT, 1));

		ThaumcraftBoozeHelper.registerAspectsForBucket(GrowthCraftRice.booze.riceSakeBuckets[0], new AspectList());
		ThaumcraftBoozeHelper.registerAspectsForBucket(GrowthCraftRice.booze.riceSakeBuckets[1], new AspectList().add(Aspect.FLIGHT, 3));
		ThaumcraftBoozeHelper.registerAspectsForBucket(GrowthCraftRice.booze.riceSakeBuckets[2], new AspectList().add(Aspect.FLIGHT, 6));
		ThaumcraftBoozeHelper.registerAspectsForBucket(GrowthCraftRice.booze.riceSakeBuckets[3], new AspectList().add(Aspect.FLIGHT, 3));

		ThaumcraftBoozeHelper.registerAspectsForFluidBlock(GrowthCraftRice.booze.riceSakeFluids[0], new AspectList());
		ThaumcraftBoozeHelper.registerAspectsForFluidBlock(GrowthCraftRice.booze.riceSakeFluids[1], new AspectList().add(Aspect.FLIGHT, 3));
		ThaumcraftBoozeHelper.registerAspectsForFluidBlock(GrowthCraftRice.booze.riceSakeFluids[2], new AspectList().add(Aspect.FLIGHT, 6));
		ThaumcraftBoozeHelper.registerAspectsForFluidBlock(GrowthCraftRice.booze.riceSakeFluids[3], new AspectList().add(Aspect.FLIGHT, 3));
	}
}
