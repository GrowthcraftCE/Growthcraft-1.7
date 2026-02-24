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
package growthcraft.apples.integration;

import growthcraft.apples.GrowthCraftApples;
import growthcraft.core.integration.forestry.ForestryFluids;
import growthcraft.core.integration.ForestryModuleBase;

import cpw.mods.fml.common.Optional;
import net.minecraft.item.ItemStack;

public class ForestryModule extends ForestryModuleBase
{
	public ForestryModule()
	{
		super(GrowthCraftApples.MOD_ID);
	}

	@Override
	@Optional.Method(modid="Forestry")
	protected void integrate()
	{
		final ItemStack appleSeed = GrowthCraftApples.items.appleSeeds.asStack();
		final int seedamount = getActiveMode().getIntegerSetting("squeezer.liquid.seed");
		if (ForestryFluids.SEEDOIL.exists()) recipes().squeezerManager.addRecipe(10, new ItemStack[]{appleSeed}, ForestryFluids.SEEDOIL.asFluidStack(seedamount));
		Backpack.FORESTERS.add(appleSeed);
	}
}
