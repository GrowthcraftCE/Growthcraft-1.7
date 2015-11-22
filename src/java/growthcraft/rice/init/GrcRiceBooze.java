package growthcraft.rice.init;

import growthcraft.api.cellar.booze.Booze;
import growthcraft.api.cellar.booze.BoozeRegistry;
import growthcraft.api.cellar.booze.BoozeEffect;
import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.common.Residue;
import growthcraft.api.cellar.fermenting.FermentingRegistry;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.common.item.ItemBoozeBottle;
import growthcraft.cellar.common.item.ItemBoozeBucketDEPRECATED;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.BoozeRegistryHelper;
import growthcraft.core.common.definition.FluidDefinition;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.rice.GrowthCraftRice;
import growthcraft.cellar.util.YeastType;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidRegistry;

public class GrcRiceBooze extends GrcModuleBase
{
	public Booze[] riceSakeBooze;
	public FluidDefinition[] riceSakeBoozeDefs;
	public BlockBoozeDefinition[] riceSakeFluids;
	public ItemDefinition riceSake;
	public ItemDefinition riceSakeBucket_deprecated;
	public ItemBucketBoozeDefinition[] riceSakeBuckets;

	@Override
	public void preInit()
	{
		riceSakeBooze = new Booze[7];
		riceSakeFluids = new BlockBoozeDefinition[riceSakeBooze.length];
		riceSakeBuckets = new ItemBucketBoozeDefinition[riceSakeBooze.length];
		BoozeRegistryHelper.initializeBooze(riceSakeBooze, riceSakeFluids, riceSakeBuckets, "grc.riceSake", GrowthCraftRice.getConfig().riceSakeColor);
		riceSakeBooze[4].setColor(GrowthCraftRice.getConfig().riceSakeDivineColor);
		riceSakeFluids[4].getBlock().refreshColor();
		riceSake = new ItemDefinition(new ItemBoozeBottle(5, -0.6F, riceSakeBooze));
		riceSakeBucket_deprecated = new ItemDefinition(new ItemBoozeBucketDEPRECATED(riceSakeBooze).setColor(GrowthCraftRice.getConfig().riceSakeColor));
		riceSakeBoozeDefs = FluidDefinition.convertArray(riceSakeBooze);
	}

	private void registerFermentations()
	{
		final BoozeRegistry boozeReg = CellarRegistry.instance().booze();
		final FermentingRegistry fermentReg = CellarRegistry.instance().fermenting();

		boozeReg.addTags(riceSakeBooze[0], BoozeTag.YOUNG);
		boozeReg.addTags(riceSakeBooze[1], BoozeTag.FERMENTED);
		boozeReg.addTags(riceSakeBooze[2], BoozeTag.FERMENTED, BoozeTag.POTENT);
		boozeReg.addTags(riceSakeBooze[3], BoozeTag.FERMENTED, BoozeTag.EXTENDED);
		// Divine Sake
		boozeReg.addTags(riceSakeBooze[4], BoozeTag.FERMENTED, BoozeTag.HYPER_EXTENDED);
		boozeReg.addTags(riceSakeBooze[5], BoozeTag.FERMENTED, BoozeTag.INTOXICATED);
		// Poisoned Sake - created from netherrash,
		// the booze looses all its benefits and effectively becomes poisoned
		boozeReg.addTags(riceSakeBooze[6], BoozeTag.FERMENTED, BoozeTag.POISONED);

		final int fermentTime = GrowthCraftCellar.getConfig().fermentTime;
		fermentReg.addFermentation(riceSakeBoozeDefs[1].asFluidStack(), riceSakeBoozeDefs[0].asFluidStack(), YeastType.BREWERS.asStack(), fermentTime);
		fermentReg.addFermentation(riceSakeBoozeDefs[1].asFluidStack(), riceSakeBoozeDefs[0].asFluidStack(), new ItemStack(Items.nether_wart), (int)(fermentTime * 0.66));
		fermentReg.addFermentation(riceSakeBoozeDefs[2].asFluidStack(), riceSakeBoozeDefs[1].asFluidStack(), new ItemStack(Items.glowstone_dust), fermentTime);
		fermentReg.addFermentation(riceSakeBoozeDefs[2].asFluidStack(), riceSakeBoozeDefs[3].asFluidStack(), new ItemStack(Items.glowstone_dust), fermentTime);
		fermentReg.addFermentation(riceSakeBoozeDefs[3].asFluidStack(), riceSakeBoozeDefs[1].asFluidStack(), new ItemStack(Items.redstone), fermentTime);
		fermentReg.addFermentation(riceSakeBoozeDefs[3].asFluidStack(), riceSakeBoozeDefs[2].asFluidStack(), new ItemStack(Items.redstone), fermentTime);

		for (int i = 2; i < 4; ++i)
		{
			fermentReg.addFermentation(riceSakeBoozeDefs[4].asFluidStack(), riceSakeBoozeDefs[i].asFluidStack(), YeastType.ETHEREAL.asStack(), fermentTime);
		}

		// Reserved for netherrash
		//for (int i = 0; i < 5; ++i)
		//{
		//	fermentReg.addFermentation(riceSakeBoozeDefs[6].asFluidStack(), riceSakeBoozeDefs[i].asFluidStack(), NETHERRASH, fermentTime);
		//}
	}

	@Override
	public void register()
	{
		GameRegistry.registerItem(riceSake.getItem(), "grc.riceSake");
		GameRegistry.registerItem(riceSakeBucket_deprecated.getItem(), "grc.riceSake_bucket");
		BoozeRegistryHelper.registerBooze(riceSakeBooze, riceSakeFluids, riceSakeBuckets, riceSake, "grc.riceSake", riceSakeBucket_deprecated);
		registerFermentations();
		for (BoozeEffect effect : BoozeRegistryHelper.getBoozeEffects(riceSakeBooze))
		{
			effect.setTipsy(0.65F, 900);
			effect.addPotionEntry(Potion.moveSpeed.id, 3600, 0);
			effect.addPotionEntry(Potion.jump.id, 3600, 0);
		}
		// Intoxicated
		CellarRegistry.instance().booze().getEffect(riceSakeBooze[6])
			.clearPotionEntries()
			//.addOptionalPotionEntry((int[]){ Potion.moveSlowdown.id, Potion.moveSpeed.id }, 3600, 0)
			.addPotionEntry(Potion.jump.id, 3600, 0);

		// poisoned
		CellarRegistry.instance().booze().getEffect(riceSakeBooze[6])
			.clearPotionEntries()
			.addPotionEntry(Potion.poison.id, 3600, 0);

		final int yieldAmount = GrowthCraftRice.getConfig().riceSakeBrewingYield;
		CellarRegistry.instance().brewing().addBrewing(
			new FluidStack(FluidRegistry.WATER, yieldAmount),
			GrowthCraftRice.rice.asStack(),
			new FluidStack(riceSakeBooze[0], yieldAmount),
			GrowthCraftRice.getConfig().riceSakeBrewingTime,
			Residue.newDefault(0.2F)
		);
	}
}
