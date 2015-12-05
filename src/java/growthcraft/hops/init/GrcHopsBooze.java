package growthcraft.hops.init;

import growthcraft.api.cellar.booze.Booze;
import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.common.Residue;
import growthcraft.api.cellar.util.CellarBoozeBuilder;
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
import growthcraft.hops.GrowthCraftHops;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidRegistry;

public class GrcHopsBooze extends GrcModuleBase
{
	public Booze[] hopAleBooze;
	public Booze[] lagerBooze;
	public BlockBoozeDefinition[] hopAleFluids;
	public BlockBoozeDefinition[] lagerFluids;
	public ItemDefinition hopAle;
	public ItemDefinition lager;
	public ItemDefinition hopAleBucket_deprecated;
	public ItemBucketBoozeDefinition[] hopAleBuckets;
	public ItemBucketBoozeDefinition[] lagerBuckets;

	@Override
	public void preInit()
	{
		lagerBooze = new Booze[7];
		lagerFluids = new BlockBoozeDefinition[lagerBooze.length];
		lagerBuckets = new ItemBucketBoozeDefinition[lagerBooze.length];
		BoozeRegistryHelper.initializeBooze(lagerBooze, lagerFluids, lagerBuckets, "grc.lager", GrowthCraftHops.getConfig().lagerColor);

		lager = new ItemDefinition(new ItemBoozeBottle(5, -0.6F, lagerBooze));

		hopAleBooze = new Booze[9];
		hopAleFluids = new BlockBoozeDefinition[hopAleBooze.length];
		hopAleBuckets = new ItemBucketBoozeDefinition[hopAleBooze.length];
		BoozeRegistryHelper.initializeBooze(hopAleBooze, hopAleFluids, hopAleBuckets, "grc.hopAle", GrowthCraftHops.getConfig().hopAleColor);

		hopAle = new ItemDefinition(new ItemBoozeBottle(5, -0.6F, hopAleBooze));
		hopAleBucket_deprecated = new ItemDefinition(new ItemBoozeBucketDEPRECATED(hopAleBooze).setColor(GrowthCraftHops.getConfig().hopAleColor));
	}

	private void registerLager()
	{
		final float defaultTipsy = 0.80f;
		final int fermentTime = GrowthCraftCellar.getConfig().fermentTime;
		final FluidStack[] fs = new FluidStack[lagerBooze.length];
		for (int i = 0; i < fs.length; ++i)
		{
			fs[i] = new FluidStack(lagerBooze[i], 1);
		}

		CellarBoozeBuilder.create(lagerBooze[0])
			.tags(BoozeTag.YOUNG, BoozeTag.CHILLED)
			.brewsFrom(
				new FluidStack(hopAleBooze[4], GrowthCraftHops.getConfig().lagerBrewYield),
				YeastType.LAGER.asStack(),
				GrowthCraftHops.getConfig().lagerBrewTime,
				Residue.newDefault(0.0F));

		CellarBoozeBuilder.create(lagerBooze[1])
			.tags(BoozeTag.FERMENTED, BoozeTag.CHILLED)
			.fermentsFrom(fs[0], YeastType.BREWERS.asStack(), fermentTime)
			.fermentsFrom(fs[0], new ItemStack(Items.nether_wart), (int)(fermentTime * 0.66))
			.getEffect()
				.setTipsy(defaultTipsy, TickUtils.seconds(45))
				.addPotionEntry(Potion.moveSpeed, TickUtils.seconds(90), 0)
				.addPotionEntry(Potion.digSpeed, TickUtils.seconds(90), 0);

		CellarBoozeBuilder.create(lagerBooze[2])
			.tags(BoozeTag.FERMENTED, BoozeTag.CHILLED, BoozeTag.POTENT)
			.fermentsFrom(fs[1], new ItemStack(Items.glowstone_dust), fermentTime)
			.fermentsFrom(fs[3], new ItemStack(Items.glowstone_dust), fermentTime)
			.getEffect()
				.setTipsy(defaultTipsy, TickUtils.seconds(45))
				.addPotionEntry(Potion.moveSpeed, TickUtils.seconds(90), 0)
				.addPotionEntry(Potion.digSpeed, TickUtils.seconds(90), 0);

		CellarBoozeBuilder.create(lagerBooze[3])
			.tags(BoozeTag.FERMENTED, BoozeTag.CHILLED, BoozeTag.EXTENDED)
			.fermentsFrom(fs[1], new ItemStack(Items.redstone), fermentTime)
			.fermentsFrom(fs[2], new ItemStack(Items.redstone), fermentTime)
			.getEffect()
				.setTipsy(defaultTipsy, TickUtils.seconds(45))
				.addPotionEntry(Potion.moveSpeed, TickUtils.seconds(90), 0)
				.addPotionEntry(Potion.digSpeed, TickUtils.seconds(90), 0);

		CellarBoozeBuilder.create(lagerBooze[4])
			.tags(BoozeTag.FERMENTED, BoozeTag.CHILLED, BoozeTag.HYPER_EXTENDED)
			.fermentsFrom(fs[2], YeastType.ETHEREAL.asStack(), fermentTime)
			.fermentsFrom(fs[3], YeastType.ETHEREAL.asStack(), fermentTime)
			.getEffect()
				.setTipsy(defaultTipsy, TickUtils.seconds(45))
				.addPotionEntry(Potion.moveSpeed, TickUtils.seconds(90), 0)
				.addPotionEntry(Potion.digSpeed, TickUtils.seconds(90), 0);

		CellarBoozeBuilder.create(lagerBooze[5])
			.tags(BoozeTag.FERMENTED, BoozeTag.CHILLED, BoozeTag.INTOXICATED)
			.fermentsFrom(fs[2], YeastType.ORIGIN.asStack(), fermentTime)
			.fermentsFrom(fs[3], YeastType.ORIGIN.asStack(), fermentTime)
			.getEffect()
				.setTipsy(0.90F, TickUtils.seconds(45))
				.addEffect(new EffectWeightedRandomList()
					.add(8, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.digSpeed.id, TickUtils.minutes(3), 2)))
					.add(2, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.digSlowdown.id, TickUtils.minutes(3), 2))))
				.addEffect(new EffectWeightedRandomList()
					.add(8, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.moveSpeed.id, TickUtils.minutes(3), 2)))
					.add(2, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.moveSlowdown.id, TickUtils.minutes(3), 2))));

		CellarBoozeBuilder.create(lagerBooze[6])
			.tags(BoozeTag.FERMENTED, BoozeTag.CHILLED, BoozeTag.POISONED)
			//.fermentsFrom(fs[2], YeastType.NETHERRASH.asStack(), fermentTime)
			//.fermentsFrom(fs[3], YeastType.NETHERRASH.asStack(), fermentTime)
			.getEffect()
				.setTipsy(defaultTipsy, TickUtils.seconds(45))
				.addPotionEntry(Potion.poison, TickUtils.seconds(90), 0);
	}

	private void registerHopAle()
	{
		final float defaultTipsy = 0.70f;
		final int fermentTime = GrowthCraftCellar.getConfig().fermentTime;
		final FluidStack[] fs = new FluidStack[hopAleBooze.length];
		for (int i = 0; i < fs.length; ++i)
		{
			fs[i] = new FluidStack(hopAleBooze[i], 1);
		}

		// Unhopped
		CellarBoozeBuilder.create(hopAleBooze[4])
			.tags(BoozeTag.YOUNG)
			.brewsFrom(
				new FluidStack(FluidRegistry.WATER, GrowthCraftHops.getConfig().hopAleBrewYield),
				new ItemStack(Items.wheat),
				GrowthCraftHops.getConfig().hopAleBrewTime,
				Residue.newDefault(0.3F));

		CellarBoozeBuilder.create(hopAleBooze[0])
			.tags(BoozeTag.YOUNG, BoozeTag.HOPPED)
			.brewsFrom(
				new FluidStack(hopAleBooze[4], GrowthCraftHops.getConfig().hopAleHoppedBrewYield),
				GrowthCraftHops.hops.asStack(),
				GrowthCraftHops.getConfig().hopAleHoppedBrewTime,
				Residue.newDefault(0.0F));

		CellarBoozeBuilder.create(hopAleBooze[1])
			.tags(BoozeTag.FERMENTED)
			.fermentsFrom(fs[0], YeastType.BREWERS.asStack(), fermentTime)
			.fermentsFrom(fs[0], new ItemStack(Items.nether_wart), (int)(fermentTime * 0.66))
			.getEffect()
				.setTipsy(defaultTipsy, TickUtils.seconds(45))
				.addPotionEntry(Potion.digSpeed, TickUtils.minutes(3), 0);

		// Glowstone
		CellarBoozeBuilder.create(hopAleBooze[2])
			.tags(BoozeTag.HOPPED, BoozeTag.FERMENTED, BoozeTag.POTENT)
			.fermentsFrom(fs[1], new ItemStack(Items.glowstone_dust), fermentTime)
			.fermentsFrom(fs[3], new ItemStack(Items.glowstone_dust), fermentTime)
			.getEffect()
				.setTipsy(defaultTipsy, TickUtils.seconds(45))
				.addPotionEntry(Potion.digSpeed, TickUtils.minutes(3), 0);

		// Redstone
		CellarBoozeBuilder.create(hopAleBooze[3])
			.tags(BoozeTag.HOPPED, BoozeTag.FERMENTED, BoozeTag.EXTENDED)
			.fermentsFrom(fs[1], new ItemStack(Items.redstone), fermentTime)
			.fermentsFrom(fs[2], new ItemStack(Items.redstone), fermentTime)
			.getEffect()
				.setTipsy(defaultTipsy, TickUtils.seconds(45))
				.addPotionEntry(Potion.digSpeed, TickUtils.minutes(3), 0);

		// Ethereal Yeast
		CellarBoozeBuilder.create(hopAleBooze[5])
			.tags(BoozeTag.HOPPED, BoozeTag.FERMENTED, BoozeTag.HYPER_EXTENDED)
			.fermentsFrom(fs[2], YeastType.ETHEREAL.asStack(), fermentTime)
			.fermentsFrom(fs[3], YeastType.ETHEREAL.asStack(), fermentTime)
			.getEffect()
				.setTipsy(defaultTipsy, TickUtils.seconds(45))
				.addPotionEntry(Potion.digSpeed, TickUtils.minutes(3), 0);

		// Lager Yeast
		CellarBoozeBuilder.create(hopAleBooze[6])
			.tags(BoozeTag.HOPPED, BoozeTag.FERMENTED, BoozeTag.CHILLED)
			.fermentsFrom(fs[5], YeastType.LAGER.asStack(), fermentTime)
			.getEffect()
				.setTipsy(0.80F, TickUtils.seconds(45))
				.addPotionEntry(Potion.digSpeed, TickUtils.minutes(10), 2);

		// Intoxicated - Origin
		CellarBoozeBuilder.create(hopAleBooze[7])
			.tags(BoozeTag.HOPPED, BoozeTag.FERMENTED, BoozeTag.INTOXICATED)
			.fermentsFrom(fs[2], YeastType.ORIGIN.asStack(), fermentTime)
			.fermentsFrom(fs[3], YeastType.ORIGIN.asStack(), fermentTime)
			.getEffect()
				.setTipsy(0.90F, TickUtils.seconds(45))
				.addEffect(new EffectWeightedRandomList()
					.add(8, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.digSpeed.id, TickUtils.minutes(3), 2)))
					.add(2, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.digSlowdown.id, TickUtils.minutes(3), 2))));

		// Poisoned - Netherrash
		// Regardless of what you brewed it with, it will kill the hops in the
		// booze and poison it.
		CellarBoozeBuilder.create(hopAleBooze[8])
			.tags(BoozeTag.FERMENTED, BoozeTag.POISONED)
			//.fermentsFrom(fs[1], YeastType.NETHERRASH.asStack(), fermentTime)
			//.fermentsFrom(fs[2], YeastType.NETHERRASH.asStack(), fermentTime)
			//.fermentsFrom(fs[3], YeastType.NETHERRASH.asStack(), fermentTime)
			//.fermentsFrom(fs[5], YeastType.NETHERRASH.asStack(), fermentTime)
			//.fermentsFrom(fs[6], YeastType.NETHERRASH.asStack(), fermentTime)
			//.fermentsFrom(fs[7], YeastType.NETHERRASH.asStack(), fermentTime)
			.getEffect()
				.setTipsy(defaultTipsy, TickUtils.seconds(45))
				.addPotionEntry(Potion.poison, TickUtils.minutes(3), 0);
	}

	private void registerFermentations()
	{
		registerHopAle();
		registerLager();
	}

	@Override
	public void register()
	{
		GameRegistry.registerItem(hopAle.getItem(), "grc.hopAle");
		GameRegistry.registerItem(lager.getItem(), "grc.lager");
		GameRegistry.registerItem(hopAleBucket_deprecated.getItem(), "grc.hopAle_bucket");

		BoozeRegistryHelper.registerBooze(hopAleBooze, hopAleFluids, hopAleBuckets, hopAle, "grc.hopAle", hopAleBucket_deprecated);
		BoozeRegistryHelper.registerBooze(lagerBooze, lagerFluids, lagerBuckets, lager, "grc.lager", null);
		registerFermentations();
	}
}
