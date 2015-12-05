package growthcraft.bees.init;

import growthcraft.api.cellar.booze.Booze;
import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.util.CellarBoozeBuilder;
import growthcraft.api.core.effect.EffectAddPotionEffect;
import growthcraft.api.core.effect.EffectWeightedRandomList;
import growthcraft.api.core.effect.SimplePotionEffectFactory;
import growthcraft.api.core.util.TickUtils;
import growthcraft.bees.GrowthCraftBees;
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

public class GrcBeesBooze extends GrcModuleBase
{
	public Booze[] honeyMeadBooze;
	public ItemDefinition honeyMead;
	public ItemDefinition honeyMeadBucket_deprecated;
	public ItemBucketBoozeDefinition[] honeyMeadBuckets;
	public BlockBoozeDefinition[] honeyMeadFluids;

	@Override
	public void preInit()
	{
		honeyMeadBooze = new Booze[7];
		honeyMeadFluids = new BlockBoozeDefinition[honeyMeadBooze.length];
		honeyMeadBuckets = new ItemBucketBoozeDefinition[honeyMeadBooze.length];
		BoozeRegistryHelper.initializeBooze(honeyMeadBooze, honeyMeadFluids, honeyMeadBuckets, "grc.honeyMead", GrowthCraftBees.getConfig().honeyMeadColor);

		honeyMead = new ItemDefinition(new ItemBoozeBottle(6, -0.45F, honeyMeadBooze));
		honeyMeadBucket_deprecated = new ItemDefinition(new ItemBoozeBucketDEPRECATED(honeyMeadBooze).setColor(GrowthCraftBees.getConfig().honeyMeadColor));
	}

	private void registerRecipes()
	{
		final int fermentTime = GrowthCraftCellar.getConfig().fermentTime;
		final FluidStack[] fs = new FluidStack[honeyMeadBooze.length];
		for (int i = 0; i < honeyMeadBooze.length; ++i)
		{
			fs[i] = new FluidStack(honeyMeadBooze[i], 1);
		}

		CellarBoozeBuilder.create(honeyMeadBooze[0])
			.tags(BoozeTag.YOUNG);

		CellarBoozeBuilder.create(honeyMeadBooze[1])
			.tags(BoozeTag.FERMENTED)
			.fermentsFrom(fs[0], YeastType.BREWERS.asStack(), fermentTime)
			.fermentsFrom(fs[0], new ItemStack(Items.nether_wart), (int)(fermentTime * 0.66))
			.getEffect()
				.setTipsy(0.60F, TickUtils.seconds(90))
				.addPotionEntry(Potion.regeneration, TickUtils.seconds(90), 0);

		CellarBoozeBuilder.create(honeyMeadBooze[2])
			.tags(BoozeTag.FERMENTED, BoozeTag.POTENT)
			.fermentsFrom(fs[1], new ItemStack(Items.glowstone_dust), fermentTime)
			.fermentsFrom(fs[3], new ItemStack(Items.glowstone_dust), fermentTime)
			.getEffect()
				.setTipsy(0.60F, TickUtils.seconds(90))
				.addPotionEntry(Potion.regeneration, TickUtils.seconds(90), 0);

		CellarBoozeBuilder.create(honeyMeadBooze[3])
			.tags(BoozeTag.FERMENTED, BoozeTag.EXTENDED)
			.fermentsFrom(fs[1], new ItemStack(Items.redstone), fermentTime)
			.fermentsFrom(fs[2], new ItemStack(Items.redstone), fermentTime)
			.getEffect()
				.setTipsy(0.60F, TickUtils.seconds(90))
				.addPotionEntry(Potion.regeneration, TickUtils.seconds(90), 0);

		CellarBoozeBuilder.create(honeyMeadBooze[4])
			.tags(BoozeTag.FERMENTED, BoozeTag.HYPER_EXTENDED)
			.fermentsFrom(fs[2], YeastType.ETHEREAL.asStack(), fermentTime)
			.fermentsFrom(fs[3], YeastType.ETHEREAL.asStack(), fermentTime)
			.getEffect()
				.setTipsy(0.60F, TickUtils.seconds(90))
				.addPotionEntry(Potion.regeneration, TickUtils.seconds(90), 0);

		CellarBoozeBuilder.create(honeyMeadBooze[5])
			.tags(BoozeTag.FERMENTED, BoozeTag.INTOXICATED)
			.fermentsFrom(fs[2], YeastType.ORIGIN.asStack(), fermentTime)
			.fermentsFrom(fs[3], YeastType.ORIGIN.asStack(), fermentTime)
			.getEffect()
				.setTipsy(0.60F, TickUtils.seconds(90))
				.addEffect(new EffectWeightedRandomList()
					.add(8, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.regeneration.id, TickUtils.seconds(90), 2)))
					.add(2, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.poison.id, TickUtils.seconds(90), 2)))
				);

		CellarBoozeBuilder.create(honeyMeadBooze[6])
			.tags(BoozeTag.FERMENTED, BoozeTag.POISONED)
			//.fermentsFrom(fs[0], YeastType.NETHERRASH.asStack(), fermentTime)
			//.fermentsFrom(fs[1], YeastType.NETHERRASH.asStack(), fermentTime)
			//.fermentsFrom(fs[2], YeastType.NETHERRASH.asStack(), fermentTime)
			//.fermentsFrom(fs[3], YeastType.NETHERRASH.asStack(), fermentTime)
			//.fermentsFrom(fs[4], YeastType.NETHERRASH.asStack(), fermentTime)
			//.fermentsFrom(fs[5], YeastType.NETHERRASH.asStack(), fermentTime)
			.getEffect()
				.setTipsy(0.60F, TickUtils.seconds(90))
				.addPotionEntry(Potion.poison, TickUtils.seconds(90), 0);
	}

	@Override
	public void register()
	{
		GameRegistry.registerItem(honeyMead.getItem(), "grc.honeyMead");
		GameRegistry.registerItem(honeyMeadBucket_deprecated.getItem(), "grc.honeyMead_bucket");

		GameRegistry.addShapelessRecipe(honeyMeadBuckets[0].asStack(), Items.water_bucket, GrowthCraftBees.honeyJar.getItem(), Items.bucket);

		// Booze
		BoozeRegistryHelper.registerBooze(honeyMeadBooze, honeyMeadFluids, honeyMeadBuckets, honeyMead, "grc.honeyMead", honeyMeadBucket_deprecated);
		registerRecipes();
	}
}
