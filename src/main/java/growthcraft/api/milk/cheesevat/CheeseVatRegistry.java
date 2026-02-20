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

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.core.definition.IMultiFluidStacks;
import growthcraft.api.core.definition.IMultiItemStacks;
import growthcraft.api.core.fluids.FluidTest;
import growthcraft.api.core.item.ItemTest;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class CheeseVatRegistry implements ICheeseVatRegistry
{
	private ILogger logger = NullLogger.INSTANCE;
	private List<ICheeseVatRecipe> recipes = new ArrayList<ICheeseVatRecipe>();

	@Override
	public void setLogger(@Nonnull ILogger l)
	{
		this.logger = l;
	}

	@Override
	public void addRecipe(ICheeseVatRecipe recipe)
	{
		recipes.add(recipe);
		logger.debug("Added Cheese Vat recipe {%s}", recipe);
	}

	@Override
	public void addRecipe(@Nonnull List<FluidStack> outputFluids, @Nonnull List<ItemStack> outputItems, @Nonnull List<IMultiFluidStacks> inputFluids, @Nonnull List<IMultiItemStacks> inputItems)
	{
		final ICheeseVatRecipe recipe = new CheeseVatRecipe(outputFluids, outputItems, inputFluids, inputItems);
		addRecipe(recipe);
	}

	@Override
	public boolean isFluidIngredient(@Nullable Fluid fluid)
	{
		if (!FluidTest.isValid(fluid)) return false;
		for (ICheeseVatRecipe recipe : recipes)
		{
			if (recipe.isFluidIngredient(fluid)) return true;
		}
		return false;
	}

	@Override
	public boolean isFluidIngredient(@Nullable FluidStack fluid)
	{
		if (!FluidTest.isValid(fluid)) return false;
		for (ICheeseVatRecipe recipe : recipes)
		{
			if (recipe.isFluidIngredient(fluid)) return true;
		}
		return false;
	}

	@Override
	public boolean isItemIngredient(@Nullable ItemStack item)
	{
		if (!ItemTest.isValid(item)) return false;
		for (ICheeseVatRecipe recipe : recipes)
		{
			if (recipe.isItemIngredient(item)) return true;
		}
		return false;
	}

	@Override
	@Nullable
	public ICheeseVatRecipe findRecipe(@Nonnull List<FluidStack> fluids, @Nonnull List<ItemStack> stacks)
	{
		for (ICheeseVatRecipe recipe : recipes)
		{
			if (recipe.isMatchingRecipe(fluids, stacks))
			{
				return recipe;
			}
		}
		return null;
	}
}
