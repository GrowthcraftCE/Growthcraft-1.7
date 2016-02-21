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
package growthcraft.core.integration;

import growthcraft.core.common.block.GrcBlockContainer;
import growthcraft.core.common.block.ICropDataProvider;
import growthcraft.core.common.block.IPaddy;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.integration.waila.CoreDataProvider;
import growthcraft.core.integration.waila.CropDataProvider;
import growthcraft.core.integration.waila.PaddyDataProvider;

import cpw.mods.fml.common.Optional;

import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

import net.minecraft.item.Item;

public class Waila extends WailaIntegrationBase
{
	public Waila()
	{
		super(GrowthCraftCore.MOD_ID);
	}

	@Optional.Method(modid = "Waila")
	public static void register(IWailaRegistrar reg)
	{
		final IWailaDataProvider coreProvider = new CoreDataProvider();
		reg.registerBodyProvider(coreProvider, GrcBlockContainer.class);
		reg.registerNBTProvider(coreProvider, GrcBlockContainer.class);

		final IWailaDataProvider cropProvider = new CropDataProvider();
		reg.registerBodyProvider(cropProvider, ICropDataProvider.class);
		reg.registerBodyProvider(cropProvider, Item.class);

		final IWailaDataProvider paddyProvider = new PaddyDataProvider();
		reg.registerBodyProvider(paddyProvider, IPaddy.class);

		final String option = "grccore.waila.option.";
		reg.addConfig(GrowthCraftCore.MOD_NAME, "DisplayFluidContent", option + "DisplayFluidContent", true);
		reg.addConfig(GrowthCraftCore.MOD_NAME, "DisplayHeated", option + "DisplayHeated", true);
	}
}
