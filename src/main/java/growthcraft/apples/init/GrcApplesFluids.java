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
package growthcraft.apples.init;

import growthcraft.api.cellar.booze.Booze;
import growthcraft.apples.GrowthCraftApples;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.common.item.ItemBoozeBottle;
import growthcraft.cellar.util.BoozeRegistryHelper;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.GrcModuleBase;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class GrcApplesFluids extends GrcModuleBase
{
	public Booze[] appleCiderBooze;
	public BlockBoozeDefinition[] appleCiderFluids;
	public ItemDefinition appleCider;
	public ItemBucketBoozeDefinition[] appleCiderBuckets;

	@Override
	public void preInit()
	{
		appleCiderBooze = new Booze[7];
		appleCiderFluids = new BlockBoozeDefinition[appleCiderBooze.length];
		appleCiderBuckets = new ItemBucketBoozeDefinition[appleCiderBooze.length];
		BoozeRegistryHelper.initializeBoozeFluids("grc.appleCider", appleCiderBooze);
		for (Booze booze : appleCiderBooze)
		{
			booze.setColor(GrowthCraftApples.getConfig().appleCiderColor).setDensity(1010);
		}
		BoozeRegistryHelper.initializeBooze(appleCiderBooze, appleCiderFluids, appleCiderBuckets);
		BoozeRegistryHelper.setBoozeFoodStats(appleCiderBooze, 1, -0.3f);
		BoozeRegistryHelper.setBoozeFoodStats(appleCiderBooze[0], 1, 0.3f);

		appleCiderBooze[4].setColor(GrowthCraftApples.getConfig().silkenNectarColor);
		appleCiderFluids[4].getBlock().refreshColor();

		appleCider = new ItemDefinition(new ItemBoozeBottle(appleCiderBooze));
	}

	@Override
	public void register()
	{
		GameRegistry.registerItem(appleCider.getItem(), "grc.appleCider");
		BoozeRegistryHelper.registerBooze(appleCiderBooze, appleCiderFluids, appleCiderBuckets, appleCider, "grc.appleCider", null);
		// Ore Dictionary
		OreDictionary.registerOre("foodApplejuice", appleCider.asStack());
	}
}
