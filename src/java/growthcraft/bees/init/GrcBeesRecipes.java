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
import growthcraft.api.core.CoreRegistry;
import growthcraft.api.core.effect.EffectAddPotionEffect;
import growthcraft.api.core.effect.EffectWeightedRandomList;
import growthcraft.api.core.effect.SimplePotionEffectFactory;
import growthcraft.api.core.util.TickUtils;
import growthcraft.bees.GrowthCraftBees;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.BoozeUtils;
import growthcraft.cellar.common.item.EnumYeast;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.core.GrowthCraftCore;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class GrcBeesRecipes extends GrcModuleBase
{
	private void registerRecipes()
	{
		final int fermentTime = GrowthCraftCellar.getConfig().fermentTime;
		final FluidStack[] fs = new FluidStack[GrowthCraftBees.fluids.honeyMeadBooze.length];
		for (int i = 0; i < GrowthCraftBees.fluids.honeyMeadBooze.length; ++i)
		{
			fs[i] = new FluidStack(GrowthCraftBees.fluids.honeyMeadBooze[i], 1);
		}

		GrowthCraftCellar.boozeBuilderFactory.create(GrowthCraftBees.fluids.honeyMeadBooze[0])
			.tags(BoozeTag.YOUNG);

		GrowthCraftCellar.boozeBuilderFactory.create(GrowthCraftBees.fluids.honeyMeadBooze[1])
			.tags(BoozeTag.FERMENTED)
			.fermentsFrom(fs[0], EnumYeast.BREWERS.asStack(), fermentTime)
			.fermentsFrom(fs[0], new ItemStack(Items.nether_wart), (int)(fermentTime * 0.66))
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.15f), TickUtils.seconds(90))
				.addPotionEntry(Potion.regeneration, TickUtils.seconds(90), 0);

		GrowthCraftCellar.boozeBuilderFactory.create(GrowthCraftBees.fluids.honeyMeadBooze[2])
			.tags(BoozeTag.FERMENTED, BoozeTag.POTENT)
			.fermentsFrom(fs[1], new ItemStack(Items.glowstone_dust), fermentTime)
			.fermentsFrom(fs[3], new ItemStack(Items.glowstone_dust), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.17f), TickUtils.seconds(90))
				.addPotionEntry(Potion.regeneration, TickUtils.seconds(90), 0);

		GrowthCraftCellar.boozeBuilderFactory.create(GrowthCraftBees.fluids.honeyMeadBooze[3])
			.tags(BoozeTag.FERMENTED, BoozeTag.EXTENDED)
			.fermentsFrom(fs[1], new ItemStack(Items.redstone), fermentTime)
			.fermentsFrom(fs[2], new ItemStack(Items.redstone), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.15f), TickUtils.seconds(90))
				.addPotionEntry(Potion.regeneration, TickUtils.seconds(90), 0);

		GrowthCraftCellar.boozeBuilderFactory.create(GrowthCraftBees.fluids.honeyMeadBooze[4])
			.tags(BoozeTag.FERMENTED, BoozeTag.HYPER_EXTENDED)
			.fermentsFrom(fs[2], EnumYeast.ETHEREAL.asStack(), fermentTime)
			.fermentsFrom(fs[3], EnumYeast.ETHEREAL.asStack(), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.15f), TickUtils.seconds(90))
				.addPotionEntry(Potion.regeneration, TickUtils.seconds(90), 0);

		GrowthCraftCellar.boozeBuilderFactory.create(GrowthCraftBees.fluids.honeyMeadBooze[5])
			.tags(BoozeTag.FERMENTED, BoozeTag.INTOXICATED)
			.fermentsFrom(fs[2], EnumYeast.ORIGIN.asStack(), fermentTime)
			.fermentsFrom(fs[3], EnumYeast.ORIGIN.asStack(), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.15f * 1.5f), TickUtils.seconds(90))
				.addEffect(new EffectWeightedRandomList()
					.add(8, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.regeneration.id, TickUtils.seconds(90), 2)))
					.add(2, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.poison.id, TickUtils.seconds(90), 2)))
				);

		GrowthCraftCellar.boozeBuilderFactory.create(GrowthCraftBees.fluids.honeyMeadBooze[6])
			.tags(BoozeTag.FERMENTED, BoozeTag.POISONED)
			//.fermentsFrom(fs[0], EnumYeast.NETHERRASH.asStack(), fermentTime)
			//.fermentsFrom(fs[1], EnumYeast.NETHERRASH.asStack(), fermentTime)
			//.fermentsFrom(fs[2], EnumYeast.NETHERRASH.asStack(), fermentTime)
			//.fermentsFrom(fs[3], EnumYeast.NETHERRASH.asStack(), fermentTime)
			//.fermentsFrom(fs[4], EnumYeast.NETHERRASH.asStack(), fermentTime)
			//.fermentsFrom(fs[5], EnumYeast.NETHERRASH.asStack(), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.15f), TickUtils.seconds(90))
				.createPotionEntry(Potion.poison, TickUtils.seconds(90), 0).toggleDescription(!GrowthCraftCore.getConfig().hidePoisonedBooze);
	}

	@Override
	public void register()
	{
		final ItemStack waterBottle = new ItemStack(Items.potionitem, 1, 0);

		// Bucket of mead recipes
		/// Single bucket
		GameRegistry.addShapelessRecipe(GrowthCraftBees.fluids.honeyMeadBuckets[0].asStack(),
			Items.water_bucket,
			GrowthCraftBees.items.honeyJar.getItem(),
			Items.bucket);
		GameRegistry.addShapelessRecipe(GrowthCraftBees.fluids.honeyMeadBuckets[0].asStack(),
			waterBottle, waterBottle, waterBottle,
			GrowthCraftBees.items.honeyJar.getItem(),
			Items.bucket);
		
		if (GrowthCraftBees.fluids.honey != null) 
		{
			final ItemStack honeyBottleStack = GrowthCraftBees.fluids.honey.asBottleItemStack();
			final ItemStack meadBucket = GrowthCraftBees.fluids.honeyMeadBuckets[0].asStack();
			
			GameRegistry.addShapelessRecipe(meadBucket,
					Items.water_bucket,
					honeyBottleStack, honeyBottleStack, honeyBottleStack,
					Items.bucket);
			
			GameRegistry.addShapelessRecipe(meadBucket,
					waterBottle, waterBottle, waterBottle,
					honeyBottleStack, honeyBottleStack, honeyBottleStack,
					Items.bucket);
		}
		
	{
		GrowthCraftCellar.boozeBuilderFactory.create(GrowthCraftBees.fluids.beeWaste.fluid.getFluid())
			.brewsFrom(new FluidStack(FluidRegistry.WATER, 1000),
			new ItemStack(grc.honeyCombEmpty), TickUtils.minutes(1), new Residue(grcbees.BeesWax.asStack(1), 1.0f));
	}

		registerRecipes();
	}
	
	@Override
	public void postInit()
	{
		final ItemStack waterBottle = new ItemStack(Items.potionitem, 1, 0);
		
		for (Fluid fluid : CoreRegistry.instance().fluidDictionary().getFluidsByTags(BeesFluidTag.HONEY))
		{
			final ItemStack honeyBucket = FluidContainerRegistry.fillFluidContainer(new FluidStack(fluid, 1000), new ItemStack(Items.bucket));
			final ItemStack meadBucket = GrowthCraftBees.fluids.honeyMeadBuckets[0].asStack();
			
			if (honeyBucket != null)
			{
				GameRegistry.addShapelessRecipe(meadBucket,
					Items.water_bucket,
					honeyBucket,
					Items.bucket);
				
				/// Water bottles
				GameRegistry.addShapelessRecipe(meadBucket,
					waterBottle, waterBottle, waterBottle,
					honeyBucket,
					Items.bucket);
			}
		}
	}
}
