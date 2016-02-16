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
package growthcraft.nether.init;

import growthcraft.api.cellar.booze.Booze;
import growthcraft.api.cellar.booze.BoozeEffect;
import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.common.Residue;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.common.item.ItemBoozeBottle;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.BoozeRegistryHelper;
import growthcraft.cellar.util.YeastType;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.nether.GrowthCraftNether;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class GrcNetherFluids extends GrcModuleBase
{
	public Booze[] fireBrandyBooze;
	public Booze[] maliceCiderBooze;
	public BlockBoozeDefinition[] fireBrandyFluids;
	public BlockBoozeDefinition[] maliceCiderFluids;
	public ItemDefinition fireBrandy;
	public ItemDefinition maliceCider;
	public ItemBucketBoozeDefinition[] fireBrandyBuckets;
	public ItemBucketBoozeDefinition[] maliceCiderBuckets;

	@Override
	public void preInit()
	{
		this.fireBrandyBooze = new Booze[4];
		this.fireBrandyFluids = new BlockBoozeDefinition[fireBrandyBooze.length];
		this.fireBrandyBuckets = new ItemBucketBoozeDefinition[fireBrandyBooze.length];
		BoozeRegistryHelper.initializeBooze(fireBrandyBooze, fireBrandyFluids, fireBrandyBuckets, "grcnether.fireBrandy", GrowthCraftNether.getConfig().fireBrandyColor);

		this.maliceCiderBooze = new Booze[8];
		this.maliceCiderFluids = new BlockBoozeDefinition[maliceCiderBooze.length];
		this.maliceCiderBuckets = new ItemBucketBoozeDefinition[maliceCiderBooze.length];
		BoozeRegistryHelper.initializeBooze(maliceCiderBooze, maliceCiderFluids, maliceCiderBuckets, "grcnether.maliceCider", GrowthCraftNether.getConfig().maliceCiderColor);
		maliceCiderBooze[4].setColor(GrowthCraftNether.getConfig().amritaColor);
		maliceCiderFluids[4].getBlock().refreshColor();

		maliceCiderBooze[6].setColor(GrowthCraftNether.getConfig().gelidBoozeColor);
		maliceCiderFluids[6].getBlock().refreshColor();

		maliceCiderBooze[7].setColor(GrowthCraftNether.getConfig().vileSlopColor);
		maliceCiderFluids[7].getBlock().refreshColor();

		this.fireBrandy = new ItemDefinition(new ItemBoozeBottle(5, -0.6F, fireBrandyBooze));
		this.maliceCider = new ItemDefinition(new ItemBoozeBottle(5, -0.6F, maliceCiderBooze));
	}

	@Override
	public void register()
	{
		final ItemStack yeastRash = GrowthCraftNether.items.netherRashSpores.asStack();
		final int fermentTime = GrowthCraftCellar.getConfig().fermentTime;

		GameRegistry.registerItem(fireBrandy.getItem(), "grcnether.fireBrandy");
		GameRegistry.registerItem(maliceCider.getItem(), "grcnether.maliceCider");

		BoozeRegistryHelper.registerBooze(fireBrandyBooze, fireBrandyFluids, fireBrandyBuckets, fireBrandy, "grcnether.fireBrandy", null);
		BoozeRegistryHelper.registerBooze(maliceCiderBooze, maliceCiderFluids, maliceCiderBuckets, maliceCider, "grcnether.maliceCider", null);

		for (BoozeEffect effect : BoozeRegistryHelper.getBoozeEffects(fireBrandyBooze))
		{
			effect.setTipsy(0.70F, 900);
			effect.addPotionEntry(Potion.fireResistance, 3600, 0);
		}

		{
			final FluidStack[] fs = new FluidStack[maliceCiderBooze.length];
			for (int i = 0; i < maliceCiderBooze.length; ++i)
			{
				fs[i] = new FluidStack(maliceCiderBooze[i], 1);
			}

			GrowthCraftCellar.boozeBuilderFactory.create(maliceCiderBooze[0])
				.tags(BoozeTag.CIDER, BoozeTag.YOUNG)
				.pressesFrom(
					GrowthCraftNether.items.netherMaliceFruit.asStack(),
					GrowthCraftNether.getConfig().maliceCiderPressingTime,
					GrowthCraftNether.getConfig().maliceCiderYield,
					Residue.newDefault(0.3F));


			// fermented
			GrowthCraftCellar.boozeBuilderFactory.create(maliceCiderBooze[1])
				.tags(BoozeTag.CIDER, BoozeTag.FERMENTED)
				.fermentsFrom(fs[0], YeastType.BREWERS.asStack(), fermentTime)
				.fermentsFrom(fs[0], new ItemStack(Items.nether_wart), (int)(fermentTime * 0.66))
				.getEffect()
					.setTipsy(1.00F, 900)
					.addPotionEntry(Potion.regeneration, 3600, 0)
					.addPotionEntry(Potion.damageBoost, 1200, 1);

			// potent
			GrowthCraftCellar.boozeBuilderFactory.create(maliceCiderBooze[2])
				.tags(BoozeTag.CIDER, BoozeTag.FERMENTED, BoozeTag.POTENT)
				.fermentsFrom(fs[1], new ItemStack(Items.glowstone_dust), fermentTime)
				.fermentsFrom(fs[3], new ItemStack(Items.glowstone_dust), fermentTime)
				.getEffect()
					.setTipsy(1.00F, 900)
					.addPotionEntry(Potion.regeneration, 3600, 0)
					.addPotionEntry(Potion.damageBoost, 1200, 1);

			// extended
			GrowthCraftCellar.boozeBuilderFactory.create(maliceCiderBooze[3])
				.tags(BoozeTag.CIDER, BoozeTag.FERMENTED, BoozeTag.EXTENDED)
				.fermentsFrom(fs[1], new ItemStack(Items.redstone), fermentTime)
				.fermentsFrom(fs[2], new ItemStack(Items.redstone), fermentTime)
				.getEffect()
					.setTipsy(1.00F, 900)
					.addPotionEntry(Potion.regeneration, 3600, 0)
					.addPotionEntry(Potion.damageBoost, 1200, 1);

			// Amrita - Ethereal Yeast
			GrowthCraftCellar.boozeBuilderFactory.create(maliceCiderBooze[4])
				.tags(BoozeTag.CIDER, BoozeTag.FERMENTED, BoozeTag.HYPER_EXTENDED)
				.fermentsFrom(fs[2], YeastType.ETHEREAL.asStack(), fermentTime)
				.fermentsFrom(fs[3], YeastType.ETHEREAL.asStack(), fermentTime)
				.getEffect()
					.setTipsy(1.00F, 900)
					.addPotionEntry(Potion.regeneration, 3600, 0)
					.addPotionEntry(Potion.damageBoost, 1200, 1);

			// :Intoxicated - Origin Yeast
			GrowthCraftCellar.boozeBuilderFactory.create(maliceCiderBooze[5])
				.tags(BoozeTag.CIDER, BoozeTag.FERMENTED, BoozeTag.INTOXICATED)
				.fermentsFrom(fs[2], YeastType.ORIGIN.asStack(), fermentTime)
				.fermentsFrom(fs[3], YeastType.ORIGIN.asStack(), fermentTime)
				.getEffect()
					.setTipsy(1.00F, 900)
					.addPotionEntry(Potion.regeneration, 3600, 0)
					.addPotionEntry(Potion.damageBoost, 1200, 1);

			// Gelid Booze - Lager Yeast
			GrowthCraftCellar.boozeBuilderFactory.create(maliceCiderBooze[6])
				.tags(BoozeTag.CIDER, BoozeTag.FERMENTED, BoozeTag.CHILLED)
				.fermentsFrom(fs[2], YeastType.LAGER.asStack(), fermentTime)
				.fermentsFrom(fs[3], YeastType.LAGER.asStack(), fermentTime)
				.getEffect()
					.setTipsy(1.00F, 900)
					.addPotionEntry(Potion.regeneration, 3600, 0)
					.addPotionEntry(Potion.moveSpeed, 1200, 1);

			// Vile Slop - Netherrash
			GrowthCraftCellar.boozeBuilderFactory.create(maliceCiderBooze[7])
				.tags(BoozeTag.CIDER, BoozeTag.FERMENTED, BoozeTag.INTOXICATED, BoozeTag.DEADLY)
				.fermentsFrom(fs[1], yeastRash, fermentTime)
				.fermentsFrom(fs[2], yeastRash, fermentTime)
				.fermentsFrom(fs[3], yeastRash, fermentTime)
				.fermentsFrom(fs[4], yeastRash, fermentTime)
				.fermentsFrom(fs[5], yeastRash, fermentTime)
				.fermentsFrom(fs[6], yeastRash, fermentTime)
				.getEffect()
					.setTipsy(1.00F, 900)
					.addPotionEntry(Potion.regeneration, 3600, 0)
					.addPotionEntry(Potion.damageBoost, 1200, 1);
		}

		{
			final FluidStack[] fs = new FluidStack[fireBrandyBooze.length];
			for (int i = 0; i < fireBrandyBooze.length; ++i)
			{
				fs[i] = new FluidStack(fireBrandyBooze[i], 1);
			}

			GrowthCraftCellar.boozeBuilderFactory.create(fireBrandyBooze[0])
				.tags(BoozeTag.YOUNG)
				.brewsFrom(
					new FluidStack(FluidRegistry.WATER, GrowthCraftNether.getConfig().fireBrandyYield),
					GrowthCraftNether.blocks.netherCinderrot.asStack(),
					GrowthCraftNether.getConfig().fireBrandyBrewTime,
					Residue.newDefault(0.5F));
		}
	}

	public void setBoozeIcons(IIcon icon)
	{
		for (Booze booze : fireBrandyBooze)
		{
			booze.setIcons(icon);
		}

		for (Booze booze : maliceCiderBooze)
		{
			booze.setIcons(icon);
		}
	}
}
