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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;
import growthcraft.api.core.util.FluidKey;
import growthcraft.api.core.util.FluidTest;
import growthcraft.api.core.util.ItemKey;
import growthcraft.api.core.util.ItemTest;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CheeseVatRegistry implements ICheeseVatRegistry
{
	private ILogger logger = NullLogger.INSTANCE;
	private Set<FluidKey> fluidIngredients = new HashSet<FluidKey>();
	private Set<ItemKey> itemIngredients = new HashSet<ItemKey>();
	private List<ICheeseVatRecipe> recipes = new ArrayList<ICheeseVatRecipe>();

	@Override
	public void setLogger(@Nonnull ILogger l)
	{
		this.logger = l;
	}

	@Override
	public void addRecipe(@Nonnull List<FluidStack> outputFluids, @Nonnull List<ItemStack> outputItems, @Nonnull List<FluidStack> inputFluids, @Nonnull List<ItemStack> inputItems)
	{
		final ICheeseVatRecipe recipe = new CheeseVatRecipe(outputFluids, outputItems, inputFluids, inputItems);
		recipes.add(recipe);
		logger.info("Adding Cheese Vat recipe {%s}", recipe);
		for (FluidStack stack : inputFluids)
		{
			fluidIngredients.add(new FluidKey(stack));
		}
		for (ItemStack stack : inputItems)
		{
			itemIngredients.add(new ItemKey(stack));
		}
	}

	@Override
	public boolean isFluidIngredient(@Nullable FluidStack fluid)
	{
		if (!FluidTest.isValid(fluid)) return false;
		return fluidIngredients.contains(new FluidKey(fluid));
	}

	@Override
	public boolean isItemIngredient(@Nullable ItemStack item)
	{
		if (!ItemTest.isValid(item)) return false;
		return itemIngredients.contains(new ItemKey(item));
	}

	@Override
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
