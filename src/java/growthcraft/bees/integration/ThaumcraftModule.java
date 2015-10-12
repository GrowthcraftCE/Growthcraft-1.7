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
package growthcraft.bees.integration;

import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.integration.ThaumcraftModuleBase;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspect;

public class ThaumcraftModule extends ThaumcraftModuleBase
{
	public ThaumcraftModule()
	{
		super(GrowthCraftBees.MOD_ID);
	}

	@Override
	protected void integrate()
	{
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.honeyComb.asStack(), new AspectList().add(Aspect.SLIME, 2));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.honeyJar.asStack(), new AspectList().add(Aspect.HUNGER, 2).add(Aspect.WATER, 2));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.bee.asStack(), new AspectList().add(Aspect.BEAST, 1).add(Aspect.AIR, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.beeBox.asStack(), new AspectList().add(Aspect.AIR, 2));
		ThaumcraftApi.registerObjectTag(GrowthCraftBees.beeHive.asStack(), new AspectList().add(Aspect.AIR, 2));

		for (int i = 0; i < GrowthCraftBees.honeyMeadBooze.length; ++i)
		{
			if (i == 0 || i == 4)
			{
				ThaumcraftApi.registerObjectTag(GrowthCraftBees.honeyMead.asStack(1, i), new AspectList().add(Aspect.HUNGER, 2).add(Aspect.WATER, 1).add(Aspect.CRYSTAL, 1));
				ThaumcraftApi.registerObjectTag(GrowthCraftBees.honeyMeadBucket_deprecated.asStack(1, i), new AspectList().add(Aspect.WATER, 2));
				ThaumcraftApi.registerObjectTag(GrowthCraftBees.honeyMeadBuckets[i].asStack(), new AspectList().add(Aspect.WATER, 2));
			}
			else
			{
				final int m = i == 2 ? 4 : 2;
				ThaumcraftApi.registerObjectTag(GrowthCraftBees.honeyMead.asStack(1, i), new AspectList().add(Aspect.MAGIC, m).add(Aspect.HUNGER, 2).add(Aspect.WATER, 1).add(Aspect.CRYSTAL, 1));
				ThaumcraftApi.registerObjectTag(GrowthCraftBees.honeyMeadBucket_deprecated.asStack(1, i), new AspectList().add(Aspect.MAGIC, m * 2).add(Aspect.WATER, 2));
				ThaumcraftApi.registerObjectTag(GrowthCraftBees.honeyMeadBuckets[i].asStack(), new AspectList().add(Aspect.MAGIC, m * 2).add(Aspect.WATER, 2));
			}
		}
	}
}

