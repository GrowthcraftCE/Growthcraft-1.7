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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import growthcraft.api.cellar.booze.Booze;
import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.util.ICellarBoozeBuilder;
import growthcraft.api.core.CoreRegistry;
import growthcraft.api.core.effect.IEffect;
import growthcraft.api.core.GrcFluid;
import growthcraft.api.core.util.StringUtils;
import growthcraft.api.core.util.TickUtils;
import growthcraft.api.milk.MilkFluidTags;
import growthcraft.api.milk.MilkRegistry;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.common.item.ItemBoozeBottle;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.BoozeRegistryHelper;
import growthcraft.cellar.util.YeastType;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.core.eventhandler.EventHandlerBucketFill;
import growthcraft.core.integration.forestry.ForestryFluids;
import growthcraft.core.util.FluidFactory;
import growthcraft.milk.common.effect.EffectBoozeMilk;
import growthcraft.milk.common.effect.EffectEvilBoozeMilk;
import growthcraft.milk.common.item.EnumCheeseType;
import growthcraft.milk.GrowthCraftMilk;

import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class GrcMilkFluids extends GrcModuleBase
{
	private static final String kumisBasename = "grcmilk.Kumis";

	// not an actual Booze, but we use the class since it has the ability to set color
	public FluidFactory.FluidDetails butterMilk;
	public FluidFactory.FluidDetails cream;
	public FluidFactory.FluidDetails milk;
	public FluidFactory.FluidDetails curds;
	public FluidFactory.FluidDetails rennet;
	public FluidFactory.FluidDetails skimMilk;
	public FluidFactory.FluidDetails whey;
	public Map<EnumCheeseType, FluidFactory.FluidDetails> cheeses = new HashMap<EnumCheeseType, FluidFactory.FluidDetails>();
	public Map<Fluid, EnumCheeseType> fluidToCheeseType = new HashMap<Fluid, EnumCheeseType>();
	public Booze[] kumisFluids = new Booze[6];
	public BlockBoozeDefinition[] kumisFluidBlocks = new BlockBoozeDefinition[kumisFluids.length];
	public ItemBucketBoozeDefinition[] kumisFluidBuckets = new ItemBucketBoozeDefinition[kumisFluids.length];
	public ItemDefinition kumisBottle;

	@Override
	public void preInit()
	{
		if (GrowthCraftMilk.getConfig().milkEnabled)
			this.milk = FluidFactory.instance().create(new GrcFluid("grcmilk.Milk"), FluidFactory.FEATURE_BOTTLE | FluidFactory.FEATURE_BLOCK);

		if (milk != null) milk.setCreativeTab(GrowthCraftMilk.creativeTab).setItemColor(0xFFFFFF);
		if (milk != null) milk.block.getBlock().setBlockTextureName("grcmilk:fluids/milk");

		this.butterMilk = FluidFactory.instance().create(new GrcFluid("grcmilk.ButterMilk"));
		butterMilk.setCreativeTab(GrowthCraftMilk.creativeTab).setItemColor(0xFFFEE7);
		butterMilk.block.getBlock().setBlockTextureName("grcmilk:fluids/buttermilk");

		this.cream = FluidFactory.instance().create(new GrcFluid("grcmilk.Cream"));
		cream.setCreativeTab(GrowthCraftMilk.creativeTab).setItemColor(0xFFFDD0);
		cream.block.getBlock().setBlockTextureName("grcmilk:fluids/cream");

		this.curds = FluidFactory.instance().create(new GrcFluid("grcmilk.Curds"));
		curds.setCreativeTab(GrowthCraftMilk.creativeTab).setItemColor(0xFFFFF6);
		curds.block.getBlock().setBlockTextureName("grcmilk:fluids/milk");

		this.rennet = FluidFactory.instance().create(new GrcFluid("grcmilk.Rennet"));
		rennet.setCreativeTab(GrowthCraftMilk.creativeTab).setItemColor(0x877243);
		rennet.block.getBlock().setBlockTextureName("grcmilk:fluids/rennet");

		this.skimMilk = FluidFactory.instance().create(new GrcFluid("grcmilk.SkimMilk"));
		skimMilk.setCreativeTab(GrowthCraftMilk.creativeTab).setItemColor(0xFFFFFA);
		skimMilk.block.getBlock().setBlockTextureName("grcmilk:fluids/skimmilk");

		this.whey = FluidFactory.instance().create(new GrcFluid("grcmilk.Whey"));
		whey.setCreativeTab(GrowthCraftMilk.creativeTab).setItemColor(0x94a860);
		whey.block.getBlock().setBlockTextureName("grcmilk:fluids/whey");

		for (EnumCheeseType cheese : EnumCheeseType.VALUES)
		{
			final String fluidName = "grcmilk.Cheese" + StringUtils.capitalize(cheese.name);
			final Fluid fluid = new GrcFluid(fluidName).setColor(cheese.getColor());
			final FluidFactory.FluidDetails details = FluidFactory.instance().create(fluid);
			cheeses.put(cheese, details);
			details.block.getBlock().setColor(cheese.getColor()).setBlockTextureName("grcmilk:fluids/milk");
			details.setItemColor(cheese.getColor());
			fluidToCheeseType.put(fluid, cheese);
		}

		this.kumisBottle = new ItemDefinition(new ItemBoozeBottle(5, -0.6F, kumisFluids));
		BoozeRegistryHelper.initializeBooze(kumisFluids, kumisFluidBlocks, kumisFluidBuckets, kumisBasename, GrowthCraftMilk.getConfig().kumisColor);
		kumisFluids[5].setColor(GrowthCraftMilk.getConfig().poisonedKumisColor);
		kumisFluidBlocks[5].getBlock().refreshColor();
		for (BlockBoozeDefinition def : kumisFluidBlocks)
		{
			def.getBlock().setBlockTextureName("grcmilk:fluids/milk");
		}
	}

	@Override
	public void register()
	{
		kumisBottle.register("grcmilk.KumisBottle");

		if (milk != null)
		{
			milk.registerObjects("grcmilk", "Milk");
			// ensure that we don't already have some variation of milk present
			if (FluidRegistry.getFluid("milk") == null)
			{
				FluidContainerRegistry.registerFluidContainer(milk.getFluid(), new ItemStack(Items.milk_bucket, 1), new ItemStack(Items.bucket, 1));
				EventHandlerBucketFill.instance().register(milk.getFluidBlock(), new ItemStack(Items.milk_bucket, 1));
			}
		}
		butterMilk.registerObjects("grcmilk", "ButterMilk");
		cream.registerObjects("grcmilk", "Cream");
		curds.registerObjects("grcmilk", "Curds");
		rennet.registerObjects("grcmilk", "Rennet");
		skimMilk.registerObjects("grcmilk", "SkimMilk");
		whey.registerObjects("grcmilk", "Whey");

		for (Map.Entry<EnumCheeseType, FluidFactory.FluidDetails> pair : cheeses.entrySet())
		{
			pair.getValue().registerObjects("grcmilk", "Cheese" + StringUtils.capitalize(pair.getKey().name));
		}

		BoozeRegistryHelper.registerBooze(kumisFluids, kumisFluidBlocks, kumisFluidBuckets, kumisBottle, kumisBasename, null);

		CoreRegistry.instance().fluidDictionary().addFluidTags(cream.getFluid(), MilkFluidTags.CREAM);
		CoreRegistry.instance().fluidDictionary().addFluidTags(curds.getFluid(), MilkFluidTags.MILK_CURDS);
		CoreRegistry.instance().fluidDictionary().addFluidTags(rennet.getFluid(), MilkFluidTags.RENNET);
		CoreRegistry.instance().fluidDictionary().addFluidTags(whey.getFluid(), MilkFluidTags.WHEY);
	}

	public List<Fluid> getMilkFluids()
	{
		final List<Fluid> milks = new ArrayList<Fluid>();
		if (milk != null) milks.add(milk.getFluid());
		if (ForestryFluids.MILK.exists()) milks.add(ForestryFluids.MILK.getFluid());
		return milks;
	}

	private void registerFermentations()
	{
		final IEffect milkEffect = EffectBoozeMilk.create(GrowthCraftCellar.potionTipsy);
		final IEffect evilMilkEffect = new EffectEvilBoozeMilk();

		final FluidStack[] fs = new FluidStack[kumisFluids.length];
		for (int i = 0; i < kumisFluids.length; ++i)
		{
			fs[i] = new FluidStack(kumisFluids[i], 1);
		}

		final List<Fluid> milks = getMilkFluids();

		final int fermentTime = GrowthCraftCellar.getConfig().fermentTime;
		final ICellarBoozeBuilder builder = GrowthCraftCellar.boozeBuilderFactory.create(kumisFluids[0]);
		builder
			.tags(BoozeTag.FERMENTED)
			.getEffect()
				.setTipsy(0.10f, 900)
				.addEffect(milkEffect);

		for (Fluid fluid : milks)
		{
			final FluidStack milkStack = new FluidStack(fluid, 1);
			builder.fermentsFrom(milkStack, YeastType.BREWERS.asStack(), fermentTime);
			builder.fermentsFrom(milkStack, new ItemStack(Items.nether_wart), (int)(fermentTime * 0.66));
		}

		GrowthCraftCellar.boozeBuilderFactory.create(kumisFluids[1])
			.tags(BoozeTag.FERMENTED, BoozeTag.POTENT)
			.fermentsFrom(fs[0], new ItemStack(Items.glowstone_dust), fermentTime)
			.fermentsFrom(fs[2], new ItemStack(Items.glowstone_dust), fermentTime)
			.getEffect()
				.setTipsy(0.10f, 900)
				.addEffect(milkEffect);

		GrowthCraftCellar.boozeBuilderFactory.create(kumisFluids[2])
			.tags(BoozeTag.FERMENTED, BoozeTag.EXTENDED)
			.fermentsFrom(fs[0], new ItemStack(Items.redstone), fermentTime)
			.fermentsFrom(fs[1], new ItemStack(Items.redstone), fermentTime)
			.getEffect()
				.setTipsy(0.10f, 900)
				.addEffect(milkEffect);

		GrowthCraftCellar.boozeBuilderFactory.create(kumisFluids[3])
			.tags(BoozeTag.FERMENTED, BoozeTag.HYPER_EXTENDED)
			.fermentsFrom(fs[1], YeastType.ETHEREAL.asStack(), fermentTime)
			.fermentsFrom(fs[2], YeastType.ETHEREAL.asStack(), fermentTime)
			.getEffect()
				.setTipsy(0.10f, 900)
				.addEffect(milkEffect);

		GrowthCraftCellar.boozeBuilderFactory.create(kumisFluids[4])
			.tags(BoozeTag.FERMENTED, BoozeTag.INTOXICATED)
			.fermentsFrom(fs[1], YeastType.ORIGIN.asStack(), fermentTime)
			.fermentsFrom(fs[2], YeastType.ORIGIN.asStack(), fermentTime)
			.getEffect()
				.setTipsy(0.50f, 900)
				.addEffect(milkEffect);

		GrowthCraftCellar.boozeBuilderFactory.create(kumisFluids[5])
			.tags(BoozeTag.FERMENTED, BoozeTag.POISONED)
			.getEffect()
				.setTipsy(0.10f, 900)
				.addEffect(evilMilkEffect);
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

		registerFermentations();

		GrowthCraftCellar.boozeBuilderFactory.create(rennet.fluid.getFluid())
			.brewsFrom(new FluidStack(FluidRegistry.WATER, 1000), GrowthCraftMilk.items.stomach.asStack(), TickUtils.minutes(1), null);

		for (Map.Entry<EnumCheeseType, FluidFactory.FluidDetails> pair : cheeses.entrySet())
		{
			CoreRegistry.instance().fluidDictionary().addFluidTags(pair.getValue().getFluid(), MilkFluidTags.CHEESE);
		}

		MilkRegistry.instance().churn().addRecipe(
			cream.fluid.asFluidStack(1000),
			butterMilk.fluid.asFluidStack(500),
			GrowthCraftMilk.items.butter.asStack(2),
			16);
	}
}
