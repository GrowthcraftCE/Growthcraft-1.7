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
import growthcraft.core.integration.ThaumcraftModuleBase;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspect;

import cpw.mods.fml.common.event.FMLInterModComms;

public class ThaumcraftModule extends ThaumcraftModuleBase
{
	public ThaumcraftModule()
	{
		super(GrowthCraftApples.MOD_ID);
	}

	@Override
	protected void integrate()
	{
		FMLInterModComms.sendMessage("Thaumcraft", "harvestStandardCrop", GrowthCraftApples.appleBlock.asStack(1, 2));

		ThaumcraftApi.registerObjectTag(GrowthCraftApples.appleSeeds.asStack(), new AspectList().add(Aspect.PLANT, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftApples.appleSapling.asStack(), new AspectList().add(Aspect.TREE, 1).add(Aspect.PLANT, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftApples.appleLeaves.asStack(), new AspectList().add(Aspect.PLANT, 1));

		for (int i = 0; i < GrowthCraftApples.appleCiderBooze.length; ++i)
		{
			if (i == 0 || i == 4)
			{
				ThaumcraftApi.registerObjectTag(GrowthCraftApples.appleCider.asStack(1, i), new AspectList().add(Aspect.HUNGER, 2).add(Aspect.WATER, 1).add(Aspect.CRYSTAL, 1));
				ThaumcraftApi.registerObjectTag(GrowthCraftApples.appleCiderBucket_deprecated.asStack(1, i), new AspectList().add(Aspect.WATER, 2));
				ThaumcraftApi.registerObjectTag(GrowthCraftApples.appleCiderBuckets[i].asStack(1), new AspectList().add(Aspect.WATER, 2));
			}
			else
			{
				final int m = i == 2 ? 4 : 2;
				ThaumcraftApi.registerObjectTag(GrowthCraftApples.appleCider.asStack(1, i), new AspectList().add(Aspect.MAGIC, m).add(Aspect.HUNGER, 2).add(Aspect.WATER, 1).add(Aspect.CRYSTAL, 1));
				ThaumcraftApi.registerObjectTag(GrowthCraftApples.appleCiderBucket_deprecated.asStack(1, i), new AspectList().add(Aspect.MAGIC, m * 2).add(Aspect.WATER, 2));
				ThaumcraftApi.registerObjectTag(GrowthCraftApples.appleCiderBuckets[i].asStack(1), new AspectList().add(Aspect.MAGIC, m * 2).add(Aspect.WATER, 2));
			}
		}
	}
}
