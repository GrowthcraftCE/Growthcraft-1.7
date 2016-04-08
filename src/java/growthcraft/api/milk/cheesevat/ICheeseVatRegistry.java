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
import growthcraft.api.core.log.ILoggable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public interface ICheeseVatRegistry extends ILoggable
{
	/**
	 * Adds a new CheeseVat recipe
	 *
	 * @param recipe - the recipe to add
	 */
	void addRecipe(ICheeseVatRecipe recipe);

	/**
	 * Adds a new CheeseVat recipe
	 *
	 * @param outputFluids - fluids to output, currently only supports 1
	 * @param outputItems - items to output, usually empty
	 * @param inputFluids - input fluids, maximum 2
	 * @param inputItems - input items, maximum 3
	 */
	void addRecipe(@Nonnull List<FluidStack> outputFluids, @Nonnull List<ItemStack> outputItems, @Nonnull List<IMultiFluidStacks> inputFluids, @Nonnull List<IMultiItemStacks> inputItems);

	/**
	 * Determine if the fluid is a valid ingredient
	 *
	 * @return true, the fluid is an ingredient, false otherwise
	 */
	boolean isFluidIngredient(@Nullable Fluid fluid);

	/**
	 * Determine if the fluidstack is a valid input fluid
	 *
	 * @return true, the fluid is an ingredient, false otherwise
	 */
	boolean isFluidIngredient(@Nullable FluidStack fluid);

	/**
	 * Determines if the item is an ingredient
	 *
	 * @return true, the item is an ingredient, false otherwise
	 */
	boolean isItemIngredient(@Nullable ItemStack item);

	/**
	 * Finds a recipe given the input fluids and input items
	 * Note that this should filter the recipes out by fluid amounts and input items
	 * THAT IS: If the provided fluids do not have an amount greater than or equal to the input fluids, it should return null
	 * Same with items
	 *
	 * @return recipe
	 */
	@Nullable ICheeseVatRecipe findRecipe(@Nonnull List<FluidStack> inputFluids, @Nonnull List<ItemStack> inputItems);
}
