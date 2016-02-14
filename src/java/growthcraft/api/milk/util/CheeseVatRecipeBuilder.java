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

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

import growthcraft.api.core.util.OreItemStacks;
import growthcraft.api.core.util.TaggedFluidStacks;
import growthcraft.api.milk.MilkRegistry;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CheeseVatRecipeBuilder
{
	private List<ItemStack> outputStacks = new ArrayList<ItemStack>();
	private List<ItemStack> inputStacks = new ArrayList<ItemStack>();
	private List<FluidStack> inputFluids = new ArrayList<FluidStack>();

	private void addItemStacksToList(@Nonnull List<ItemStack> list, @Nonnull Object... objs)
	{
		for (Object obj : objs)
		{
			if (obj instanceof ItemStack)
			{
				list.add((ItemStack)obj);
			}
			else if (obj instanceof OreItemStacks)
			{
				list.addAll(((OreItemStacks)obj).getItemStacks());
			}
			else
			{
				throw new IllegalArgumentException("Wrong type, expected a FluidStack or TaggedFluidStacks");
			}
		}
	}

	public CheeseVatRecipeBuilder outputItems(@Nonnull Object... objs)
	{
		addItemStacksToList(outputStacks, objs);
		return this;
	}

	public CheeseVatRecipeBuilder inputFluids(@Nonnull Object... objs)
	{
		for (Object obj : objs)
		{
			if (obj instanceof FluidStack)
			{
				inputFluids.add((FluidStack)obj);
			}
			else if (obj instanceof TaggedFluidStacks)
			{
				inputFluids.addAll(((TaggedFluidStacks)obj).getFluidStacks());
			}
			else
			{
				throw new IllegalArgumentException("Wrong type, expected a FluidStack or TaggedFluidStacks");
			}
		}
		return this;
	}

	public CheeseVatRecipeBuilder inputItems(@Nonnull Object... objs)
	{
		addItemStacksToList(inputStacks, objs);
		return this;
	}

	public CheeseVatRecipeBuilder register()
	{
		MilkRegistry.instance().cheeseVat().addRecipe(outputStacks, inputFluids, inputStacks);
		return this;
	}

	public static CheeseVatRecipeBuilder buildRecipe()
	{
		return new CheeseVatRecipeBuilder();
	}
}
