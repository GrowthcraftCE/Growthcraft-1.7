package growthcraft.hops.init;

import growthcraft.api.cellar.booze.Booze;
import growthcraft.api.cellar.booze.BoozeEffect;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.common.Residue;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.common.item.ItemBoozeBottle;
import growthcraft.cellar.common.item.ItemBoozeBucketDEPRECATED;
import growthcraft.cellar.util.BoozeRegistryHelper;
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
	public BlockBoozeDefinition[] hopAleFluids;
	public ItemDefinition hopAle;
	public ItemDefinition hopAleBucket_deprecated;
	public ItemBucketBoozeDefinition[] hopAleBuckets;

	@Override
	public void preInit()
	{
		hopAleBooze = new Booze[5];
		hopAleFluids = new BlockBoozeDefinition[hopAleBooze.length];
		hopAleBuckets = new ItemBucketBoozeDefinition[hopAleBooze.length];
		BoozeRegistryHelper.initializeBooze(hopAleBooze, hopAleFluids, hopAleBuckets, "grc.hopAle", GrowthCraftHops.getConfig().hopAleColor);

		hopAle = new ItemDefinition(new ItemBoozeBottle(5, -0.6F, hopAleBooze));
		hopAleBucket_deprecated = new ItemDefinition(new ItemBoozeBucketDEPRECATED(hopAleBooze).setColor(GrowthCraftHops.getConfig().hopAleColor));
	}

	@Override
	public void register()
	{
		GameRegistry.registerItem(hopAle.getItem(), "grc.hopAle");
		GameRegistry.registerItem(hopAleBucket_deprecated.getItem(), "grc.hopAle_bucket");

		BoozeRegistryHelper.registerBooze(hopAleBooze, hopAleFluids, hopAleBuckets, hopAle, "grc.hopAle", hopAleBucket_deprecated);
		BoozeRegistryHelper.registerDefaultFermentation(hopAleBooze);
		for (BoozeEffect effect : BoozeRegistryHelper.getBoozeEffects(hopAleBooze))
		{
			effect.setTipsy(0.70F, 900);
			effect.addPotionEntry(Potion.digSpeed, 3600, 0);
		}

		CellarRegistry.instance().brewing().addBrewing(
			new FluidStack(FluidRegistry.WATER, GrowthCraftHops.getConfig().hopAleBrewYield),
			new ItemStack(Items.wheat),
			new FluidStack(hopAleBooze[4], GrowthCraftHops.getConfig().hopAleBrewYield),
			GrowthCraftHops.getConfig().hopAleBrewTime,
			Residue.newDefault(0.3F)
		);

		CellarRegistry.instance().brewing().addBrewing(
			new FluidStack(hopAleBooze[4], GrowthCraftHops.getConfig().hopAleHoppedBrewYield),
			GrowthCraftHops.hops.asStack(),
			new FluidStack(hopAleBooze[0], GrowthCraftHops.getConfig().hopAleHoppedBrewYield),
			GrowthCraftHops.getConfig().hopAleHoppedBrewTime,
			Residue.newDefault(0.0F)
		);
	}
}
