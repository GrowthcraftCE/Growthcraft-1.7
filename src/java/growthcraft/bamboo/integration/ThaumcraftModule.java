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
package growthcraft.bamboo.integration;

import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.core.integration.ThaumcraftModuleBase;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspect;

public class ThaumcraftModule extends ThaumcraftModuleBase
{
	public ThaumcraftModule()
	{
		super(GrowthCraftBamboo.MOD_ID);
	}

	@Override
	protected void integrate()
	{
		ThaumcraftApi.registerObjectTag(GrowthCraftBamboo.bambooBlock.asStack(), new AspectList().add(Aspect.TREE, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBamboo.bambooShoot.asStack(), new AspectList().add(Aspect.TREE, 1).add(Aspect.PLANT, 1).add(Aspect.CROP, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBamboo.bambooLeaves.asStack(), new AspectList().add(Aspect.PLANT, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBamboo.bambooFence.asStack(), new AspectList().add(Aspect.TREE, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBamboo.bambooWall.asStack(), new AspectList().add(Aspect.TREE, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBamboo.bambooStairs.asStack(), new AspectList().add(Aspect.TREE, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBamboo.bambooSingleSlab.asStack(), new AspectList().add(Aspect.TREE, 1));

		ThaumcraftApi.registerObjectTag(GrowthCraftBamboo.bamboo.asStack(), new AspectList().add(Aspect.TREE, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBamboo.bambooDoorItem.asStack(), new AspectList().add(Aspect.TREE, 1));
		ThaumcraftApi.registerObjectTag(GrowthCraftBamboo.bambooRaft.asStack(), new AspectList().add(Aspect.WATER, 4).add(Aspect.TRAVEL, 4));
		ThaumcraftApi.registerObjectTag(GrowthCraftBamboo.bambooShootFood.asStack(), new AspectList().add(Aspect.TREE, 1).add(Aspect.PLANT, 1).add(Aspect.CROP, 1));
	}
}
