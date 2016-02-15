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

import java.util.List;
import java.util.Arrays;

import growthcraft.api.core.definition.IItemStackFactory;
import growthcraft.api.core.definition.IFluidStackFactory;
import growthcraft.milk.GrowthCraftMilk;

import io.netty.buffer.ByteBuf;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public enum EnumCheeseType implements IItemStackFactory, IFluidStackFactory
{
	CHEDDAR("cheddar",
		new EnumCheeseFeature[]{EnumCheeseFeature.HAS_BLOCK},
		new EnumCheeseStage[]{EnumCheeseStage.UNAGED, EnumCheeseStage.AGED, EnumCheeseStage.CUT, EnumCheeseStage.UNWAXED}),
	GORGONZOLA("gorgonzola",
		new EnumCheeseFeature[]{EnumCheeseFeature.HAS_BLOCK},
		new EnumCheeseStage[]{EnumCheeseStage.UNAGED, EnumCheeseStage.AGED, EnumCheeseStage.CUT}),
	EMMENTALER("emmentaler",
		new EnumCheeseFeature[]{EnumCheeseFeature.HAS_BLOCK},
		new EnumCheeseStage[]{EnumCheeseStage.UNAGED, EnumCheeseStage.AGED, EnumCheeseStage.CUT}),
	APPENZELLER("appenzeller",
		new EnumCheeseFeature[]{EnumCheeseFeature.HAS_BLOCK},
		new EnumCheeseStage[]{EnumCheeseStage.UNAGED, EnumCheeseStage.AGED, EnumCheeseStage.CUT}),
	ASIAGO("asiago",
		new EnumCheeseFeature[]{EnumCheeseFeature.HAS_BLOCK},
		new EnumCheeseStage[]{EnumCheeseStage.UNAGED, EnumCheeseStage.AGED, EnumCheeseStage.CUT}),
	PARMESAN("parmesan",
		new EnumCheeseFeature[]{EnumCheeseFeature.HAS_BLOCK},
		new EnumCheeseStage[]{EnumCheeseStage.UNAGED, EnumCheeseStage.AGED, EnumCheeseStage.CUT}),
	MONTEREY("monterey",
		new EnumCheeseFeature[]{EnumCheeseFeature.HAS_BLOCK},
		new EnumCheeseStage[]{EnumCheeseStage.UNAGED, EnumCheeseStage.AGED, EnumCheeseStage.CUT, EnumCheeseStage.UNWAXED}),
	RICOTTA("ricotta",
		new EnumCheeseFeature[]{},
		new EnumCheeseStage[]{EnumCheeseStage.UNAGED, EnumCheeseStage.AGED, EnumCheeseStage.CUT});

	public static final EnumCheeseType[] VALUES = values();

	public final String name;
	public final int meta;
	public final List<EnumCheeseFeature> features;
	public final List<EnumCheeseStage> stages;

	private EnumCheeseType(String n, EnumCheeseFeature[] fets, EnumCheeseStage[] stgs)
	{
		this.name = n;
		this.meta = ordinal();
		this.features = Arrays.asList(fets);
		this.stages = Arrays.asList(stgs);
	}

	public boolean hasBlock()
	{
		return features.contains(EnumCheeseFeature.HAS_BLOCK);
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

	public void writeToStream(ByteBuf stream)
	{
		stream.writeInt(meta);
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
