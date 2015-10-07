package growthcraft.cellar.utils;

import growthcraft.api.cellar.Booze;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.item.ItemBoozeBottle;
import growthcraft.cellar.item.ItemBoozeBucketDEPRECATED;
import growthcraft.cellar.item.ItemBucketBooze;
import growthcraft.cellar.item.ItemBlockFluidBooze;
import growthcraft.cellar.block.BlockFluidBooze;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.handler.BucketHandler;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class BoozeRegistryHelper
{
	public static void initializeBooze(Fluid[] boozes, BlockFluidBooze[] fluidBlocks, ItemBucketBooze[] buckets, String basename, int color)
	{
		for (int i = 0; i < boozes.length; ++i)
		{
			boozes[i] = new Booze(basename + i);
			FluidRegistry.registerFluid(boozes[i]);
			fluidBlocks[i] = new BlockFluidBooze(boozes[i], color);
			buckets[i] = new ItemBucketBooze(fluidBlocks[i], boozes, i).setColor(color);
		}
		CellarRegistry.instance().booze().createBooze(boozes, color, "fluid." + basename);
	}

	public static void registerBooze(Fluid[] boozes, BlockFluidBooze[] fluidBlocks, ItemBucketBooze[] buckets, Item bottle, String basename, Item oldBucket)
	{
		for (int i = 0; i < boozes.length; ++i)
		{
			GameRegistry.registerItem(buckets[i], basename + "Bucket." + i);
			GameRegistry.registerBlock(fluidBlocks[i], ItemBlockFluidBooze.class, basename + "Fluid." + i);
			// forward compat recipe
			GameRegistry.addShapelessRecipe(new ItemStack(buckets[i], 1), new ItemStack(oldBucket, 1, i));

			BucketHandler.instance().register(fluidBlocks[i], buckets[i]);

			FluidStack stack = new FluidStack(boozes[i].getID(), FluidContainerRegistry.BUCKET_VOLUME);
			FluidContainerRegistry.registerFluidContainer(stack, new ItemStack(oldBucket, 1, i), FluidContainerRegistry.EMPTY_BUCKET);
			FluidContainerRegistry.registerFluidContainer(stack, new ItemStack(buckets[i]), FluidContainerRegistry.EMPTY_BUCKET);

			FluidStack stack2 = new FluidStack(boozes[i].getID(), GrowthCraftCellar.BOTTLE_VOLUME);
			FluidContainerRegistry.registerFluidContainer(stack2, new ItemStack(bottle, 1, i), GrowthCraftCellar.EMPTY_BOTTLE);
		}
	}
}
