package growthcraft.nether.init;

import java.util.List;
import java.util.ArrayList;

import growthcraft.api.cellar.booze.Booze;
import growthcraft.api.cellar.booze.BoozeEffect;
import growthcraft.api.cellar.booze.BoozeRegistry;
import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.booze.PotionEntry;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.common.Residue;
import growthcraft.api.cellar.fermenting.FermentingRegistry;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.common.item.ItemBoozeBottle;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.BoozeRegistryHelper;
import growthcraft.cellar.util.YeastType;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.nether.GrowthCraftNether;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
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

		this.maliceCiderBooze = new Booze[8];
		this.maliceCiderFluids = new BlockBoozeDefinition[maliceCiderBooze.length];
		this.maliceCiderBuckets = new ItemBucketBoozeDefinition[maliceCiderBooze.length];
		BoozeRegistryHelper.initializeBooze(maliceCiderBooze, maliceCiderFluids, maliceCiderBuckets, "grcnether.maliceCider", GrowthCraftNether.getConfig().maliceCiderColor);
		maliceCiderBooze[4].setColor(GrowthCraftNether.getConfig().amritaColor);
		maliceCiderFluids[4].getBlock().refreshColor();

		maliceCiderBooze[6].setColor(GrowthCraftNether.getConfig().gelidBoozeColor);
		maliceCiderFluids[6].getBlock().refreshColor();

		maliceCiderBooze[7].setColor(GrowthCraftNether.getConfig().vileSlopColor);
		maliceCiderFluids[7].getBlock().refreshColor();

		this.fireBrandy = new ItemDefinition(new ItemBoozeBottle(5, -0.6F, fireBrandyBooze));
		this.maliceCider = new ItemDefinition(new ItemBoozeBottle(5, -0.6F, maliceCiderBooze));
	}

	@Override
	public void register()
	{
		final BoozeRegistry br = CellarRegistry.instance().booze();
		final FermentingRegistry fr = CellarRegistry.instance().fermenting();
		final ItemStack yeastRash = GrowthCraftNether.items.netherRashSpores.asStack();
		final int fermentTime = GrowthCraftCellar.getConfig().fermentTime;

		GameRegistry.registerItem(fireBrandy.getItem(), "grcnether.fireBrandy");
		GameRegistry.registerItem(maliceCider.getItem(), "grcnether.maliceCider");

		BoozeRegistryHelper.registerBooze(fireBrandyBooze, fireBrandyFluids, fireBrandyBuckets, fireBrandy, "grcnether.fireBrandy", null);
		BoozeRegistryHelper.registerBooze(maliceCiderBooze, maliceCiderFluids, maliceCiderBuckets, maliceCider, "grcnether.maliceCider", null);

		for (BoozeEffect effect : BoozeRegistryHelper.getBoozeEffects(fireBrandyBooze))
		{
			effect.setTipsy(0.70F, 900);
			effect.addPotionEntry(Potion.fireResistance.id, 3600, 0);
		}

		{
			final List<PotionEntry> defaultPotionEntries = new ArrayList<PotionEntry>();
			defaultPotionEntries.add(new PotionEntry(Potion.regeneration.id, 3600, 0));
			defaultPotionEntries.add(new PotionEntry(Potion.damageBoost.id, 1200, 1));

			final FluidStack[] fs = new FluidStack[maliceCiderBooze.length];
			for (int i = 0; i < maliceCiderBooze.length; ++i)
			{
				fs[i] = new FluidStack(maliceCiderBooze[i], 1);
			}

			br.addTags(maliceCiderBooze[0], BoozeTag.YOUNG);
			//br.getEffect(maliceCiderBooze[0]);

			// fermented
			br.addTags(maliceCiderBooze[1], BoozeTag.FERMENTED);
			br.getEffect(maliceCiderBooze[1])
				.setTipsy(1.00F, 900)
				.addPotionEntries(defaultPotionEntries);
			fr.addFermentation(fs[1], fs[0], YeastType.BREWERS.asStack(), fermentTime);
			fr.addFermentation(fs[1], fs[0], new ItemStack(Items.nether_wart), (int)(fermentTime * 0.66));

			// potent
			br.addTags(maliceCiderBooze[2], BoozeTag.FERMENTED, BoozeTag.POTENT);
			br.getEffect(maliceCiderBooze[2])
				.setTipsy(1.00F, 900)
				.addPotionEntries(defaultPotionEntries);
			fr.addFermentation(fs[2], fs[1], new ItemStack(Items.glowstone_dust), fermentTime);
			fr.addFermentation(fs[2], fs[3], new ItemStack(Items.glowstone_dust), fermentTime);

			// extended
			br.addTags(maliceCiderBooze[3], BoozeTag.FERMENTED, BoozeTag.EXTENDED);
			br.getEffect(maliceCiderBooze[3])
				.setTipsy(1.00F, 900)
				.addPotionEntries(defaultPotionEntries);
			fr.addFermentation(fs[3], fs[1], new ItemStack(Items.redstone), fermentTime);
			fr.addFermentation(fs[3], fs[2], new ItemStack(Items.redstone), fermentTime);

			// Amrita - Ethereal Yeast
			br.addTags(maliceCiderBooze[4], BoozeTag.FERMENTED, BoozeTag.HYPER_EXTENDED);
			br.getEffect(maliceCiderBooze[4])
				.setTipsy(1.00F, 900)
				.addPotionEntries(defaultPotionEntries);
			fr.addFermentation(fs[4], fs[2], YeastType.ETHEREAL.asStack(), fermentTime);
			fr.addFermentation(fs[4], fs[3], YeastType.ETHEREAL.asStack(), fermentTime);

			// :Intoxicated - Origin Yeast
			br.addTags(maliceCiderBooze[5], BoozeTag.FERMENTED, BoozeTag.INTOXICATED);
			br.getEffect(maliceCiderBooze[5])
				.setTipsy(1.00F, 900)
				.addPotionEntries(defaultPotionEntries);
			fr.addFermentation(fs[5], fs[2], YeastType.ORIGIN.asStack(), fermentTime);
			fr.addFermentation(fs[5], fs[3], YeastType.ORIGIN.asStack(), fermentTime);

			// Gelid Booze - Lager Yeast
			br.addTags(maliceCiderBooze[6], BoozeTag.FERMENTED, BoozeTag.CHILLED);
			br.getEffect(maliceCiderBooze[6])
				.setTipsy(1.00F, 900)
				.addPotionEntry(Potion.regeneration.id, 3600, 0)
				.addPotionEntry(Potion.moveSpeed.id, 1200, 1);
			fr.addFermentation(fs[6], fs[2], YeastType.LAGER.asStack(), fermentTime);
			fr.addFermentation(fs[6], fs[3], YeastType.LAGER.asStack(), fermentTime);

			// Vile Slop - Netherrash
			br.addTags(maliceCiderBooze[7], BoozeTag.FERMENTED, BoozeTag.INTOXICATED, BoozeTag.DEADLY);
			br.getEffect(maliceCiderBooze[7])
				.setTipsy(1.00F, 900)
				.addPotionEntries(defaultPotionEntries);
			fr.addFermentation(fs[7], fs[1], yeastRash, fermentTime);
			fr.addFermentation(fs[7], fs[2], yeastRash, fermentTime);
			fr.addFermentation(fs[7], fs[3], yeastRash, fermentTime);
			fr.addFermentation(fs[7], fs[4], yeastRash, fermentTime);
			fr.addFermentation(fs[7], fs[5], yeastRash, fermentTime);
			fr.addFermentation(fs[7], fs[6], yeastRash, fermentTime);
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
