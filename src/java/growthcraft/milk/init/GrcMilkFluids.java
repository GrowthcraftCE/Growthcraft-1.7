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
package growthcraft.milk.init;

import java.util.ArrayList;
import java.util.List;

import growthcraft.api.cellar.booze.Booze;
import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.core.CoreRegistry;
import growthcraft.api.core.effect.IEffect;
import growthcraft.api.core.GrcFluid;
import growthcraft.api.core.util.TickUtils;
import growthcraft.api.milk.MilkFluidTags;
import growthcraft.api.milk.MilkRegistry;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.common.item.ItemBoozeBottle;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.BoozeRegistryHelper;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.core.util.FluidFactory;
import growthcraft.milk.common.effect.EffectBoozeMilk;
import growthcraft.milk.common.effect.EffectEvilBoozeMilk;
import growthcraft.milk.GrowthCraftMilk;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidRegistry;

public class GrcMilkFluids extends GrcModuleBase
{
	private static final String milkBasename = "grcmilk.Milk";

	// not an actual Booze, but we use the class since it has the ability to set color
	public FluidFactory.FluidDetails butterMilk;
	public FluidFactory.FluidDetails cream;
	public FluidFactory.FluidDetails milk;
	public FluidFactory.FluidDetails curds;
	public FluidFactory.FluidDetails rennet;
	public FluidFactory.FluidDetails saltWater;
	public FluidFactory.FluidDetails skimMilk;
	public FluidFactory.FluidDetails whey;
	public Booze[] boozeMilk = new Booze[6];
	//public BlockBoozeDefinition
	public BlockBoozeDefinition[] boozeMilkBlocks = new BlockBoozeDefinition[boozeMilk.length];
	public ItemBucketBoozeDefinition[] boozeMilkBuckets = new ItemBucketBoozeDefinition[boozeMilk.length];
	public ItemDefinition milkBottle;

	@Override
	public void preInit()
	{
		this.butterMilk = FluidFactory.instance().create(new GrcFluid("grcmilk.ButterMilk"));
		this.cream = FluidFactory.instance().create(new GrcFluid("grcmilk.Cream"));
		this.milk = FluidFactory.instance().create(new GrcFluid("grcmilk.Milk"));
		this.curds = FluidFactory.instance().create(new GrcFluid("grcmilk.Curds"));
		this.rennet = FluidFactory.instance().create(new GrcFluid("grcmilk.Rennet"));
		this.saltWater = FluidFactory.instance().create(new GrcFluid("grcmilk.SaltWater"));
		this.skimMilk = FluidFactory.instance().create(new GrcFluid("grcmilk.SkimMilk"));
		this.whey = FluidFactory.instance().create(new GrcFluid("grcmilk.Whey"));

		butterMilk.setCreativeTab(GrowthCraftMilk.creativeTab).setItemColor(0xFFFEE7);
		cream.setCreativeTab(GrowthCraftMilk.creativeTab).setItemColor(0xFFFDD0);
		milk.setCreativeTab(GrowthCraftMilk.creativeTab).setItemColor(0xFFFFF6);
		curds.setCreativeTab(GrowthCraftMilk.creativeTab).setItemColor(0xFFFFF6);
		rennet.setCreativeTab(GrowthCraftMilk.creativeTab).setItemColor(0x877243);
		saltWater.setCreativeTab(GrowthCraftMilk.creativeTab).setItemColor(0x2C41F6);
		skimMilk.setCreativeTab(GrowthCraftMilk.creativeTab).setItemColor(0xFFFFFA);
		whey.setCreativeTab(GrowthCraftMilk.creativeTab).setItemColor(0x94a860);

		butterMilk.block.getBlock().setBlockTextureName("grcmilk:fluids/buttermilk");
		cream.block.getBlock().setBlockTextureName("grcmilk:fluids/cream");
		milk.block.getBlock().setBlockTextureName("grcmilk:fluids/milk");
		curds.block.getBlock().setBlockTextureName("grcmilk:fluids/milk");
		rennet.block.getBlock().setBlockTextureName("grcmilk:fluids/rennet");
		skimMilk.block.getBlock().setBlockTextureName("grcmilk:fluids/skimmilk");
		whey.block.getBlock().setBlockTextureName("grcmilk:fluids/whey");
		saltWater.block.getBlock().setBlockTextureName("minecraft:water");

		this.milkBottle = new ItemDefinition(new ItemBoozeBottle(5, -0.6F, boozeMilk));
		BoozeRegistryHelper.initializeBooze(boozeMilk, boozeMilkBlocks, boozeMilkBuckets, milkBasename, GrowthCraftMilk.getConfig().milkColor);
		boozeMilk[5].setColor(GrowthCraftMilk.getConfig().evilMilkColor);
		boozeMilkBlocks[5].getBlock().refreshColor();
		for (BlockBoozeDefinition def : boozeMilkBlocks)
		{
			def.getBlock().setBlockTextureName("grcmilk:fluids/milk");
		}
	}

	private void registerFermentations()
	{
		final IEffect milkEffect = EffectBoozeMilk.create(GrowthCraftCellar.potionTipsy);
		final IEffect evilMilkEffect = new EffectEvilBoozeMilk();

		final FluidStack[] fs = new FluidStack[boozeMilk.length];
		for (int i = 0; i < boozeMilk.length; ++i)
		{
			fs[i] = new FluidStack(boozeMilk[i], 1);
		}

		GrowthCraftCellar.boozeBuilderFactory.create(boozeMilk[0])
			.tags(BoozeTag.FERMENTED)
			.getEffect()
				.setTipsy(0.10f, 900)
				.addEffect(milkEffect);

		GrowthCraftCellar.boozeBuilderFactory.create(boozeMilk[1])
			.tags(BoozeTag.FERMENTED, BoozeTag.POTENT)
			.getEffect()
				.setTipsy(0.10f, 900)
				.addEffect(milkEffect);

		GrowthCraftCellar.boozeBuilderFactory.create(boozeMilk[2])
			.tags(BoozeTag.FERMENTED, BoozeTag.EXTENDED)
			.getEffect()
				.setTipsy(0.10f, 900)
				.addEffect(milkEffect);

		GrowthCraftCellar.boozeBuilderFactory.create(boozeMilk[3])
			.tags(BoozeTag.FERMENTED, BoozeTag.HYPER_EXTENDED)
			.getEffect()
				.setTipsy(0.10f, 900)
				.addEffect(milkEffect);

		GrowthCraftCellar.boozeBuilderFactory.create(boozeMilk[4])
			.tags(BoozeTag.FERMENTED, BoozeTag.INTOXICATED)
			.getEffect()
				.setTipsy(0.50f, 900)
				.addEffect(milkEffect);

		GrowthCraftCellar.boozeBuilderFactory.create(boozeMilk[5])
			.tags(BoozeTag.FERMENTED, BoozeTag.POISONED)
			.getEffect()
				.setTipsy(0.10f, 900)
				.addEffect(evilMilkEffect);
	}

	@Override
	public void register()
	{
		milkBottle.register("grcmilk.MilkBottle");

		butterMilk.registerObjects("grcmilk", "ButterMilk");
		cream.registerObjects("grcmilk", "Cream");
		milk.registerObjects("grcmilk", "Milk");
		curds.registerObjects("grcmilk", "Curds");
		rennet.registerObjects("grcmilk", "Rennet");
		skimMilk.registerObjects("grcmilk", "SkimMilk");
		whey.registerObjects("grcmilk", "Whey");
		saltWater.registerObjects("grcmilk", "SaltWater");

		BoozeRegistryHelper.registerBooze(boozeMilk, boozeMilkBlocks, boozeMilkBuckets, milkBottle, milkBasename, null);
		registerFermentations();

		GrowthCraftCellar.boozeBuilderFactory.create(rennet.fluid.getFluid())
			.brewsFrom(new FluidStack(FluidRegistry.WATER, 1000), GrowthCraftMilk.items.stomach.asStack(), TickUtils.minutes(1), null);
	}

	public List<Fluid> getMilkFluids()
	{
		final List<Fluid> milks = new ArrayList<Fluid>();
		milks.add(milk.getFluid());
		// Forestry Milk
		{
			final Fluid f =  FluidRegistry.getFluid("milk");
			if (f != null)
			{
				milks.add(f);
			}
		}
		return milks;
	}

	@Override
	public void init()
	{
		final List<Fluid> milks = getMilkFluids();
		for (Fluid f : milks)
		{
			CoreRegistry.instance().fluidDictionary().addFluidTags(f, MilkFluidTags.MILK);

			MilkRegistry.instance().pancheon().addRecipe(
				new FluidStack(f, 1000),
				cream.fluid.asFluidStack(333), skimMilk.fluid.asFluidStack(666),
				TickUtils.minutes(1));
		}

		CoreRegistry.instance().fluidDictionary().addFluidTags(cream.getFluid(), MilkFluidTags.CREAM);
		CoreRegistry.instance().fluidDictionary().addFluidTags(curds.getFluid(), MilkFluidTags.MILK_CURDS);
		CoreRegistry.instance().fluidDictionary().addFluidTags(rennet.getFluid(), MilkFluidTags.RENNET);

		MilkRegistry.instance().churn().addRecipe(
			cream.fluid.asFluidStack(1000),
			butterMilk.fluid.asFluidStack(500),
			GrowthCraftMilk.items.butter.asStack(2),
			16);
	}
}
