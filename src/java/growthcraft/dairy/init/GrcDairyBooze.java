package growthcraft.dairy.init;

import growthcraft.api.cellar.booze.Booze;
import growthcraft.api.cellar.booze.BoozeRegistry;
import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.fermenting.FermentingRegistry;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.common.item.ItemBoozeBottle;
import growthcraft.cellar.util.BoozeRegistryHelper;
import growthcraft.core.common.GrcModuleBase;

public class GrcDairyBooze extends GrcModuleBase
{
	// not an actual Booze, but we use the class since it has the ability to set color
	public Booze[] milkBooze = new Booze[5];
	public BlockBoozeDefinition[] milkBoozeBlocks = new BlockBoozeDefinition[milkBooze.length];
	public ItemBucketBoozeDefinition[] milkBoozeBuckets = new ItemBucketBoozeDefinition[milkBooze.length];
	public ItemDefinition milk;

	@Override
	public void preInit()
	{
		this.milk = new ItemDefinition(new ItemBoozeBottle(5, -0.6F, milkBooze));
		BoozeRegistryHelper.initializeBooze(milkBooze, milkBoozeBlocks, milkBoozeBuckets, "grcdairy.milk", GrowthCraftDairy.getConfig().milkColor);
	}

	private void registerFermentations()
	{
		final BoozeRegistry br = CellarRegistry.instance().booze();
		final FermentingRegistry fr = CellarRegistry.instance().fermenting();

		final FluidStack[] fs = new FluidStack[milkBooze.length];
		for (int i = 0; i < milkBooze.length; ++i)
		{
			fs[i] = new FluidStack(milkBooze[i], 1);
		}

		br.addTags(milkBooze[0], BoozeTag.YOUNG);
		//fr.

		br.addTags(milkBooze[1], BoozeTag.FERMENTED);

		br.addTags(milkBooze[2], BoozeTag.FERMENTED, BoozeTag.POTENT);

		br.addTags(milkBooze[3], BoozeTag.FERMENTED, BoozeTag.EXTENDED);

		br.addTags(milkBooze[4], BoozeTag.FERMENTED, BoozeTag.POISONED);
	}

	@Override
	public void register()
	{
		registerFermentations();
	}
}
