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

import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.common.Residue;
import growthcraft.api.core.effect.EffectAddPotionEffect;
import growthcraft.api.core.effect.EffectRandomList;
import growthcraft.api.core.effect.EffectWeightedRandomList;
import growthcraft.api.core.effect.SimplePotionEffectFactory;
import growthcraft.api.core.item.OreItemStacks;
import growthcraft.api.core.util.TickUtils;
import growthcraft.apples.GrowthCraftApples;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.BoozeUtils;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.core.GrowthCraftCore;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.fluids.FluidStack;

public class GrcApplesRecipes extends GrcModuleBase
{
	private void registerRecipes()
	{
		final int fermentTime = GrowthCraftCellar.getConfig().fermentTime;
		final FluidStack[] fs = new FluidStack[GrowthCraftApples.fluids.appleCiderBooze.length];
		for (int i = 0; i < GrowthCraftApples.fluids.appleCiderBooze.length; ++i)
		{
			fs[i] = new FluidStack(GrowthCraftApples.fluids.appleCiderBooze[i], 1);
		}

		GrowthCraftCellar.boozeBuilderFactory.create(GrowthCraftApples.fluids.appleCiderBooze[0])
			.tags(BoozeTag.CIDER, BoozeTag.YOUNG)
			.pressesFrom(
				new OreItemStacks("foodApple"),
				TickUtils.seconds(2),
				40,
				Residue.newDefault(0.3F)
			);

		GrowthCraftCellar.boozeBuilderFactory.create(GrowthCraftApples.fluids.appleCiderBooze[1])
			.tags(BoozeTag.CIDER, BoozeTag.FERMENTED)
			.fermentsFrom(fs[0], new OreItemStacks("yeastBrewers"), fermentTime)
			.fermentsFrom(fs[0], new ItemStack(Items.nether_wart), (int)(fermentTime * 0.66))
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.045f), TickUtils.seconds(45))
				.addPotionEntry(Potion.field_76444_x, TickUtils.seconds(90), 0);

		GrowthCraftCellar.boozeBuilderFactory.create(GrowthCraftApples.fluids.appleCiderBooze[2])
			.tags(BoozeTag.CIDER, BoozeTag.FERMENTED, BoozeTag.POTENT)
			.fermentsFrom(fs[1], new OreItemStacks("dustGlowstone"), fermentTime)
			.fermentsFrom(fs[3], new OreItemStacks("dustGlowstone"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.080f), TickUtils.seconds(45))
				.addPotionEntry(Potion.field_76444_x, TickUtils.seconds(90), 0);

		GrowthCraftCellar.boozeBuilderFactory.create(GrowthCraftApples.fluids.appleCiderBooze[3])
			.tags(BoozeTag.CIDER, BoozeTag.FERMENTED, BoozeTag.EXTENDED)
			.fermentsFrom(fs[1], new OreItemStacks("dustRedstone"), fermentTime)
			.fermentsFrom(fs[2], new OreItemStacks("dustRedstone"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.045f), TickUtils.seconds(45))
				.addPotionEntry(Potion.field_76444_x, TickUtils.seconds(90), 0);

		// Silken Nectar - ETHEREAL
		GrowthCraftCellar.boozeBuilderFactory.create(GrowthCraftApples.fluids.appleCiderBooze[4])
			.tags(BoozeTag.CIDER, BoozeTag.FERMENTED, BoozeTag.MAGICAL)
			.fermentsFrom(fs[1], new OreItemStacks("yeastEthereal"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.045f), TickUtils.seconds(45))
				.addEffect(new EffectRandomList()
					// This is terrifying, thank heavens for a decent text editor...
					.add(new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.moveSpeed.id, TickUtils.minutes(10), 0)))
					.add(new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.digSpeed.id, TickUtils.minutes(10), 0)))
					.add(new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.damageBoost.id, TickUtils.minutes(10), 0)))
					.add(new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.heal.id, TickUtils.minutes(10), 0)))
					.add(new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.jump.id, TickUtils.minutes(10), 0)))
					.add(new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.regeneration.id, TickUtils.minutes(10), 0)))
					.add(new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.resistance.id, TickUtils.minutes(10), 0)))
					.add(new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.fireResistance.id, TickUtils.minutes(10), 0)))
					.add(new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.waterBreathing.id, TickUtils.minutes(10), 0)))
					.add(new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.invisibility.id, TickUtils.minutes(10), 0)))
					.add(new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.nightVision.id, TickUtils.minutes(10), 0)))
					.add(new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.field_76434_w.id, TickUtils.minutes(10), 0)))
					.add(new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.field_76444_x.id, TickUtils.minutes(10), 0)))
					.add(new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.field_76443_y.id, TickUtils.minutes(10), 0)))
				);

		// Intoxicated - Origin Yeast
		GrowthCraftCellar.boozeBuilderFactory.create(GrowthCraftApples.fluids.appleCiderBooze[5])
			.tags(BoozeTag.CIDER, BoozeTag.FERMENTED, BoozeTag.INTOXICATED)
			.fermentsFrom(fs[2], new OreItemStacks("yeastOrigin"), fermentTime)
			.fermentsFrom(fs[3], new OreItemStacks("yeastOrigin"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.045f * 1.5f), TickUtils.seconds(45))
				.addEffect(new EffectWeightedRandomList()
					.add(8, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.field_76444_x.id, TickUtils.seconds(90), 2)))
					.add(2, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.wither.id, TickUtils.seconds(90), 2)))
				);

		// Poisoned - created from netherrash,
		// the booze looses all its benefits and effectively becomes poisoned
		GrowthCraftCellar.boozeBuilderFactory.create(GrowthCraftApples.fluids.appleCiderBooze[6])
			.tags(BoozeTag.CIDER, BoozeTag.FERMENTED, BoozeTag.POISONED)
			.fermentsFrom(fs[0], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsFrom(fs[1], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsFrom(fs[2], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsFrom(fs[3], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsFrom(fs[4], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsFrom(fs[5], new OreItemStacks("yeastPoison"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.045f), TickUtils.seconds(45))
				.createPotionEntry(Potion.poison, TickUtils.seconds(90), 0).toggleDescription(!GrowthCraftCore.getConfig().hidePoisonedBooze);
	}

	@Override
	public void init()
	{
		registerRecipes();
	}
}
