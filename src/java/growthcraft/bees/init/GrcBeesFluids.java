package growthcraft.bees.init;

import growthcraft.api.bees.BeesFluidTag;
import growthcraft.api.cellar.booze.Booze;
import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.core.CoreRegistry;
import growthcraft.api.core.effect.EffectAddPotionEffect;
import growthcraft.api.core.effect.EffectWeightedRandomList;
import growthcraft.api.core.effect.SimplePotionEffectFactory;
import growthcraft.api.core.GrcFluid;
import growthcraft.api.core.util.TickUtils;
import growthcraft.bees.GrowthCraftBees;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.common.item.ItemBoozeBottle;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.BoozeRegistryHelper;
import growthcraft.cellar.util.BoozeUtils;
import growthcraft.cellar.util.YeastType;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.integration.forestry.ForestryFluids;
import growthcraft.core.util.FluidFactory;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class GrcBeesFluids extends GrcModuleBase
{
	public FluidFactory.FluidDetails honey;
	public Booze[] honeyMeadBooze;
	public ItemDefinition honeyMeadBottle;
	public ItemBucketBoozeDefinition[] honeyMeadBuckets;
	public BlockBoozeDefinition[] honeyMeadFluids;

	@Override
	public void preInit()
	{
		if (GrowthCraftBees.getConfig().honeyEnabled)
		{
			final Fluid honeyFluid = new GrcFluid("grc.honey")
				.setColor(0xffac01)
				.setDensity(1420)
				.setViscosity(73600);
			honey = FluidFactory.instance().create(honeyFluid);
		}
		honeyMeadBooze = new Booze[7];
		honeyMeadFluids = new BlockBoozeDefinition[honeyMeadBooze.length];
		honeyMeadBuckets = new ItemBucketBoozeDefinition[honeyMeadBooze.length];
		BoozeRegistryHelper.initializeBoozeFluids("grc.honeyMead", honeyMeadBooze);
		for (Booze booze : honeyMeadBooze)
		{
			booze.setColor(GrowthCraftBees.getConfig().honeyMeadColor).setDensity(1000).setViscosity(1200);
		}
		BoozeRegistryHelper.initializeBooze(honeyMeadBooze, honeyMeadFluids, honeyMeadBuckets);
		honeyMeadBottle = new ItemDefinition(new ItemBoozeBottle(6, -0.45F, honeyMeadBooze));

		if (honey != null)
		{
			honey.setCreativeTab(GrowthCraftBees.tab);
			honey.block.getBlock().setBlockTextureName("grcbees:fluids/honey");
			honey.refreshItemColor();
		}
	}

	private void registerRecipes()
	{
		final int fermentTime = GrowthCraftCellar.getConfig().fermentTime;
		final FluidStack[] fs = new FluidStack[honeyMeadBooze.length];
		for (int i = 0; i < honeyMeadBooze.length; ++i)
		{
			fs[i] = new FluidStack(honeyMeadBooze[i], 1);
		}

		GrowthCraftCellar.boozeBuilderFactory.create(honeyMeadBooze[0])
			.tags(BoozeTag.YOUNG);

		GrowthCraftCellar.boozeBuilderFactory.create(honeyMeadBooze[1])
			.tags(BoozeTag.FERMENTED)
			.fermentsFrom(fs[0], YeastType.BREWERS.asStack(), fermentTime)
			.fermentsFrom(fs[0], new ItemStack(Items.nether_wart), (int)(fermentTime * 0.66))
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.15f), TickUtils.seconds(90))
				.addPotionEntry(Potion.regeneration, TickUtils.seconds(90), 0);

		GrowthCraftCellar.boozeBuilderFactory.create(honeyMeadBooze[2])
			.tags(BoozeTag.FERMENTED, BoozeTag.POTENT)
			.fermentsFrom(fs[1], new ItemStack(Items.glowstone_dust), fermentTime)
			.fermentsFrom(fs[3], new ItemStack(Items.glowstone_dust), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.17f), TickUtils.seconds(90))
				.addPotionEntry(Potion.regeneration, TickUtils.seconds(90), 0);

		GrowthCraftCellar.boozeBuilderFactory.create(honeyMeadBooze[3])
			.tags(BoozeTag.FERMENTED, BoozeTag.EXTENDED)
			.fermentsFrom(fs[1], new ItemStack(Items.redstone), fermentTime)
			.fermentsFrom(fs[2], new ItemStack(Items.redstone), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.15f), TickUtils.seconds(90))
				.addPotionEntry(Potion.regeneration, TickUtils.seconds(90), 0);

		GrowthCraftCellar.boozeBuilderFactory.create(honeyMeadBooze[4])
			.tags(BoozeTag.FERMENTED, BoozeTag.HYPER_EXTENDED)
			.fermentsFrom(fs[2], YeastType.ETHEREAL.asStack(), fermentTime)
			.fermentsFrom(fs[3], YeastType.ETHEREAL.asStack(), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.15f), TickUtils.seconds(90))
				.addPotionEntry(Potion.regeneration, TickUtils.seconds(90), 0);

		GrowthCraftCellar.boozeBuilderFactory.create(honeyMeadBooze[5])
			.tags(BoozeTag.FERMENTED, BoozeTag.INTOXICATED)
			.fermentsFrom(fs[2], YeastType.ORIGIN.asStack(), fermentTime)
			.fermentsFrom(fs[3], YeastType.ORIGIN.asStack(), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.15f * 1.5f), TickUtils.seconds(90))
				.addEffect(new EffectWeightedRandomList()
					.add(8, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.regeneration.id, TickUtils.seconds(90), 2)))
					.add(2, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.poison.id, TickUtils.seconds(90), 2)))
				);

		GrowthCraftCellar.boozeBuilderFactory.create(honeyMeadBooze[6])
			.tags(BoozeTag.FERMENTED, BoozeTag.POISONED)
			//.fermentsFrom(fs[0], YeastType.NETHERRASH.asStack(), fermentTime)
			//.fermentsFrom(fs[1], YeastType.NETHERRASH.asStack(), fermentTime)
			//.fermentsFrom(fs[2], YeastType.NETHERRASH.asStack(), fermentTime)
			//.fermentsFrom(fs[3], YeastType.NETHERRASH.asStack(), fermentTime)
			//.fermentsFrom(fs[4], YeastType.NETHERRASH.asStack(), fermentTime)
			//.fermentsFrom(fs[5], YeastType.NETHERRASH.asStack(), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.15f), TickUtils.seconds(90))
				.createPotionEntry(Potion.poison, TickUtils.seconds(90), 0).toggleDescription(!GrowthCraftCore.getConfig().hidePoisonedBooze);
	}

	@Override
	public void register()
	{
		honey.registerObjects("grc", "Honey");
		GameRegistry.registerItem(honeyMeadBottle.getItem(), "grc.honeyMead");

		final ItemStack waterBottle = new ItemStack(Items.potionitem, 1, 0);

		// Bucket of mead recipes
		/// Single bucket
		GameRegistry.addShapelessRecipe(honeyMeadBuckets[0].asStack(),
			Items.water_bucket,
			GrowthCraftBees.items.honeyJar.getItem(),
			Items.bucket);
		GameRegistry.addShapelessRecipe(honeyMeadBuckets[0].asStack(),
			waterBottle, waterBottle, waterBottle,
			GrowthCraftBees.items.honeyJar.getItem(),
			Items.bucket);

		if (honey != null)
		{
			final ItemStack honeyBottleStack = honey.asBottleItemStack();
			GameRegistry.addShapelessRecipe(honeyMeadBuckets[0].asStack(),
				Items.water_bucket,
				honey.asBucketItemStack(),
				Items.bucket);
			GameRegistry.addShapelessRecipe(honeyMeadBuckets[0].asStack(),
				Items.water_bucket,
				honeyBottleStack, honeyBottleStack, honeyBottleStack,
				Items.bucket);
			/// Water bottles
			GameRegistry.addShapelessRecipe(honeyMeadBuckets[0].asStack(),
				waterBottle, waterBottle, waterBottle,
				honey.asBucketItemStack(),
				Items.bucket);
			GameRegistry.addShapelessRecipe(honeyMeadBuckets[0].asStack(),
				waterBottle, waterBottle, waterBottle,
				honeyBottleStack, honeyBottleStack, honeyBottleStack,
				Items.bucket);
		}

		// Booze
		BoozeRegistryHelper.registerBooze(honeyMeadBooze, honeyMeadFluids, honeyMeadBuckets, honeyMeadBottle, "grc.honeyMead", null);
		if (honey != null) CoreRegistry.instance().fluidDictionary().addFluidTags(honey.getFluid(), BeesFluidTag.HONEY);
		if (ForestryFluids.HONEY.exists()) CoreRegistry.instance().fluidDictionary().addFluidTags(ForestryFluids.HONEY.getFluid(), BeesFluidTag.HONEY);
		registerRecipes();
	}
}
