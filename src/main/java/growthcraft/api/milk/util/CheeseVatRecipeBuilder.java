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
package growthcraft.api.milk.util;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

import growthcraft.api.core.definition.IMultiFluidStacks;
import growthcraft.api.core.definition.IMultiItemStacks;
import growthcraft.api.core.util.MultiStacksUtil;
import growthcraft.api.milk.MilkRegistry;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CheeseVatRecipeBuilder
{
	private String label;
	private List<FluidStack> outputFluids = new ArrayList<FluidStack>();
	private List<ItemStack> outputStacks = new ArrayList<ItemStack>();
	private List<IMultiFluidStacks> inputFluids = new ArrayList<IMultiFluidStacks>();
	private List<IMultiItemStacks> inputStacks = new ArrayList<IMultiItemStacks>();

	public CheeseVatRecipeBuilder(String l)
	{
		this.label = l;
	}

	private void addFluidStacksToList(@Nonnull List<IMultiFluidStacks> list, @Nonnull Object... objs)
	{
		for (Object obj : objs)
		{
			list.add(MultiStacksUtil.toMultiFluidStacks(obj));
		}
	}

	private void addItemStacksToList(@Nonnull List<IMultiItemStacks> list, @Nonnull Object... objs)
	{
		for (Object obj : objs)
		{
			list.add(MultiStacksUtil.toMultiItemStacks(obj));
		}
	}

	public CheeseVatRecipeBuilder outputFluids(@Nonnull FluidStack... objs)
	{
		outputFluids.addAll(Arrays.asList(objs));
		return this;
	}

	public CheeseVatRecipeBuilder outputItems(@Nonnull ItemStack... objs)
	{
		outputStacks.addAll(Arrays.asList(objs));
		return this;
	}

	public CheeseVatRecipeBuilder inputFluids(@Nonnull Object... objs)
	{
		addFluidStacksToList(inputFluids, objs);
		return this;
	}

	public CheeseVatRecipeBuilder inputItems(@Nonnull Object... objs)
	{
		addItemStacksToList(inputStacks, objs);
		return this;
	}

	public CheeseVatRecipeBuilder register()
	{
		MilkRegistry.instance().cheeseVat().addRecipe(outputFluids, outputStacks, inputFluids, inputStacks);
		return this;
	}

	public static CheeseVatRecipeBuilder buildRecipe(String label)
	{
		return new CheeseVatRecipeBuilder(label);
	}
}
