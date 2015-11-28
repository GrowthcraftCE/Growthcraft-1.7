package growthcraft.cellar.util;

import java.util.List;
import java.util.ArrayList;
import javax.annotation.Nullable;

import growthcraft.api.cellar.booze.Booze;
import growthcraft.api.cellar.booze.BoozeEffect;
import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.booze.IBoozeRegistry;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.common.block.BlockFluidBooze;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.common.item.ItemBlockFluidBooze;
import growthcraft.cellar.common.item.ItemBucketBooze;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.handler.BucketHandler;
import growthcraft.core.integration.NEI;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class BoozeRegistryHelper
{
	private BoozeRegistryHelper() {}

	public static void initializeBooze(Fluid[] boozes, BlockBoozeDefinition[] fluidBlocks, ItemBucketBoozeDefinition[] buckets, String basename, int color)
	{
		for (int i = 0; i < boozes.length; ++i)
		{
			boozes[i] = new Booze(basename + i).setColor(color);
			FluidRegistry.registerFluid(boozes[i]);
			CellarRegistry.instance().booze().registerBooze(boozes[i]);
			final BlockFluidBooze boozeBlock = new BlockFluidBooze(boozes[i]);
			fluidBlocks[i] = new BlockBoozeDefinition(boozeBlock);
			buckets[i] = new ItemBucketBoozeDefinition(new ItemBucketBooze(boozeBlock, boozes[i]));
		}
	}

	public static void registerBooze(Fluid[] boozes, BlockBoozeDefinition[] fluidBlocks, ItemBucketBoozeDefinition[] buckets, ItemDefinition bottle, String basename, @Nullable ItemDefinition oldBucket)
	{
		for (int i = 0; i < boozes.length; ++i)
		{
			GameRegistry.registerItem(buckets[i].getItem(), basename + "Bucket." + i);
			GameRegistry.registerBlock(fluidBlocks[i].getBlock(), ItemBlockFluidBooze.class, basename + "Fluid." + i);

			BucketHandler.instance().register(fluidBlocks[i].getBlock(), buckets[i].getItem());

			final FluidStack boozeStack = new FluidStack(boozes[i], FluidContainerRegistry.BUCKET_VOLUME);
			FluidContainerRegistry.registerFluidContainer(boozeStack, buckets[i].asStack(), FluidContainerRegistry.EMPTY_BUCKET);

			final FluidStack fluidStack = new FluidStack(boozes[i], GrowthCraftCellar.getConfig().bottleCapacity);
			FluidContainerRegistry.registerFluidContainer(fluidStack, bottle.asStack(1, i), GrowthCraftCellar.EMPTY_BOTTLE);


			GameRegistry.addShapelessRecipe(bottle.asStack(3, i), buckets[i].getItem(), Items.glass_bottle, Items.glass_bottle, Items.glass_bottle);
			// forward compat recipe
			if (oldBucket != null)
			{
				GameRegistry.addShapelessRecipe(buckets[i].asStack(), oldBucket.asStack(1, i));
				NEI.hideItem(oldBucket.asStack(1, i));
			}
		}
	}

	public static List<BoozeEffect> getBoozeEffects(Fluid[] boozes)
	{
		final IBoozeRegistry reg = CellarRegistry.instance().booze();
		final List<BoozeEffect> effects = new ArrayList<BoozeEffect>();
		for (int i = 0; i < boozes.length; ++i)
		{
			if (reg.hasTags(boozes[i], BoozeTag.FERMENTED))
			{
				effects.add(reg.getEffect(boozes[i]));
			}
		}
		return effects;
	}
}
