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

import growthcraft.cellar.integration.ThaumcraftBoozeHelper;
import growthcraft.core.integration.thaumcraft.AspectsHelper;
import growthcraft.core.integration.ThaumcraftModuleBase;
import growthcraft.grapes.GrowthCraftGrapes;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspect;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.FMLInterModComms;

public class ThaumcraftModule extends ThaumcraftModuleBase
{
	public ThaumcraftModule()
	{
		super(GrowthCraftGrapes.MOD_ID);
	}

	@Override
	@Optional.Method(modid="Thaumcraft")
	protected void integrate()
	{
		FMLInterModComms.sendMessage("Thaumcraft", "harvestStandardCrop", GrowthCraftGrapes.grapeBlock.asStack());

		ThaumcraftApi.registerObjectTag(GrowthCraftGrapes.grapeSeeds.asStack(), new AspectList().add(Aspect.PLANT, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftGrapes.grapes.asStack(), new AspectList().add(Aspect.CROP, 1).add(Aspect.HUNGER, 1));

		final AspectList[] common = new AspectList[]
		{
			// 0
			new AspectList(),
			// 1
			new AspectList().add(Aspect.ARMOR, 1),
			// 2
			new AspectList().add(Aspect.ARMOR, 2),
			// 3
			new AspectList().add(Aspect.ARMOR, 1),
			// 4
			new AspectList().add(Aspect.ARMOR, 2).add(Aspect.HEAL, 1),
			// 5
			new AspectList().add(Aspect.ARMOR, 3),
			// 6
			new AspectList().add(Aspect.ARMOR, 1).add(Aspect.POISON, 1),
			// 7
			new AspectList().add(Aspect.POISON, 2),
		};

		for (int i = 0; i < common.length; ++i)
		{
			final AspectList list = common[i];
			ThaumcraftBoozeHelper.instance().registerAspectsForBottleStack(GrowthCraftGrapes.fluids.grapeWine.asStack(1, i), list.copy());
			ThaumcraftBoozeHelper.instance().registerAspectsForBucket(GrowthCraftGrapes.fluids.grapeWineBuckets[i], AspectsHelper.scaleAspects(list.copy(), 3, Aspect.ARMOR, Aspect.HEAL));
			ThaumcraftBoozeHelper.instance().registerAspectsForFluidBlock(GrowthCraftGrapes.fluids.grapeWineFluids[i], AspectsHelper.scaleAspects(list.copy(), 3, Aspect.ARMOR, Aspect.HEAL));
		}
	}
}
