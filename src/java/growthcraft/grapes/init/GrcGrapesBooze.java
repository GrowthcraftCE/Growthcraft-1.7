package growthcraft.grapes.init;

import growthcraft.api.cellar.booze.Booze;
import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.common.Residue;
import growthcraft.api.core.effect.EffectAddPotionEffect;
import growthcraft.api.core.effect.EffectWeightedRandomList;
import growthcraft.api.core.effect.SimplePotionEffectFactory;
import growthcraft.api.core.util.TickUtils;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.common.item.ItemBoozeBottle;
import growthcraft.cellar.common.item.ItemBoozeBucketDEPRECATED;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.BoozeRegistryHelper;
import growthcraft.cellar.util.YeastType;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.core.GrowthCraftCore;
import growthcraft.grapes.GrowthCraftGrapes;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class GrcGrapesBooze extends GrcModuleBase
{
	public Booze[] grapeWineBooze;
	public BlockBoozeDefinition[] grapeWineFluids;
	public ItemDefinition grapeWine;
	public ItemDefinition grapeWineBucket_deprecated;
	public ItemBucketBoozeDefinition[] grapeWineBuckets;

	@Override
	public void preInit()
	{
		grapeWineBooze = new Booze[8];
		grapeWineFluids = new BlockBoozeDefinition[grapeWineBooze.length];
		grapeWineBuckets = new ItemBucketBoozeDefinition[grapeWineBooze.length];
		BoozeRegistryHelper.initializeBooze(grapeWineBooze, grapeWineFluids, grapeWineBuckets, "grc.grapeWine", GrowthCraftGrapes.getConfig().grapeWineColor);
		grapeWineBooze[4].setColor(GrowthCraftGrapes.getConfig().ambrosiaColor);
		grapeWineFluids[4].getBlock().refreshColor();
		grapeWineBooze[5].setColor(GrowthCraftGrapes.getConfig().portWineColor);
		grapeWineFluids[5].getBlock().refreshColor();

		grapeWine        = new ItemDefinition(new ItemBoozeBottle(2, -0.3F, grapeWineBooze));
		grapeWineBucket_deprecated = new ItemDefinition(new ItemBoozeBucketDEPRECATED(grapeWineBooze).setColor(GrowthCraftGrapes.getConfig().grapeWineColor));
	}

	private void registerFermentations()
	{
		final float defaultTipsy = 0.60f;
		final int fermentTime = GrowthCraftCellar.getConfig().fermentTime;
		final FluidStack[] fs = new FluidStack[grapeWineBooze.length];
		for (int i = 0; i < grapeWineBooze.length; ++i)
		{
			fs[i] = new FluidStack(grapeWineBooze[i], 1);
		}

		GrowthCraftCellar.boozeBuilderFactory.create(grapeWineBooze[0])
			.tags(BoozeTag.WINE, BoozeTag.YOUNG)
			.pressesFrom(
				GrowthCraftGrapes.grapes.asStack(),
				TickUtils.seconds(2),
				40,
				Residue.newDefault(0.3F));

		// Brewers Yeast, Nether Wart
		GrowthCraftCellar.boozeBuilderFactory.create(grapeWineBooze[1])
			.tags(BoozeTag.WINE, BoozeTag.FERMENTED)
			.fermentsFrom(fs[0], YeastType.BREWERS.asStack(), fermentTime)
			.fermentsFrom(fs[0], new ItemStack(Items.nether_wart), (int)(fermentTime * 0.66))
			.getEffect()
				.setTipsy(defaultTipsy, TickUtils.seconds(90))
				.addPotionEntry(Potion.resistance, TickUtils.minutes(3), 0);

		// Glowstone Dust
		GrowthCraftCellar.boozeBuilderFactory.create(grapeWineBooze[2])
			.tags(BoozeTag.WINE, BoozeTag.FERMENTED, BoozeTag.POTENT)
			.fermentsFrom(fs[1], new ItemStack(Items.glowstone_dust), fermentTime)
			.fermentsFrom(fs[3], new ItemStack(Items.glowstone_dust), fermentTime)
			.getEffect()
				.setTipsy(defaultTipsy, TickUtils.seconds(90))
				.addPotionEntry(Potion.resistance, TickUtils.minutes(3), 0);

		// Redstone Dust
		GrowthCraftCellar.boozeBuilderFactory.create(grapeWineBooze[3])
			.tags(BoozeTag.WINE, BoozeTag.FERMENTED, BoozeTag.EXTENDED)
			.fermentsFrom(fs[1], new ItemStack(Items.redstone), fermentTime)
			.fermentsFrom(fs[2], new ItemStack(Items.redstone), fermentTime)
			.getEffect()
				.setTipsy(defaultTipsy, TickUtils.seconds(90))
				.addPotionEntry(Potion.resistance, TickUtils.minutes(3), 0);

		// Ambrosia - Ethereal Yeast
		GrowthCraftCellar.boozeBuilderFactory.create(grapeWineBooze[4])
			.tags(BoozeTag.WINE, BoozeTag.FERMENTED, BoozeTag.HYPER_EXTENDED)
			.fermentsFrom(fs[2], YeastType.ETHEREAL.asStack(), fermentTime)
			.fermentsFrom(fs[3], YeastType.ETHEREAL.asStack(), fermentTime)
			.getEffect()
				.setTipsy(defaultTipsy, TickUtils.seconds(90))
				.addPotionEntry(Potion.field_76434_w, TickUtils.minutes(3), 0)
				.addPotionEntry(Potion.resistance, TickUtils.minutes(3), 0);

		// Port Wine - Bayanus Yeast
		GrowthCraftCellar.boozeBuilderFactory.create(grapeWineBooze[5])
			.tags(BoozeTag.WINE, BoozeTag.FERMENTED, BoozeTag.FORTIFIED)
			.brewsFrom(
				new FluidStack(grapeWineBooze[1], GrowthCraftGrapes.getConfig().portWineBrewingYield),
				YeastType.BAYANUS.asStack(),
				GrowthCraftGrapes.getConfig().portWineBrewingTime,
				Residue.newDefault(0.3F))
			.getEffect()
				.setTipsy(defaultTipsy, TickUtils.seconds(90))
				.addPotionEntry(Potion.resistance, TickUtils.minutes(3), 2);

		// Intoxicated Wine
		GrowthCraftCellar.boozeBuilderFactory.create(grapeWineBooze[6])
			.tags(BoozeTag.WINE, BoozeTag.FERMENTED, BoozeTag.INTOXICATED)
			.fermentsFrom(fs[2], YeastType.ORIGIN.asStack(), fermentTime)
			.fermentsFrom(fs[3], YeastType.ORIGIN.asStack(), fermentTime)
			.getEffect()
				.setTipsy(0.80F, TickUtils.seconds(90))
				.addEffect(new EffectWeightedRandomList()
					.add(8, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.resistance.id, TickUtils.minutes(3), 2)))
					.add(2, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.weakness.id, TickUtils.minutes(3), 2))));

		GrowthCraftCellar.boozeBuilderFactory.create(grapeWineBooze[7])
			.tags(BoozeTag.WINE, BoozeTag.FERMENTED, BoozeTag.POISONED)
			//.fermentsTo(fs[1], YeastType.NETHERRASH.asStack(), fermentTime)
			//.fermentsTo(fs[2], YeastType.NETHERRASH.asStack(), fermentTime)
			//.fermentsTo(fs[3], YeastType.NETHERRASH.asStack(), fermentTime)
			//.fermentsTo(fs[4], YeastType.NETHERRASH.asStack(), fermentTime)
			//.fermentsTo(fs[5], YeastType.NETHERRASH.asStack(), fermentTime)
			//.fermentsTo(fs[6], YeastType.NETHERRASH.asStack(), fermentTime)
			.getEffect()
				.setTipsy(defaultTipsy, TickUtils.seconds(90))
				.createPotionEntry(Potion.poison, TickUtils.seconds(90), 0).toggleDescription(!GrowthCraftCore.getConfig().hidePoisonedBooze);
	}

	@Override
	public void register()
	{
		GameRegistry.registerItem(grapeWine.getItem(), "grc.grapeWine");
		GameRegistry.registerItem(grapeWineBucket_deprecated.getItem(), "grc.grapeWine_bucket");

		BoozeRegistryHelper.registerBooze(grapeWineBooze, grapeWineFluids, grapeWineBuckets, grapeWine, "grc.grapeWine", grapeWineBucket_deprecated);
		registerFermentations();

		OreDictionary.registerOre("foodGrapejuice", grapeWine.asStack(1, 0));
	}
}
