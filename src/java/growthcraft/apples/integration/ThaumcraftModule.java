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
package growthcraft.apples.integration;

import growthcraft.apples.GrowthCraftApples;
import growthcraft.cellar.integration.ThaumcraftBoozeHelper;
import growthcraft.core.integration.thaumcraft.AspectsHelper;
import growthcraft.core.integration.ThaumcraftModuleBase;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspect;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.Optional;

public class ThaumcraftModule extends ThaumcraftModuleBase
{
	public ThaumcraftModule()
	{
		super(GrowthCraftApples.MOD_ID);
	}

	@Override
	@Optional.Method(modid="Thaumcraft")
	protected void integrate()
	{
		FMLInterModComms.sendMessage(modID, "harvestStandardCrop", GrowthCraftApples.blocks.appleBlock.asStack(1, 2));

		ThaumcraftApi.registerObjectTag(GrowthCraftApples.blocks.appleSapling.asStack(), new AspectList().add(Aspect.PLANT, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftApples.items.appleSeeds.asStack(), new AspectList().add(Aspect.PLANT, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftApples.blocks.appleLeaves.asStack(), new AspectList().add(Aspect.PLANT, 1));

		final AspectList[] common = new AspectList[]
		{
			new AspectList(),
			new AspectList().add(Aspect.HEAL, 1),
			new AspectList().add(Aspect.HEAL, 2),
			new AspectList().add(Aspect.HEAL, 1),
			new AspectList().add(Aspect.EXCHANGE, 1),
			new AspectList().add(Aspect.HEAL, 1).add(Aspect.POISON, 1),
			new AspectList().add(Aspect.POISON, 1)
		};

		for (int i = 0; i < common.length; ++i)
		{
			final AspectList list = common[i];
			ThaumcraftBoozeHelper.instance().registerAspectsForBottleStack(GrowthCraftApples.fluids.appleCider.asStack(1, i), list.copy());
			ThaumcraftBoozeHelper.instance().registerAspectsForBucket(GrowthCraftApples.fluids.appleCiderBuckets[i], AspectsHelper.scaleAspects(list.copy(), 3, Aspect.HEAL, Aspect.EXCHANGE));
			ThaumcraftBoozeHelper.instance().registerAspectsForFluidBlock(GrowthCraftApples.fluids.appleCiderFluids[i], AspectsHelper.scaleAspects(list.copy(), 3, Aspect.HEAL, Aspect.EXCHANGE));
		}
	}
}
