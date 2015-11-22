package growthcraft.nether.init;

import growthcraft.api.cellar.booze.Booze;
import growthcraft.api.cellar.booze.BoozeEffect;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.common.Residue;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.common.item.ItemBoozeBottle;
import growthcraft.cellar.util.BoozeRegistryHelper;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.nether.GrowthCraftNether;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.potion.Potion;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class GrcNetherBooze extends GrcModuleBase
{
	public Booze[] fireBrandyBooze;
	public Booze[] maliceCiderBooze;
	public BlockBoozeDefinition[] fireBrandyFluids;
	public BlockBoozeDefinition[] maliceCiderFluids;
	public ItemDefinition fireBrandy;
	public ItemDefinition maliceCider;
	public ItemBucketBoozeDefinition[] fireBrandyBuckets;
	public ItemBucketBoozeDefinition[] maliceCiderBuckets;

	@Override
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

		this.fireBrandy = new ItemDefinition(new ItemBoozeBottle(5, -0.6F, fireBrandyBooze));
		this.maliceCider = new ItemDefinition(new ItemBoozeBottle(5, -0.6F, maliceCiderBooze));
	}

	@Override
	public void register()
	{
		GameRegistry.registerItem(fireBrandy.getItem(), "grcnether.fireBrandy");
		GameRegistry.registerItem(maliceCider.getItem(), "grcnether.maliceCider");

		BoozeRegistryHelper.registerBooze(fireBrandyBooze, fireBrandyFluids, fireBrandyBuckets, fireBrandy, "grcnether.fireBrandy", null);
		BoozeRegistryHelper.registerBooze(maliceCiderBooze, maliceCiderFluids, maliceCiderBuckets, maliceCider, "grcnether.maliceCider", null);

		for (BoozeEffect effect : BoozeRegistryHelper.getBoozeEffects(fireBrandyBooze))
		{
			effect.setTipsy(0.70F, 900);
			effect.addPotionEntry(Potion.fireResistance.id, 3600, 0);
		}

		for (BoozeEffect effect : BoozeRegistryHelper.getBoozeEffects(maliceCiderBooze))
		{
			effect.setTipsy(1.00F, 900);
			effect.addPotionEntry(Potion.regeneration.id, 3600, 0);
			effect.addPotionEntry(Potion.damageBoost.id, 1200, 0);
		}

		CellarRegistry.instance().brewing().addBrewing(
			new FluidStack(FluidRegistry.WATER, GrowthCraftNether.getConfig().fireBrandyYield),
			GrowthCraftNether.blocks.netherCinderrot.asStack(),
			new FluidStack(fireBrandyBooze[0], GrowthCraftNether.getConfig().fireBrandyYield),
			GrowthCraftNether.getConfig().fireBrandyBrewTime,
			Residue.newDefault(0.5F)
		);
		CellarRegistry.instance().pressing().addPressing(
			GrowthCraftNether.items.netherMaliceFruit.getItem(),
			maliceCiderBooze[0],
			GrowthCraftNether.getConfig().maliceCiderPressingTime,
			GrowthCraftNether.getConfig().maliceCiderYield,
			Residue.newDefault(0.3F)
		);
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
