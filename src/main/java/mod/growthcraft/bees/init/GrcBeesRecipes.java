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
package growthcraft.bees.init;

import growthcraft.api.bees.BeesFluidTag;
import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.core.effect.EffectAddPotionEffect;
import growthcraft.api.core.effect.EffectWeightedRandomList;
import growthcraft.api.core.effect.SimplePotionEffectFactory;
import growthcraft.api.core.fluids.TaggedFluidStacks;
import growthcraft.api.core.item.OreItemStacks;
import growthcraft.api.core.item.recipes.ShapelessMultiRecipe;
import growthcraft.api.core.util.TickUtils;
import growthcraft.bees.common.item.EnumBeesWax;
import growthcraft.bees.GrowthCraftBees;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.BoozeUtils;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.core.GrowthCraftCore;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class GrcBeesRecipes extends GrcModuleBase
{
	private void registerCellarRecipes()
	{
		final int fermentTime = GrowthCraftCellar.getConfig().fermentTime;
		final FluidStack[] fs = new FluidStack[GrowthCraftBees.fluids.honeyMeadBooze.length];
		for (int i = 0; i < GrowthCraftBees.fluids.honeyMeadBooze.length; ++i)
		{
			fs[i] = new FluidStack(GrowthCraftBees.fluids.honeyMeadBooze[i], 1);
		}

		GrowthCraftCellar.boozeBuilderFactory.create(GrowthCraftBees.fluids.honeyMeadBooze[0])
			.tags(BoozeTag.YOUNG, BeesFluidTag.MEAD);

		final TaggedFluidStacks youngMead = new TaggedFluidStacks(1, "young", "mead");

		GrowthCraftCellar.boozeBuilderFactory.create(GrowthCraftBees.fluids.honeyMeadBooze[1])
			.tags(BoozeTag.FERMENTED, BeesFluidTag.MEAD)
			.fermentsFrom(youngMead, new OreItemStacks("yeastBrewers"), fermentTime)
			.fermentsFrom(youngMead, new ItemStack(Items.nether_wart), (int)(fermentTime * 0.66))
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.15f), TickUtils.seconds(90))
				.addPotionEntry(Potion.regeneration, TickUtils.seconds(90), 0);

		GrowthCraftCellar.boozeBuilderFactory.create(GrowthCraftBees.fluids.honeyMeadBooze[2])
			.tags(BoozeTag.FERMENTED, BoozeTag.POTENT, BeesFluidTag.MEAD)
			.fermentsFrom(fs[1], new OreItemStacks("dustGlowstone"), fermentTime)
			.fermentsFrom(fs[3], new OreItemStacks("dustGlowstone"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.17f), TickUtils.seconds(90))
				.addPotionEntry(Potion.regeneration, TickUtils.seconds(90), 0);

		GrowthCraftCellar.boozeBuilderFactory.create(GrowthCraftBees.fluids.honeyMeadBooze[3])
			.tags(BoozeTag.FERMENTED, BoozeTag.EXTENDED, BeesFluidTag.MEAD)
			.fermentsFrom(fs[1], new OreItemStacks("dustRedstone"), fermentTime)
			.fermentsFrom(fs[2], new OreItemStacks("dustRedstone"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.15f), TickUtils.seconds(90))
				.addPotionEntry(Potion.regeneration, TickUtils.seconds(90), 0);

		GrowthCraftCellar.boozeBuilderFactory.create(GrowthCraftBees.fluids.honeyMeadBooze[4])
			.tags(BoozeTag.FERMENTED, BoozeTag.HYPER_EXTENDED, BeesFluidTag.MEAD)
			.fermentsFrom(fs[2], new OreItemStacks("yeastEthereal"), fermentTime)
			.fermentsFrom(fs[3], new OreItemStacks("yeastEthereal"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.15f), TickUtils.seconds(90))
				.addPotionEntry(Potion.regeneration, TickUtils.seconds(90), 0);

		GrowthCraftCellar.boozeBuilderFactory.create(GrowthCraftBees.fluids.honeyMeadBooze[5])
			.tags(BoozeTag.FERMENTED, BoozeTag.INTOXICATED, BeesFluidTag.MEAD)
			.fermentsFrom(fs[2], new OreItemStacks("yeastOrigin"), fermentTime)
			.fermentsFrom(fs[3], new OreItemStacks("yeastOrigin"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.15f * 1.5f), TickUtils.seconds(90))
				.addEffect(new EffectWeightedRandomList()
					.add(8, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.regeneration.id, TickUtils.seconds(90), 2)))
					.add(2, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.poison.id, TickUtils.seconds(90), 2)))
				);

		GrowthCraftCellar.boozeBuilderFactory.create(GrowthCraftBees.fluids.honeyMeadBooze[6])
			.tags(BoozeTag.FERMENTED, BoozeTag.POISONED, BeesFluidTag.MEAD)
			.fermentsFrom(fs[0], new OreItemStacks("yeastPoison", 1), fermentTime)
			.fermentsFrom(fs[1], new OreItemStacks("yeastPoison", 1), fermentTime)
			.fermentsFrom(fs[2], new OreItemStacks("yeastPoison", 1), fermentTime)
			.fermentsFrom(fs[3], new OreItemStacks("yeastPoison", 1), fermentTime)
			.fermentsFrom(fs[4], new OreItemStacks("yeastPoison", 1), fermentTime)
			.fermentsFrom(fs[5], new OreItemStacks("yeastPoison", 1), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.15f), TickUtils.seconds(90))
				.createPotionEntry(Potion.poison, TickUtils.seconds(90), 0).toggleDescription(!GrowthCraftCore.getConfig().hidePoisonedBooze);
	}

	@Override
	public void register()
	{
		final ItemStack emptyComb = GrowthCraftBees.items.honeyCombEmpty.asStack();
		GameRegistry.addShapelessRecipe(EnumBeesWax.NORMAL.asStack(),
			emptyComb, emptyComb, emptyComb,
			emptyComb, emptyComb, emptyComb,
			emptyComb, emptyComb, emptyComb);

		GameRegistry.addRecipe(new ShapelessOreRecipe(EnumBeesWax.BLACK.asStack(),
			EnumBeesWax.NORMAL.asStack(), "dyeBlack"));

		GameRegistry.addRecipe(new ShapelessOreRecipe(EnumBeesWax.RED.asStack(),
			EnumBeesWax.NORMAL.asStack(), "dyeRed"));

		registerCellarRecipes();
	}

	@Override
	public void postInit()
	{
		final ItemStack meadBucket = GrowthCraftBees.fluids.honeyMeadBuckets[0].asStack();
		final ItemStack meadBottle = GrowthCraftBees.fluids.honeyMeadBottle.asStack();
		final int bottleCapacity = GrowthCraftCore.getConfig().bottleCapacity;

		// Tagged Fluid compat
		/// Buckets
		GameRegistry.addRecipe(new ShapelessMultiRecipe(
			meadBucket,
			Items.bucket,
			new TaggedFluidStacks(1000, "honey"),
			new FluidStack(FluidRegistry.WATER, 1000)));

		GameRegistry.addRecipe(new ShapelessMultiRecipe(
			GrowthCraftBees.fluids.honeyMeadBottle.asStack(3),
			Items.glass_bottle,
			Items.glass_bottle,
			Items.glass_bottle,
			new TaggedFluidStacks(1000, "honey"),
			new FluidStack(FluidRegistry.WATER, 1000)));

		/// Bottles
		GameRegistry.addRecipe(new ShapelessMultiRecipe(
			meadBottle,
			Items.glass_bottle,
			new TaggedFluidStacks(bottleCapacity, "honey"),
			new FluidStack(FluidRegistry.WATER, bottleCapacity)));

		// Ore Dictionary compat
		/// Buckets
		GameRegistry.addRecipe(new ShapelessMultiRecipe(
			meadBucket,
			Items.bucket,
			new OreItemStacks("bucketHoney"),
			new FluidStack(FluidRegistry.WATER, 1000)));

		GameRegistry.addRecipe(new ShapelessMultiRecipe(
			meadBottle,
			Items.glass_bottle,
			new OreItemStacks("bottleHoney"),
			new FluidStack(FluidRegistry.WATER, bottleCapacity)));

		// Transfer recipes
		/// To Honey Jar from `bucketHoney`
		GameRegistry.addRecipe(new ShapelessMultiRecipe(
			GrowthCraftBees.items.honeyJar.asStack(),
			Blocks.flower_pot,
			new TaggedFluidStacks(1000, "honey")
		));

		GameRegistry.addRecipe(new ShapelessOreRecipe(
			GrowthCraftBees.items.honeyJar.asStack(),
			Blocks.flower_pot,
			"bucketHoney"
		));

		/// To Honey Bucket from `bucketHoney`
		GameRegistry.addRecipe(new ShapelessMultiRecipe(
			GrowthCraftBees.fluids.honey.asBucketItemStack(),
			Items.bucket,
			new TaggedFluidStacks(1000, "honey")
		));

		GameRegistry.addRecipe(new ShapelessOreRecipe(
			GrowthCraftBees.fluids.honey.asBucketItemStack(),
			"bucketHoney",
			Items.bucket
		));

		/// To Honey Bottle from `bucketHoney`
		GameRegistry.addRecipe(new ShapelessMultiRecipe(
			GrowthCraftBees.fluids.honey.asBottleItemStack(3),
			Items.glass_bottle,
			Items.glass_bottle,
			Items.glass_bottle,
			new TaggedFluidStacks(1000, "honey")
		));

		GameRegistry.addRecipe(new ShapelessOreRecipe(
			GrowthCraftBees.fluids.honey.asBottleItemStack(3),
			Items.glass_bottle,
			Items.glass_bottle,
			Items.glass_bottle,
			"bucketHoney"
		));

		/// To Honey Bottle from 3 `bottleHoney`
		GameRegistry.addRecipe(new ShapelessOreRecipe(
			GrowthCraftBees.fluids.honey.asBottleItemStack(3),
			Items.glass_bottle,
			Items.glass_bottle,
			Items.glass_bottle,
			"bottleHoney",
			"bottleHoney",
			"bottleHoney"
		));

		/// To Honey Bucket from 3 `bottleHoney`
		GameRegistry.addRecipe(new ShapelessOreRecipe(
			GrowthCraftBees.fluids.honey.asBucketItemStack(),
			Items.bucket,
			"bottleHoney",
			"bottleHoney",
			"bottleHoney"
		));
	}
}
