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

import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.YeastType;
import growthcraft.core.integration.ThaumcraftModuleBase;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspect;

public class ThaumcraftModule extends ThaumcraftModuleBase
{
	public ThaumcraftModule()
	{
		super(GrowthCraftCellar.MOD_ID);
	}

	@Override
	protected void integrate()
	{
		ThaumcraftApi.registerObjectTag(GrowthCraftCellar.brewKettle.asStack(), new AspectList().add(Aspect.METAL, 12).add(Aspect.CRAFT, 2).add(Aspect.WATER, 2));
		ThaumcraftApi.registerObjectTag(GrowthCraftCellar.fermentBarrel.asStack(), new AspectList().add(Aspect.METAL, 6).add(Aspect.CRAFT, 2).add(Aspect.WATER, 2));
		ThaumcraftApi.registerObjectTag(GrowthCraftCellar.fruitPress.asStack(), new AspectList().add(Aspect.METAL, 3).add(Aspect.CRAFT, 2).add(Aspect.MECHANISM, 2));

		ThaumcraftApi.registerObjectTag(YeastType.BREWERS.asStack(), new AspectList().add(Aspect.EARTH, 1));
		ThaumcraftApi.registerObjectTag(YeastType.LAGER.asStack(), new AspectList().add(Aspect.COLD, 1));
		ThaumcraftApi.registerObjectTag(YeastType.BAYANUS.asStack(), new AspectList().add(Aspect.ENERGY, 1));
		ThaumcraftApi.registerObjectTag(YeastType.ETHEREAL.asStack(), new AspectList().add(Aspect.MAGIC, 1));
		ThaumcraftApi.registerObjectTag(YeastType.ORIGIN.asStack(), new AspectList().add(Aspect.DARKNESS, 1));
	}
}
