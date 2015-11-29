package growthcraft.apples.init;

import growthcraft.api.cellar.booze.Booze;
import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.common.Residue;
import growthcraft.api.cellar.util.CellarBoozeBuilder;
import growthcraft.api.core.effect.EffectAddPotionEffect;
import growthcraft.api.core.effect.EffectRandomList;
import growthcraft.api.core.effect.EffectWeightedRandomList;
import growthcraft.api.core.effect.SimplePotionEffectFactory;
import growthcraft.api.core.util.TickUtils;
import growthcraft.apples.GrowthCraftApples;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.common.item.ItemBoozeBottle;
import growthcraft.cellar.common.item.ItemBoozeBucketDEPRECATED;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.BoozeRegistryHelper;
import growthcraft.cellar.util.YeastType;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.GrcModuleBase;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.fluids.FluidStack;

public class GrcApplesBooze extends GrcModuleBase
{
	public Booze[] appleCiderBooze;
	public BlockBoozeDefinition[] appleCiderFluids;
	public ItemDefinition appleCider;
	public ItemDefinition appleCiderBucket_deprecated;
	public ItemBucketBoozeDefinition[] appleCiderBuckets;

	@Override
	public void preInit()
	{
		appleCiderBooze = new Booze[7];
		appleCiderFluids = new BlockBoozeDefinition[appleCiderBooze.length];
		appleCiderBuckets = new ItemBucketBoozeDefinition[appleCiderBooze.length];
		BoozeRegistryHelper.initializeBooze(appleCiderBooze, appleCiderFluids, appleCiderBuckets, "grc.appleCider", GrowthCraftApples.getConfig().appleCiderColor);
		appleCiderBooze[4].setColor(GrowthCraftApples.getConfig().silkenNectarColor);
		appleCiderFluids[4].getBlock().refreshColor();

		appleCider = new ItemDefinition(new ItemBoozeBottle(4, -0.3F, appleCiderBooze));
		appleCiderBucket_deprecated = new ItemDefinition(new ItemBoozeBucketDEPRECATED(appleCiderBooze).setColor(GrowthCraftApples.getConfig().appleCiderColor));
	}

	private void registerRecipes()
	{
		final int fermentTime = GrowthCraftCellar.getConfig().fermentTime;
		final FluidStack[] fs = new FluidStack[appleCiderBooze.length];
		for (int i = 0; i < appleCiderBooze.length; ++i)
		{
			fs[i] = new FluidStack(appleCiderBooze[i], 1);
		}

		CellarBoozeBuilder.create(appleCiderBooze[0])
			.tags(BoozeTag.YOUNG)
			.pressesFrom(
				new ItemStack(Items.apple),
				GrowthCraftApples.getConfig().appleCiderPressYield,
				GrowthCraftApples.getConfig().appleCiderPressingTime,
				Residue.newDefault(0.3F)
			);

		CellarBoozeBuilder.create(appleCiderBooze[1])
			.tags(BoozeTag.FERMENTED)
			.fermentsFrom(fs[0], YeastType.BREWERS.asStack(), fermentTime)
			.fermentsFrom(fs[0], new ItemStack(Items.nether_wart), (int)(fermentTime * 0.66))
			.getEffect()
				.setTipsy(0.60F, TickUtils.seconds(45))
				.addPotionEntry(Potion.field_76444_x, TickUtils.seconds(90), 0);

		CellarBoozeBuilder.create(appleCiderBooze[2])
			.tags(BoozeTag.FERMENTED, BoozeTag.POTENT)
			.fermentsFrom(fs[1], new ItemStack(Items.glowstone_dust), fermentTime)
			.fermentsFrom(fs[3], new ItemStack(Items.glowstone_dust), fermentTime)
			.getEffect()
				.setTipsy(0.60F, TickUtils.seconds(45))
				.addPotionEntry(Potion.field_76444_x, TickUtils.seconds(90), 0);

		CellarBoozeBuilder.create(appleCiderBooze[3])
			.tags(BoozeTag.FERMENTED, BoozeTag.EXTENDED)
			.fermentsFrom(fs[1], new ItemStack(Items.redstone), fermentTime)
			.fermentsFrom(fs[2], new ItemStack(Items.redstone), fermentTime)
			.getEffect()
				.setTipsy(0.60F, TickUtils.seconds(45))
				.addPotionEntry(Potion.field_76444_x, TickUtils.seconds(90), 0);

		// Silken Nectar - ETHEREAL
		CellarBoozeBuilder.create(appleCiderBooze[4])
			.tags(BoozeTag.FERMENTED, BoozeTag.MAGICAL)
			.fermentsFrom(fs[1], YeastType.ETHEREAL.asStack(), fermentTime)
			.getEffect()
				.setTipsy(0.60F, TickUtils.seconds(45))
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
		CellarBoozeBuilder.create(appleCiderBooze[5])
			.tags(BoozeTag.FERMENTED, BoozeTag.INTOXICATED)
			.fermentsFrom(fs[2], YeastType.ORIGIN.asStack(), fermentTime)
			.fermentsFrom(fs[3], YeastType.ORIGIN.asStack(), fermentTime)
			.getEffect()
				.setTipsy(0.80F, TickUtils.seconds(45))
				.addEffect(new EffectWeightedRandomList()
					.add(8, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.field_76444_x.id, TickUtils.seconds(90), 2)))
					.add(2, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.wither.id, TickUtils.seconds(90), 2)))
				);

		// Poisoned - created from netherrash,
		// the booze looses all its benefits and effectively becomes poisoned
		CellarBoozeBuilder.create(appleCiderBooze[6])
			.tags(BoozeTag.FERMENTED, BoozeTag.POISONED)
			//.fermentsFrom(fs[0], YeastType.NETHERRASH.asStack(), fermentTime)
			//.fermentsFrom(fs[1], YeastType.NETHERRASH.asStack(), fermentTime)
			//.fermentsFrom(fs[2], YeastType.NETHERRASH.asStack(), fermentTime)
			//.fermentsFrom(fs[3], YeastType.NETHERRASH.asStack(), fermentTime)
			//.fermentsFrom(fs[4], YeastType.NETHERRASH.asStack(), fermentTime)
			//.fermentsFrom(fs[5], YeastType.NETHERRASH.asStack(), fermentTime)
			.getEffect()
				.setTipsy(0.60F, TickUtils.seconds(45))
				.addPotionEntry(Potion.poison, TickUtils.seconds(90), 0);
	}

	@Override
	public void register()
	{
		GameRegistry.registerItem(appleCider.getItem(), "grc.appleCider");
		GameRegistry.registerItem(appleCiderBucket_deprecated.getItem(), "grc.appleCider_bucket");

		BoozeRegistryHelper.registerBooze(appleCiderBooze, appleCiderFluids, appleCiderBuckets, appleCider, "grc.appleCider", appleCiderBucket_deprecated);
		registerRecipes();
	}
}
