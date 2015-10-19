package growthcraft.cellar.util;

import javax.annotation.Nullable;

import growthcraft.api.cellar.Booze;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.block.BlockFluidBooze;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.item.ItemBlockFluidBooze;
import growthcraft.cellar.item.ItemBucketBooze;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.handler.BucketHandler;
import growthcraft.core.integration.NEI;

import cpw.mods.fml.common.registry.GameRegistry;

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
			boozes[i] = new Booze(basename + i);
			FluidRegistry.registerFluid(boozes[i]);
			final BlockFluidBooze boozeBlock = new BlockFluidBooze(boozes[i], color);
			fluidBlocks[i] = new BlockBoozeDefinition(boozeBlock);
			buckets[i] = new ItemBucketBoozeDefinition(new ItemBucketBooze(boozeBlock, boozes, i).setColor(color));
		}
		CellarRegistry.instance().booze().createBooze(boozes, color, "fluid." + basename);
	}

	public static void registerBooze(Fluid[] boozes, BlockBoozeDefinition[] fluidBlocks, ItemBucketBoozeDefinition[] buckets, ItemDefinition bottle, String basename, @Nullable ItemDefinition oldBucket)
	{
		for (int i = 0; i < boozes.length; ++i)
		{
			GameRegistry.registerItem(buckets[i].getItem(), basename + "Bucket." + i);
			GameRegistry.registerBlock(fluidBlocks[i].getBlock(), ItemBlockFluidBooze.class, basename + "Fluid." + i);
			// forward compat recipe
			if (oldBucket != null)
			{
				GameRegistry.addShapelessRecipe(buckets[i].asStack(), oldBucket.asStack(1, i));
			}

			BucketHandler.instance().register(fluidBlocks[i].getBlock(), buckets[i].getItem());

			final FluidStack boozeStack = new FluidStack(boozes[i], FluidContainerRegistry.BUCKET_VOLUME);
			if (oldBucket != null)
			{
				FluidContainerRegistry.registerFluidContainer(boozeStack, oldBucket.asStack(1, i), FluidContainerRegistry.EMPTY_BUCKET);
			}
			FluidContainerRegistry.registerFluidContainer(boozeStack, buckets[i].asStack(), FluidContainerRegistry.EMPTY_BUCKET);

			final FluidStack bottleStack = new FluidStack(boozes[i], GrowthCraftCellar.BOTTLE_VOLUME);
			FluidContainerRegistry.registerFluidContainer(bottleStack, bottle.asStack(1, i), GrowthCraftCellar.EMPTY_BOTTLE);

			if (oldBucket != null) NEI.hideItem(oldBucket.asStack(1, i));
		}
	}
}
