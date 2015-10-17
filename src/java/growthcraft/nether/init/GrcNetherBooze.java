package growthcraft.nether.init;

import growthcraft.api.cellar.Booze;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.item.ItemBoozeBottle;
import growthcraft.cellar.utils.BoozeRegistryHelper;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.nether.GrowthCraftNether;

import net.minecraft.potion.Potion;
import net.minecraft.util.IIcon;

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

	public void preInit()
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
			.setTipsy(0.70F, 900)
			.setPotionEffects(new int[] {Potion.fireResistance.id}, new int[] {3600}));

		this.maliceCider = new ItemDefinition(new ItemBoozeBottle(5, -0.6F, maliceCiderBooze)
			.setColor(GrowthCraftNether.getConfig().maliceCiderColor)
			.setTipsy(1.00F, 900)
			.setPotionEffects(new int[] {Potion.regeneration.id, Potion.damageBoost.id}, new int[] {3600, 1200}));
	}

	public void init()
	{
		register();
	}

	public void register()
	{
		BoozeRegistryHelper.registerBooze(fireBrandyBooze, fireBrandyFluids, fireBrandyBuckets, fireBrandy, "grcnether.fireBrandy", null);
		BoozeRegistryHelper.registerBooze(maliceCiderBooze, maliceCiderFluids, maliceCiderBuckets, maliceCider, "grcnether.maliceCider", null);

		CellarRegistry.instance().pressing().addPressing(GrowthCraftNether.blocks.netherCinderrot.getItem(), fireBrandyBooze[0], GrowthCraftNether.getConfig().fireBrandyPressingTime, 40, 0.3F);
		CellarRegistry.instance().pressing().addPressing(GrowthCraftNether.items.netherMaliceFruit.getItem(), maliceCiderBooze[0], GrowthCraftNether.getConfig().maliceCiderPressingTime, 40, 0.3F);
	}

	public void setBoozeIcons(IIcon icon)
	{
		for (Booze booze : fireBrandyBooze)
		{
			booze.setIcons(icon);
		}

		for (Booze booze : maliceCiderBooze)
		{
			booze.setIcons(icon);
		}
	}
}