/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015, 2016 IceDragon200
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

import growthcraft.cellar.common.block.BlockCellarContainer;
import growthcraft.cellar.common.block.BlockFruitPresser;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.integration.waila.CellarDataProvider;
import growthcraft.core.integration.WailaIntegrationBase;

import cpw.mods.fml.common.Optional;

import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

public class Waila extends WailaIntegrationBase
{
	public Waila()
	{
		super(GrowthCraftCellar.MOD_ID);
	}

	@Optional.Method(modid="Waila")
	public static void register(IWailaRegistrar reg)
	{
		final IWailaDataProvider provider = new CellarDataProvider();
		reg.registerBodyProvider(provider, BlockFruitPresser.class);
		reg.registerNBTProvider(provider, BlockFruitPresser.class);
		reg.registerBodyProvider(provider, BlockCellarContainer.class);
		reg.registerNBTProvider(provider, BlockCellarContainer.class);

		final String option = "grccellar.waila.option.";
		reg.addConfig(GrowthCraftCellar.MOD_NAME, "FruitPressExtras", option + "FruitPressExtras", true);
		reg.addConfig(GrowthCraftCellar.MOD_NAME, "BrewKettleExtras", option + "BrewKettleExtras", true);
		reg.addConfig(GrowthCraftCellar.MOD_NAME, "FermentBarrelExtras", option + "FermentBarrelExtras", true);
		reg.addConfig(GrowthCraftCellar.MOD_NAME, "CultureJarExtras", option + "CultureJarExtras", true);
	}
}
