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
package growthcraft.grapes.init;

import growthcraft.api.cellar.booze.Booze;
import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.common.Residue;
import growthcraft.api.core.effect.EffectAddPotionEffect;
import growthcraft.api.core.effect.EffectWeightedRandomList;
import growthcraft.api.core.effect.SimplePotionEffectFactory;
import growthcraft.api.core.item.OreItemStacks;
import growthcraft.api.core.util.TickUtils;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.common.item.ItemBoozeBottle;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.BoozeRegistryHelper;
import growthcraft.cellar.util.BoozeUtils;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.core.GrowthCraftCore;
import growthcraft.grapes.common.item.EnumGrapes;
import growthcraft.grapes.GrowthCraftGrapes;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class GrcGrapesFluids extends GrcModuleBase
{
	public Booze[] grapeWineBooze;
	public BlockBoozeDefinition[] grapeWineFluids;
	public ItemDefinition grapeWine;
	public ItemBucketBoozeDefinition[] grapeWineBuckets;

	@Override
	public void preInit()
	{
		this.grapeWineBooze = new Booze[8];
		this.grapeWineFluids = new BlockBoozeDefinition[grapeWineBooze.length];
		this.grapeWineBuckets = new ItemBucketBoozeDefinition[grapeWineBooze.length];
		BoozeRegistryHelper.initializeBoozeFluids("grc.grapeWine", grapeWineBooze);
		for (Booze booze : grapeWineBooze)
		{
			booze.setColor(GrowthCraftGrapes.getConfig().grapeWineColor).setDensity(1120);
		}
		BoozeRegistryHelper.initializeBooze(grapeWineBooze, grapeWineFluids, grapeWineBuckets);
		BoozeRegistryHelper.setBoozeFoodStats(grapeWineBooze, 1, -0.3f);
		grapeWineBooze[4].setColor(GrowthCraftGrapes.getConfig().ambrosiaColor);
		grapeWineFluids[4].getBlock().refreshColor();
		grapeWineBooze[5].setColor(GrowthCraftGrapes.getConfig().portWineColor);
		grapeWineFluids[5].getBlock().refreshColor();

		this.grapeWine = new ItemDefinition(new ItemBoozeBottle(grapeWineBooze));
	}

	private void registerFermentations()
	{
		final int fermentTime = GrowthCraftCellar.getConfig().fermentTime;
		final FluidStack[] fs = new FluidStack[grapeWineBooze.length];
		for (int i = 0; i < grapeWineBooze.length; ++i)
		{
			fs[i] = new FluidStack(grapeWineBooze[i], 1);
		}

		GrowthCraftCellar.boozeBuilderFactory.create(grapeWineBooze[0])
			.tags(BoozeTag.YOUNG)
			.pressesFrom(
				EnumGrapes.PURPLE.asStack(),
				TickUtils.seconds(2),
				40,
				Residue.newDefault(0.3F));

		// Brewers Yeast, Nether Wart
		GrowthCraftCellar.boozeBuilderFactory.create(grapeWineBooze[1])
			.tags(BoozeTag.WINE, BoozeTag.FERMENTED)
			.fermentsFrom(fs[0], new OreItemStacks("yeastBrewers"), fermentTime)
			.fermentsFrom(fs[0], new ItemStack(Items.nether_wart), (int)(fermentTime * 0.66))
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.05f), TickUtils.seconds(90))
				.addPotionEntry(Potion.resistance, TickUtils.minutes(3), 0);

		// Glowstone Dust
		GrowthCraftCellar.boozeBuilderFactory.create(grapeWineBooze[2])
			.tags(BoozeTag.WINE, BoozeTag.FERMENTED, BoozeTag.POTENT)
			.fermentsFrom(fs[1], new OreItemStacks("dustGlowstone"), fermentTime)
			.fermentsFrom(fs[3], new OreItemStacks("dustGlowstone"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.07f), TickUtils.seconds(90))
				.addPotionEntry(Potion.resistance, TickUtils.minutes(3), 0);

		// Redstone Dust
		GrowthCraftCellar.boozeBuilderFactory.create(grapeWineBooze[3])
			.tags(BoozeTag.WINE, BoozeTag.FERMENTED, BoozeTag.EXTENDED)
			.fermentsFrom(fs[1], new OreItemStacks("dustRedstone"), fermentTime)
			.fermentsFrom(fs[2], new OreItemStacks("dustRedstone"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.05f), TickUtils.seconds(90))
				.addPotionEntry(Potion.resistance, TickUtils.minutes(3), 0);

		// Ambrosia - Ethereal Yeast
		GrowthCraftCellar.boozeBuilderFactory.create(grapeWineBooze[4])
			.tags(BoozeTag.WINE, BoozeTag.FERMENTED, BoozeTag.HYPER_EXTENDED)
			.fermentsFrom(fs[2], new OreItemStacks("yeastEthereal"), fermentTime)
			.fermentsFrom(fs[3], new OreItemStacks("yeastEthereal"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.053f), TickUtils.seconds(90))
				.addPotionEntry(Potion.field_76434_w, TickUtils.minutes(3), 0)
				.addPotionEntry(Potion.resistance, TickUtils.minutes(3), 0);

		// Port Wine - Bayanus Yeast
		GrowthCraftCellar.boozeBuilderFactory.create(grapeWineBooze[5])
			.tags(BoozeTag.WINE, BoozeTag.FERMENTED, BoozeTag.FORTIFIED)
			.brewsFrom(
				new FluidStack(grapeWineBooze[1], GrowthCraftGrapes.getConfig().portWineBrewingYield),
				new OreItemStacks("yeastBayanus"),
				GrowthCraftGrapes.getConfig().portWineBrewingTime,
				Residue.newDefault(0.3F))
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.20f), TickUtils.seconds(90))
				.addPotionEntry(Potion.resistance, TickUtils.minutes(3), 2);

		// Intoxicated Wine
		GrowthCraftCellar.boozeBuilderFactory.create(grapeWineBooze[6])
			.tags(BoozeTag.WINE, BoozeTag.FERMENTED, BoozeTag.INTOXICATED)
			.fermentsFrom(fs[2], new OreItemStacks("yeastOrigin"), fermentTime)
			.fermentsFrom(fs[3], new OreItemStacks("yeastOrigin"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.15f), TickUtils.seconds(90))
				.addEffect(new EffectWeightedRandomList()
					.add(8, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.resistance.id, TickUtils.minutes(3), 2)))
					.add(2, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.weakness.id, TickUtils.minutes(3), 2))));

		GrowthCraftCellar.boozeBuilderFactory.create(grapeWineBooze[7])
			.tags(BoozeTag.WINE, BoozeTag.FERMENTED, BoozeTag.POISONED)
			.fermentsTo(fs[1], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsTo(fs[2], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsTo(fs[3], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsTo(fs[4], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsTo(fs[5], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsTo(fs[6], new OreItemStacks("yeastPoison"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.05f), TickUtils.seconds(90))
				.createPotionEntry(Potion.poison, TickUtils.seconds(90), 0).toggleDescription(!GrowthCraftCore.getConfig().hidePoisonedBooze);
	}

	@Override
	public void register()
	{
		grapeWine.register("grc.grapeWine");

		BoozeRegistryHelper.registerBooze(grapeWineBooze, grapeWineFluids, grapeWineBuckets, grapeWine, "grc.grapeWine", null);
		registerFermentations();

		OreDictionary.registerOre("foodGrapejuice", grapeWine.asStack(1, 0));
	}
}
