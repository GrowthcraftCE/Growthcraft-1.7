/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.core.util;

import growthcraft.core.common.definition.FluidDefinition;
import growthcraft.core.common.definition.GrcBlockFluidDefinition;
import growthcraft.core.common.definition.ItemTypeDefinition;
import growthcraft.core.common.item.ItemBucketFluid;
import growthcraft.core.common.item.ItemBottleFluid;
import growthcraft.core.event.EventHandlerBucketFill;
import growthcraft.core.GrowthCraftCore;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidContainerRegistry;

/**
 * A simple factory for creating generic fluid bottles, blocks etc..
 */
public class FluidFactory
{
	public static class FluidDetails
	{
		public FluidDefinition fluid;
		public GrcBlockFluidDefinition block;
		public ItemTypeDefinition<ItemBottleFluid> bottle;
		public ItemTypeDefinition<ItemBucketFluid> bucket;

		public Fluid getFluid()
		{
			return fluid.getFluid();
		}

		public FluidDetails registerObjects(String prefix, String basename)
		{
			block.getBlock().setBlockName(prefix + ".BlockFluid" + basename);
			bottle.getItem().setUnlocalizedName(prefix + ".BottleFluid" + basename);
			bucket.getItem().setUnlocalizedName(prefix + ".BucketFluid" + basename);

			block.register(prefix + ".BlockFluid" + basename);
			bottle.register(prefix + ".BottleFluid" + basename);
			bucket.register(prefix + ".BucketFluid" + basename);

			EventHandlerBucketFill.instance().register(block.getBlock(), bucket.getItem());

			final FluidStack boozeStack = fluid.asFluidStack(FluidContainerRegistry.BUCKET_VOLUME);
			FluidContainerRegistry.registerFluidContainer(boozeStack, bucket.asStack(), FluidContainerRegistry.EMPTY_BUCKET);

			final FluidStack fluidStack = fluid.asFluidStack(GrowthCraftCore.getConfig().bottleCapacity);
			FluidContainerRegistry.registerFluidContainer(fluidStack, bottle.asStack(1), GrowthCraftCore.EMPTY_BOTTLE);
			return this;
		}

		public FluidDetails setCreativeTab(CreativeTabs tab)
		{
			block.getBlock().setCreativeTab(tab);
			bottle.getItem().setCreativeTab(tab);
			bucket.getItem().setCreativeTab(tab);
			return this;
		}

		public FluidDetails setItemColor(int color)
		{
			bottle.getItem().setColor(color);
			bucket.getItem().setColor(color);
			return this;
		}
	}

	private static FluidFactory INSTANCE = new FluidFactory();

	public FluidFactory() {}

	public FluidDetails create(Fluid fluid)
	{
		final FluidDetails details = new FluidDetails();
		details.fluid = new FluidDefinition(fluid);
		details.fluid.register();
		details.block = GrcBlockFluidDefinition.create(details.fluid);
		details.bottle = new ItemTypeDefinition<ItemBottleFluid>(new ItemBottleFluid(fluid));
		details.bucket = new ItemTypeDefinition<ItemBucketFluid>(new ItemBucketFluid(details.block.getBlock(), fluid, null));
		return details;
	}

	public static FluidFactory instance()
	{
		return INSTANCE;
	}
}
