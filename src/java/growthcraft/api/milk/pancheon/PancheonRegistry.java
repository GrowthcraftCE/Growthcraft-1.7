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
package growthcraft.api.milk.pancheon;

import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class PancheonRegistry implements IPancheonRegistry
{
	protected ILogger logger = NullLogger.INSTANCE;
	private Map<Fluid, PancheonRecipe> recipes = new HashMap<Fluid, PancheonRecipe>();

	@Override
	public void setLogger(@Nonnull ILogger l)
	{
		this.logger = l;
	}

	@Override
	public void addRecipe(@Nonnull FluidStack inputStack, @Nonnull FluidStack topOutput, @Nullable FluidStack bottomOutput, int time)
	{
		final Fluid fluid = inputStack.getFluid();
		if (fluid == null)
		{
			throw new IllegalArgumentException("The provided input fluid is invalid.");
		}

		final PancheonRecipe recipe = new PancheonRecipe(inputStack, topOutput, bottomOutput, time);
		if (recipes.containsKey(fluid))
		{
			logger.warn("Overwriting existing pancheon recipe for {%s} with {%s}", inputStack, recipe);
		}
		else
		{
			logger.info("Adding new pancheon recipe {%s}", recipe);
		}

		recipes.put(fluid, recipe);
	}

	@Override
	@Nullable
	public PancheonRecipe getRecipe(FluidStack stack)
	{
		if (stack == null) return null;
		final Fluid fluid = stack.getFluid();
		if (fluid == null) return null;
		final PancheonRecipe recipe = recipes.get(fluid);
		if (recipe != null)
		{
			if (recipe.isValidForRecipe(stack)) return recipe;
		}
		return null;
	}
}
