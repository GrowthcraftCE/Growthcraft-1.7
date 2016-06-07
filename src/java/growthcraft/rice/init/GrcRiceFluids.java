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
package growthcraft.rice.init;

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
import growthcraft.cellar.common.item.EnumYeast;
import growthcraft.cellar.common.item.ItemBoozeBottle;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.BoozeRegistryHelper;
import growthcraft.cellar.util.BoozeUtils;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.core.GrowthCraftCore;
import growthcraft.rice.GrowthCraftRice;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidRegistry;

public class GrcRiceFluids extends GrcModuleBase
{
	public Booze[] riceSakeBooze;
	public BlockBoozeDefinition[] riceSakeFluids;
	public ItemDefinition riceSake;
	public ItemBucketBoozeDefinition[] riceSakeBuckets;

	@Override
	public void preInit()
	{
		riceSakeBooze = new Booze[7];
		riceSakeFluids = new BlockBoozeDefinition[riceSakeBooze.length];
		riceSakeBuckets = new ItemBucketBoozeDefinition[riceSakeBooze.length];
		BoozeRegistryHelper.initializeBoozeFluids("grc.riceSake", riceSakeBooze);
		for (Booze booze : riceSakeBooze)
		{
			booze.setColor(GrowthCraftRice.getConfig().riceSakeColor).setDensity(980);
		}
		BoozeRegistryHelper.initializeBooze(riceSakeBooze, riceSakeFluids, riceSakeBuckets);
		BoozeRegistryHelper.setBoozeFoodStats(riceSakeBooze, 1, -0.6f);
		BoozeRegistryHelper.setBoozeFoodStats(riceSakeBooze[0], 1, 0.2f);
		riceSakeBooze[4].setColor(GrowthCraftRice.getConfig().riceSakeDivineColor);
		riceSakeFluids[4].getBlock().refreshColor();
		riceSake = new ItemDefinition(new ItemBoozeBottle(riceSakeBooze));
	}

	private void registerRecipes()
	{
		final int fermentTime = GrowthCraftCellar.getConfig().fermentTime;
		final FluidStack[] fs = new FluidStack[riceSakeBooze.length];
		for (int i = 0; i < fs.length; ++i)
		{
			fs[i] = new FluidStack(riceSakeBooze[i], 1);
		}

		GrowthCraftCellar.boozeBuilderFactory.create(riceSakeBooze[0])
			.tags(BoozeTag.YOUNG)
			.brewsFrom(
				new FluidStack(FluidRegistry.WATER, 40),
				new OreItemStacks("cropRice"),
				TickUtils.minutes(1),
				Residue.newDefault(0.2F));

		GrowthCraftCellar.boozeBuilderFactory.create(riceSakeBooze[1])
			.tags(BoozeTag.FERMENTED)
			.fermentsFrom(fs[0], EnumYeast.BREWERS.asStack(), fermentTime)
			.fermentsFrom(fs[0], new ItemStack(Items.nether_wart), (int)(fermentTime * 0.66))
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.15f), TickUtils.seconds(45))
				.addPotionEntry(Potion.jump, TickUtils.minutes(3), 0);

		GrowthCraftCellar.boozeBuilderFactory.create(riceSakeBooze[2])
			.tags(BoozeTag.FERMENTED, BoozeTag.POTENT)
			.fermentsFrom(fs[1], new OreItemStacks("dustGlowstone"), fermentTime)
			.fermentsFrom(fs[3], new OreItemStacks("dustGlowstone"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.17f), TickUtils.seconds(45))
				.addPotionEntry(Potion.jump, TickUtils.minutes(3), 0);

		GrowthCraftCellar.boozeBuilderFactory.create(riceSakeBooze[3])
			.tags(BoozeTag.FERMENTED, BoozeTag.EXTENDED)
			.fermentsFrom(fs[1], new OreItemStacks("dustRedstone"), fermentTime)
			.fermentsFrom(fs[2], new OreItemStacks("dustRedstone"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.15f), TickUtils.seconds(45))
				.addPotionEntry(Potion.jump, TickUtils.minutes(3), 0);

		// Ethereal Yeast - Divine Sake
		GrowthCraftCellar.boozeBuilderFactory.create(riceSakeBooze[4])
			.tags(BoozeTag.FERMENTED, BoozeTag.HYPER_EXTENDED)
			.fermentsFrom(fs[2], EnumYeast.ETHEREAL.asStack(), fermentTime)
			.fermentsFrom(fs[3], EnumYeast.ETHEREAL.asStack(), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.15f), TickUtils.seconds(45))
				.addPotionEntry(Potion.jump, TickUtils.minutes(3), 0)
				.addPotionEntry(Potion.moveSpeed, TickUtils.minutes(3), 0);

		// Origin Yeast
		GrowthCraftCellar.boozeBuilderFactory.create(riceSakeBooze[5])
			.tags(BoozeTag.FERMENTED, BoozeTag.INTOXICATED)
			.fermentsFrom(fs[2], EnumYeast.ORIGIN.asStack(), fermentTime)
			.fermentsFrom(fs[3], EnumYeast.ORIGIN.asStack(), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.20f), TickUtils.seconds(45))
				.addEffect(new EffectWeightedRandomList()
					.add(8, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.jump.id, TickUtils.minutes(3), 2)))
					.add(2, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.confusion.id, TickUtils.minutes(3), 2))));

		// Poisoned Sake - created from netherrash,
		// the booze looses all its benefits and effectively becomes poisoned
		GrowthCraftCellar.boozeBuilderFactory.create(riceSakeBooze[6])
			.tags(BoozeTag.FERMENTED, BoozeTag.POISONED)
			//.fermentsFrom(fs[1], EnumYeast.NETHERRASH.asStack(), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.15f), TickUtils.seconds(45))
				.createPotionEntry(Potion.poison, TickUtils.seconds(90), 0).toggleDescription(!GrowthCraftCore.getConfig().hidePoisonedBooze);
	}

	@Override
	public void register()
	{
		GameRegistry.registerItem(riceSake.getItem(), "grc.riceSake");

		BoozeRegistryHelper.registerBooze(riceSakeBooze, riceSakeFluids, riceSakeBuckets, riceSake, "grc.riceSake", null);
		registerRecipes();
	}
}
