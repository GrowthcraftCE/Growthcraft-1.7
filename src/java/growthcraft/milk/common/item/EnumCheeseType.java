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
package growthcraft.milk.common.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import growthcraft.api.core.definition.IFluidStackFactory;
import growthcraft.api.core.definition.IItemStackFactory;
import growthcraft.api.core.item.ItemTest;
import growthcraft.milk.GrowthCraftMilk;

import io.netty.buffer.ByteBuf;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public enum EnumCheeseType implements IItemStackFactory, IFluidStackFactory
{
	CHEDDAR("cheddar", 0xed9200,
		new EnumCheeseFeature[]{EnumCheeseFeature.HAS_BLOCK},
		new EnumCheeseStage[]{EnumCheeseStage.UNWAXED, EnumCheeseStage.UNAGED, EnumCheeseStage.AGED, EnumCheeseStage.CUT}),
	GORGONZOLA("gorgonzola", 0xeae7de,
		new EnumCheeseFeature[]{EnumCheeseFeature.HAS_BLOCK},
		new EnumCheeseStage[]{EnumCheeseStage.UNAGED, EnumCheeseStage.AGED, EnumCheeseStage.CUT}),
	EMMENTALER("emmentaler", 0xddddbc,
		new EnumCheeseFeature[]{EnumCheeseFeature.HAS_BLOCK},
		new EnumCheeseStage[]{EnumCheeseStage.UNAGED, EnumCheeseStage.AGED, EnumCheeseStage.CUT}),
	APPENZELLER("appenzeller", 0xf3e2a7,
		new EnumCheeseFeature[]{EnumCheeseFeature.HAS_BLOCK},
		new EnumCheeseStage[]{EnumCheeseStage.UNAGED, EnumCheeseStage.AGED, EnumCheeseStage.CUT}),
	ASIAGO("asiago", 0xbfb68d,
		new EnumCheeseFeature[]{EnumCheeseFeature.HAS_BLOCK},
		new EnumCheeseStage[]{EnumCheeseStage.UNAGED, EnumCheeseStage.AGED, EnumCheeseStage.CUT}),
	PARMESAN("parmesan", 0xd8d5c6,
		new EnumCheeseFeature[]{EnumCheeseFeature.HAS_BLOCK},
		new EnumCheeseStage[]{EnumCheeseStage.UNAGED, EnumCheeseStage.AGED, EnumCheeseStage.CUT}),
	MONTEREY("monterey", 0xf5f5da,
		new EnumCheeseFeature[]{EnumCheeseFeature.HAS_BLOCK},
		new EnumCheeseStage[]{EnumCheeseStage.UNWAXED, EnumCheeseStage.UNAGED, EnumCheeseStage.AGED, EnumCheeseStage.CUT}),
	RICOTTA("ricotta", 0xc8c8c5,
		new EnumCheeseFeature[]{},
		new EnumCheeseStage[]{EnumCheeseStage.UNAGED, EnumCheeseStage.AGED, EnumCheeseStage.CUT});

	public static final EnumCheeseType[] VALUES = values();

	public final String name;
	public final int color;
	public final int meta;
	public final List<EnumCheeseFeature> features;
	public final List<EnumCheeseStage> stages;
	public final List<ItemStack> waxes = new ArrayList<ItemStack>();

	private EnumCheeseType(String n, int c, EnumCheeseFeature[] fets, EnumCheeseStage[] stgs)
	{
		this.name = n;
		this.color = c;
		this.meta = ordinal();
		this.features = Arrays.asList(fets);
		this.stages = Arrays.asList(stgs);
	}

	public boolean canWax(ItemStack stack)
	{
		if (ItemTest.isValid(stack))
		{
			for (ItemStack wax : waxes)
			{
				if (wax.isItemEqual(stack))
				{
					return stack.stackSize >= wax.stackSize;
				}
			}
		}
		return false;
	}

	public int getColor()
	{
		return color;
	}

	public boolean hasBlock()
	{
		return features.contains(EnumCheeseFeature.HAS_BLOCK);
	}

	public boolean hasCurdBlock()
	{
		return true;
	}

	public ItemStack asStack(int size)
	{
		return GrowthCraftMilk.items.cheese.asStack(size, meta);
	}

	public ItemStack asStack()
	{
		return asStack(1);
	}

	public ItemStack asBlockItemStack(int size)
	{
		return GrowthCraftMilk.blocks.cheeseBlock.asStack(size, meta);
	}

	public ItemStack asBlockItemStack()
	{
		return asBlockItemStack(1);
	}

	public ItemStack asCurdItemStack(int size)
	{
		return GrowthCraftMilk.blocks.hangingCurds.asStack(size, meta);
	}

	public ItemStack asCurdItemStack()
	{
		return asCurdItemStack(1);
	}

	public FluidStack asFluidStack(int amount)
	{
		return GrowthCraftMilk.fluids.cheeses.get(this).fluid.asFluidStack(amount);
	}

	public FluidStack asFluidStack()
	{
		return asFluidStack(1);
	}

	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		tag.setInteger("cheese_id", meta);
		return tag;
	}

	public boolean writeToStream(ByteBuf stream)
	{
		stream.writeInt(meta);
		return false;
	}

	/**
	 * Returns a EnumCheeseType given an id, if the id is invalid, returns CHEDDAR
	 *
	 * @param id  cheese id
	 * @return cheese
	 */
	public static EnumCheeseType getSafeById(int id)
	{
		if (id >= 0 && id < VALUES.length) return VALUES[id];
		return CHEDDAR;
	}

	public static EnumCheeseType loadFromStream(ByteBuf stream)
	{
		final int id = stream.readInt();
		return getSafeById(id);
	}

	public static EnumCheeseType loadFromNBT(NBTTagCompound nbt)
	{
		final int id = nbt.getInteger("cheese_id");
		return getSafeById(id);
	}
}
