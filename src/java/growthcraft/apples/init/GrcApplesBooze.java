package growthcraft.apples.init;

import growthcraft.api.cellar.booze.Booze;
import growthcraft.api.cellar.booze.BoozeRegistry;
import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.common.Residue;
import growthcraft.api.cellar.fermenting.FermentingRegistry;
import growthcraft.api.core.effect.EffectAddPotionEffect;
import growthcraft.api.core.effect.EffectRandomList;
import growthcraft.api.core.effect.SimplePotionEffectFactory;
import growthcraft.api.core.util.TickUtils;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.common.item.ItemBoozeBottle;
import growthcraft.cellar.common.item.ItemBoozeBucketDEPRECATED;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.BoozeRegistryHelper;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.apples.GrowthCraftApples;
import growthcraft.cellar.util.YeastType;

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

	private void registerFermentations()
	{
		final BoozeRegistry br = CellarRegistry.instance().booze();
		final FermentingRegistry fr = CellarRegistry.instance().fermenting();

		final FluidStack[] fs = new FluidStack[appleCiderBooze.length];
		for (int i = 0; i < appleCiderBooze.length; ++i)
		{
			fs[i] = new FluidStack(appleCiderBooze[i], 1);
		}

		final int fermentTime = GrowthCraftCellar.getConfig().fermentTime;

		br.addTags(appleCiderBooze[0], BoozeTag.YOUNG);

		br.addTags(appleCiderBooze[1], BoozeTag.FERMENTED);
		br.getEffect(appleCiderBooze[1])
			.setTipsy(0.60F, 900)
			.addPotionEntry(Potion.field_76444_x, 1800, 0);
		fr.addFermentation(fs[1], fs[0], YeastType.BREWERS.asStack(), fermentTime);
		fr.addFermentation(fs[1], fs[0], new ItemStack(Items.nether_wart), (int)(fermentTime * 0.66));

		br.addTags(appleCiderBooze[2], BoozeTag.FERMENTED, BoozeTag.POTENT);
		br.getEffect(appleCiderBooze[2])
			.setTipsy(0.60F, 900)
			.addPotionEntry(Potion.field_76444_x, 1800, 0);
		fr.addFermentation(fs[2], fs[1], new ItemStack(Items.glowstone_dust), fermentTime);
		fr.addFermentation(fs[2], fs[3], new ItemStack(Items.glowstone_dust), fermentTime);

		br.addTags(appleCiderBooze[3], BoozeTag.FERMENTED, BoozeTag.EXTENDED);
		br.getEffect(appleCiderBooze[3])
			.setTipsy(0.60F, 900)
			.addPotionEntry(Potion.field_76444_x, 1800, 0);
		fr.addFermentation(fs[3], fs[1], new ItemStack(Items.redstone), fermentTime);
		fr.addFermentation(fs[3], fs[2], new ItemStack(Items.redstone), fermentTime);

		// Silken Nectar - ETHEREAL
		br.addTags(appleCiderBooze[4], BoozeTag.FERMENTED, BoozeTag.MAGICAL);
		br.getEffect(appleCiderBooze[4])
			.setTipsy(0.60F, 900)
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
		fr.addFermentation(fs[4], fs[1], YeastType.ETHEREAL.asStack(), fermentTime);

		// Intoxicated - Origin Yeast
		br.addTags(appleCiderBooze[5], BoozeTag.FERMENTED, BoozeTag.INTOXICATED);
		br.getEffect(appleCiderBooze[5])
			.setTipsy(0.80F, 900)
			.addPotionEntry(Potion.field_76444_x, 1800, 0);
		fr.addFermentation(fs[5], fs[2], YeastType.ORIGIN.asStack(), fermentTime);
		fr.addFermentation(fs[5], fs[3], YeastType.ORIGIN.asStack(), fermentTime);

		// Poisoned - created from netherrash,
		// the booze looses all its benefits and effectively becomes poisoned
		br.addTags(appleCiderBooze[6], BoozeTag.FERMENTED, BoozeTag.POISONED);
		br.getEffect(appleCiderBooze[6])
			.setTipsy(0.60F, 900)
			.addPotionEntry(Potion.poison, 1800, 0);
		//fr.addFermentation(fs[6], fs[0], YeastType.NETHERRASH.asStack(), fermentTime);
		//fr.addFermentation(fs[6], fs[1], YeastType.NETHERRASH.asStack(), fermentTime);
		//fr.addFermentation(fs[6], fs[2], YeastType.NETHERRASH.asStack(), fermentTime);
		//fr.addFermentation(fs[6], fs[3], YeastType.NETHERRASH.asStack(), fermentTime);
		//fr.addFermentation(fs[6], fs[4], YeastType.NETHERRASH.asStack(), fermentTime);
		//fr.addFermentation(fs[6], fs[5], YeastType.NETHERRASH.asStack(), fermentTime);
	}

	@Override
	public void register()
	{
		GameRegistry.registerItem(appleCider.getItem(), "grc.appleCider");
		GameRegistry.registerItem(appleCiderBucket_deprecated.getItem(), "grc.appleCider_bucket");

		BoozeRegistryHelper.registerBooze(appleCiderBooze, appleCiderFluids, appleCiderBuckets, appleCider, "grc.appleCider", appleCiderBucket_deprecated);
		registerFermentations();

		CellarRegistry.instance().pressing().addPressing(
			Items.apple,
			appleCiderBooze[0],
			GrowthCraftApples.getConfig().appleCiderPressingTime,
			GrowthCraftApples.getConfig().appleCiderPressYield,
			Residue.newDefault(0.3F)
		);
	}
}
