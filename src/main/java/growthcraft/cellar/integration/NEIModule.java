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
package growthcraft.cellar.integration;

import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.integration.nei.RecipeHandlerBrewKettle;
import growthcraft.cellar.integration.nei.RecipeHandlerCultureJar;
import growthcraft.cellar.integration.nei.RecipeHandlerFermentBarrel;
import growthcraft.cellar.integration.nei.RecipeHandlerFruitPress;
import growthcraft.core.integration.nei.NEIPlatform;
import growthcraft.core.integration.NEIModuleBase;

import codechicken.nei.api.API;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class NEIModule extends NEIModuleBase
{
	public NEIModule()
	{
		super(GrowthCraftCellar.MOD_ID);
	}

	@Override
	@SideOnly(Side.CLIENT)
	@Optional.Method(modid=NEIPlatform.MOD_ID)
	public void integrateClient()
	{
		API.registerRecipeHandler(new RecipeHandlerBrewKettle());
		API.registerUsageHandler(new RecipeHandlerBrewKettle());

		API.registerRecipeHandler(new RecipeHandlerCultureJar());
		API.registerUsageHandler(new RecipeHandlerCultureJar());

		API.registerRecipeHandler(new RecipeHandlerFermentBarrel());
		API.registerUsageHandler(new RecipeHandlerFermentBarrel());

		API.registerRecipeHandler(new RecipeHandlerFruitPress());
		API.registerUsageHandler(new RecipeHandlerFruitPress());
	}
}
