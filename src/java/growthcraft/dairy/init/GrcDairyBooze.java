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
package growthcraft.dairy.init;

import growthcraft.api.cellar.booze.Booze;
import growthcraft.api.cellar.booze.BoozeRegistry;
import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.fermenting.FermentingRegistry;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.common.item.ItemBoozeBottle;
import growthcraft.cellar.util.BoozeRegistryHelper;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.dairy.GrowthCraftDairy;

import net.minecraftforge.fluids.FluidStack;

public class GrcDairyBooze extends GrcModuleBase
{
	private static final String milkBasename = "grcdairy.milk";
	// not an actual Booze, but we use the class since it has the ability to set color
	public Booze[] milkBooze = new Booze[5];
	public BlockBoozeDefinition[] milkBoozeBlocks = new BlockBoozeDefinition[milkBooze.length];
	public ItemBucketBoozeDefinition[] milkBoozeBuckets = new ItemBucketBoozeDefinition[milkBooze.length];
	public ItemDefinition milk;

	@Override
	public void preInit()
	{
		this.milk = new ItemDefinition(new ItemBoozeBottle(5, -0.6F, milkBooze));
		BoozeRegistryHelper.initializeBooze(milkBooze, milkBoozeBlocks, milkBoozeBuckets, milkBasename, GrowthCraftDairy.getConfig().milkColor);
	}

	private void registerFermentations()
	{
		final BoozeRegistry br = CellarRegistry.instance().booze();
		final FermentingRegistry fr = CellarRegistry.instance().fermenting();

		final FluidStack[] fs = new FluidStack[milkBooze.length];
		for (int i = 0; i < milkBooze.length; ++i)
		{
			fs[i] = new FluidStack(milkBooze[i], 1);
		}

		br.addTags(milkBooze[0], BoozeTag.YOUNG);
		//fr.

		br.addTags(milkBooze[1], BoozeTag.FERMENTED);

		br.addTags(milkBooze[2], BoozeTag.FERMENTED, BoozeTag.POTENT);

		br.addTags(milkBooze[3], BoozeTag.FERMENTED, BoozeTag.EXTENDED);

		br.addTags(milkBooze[4], BoozeTag.FERMENTED, BoozeTag.POISONED);
	}

	@Override
	public void register()
	{
		BoozeRegistryHelper.registerBooze(milkBooze, milkBoozeBlocks, milkBoozeBuckets, milk, milkBasename, null);
		registerFermentations();
	}
}
