package growthcraft.rice.init;

import growthcraft.api.cellar.booze.Booze;
import growthcraft.api.cellar.booze.BoozeRegistry;
import growthcraft.api.cellar.booze.BoozeEffect;
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
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class GrcRiceBooze extends GrcModuleBase
{
	public Fluid[] riceSakeBooze;
	public FluidDefinition[] riceSakeBoozeDefs;
	public BlockBoozeDefinition[] riceSakeFluids;
	public ItemDefinition riceSake;
	public ItemDefinition riceSakeBucket_deprecated;
	public ItemBucketBoozeDefinition[] riceSakeBuckets;

	@Override
	public void preInit()
	{
		riceSakeBooze = new Booze[4];
		riceSakeFluids = new BlockBoozeDefinition[riceSakeBooze.length];
		riceSakeBuckets = new ItemBucketBoozeDefinition[riceSakeBooze.length];
		BoozeRegistryHelper.initializeBooze(riceSakeBooze, riceSakeFluids, riceSakeBuckets, "grc.riceSake", GrowthCraftRice.getConfig().riceSakeColor);
		riceSake = new ItemDefinition(new ItemBoozeBottle(5, -0.6F, riceSakeBooze));
		riceSakeBucket_deprecated = new ItemDefinition(new ItemBoozeBucketDEPRECATED(riceSakeBooze).setColor(GrowthCraftRice.getConfig().riceSakeColor));
		riceSakeBoozeDefs = FluidDefinition.convertArray(riceSakeBooze);
	}

	private void registerFermentations()
	{
		final BoozeRegistry boozeReg = CellarRegistry.instance().booze();
		final FermentingRegistry fermentReg = CellarRegistry.instance().fermenting();

		boozeReg.addTags(riceSakeBooze[0], "young");
		boozeReg.addTags(riceSakeBooze[1], "fermented");
		boozeReg.addTags(riceSakeBooze[2], "fermented", "potent");
		boozeReg.addTags(riceSakeBooze[3], "fermented", "extended");
		//boozeReg.addTags(riceSakeBooze[4], "fermented", "hyper-extended");
		//boozeReg.addTags(riceSakeBooze[5], "fermented", "poisoned");

		fermentReg.addFermentation(riceSakeBoozeDefs[1].asStack(), riceSakeBoozeDefs[0].asStack(), YeastType.BREWERS.asStack(), GrowthCraftCellar.getConfig().fermentSpeed);
		fermentReg.addFermentation(riceSakeBoozeDefs[1].asStack(), riceSakeBoozeDefs[0].asStack(), new ItemStack(Items.nether_wart), (int)(GrowthCraftCellar.getConfig().fermentSpeed * 0.66));
		fermentReg.addFermentation(riceSakeBoozeDefs[2].asStack(), riceSakeBoozeDefs[1].asStack(), new ItemStack(Items.glowstone_dust), GrowthCraftCellar.getConfig().fermentSpeed);
		fermentReg.addFermentation(riceSakeBoozeDefs[2].asStack(), riceSakeBoozeDefs[3].asStack(), new ItemStack(Items.glowstone_dust), GrowthCraftCellar.getConfig().fermentSpeed);
		fermentReg.addFermentation(riceSakeBoozeDefs[3].asStack(), riceSakeBoozeDefs[1].asStack(), new ItemStack(Items.redstone), GrowthCraftCellar.getConfig().fermentSpeed);
		fermentReg.addFermentation(riceSakeBoozeDefs[3].asStack(), riceSakeBoozeDefs[2].asStack(), new ItemStack(Items.redstone), GrowthCraftCellar.getConfig().fermentSpeed);
	}

	@Override
	public void register()
	{
		GameRegistry.registerItem(riceSake.getItem(), "grc.riceSake");
		GameRegistry.registerItem(riceSakeBucket_deprecated.getItem(), "grc.riceSake_bucket");
		BoozeRegistryHelper.registerBooze(riceSakeBooze, riceSakeFluids, riceSakeBuckets, riceSake, "grc.riceSake", riceSakeBucket_deprecated);
		for (BoozeEffect effect : BoozeRegistryHelper.getBoozeEffects(riceSakeBooze))
		{
			effect.setTipsy(0.65F, 900);
			effect.addPotionEntry(Potion.moveSpeed.id, 3600);
			effect.addPotionEntry(Potion.jump.id, 3600);
		}
		registerFermentations();

		CellarRegistry.instance().brewing().addBrewing(
			FluidRegistry.WATER,
			GrowthCraftRice.rice.getItem(),
			riceSakeBooze[0],
			GrowthCraftRice.getConfig().riceSakeBrewingTime,
			GrowthCraftRice.getConfig().riceSakeBrewingYield,
			Residue.newDefault(0.2F)
		);
	}
}
