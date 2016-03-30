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
package growthcraft.api.milk.churn;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;
import growthcraft.api.core.fluids.FluidKey;
import growthcraft.api.core.fluids.FluidTest;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class ChurnRegistry implements IChurnRegistry
{
	protected ILogger logger = NullLogger.INSTANCE;
	private Map<Fluid, IChurnRecipe> recipes = new HashMap<Fluid, IChurnRecipe>();
	private Set<FluidKey> fluidIngredients = new HashSet<FluidKey>();

	@Override
	public void setLogger(@Nonnull ILogger l)
	{
		this.logger = l;
	}

	@Override
	public void addRecipe(@Nonnull IChurnRecipe recipe)
	{
		final FluidStack fluidStack = recipe.getInputFluidStack();
		final Fluid fluid = fluidStack.getFluid();
		if (recipes.containsKey(fluid))
		{
			logger.warn("Overwriting existing churn recipe for {%s} with {%s}", fluidStack, recipe);
		}
		else
		{
			logger.info("Adding new churn recipe {%s}", recipe);
		}
		fluidIngredients.add(new FluidKey(fluidStack));
		recipes.put(fluid, recipe);
	}

	@Override
	public void addRecipe(@Nonnull FluidStack inputFluid, @Nonnull FluidStack outputFluid, @Nullable ItemStack outputItem, int churns)
	{
		final Fluid fluid = inputFluid.getFluid();
		if (fluid == null)
		{
			throw new IllegalArgumentException("The provided input fluid is invalid.");
		}

		final IChurnRecipe recipe = new ChurnRecipe(inputFluid, outputFluid, outputItem, churns);
		addRecipe(recipe);
	}

	@Override
	public boolean isFluidIngredient(@Nullable Fluid fluid)
	{
		if (fluid != null)
		{
			return fluidIngredients.contains(new FluidKey(fluid));
		}
		return false;
	}

	@Override
	public boolean isFluidIngredient(@Nullable FluidStack stack)
	{
		if (FluidTest.isValid(stack))
		{
			return fluidIngredients.contains(new FluidKey(stack));
		}
		return false;
	}

	@Override
	@Nullable
	public IChurnRecipe getRecipe(FluidStack stack)
	{
		if (stack == null) return null;
		final Fluid fluid = stack.getFluid();
		if (fluid == null) return null;
		final IChurnRecipe recipe = recipes.get(fluid);
		if (recipe != null)
		{
			if (recipe.isValidForRecipe(stack)) return recipe;
		}
		return null;
	}
}
