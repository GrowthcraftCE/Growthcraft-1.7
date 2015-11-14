package growthcraft.cellar.util;

import javax.annotation.Nullable;

import growthcraft.api.cellar.booze.Booze;
import growthcraft.api.cellar.booze.BoozeRegistry;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.fermenting.FermentingRegistry;
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
import net.minecraft.item.ItemStack;
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
			final BlockFluidBooze boozeBlock = new BlockFluidBooze(boozes[i], color);
			fluidBlocks[i] = new BlockBoozeDefinition(boozeBlock);
			buckets[i] = new ItemBucketBoozeDefinition(new ItemBucketBooze(boozeBlock, boozes[i]));
		}
		CellarRegistry.instance().booze().createBooze(boozes, "fluid." + basename);
	}

	public static void registerDefaultFermentation(Fluid[] boozes)
	{
		final BoozeRegistry boozeReg = CellarRegistry.instance().booze();
		final FermentingRegistry fermentReg = CellarRegistry.instance().fermenting();

		boozeReg.addTags(boozes[0], "young");
		boozeReg.addTags(boozes[1], "fermented");
		boozeReg.addTags(boozes[2], "fermented", "potent");
		boozeReg.addTags(boozes[3], "fermented", "extended");

		fermentReg.addFermentation(new FluidStack(boozes[1], 1), new FluidStack(boozes[0], 1), YeastType.BREWERS.asStack(), GrowthCraftCellar.getConfig().fermentSpeed);
		fermentReg.addFermentation(new FluidStack(boozes[1], 1), new FluidStack(boozes[0], 1), new ItemStack(Items.nether_wart), (int)(GrowthCraftCellar.getConfig().fermentSpeed * 0.66));
		fermentReg.addFermentation(new FluidStack(boozes[2], 1), new FluidStack(boozes[1], 1), new ItemStack(Items.glowstone_dust), GrowthCraftCellar.getConfig().fermentSpeed);
		fermentReg.addFermentation(new FluidStack(boozes[2], 1), new FluidStack(boozes[3], 1), new ItemStack(Items.glowstone_dust), GrowthCraftCellar.getConfig().fermentSpeed);
		fermentReg.addFermentation(new FluidStack(boozes[3], 1), new FluidStack(boozes[1], 1), new ItemStack(Items.redstone), GrowthCraftCellar.getConfig().fermentSpeed);
		fermentReg.addFermentation(new FluidStack(boozes[3], 1), new FluidStack(boozes[2], 1), new ItemStack(Items.redstone), GrowthCraftCellar.getConfig().fermentSpeed);
	}

	public static void registerBooze(Fluid[] boozes, BlockBoozeDefinition[] fluidBlocks, ItemBucketBoozeDefinition[] buckets, ItemDefinition bottle, String basename, @Nullable ItemDefinition oldBucket)
	{
		for (int i = 0; i < boozes.length; ++i)
		{
			GameRegistry.registerItem(buckets[i].getItem(), basename + "Bucket." + i);
			GameRegistry.registerBlock(fluidBlocks[i].getBlock(), ItemBlockFluidBooze.class, basename + "Fluid." + i);

			BucketHandler.instance().register(fluidBlocks[i].getBlock(), buckets[i].getItem());

			final FluidStack boozeStack = new FluidStack(boozes[i], FluidContainerRegistry.BUCKET_VOLUME);
			if (oldBucket != null)
			{
				FluidContainerRegistry.registerFluidContainer(boozeStack, oldBucket.asStack(1, i), FluidContainerRegistry.EMPTY_BUCKET);
			}
			FluidContainerRegistry.registerFluidContainer(boozeStack, buckets[i].asStack(), FluidContainerRegistry.EMPTY_BUCKET);

			final FluidStack fluidStack = new FluidStack(boozes[i], GrowthCraftCellar.getConfig().bottleCapacity);
			FluidContainerRegistry.registerFluidContainer(fluidStack, bottle.asStack(1, i), GrowthCraftCellar.EMPTY_BOTTLE);


			GameRegistry.addShapelessRecipe(bottle.asStack(3, i), buckets[i].getItem(), Items.glass_bottle, Items.glass_bottle, Items.glass_bottle);
			// forward compat recipe
			if (oldBucket != null)
			{
				GameRegistry.addShapelessRecipe(buckets[i].asStack(), oldBucket.asStack(1, i));
			}
			if (oldBucket != null) NEI.hideItem(oldBucket.asStack(1, i));
		}
	}
}
