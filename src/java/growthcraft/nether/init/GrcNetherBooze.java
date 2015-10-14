package growthcraft.nether.init;

import growthcraft.api.cellar.Booze;
//import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.item.ItemBoozeBottle;
import growthcraft.cellar.utils.BoozeRegistryHelper;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.nether.GrowthCraftNether;

import net.minecraft.potion.Potion;

public class GrcNetherBooze
{
	public Booze[] fireBrandyBooze;
	public Booze[] maliceCiderBooze;
	public BlockBoozeDefinition[] fireBrandyFluids;
	public BlockBoozeDefinition[] maliceCiderFluids;
	public ItemDefinition fireBrandy;
	public ItemDefinition maliceCider;
	public ItemBucketBoozeDefinition[] fireBrandyBuckets;
	public ItemBucketBoozeDefinition[] maliceCiderBuckets;

	public GrcNetherBooze() {}

	public void init()
	{
		this.fireBrandyBooze = new Booze[4];
		this.fireBrandyFluids = new BlockBoozeDefinition[fireBrandyBooze.length];
		this.fireBrandyBuckets = new ItemBucketBoozeDefinition[fireBrandyBooze.length];
		BoozeRegistryHelper.initializeBooze(fireBrandyBooze, fireBrandyFluids, fireBrandyBuckets, "grcnether.fireBrandy", GrowthCraftNether.getConfig().fireBrandyColor);

		this.maliceCiderBooze = new Booze[4];
		this.maliceCiderFluids = new BlockBoozeDefinition[maliceCiderBooze.length];
		this.maliceCiderBuckets = new ItemBucketBoozeDefinition[maliceCiderBooze.length];
		BoozeRegistryHelper.initializeBooze(maliceCiderBooze, maliceCiderFluids, maliceCiderBuckets, "grcnether.maliceCider", GrowthCraftNether.getConfig().maliceCiderColor);

		this.fireBrandy = new ItemDefinition(new ItemBoozeBottle(5, -0.6F, fireBrandyBooze)
			.setColor(GrowthCraftNether.getConfig().fireBrandyColor)
			// place holder --
			.setTipsy(0.70F, 900)
			.setPotionEffects(new int[] {Potion.digSpeed.id}, new int[] {3600}));

		this.maliceCider = new ItemDefinition(new ItemBoozeBottle(5, -0.6F, maliceCiderBooze)
			.setColor(GrowthCraftNether.getConfig().maliceCiderColor)
			// place holder --
			.setTipsy(0.70F, 900)
			.setPotionEffects(new int[] {Potion.digSpeed.id}, new int[] {3600}));

		register();
	}

	public void register()
	{
		BoozeRegistryHelper.registerBooze(fireBrandyBooze, fireBrandyFluids, fireBrandyBuckets, fireBrandy, "grcnether.fireBrandy", null);
		BoozeRegistryHelper.registerBooze(maliceCiderBooze, maliceCiderFluids, maliceCiderBuckets, maliceCider, "grcnether.maliceCider", null);
	}
}
