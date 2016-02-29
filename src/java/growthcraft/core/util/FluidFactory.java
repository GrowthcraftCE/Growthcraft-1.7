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

import growthcraft.api.core.util.NumUtils;
import growthcraft.core.common.definition.FluidDefinition;
import growthcraft.core.common.definition.GrcBlockFluidDefinition;
import growthcraft.core.common.definition.ItemTypeDefinition;
import growthcraft.core.common.item.ItemBucketFluid;
import growthcraft.core.common.item.ItemBottleFluid;
import growthcraft.core.eventhandler.EventHandlerBucketFill;
import growthcraft.core.GrowthCraftCore;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

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
		private int itemColor = 0xFFFFFF;

		public Fluid getFluid()
		{
			return fluid.getFluid();
		}

		public Block getFluidBlock()
		{
			return block != null ? block.getBlock() : null;
		}

		public ItemStack asFluidBlockItemStack(int size)
		{
			return block != null ? block.asStack(size) : null;
		}

		public ItemStack asFluidBlockItemStack()
		{
			return asFluidBlockItemStack(1);
		}

		public ItemStack asBucketItemStack(int size)
		{
			return bucket != null ? bucket.asStack(size) : null;
		}

		public ItemStack asBucketItemStack()
		{
			return asBucketItemStack(1);
		}

		public ItemStack asBottleItemStack(int size)
		{
			return bottle != null ? bottle.asStack(size) : null;
		}

		public ItemStack asBottleItemStack()
		{
			return asBottleItemStack(1);
		}

		public FluidDetails registerObjects(String prefix, String basename)
		{
			if (block != null) block.getBlock().setBlockName(prefix + ".BlockFluid" + basename);
			if (bottle != null) bottle.getItem().setUnlocalizedName(prefix + ".BottleFluid" + basename);
			if (bucket != null) bucket.getItem().setUnlocalizedName(prefix + ".BucketFluid" + basename);

			if (block != null) block.register(prefix + ".BlockFluid" + basename);
			if (bottle != null) bottle.register(prefix + ".BottleFluid" + basename);
			if (bucket != null) bucket.register(prefix + ".BucketFluid" + basename);

			if (block != null && bucket != null)
			{
				EventHandlerBucketFill.instance().register(block.getBlock(), bucket.getItem());
			}

			if (bucket != null)
			{
				final FluidStack boozeStack = fluid.asFluidStack(FluidContainerRegistry.BUCKET_VOLUME);
				FluidContainerRegistry.registerFluidContainer(boozeStack, bucket.asStack(), FluidContainerRegistry.EMPTY_BUCKET);
			}

			if (bottle != null)
			{
				final FluidStack fluidStack = fluid.asFluidStack(GrowthCraftCore.getConfig().bottleCapacity);
				FluidContainerRegistry.registerFluidContainer(fluidStack, bottle.asStack(1), GrowthCraftCore.EMPTY_BOTTLE);
			}
			return this;
		}

		public FluidDetails setCreativeTab(CreativeTabs tab)
		{
			if (block != null) block.getBlock().setCreativeTab(tab);
			if (bottle != null) bottle.getItem().setCreativeTab(tab);
			if (bucket != null) bucket.getItem().setCreativeTab(tab);
			return this;
		}

		public FluidDetails setItemColor(int color)
		{
			this.itemColor = color;
			if (bottle != null) bottle.getItem().setColor(color);
			if (bucket != null) bucket.getItem().setColor(color);
			return this;
		}

		public FluidDetails setBlockColor(int color)
		{
			if (block != null) block.getBlock().setColor(color);
			return this;
		}

		public FluidDetails refreshItemColor()
		{
			return setItemColor(fluid.getFluid().getColor());
		}

		public FluidDetails refreshBlockColor()
		{
			return setBlockColor(fluid.getFluid().getColor());
		}

		public int getItemColor()
		{
			return itemColor;
		}
	}

	public static final int FEATURE_BLOCK = 1;
	public static final int FEATURE_BOTTLE = 2;
	public static final int FEATURE_BUCKET = 4;
	public static final int FEATURE_NONE = 0;
	public static final int FEATURE_ALL = FEATURE_BLOCK | FEATURE_BOTTLE | FEATURE_BUCKET;
	private static FluidFactory INSTANCE = new FluidFactory();

	public FluidFactory() {}

	public FluidDetails create(Fluid fluid, int features)
	{
		final FluidDetails details = new FluidDetails();
		details.fluid = new FluidDefinition(fluid);
		details.fluid.register();
		if (NumUtils.isFlagged(features, FEATURE_BLOCK))
			details.block = GrcBlockFluidDefinition.create(details.fluid);

		if (NumUtils.isFlagged(features, FEATURE_BOTTLE))
			details.bottle = new ItemTypeDefinition<ItemBottleFluid>(new ItemBottleFluid(fluid));

		if (NumUtils.isFlagged(features, FEATURE_BUCKET))
			details.bucket = new ItemTypeDefinition<ItemBucketFluid>(new ItemBucketFluid(details.block != null ? details.block.getBlock() : null, fluid, null));

		details.refreshItemColor();
		details.refreshBlockColor();
		return details;
	}

	public FluidDetails create(Fluid fluid)
	{
		return create(fluid, FEATURE_ALL);
	}

	public static FluidFactory instance()
	{
		return INSTANCE;
	}
}
