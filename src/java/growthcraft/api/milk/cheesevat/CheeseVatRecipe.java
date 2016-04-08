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
package growthcraft.api.milk.cheesevat;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.core.definition.IMultiFluidStacks;
import growthcraft.api.core.definition.IMultiItemStacks;
import growthcraft.api.core.fluids.FluidTest;
import growthcraft.api.core.item.ItemTest;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class CheeseVatRecipe implements ICheeseVatRecipe
{
	private List<FluidStack> outputFluids;
	private List<ItemStack> outputItems;
	private List<IMultiFluidStacks> inputFluids;
	private List<IMultiItemStacks> inputItems;

	public CheeseVatRecipe(List<FluidStack> pOutputFluids, List<ItemStack> pOutputItems, List<IMultiFluidStacks> pInputFluids, List<IMultiItemStacks> pInputItems)
	{
		this.outputFluids = pOutputFluids;
		this.outputItems = pOutputItems;
		this.inputFluids = pInputFluids;
		this.inputItems = pInputItems;
	}

	@Override
	public List<FluidStack> getOutputFluidStacks()
	{
		return outputFluids;
	}

	@Override
	public List<ItemStack> getOutputItemStacks()
	{
		return outputItems;
	}

	@Override
	public List<IMultiFluidStacks> getInputFluidStacks()
	{
		return inputFluids;
	}

	@Override
	public List<IMultiItemStacks> getInputItemStacks()
	{
		return inputItems;
	}

	@Override
	public boolean isMatchingRecipe(@Nonnull List<FluidStack> fluids, @Nonnull List<ItemStack> items)
	{
		if (!FluidTest.isValidAndExpected(inputFluids, fluids)) return false;
		if (!ItemTest.isValidAndExpected(inputItems, items)) return false;
		return true;
	}

	@Override
	public boolean isFluidIngredient(@Nullable Fluid fluid)
	{
		for (IMultiFluidStacks stack : inputFluids)
		{
			if (stack.containsFluid(fluid)) return true;
		}
		return false;
	}

	@Override
	public boolean isFluidIngredient(@Nullable FluidStack fluidStack)
	{
		for (IMultiFluidStacks stack : inputFluids)
		{
			if (stack.containsFluidStack(fluidStack)) return true;
		}
		return false;
	}

	@Override
	public boolean isItemIngredient(@Nullable ItemStack itemStack)
	{
		for (IMultiItemStacks item : inputItems)
		{
			if (item.containsItemStack(itemStack)) return true;
		}
		return false;
	}

	@Override
	public String toString()
	{
		return String.format("CheeseVatRecipe(output_fluids: %s, output_items: %s, input_fluids: %s, input_items: %s)", outputFluids, outputItems, inputFluids, inputItems);
	}
}
