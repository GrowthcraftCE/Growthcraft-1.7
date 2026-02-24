/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015, 2016 IceDragon200
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
package growthcraft.api.cellar.brewing.user;

import java.io.BufferedReader;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.common.Residue;
import growthcraft.api.core.definition.IMultiItemStacks;
import growthcraft.api.core.schema.FluidStackSchema;
import growthcraft.api.core.schema.ItemKeySchema;
import growthcraft.api.core.schema.ResidueSchema;
import growthcraft.api.core.user.AbstractUserJSONConfig;

import net.minecraftforge.fluids.FluidStack;

/**
 * This allows users to define new brewing recipes.
 */
public class UserBrewingRecipesConfig extends AbstractUserJSONConfig
{
	private final UserBrewingRecipes defaultRecipes = new UserBrewingRecipes();
	private UserBrewingRecipes recipes;

	public void addDefault(UserBrewingRecipe recipe)
	{
		defaultRecipes.data.add(recipe);
		logger.debug("Added new default brewing recipe={%s}", recipe);
	}

	public void addDefault(Object stack, FluidStack inp, FluidStack out, Residue residue, int time)
	{
		for (ItemKeySchema itemKey : ItemKeySchema.createMulti(stack))
		{
			addDefault(
				new UserBrewingRecipe(
					itemKey,
					new FluidStackSchema(inp),
					new FluidStackSchema(out),
					residue == null ? null : new ResidueSchema(residue),
					time
				)
			);
		}
	}

	@Override
	protected String getDefault()
	{
		return gson.toJson(defaultRecipes);
	}

	@Override
	protected void loadFromBuffer(BufferedReader reader) throws IllegalStateException
	{
		this.recipes = gson.fromJson(reader, UserBrewingRecipes.class);
	}

	private void addBrewingRecipe(UserBrewingRecipe recipe)
	{
		if (recipe == null)
		{
			logger.error("Invalid recipe");
			return;
		}

		if (recipe.item == null || recipe.item.isInvalid())
		{
			logger.error("Invalid item for recipe {%s}", recipe);
			return;
		}

		if (recipe.input_fluid == null || recipe.input_fluid.isInvalid())
		{
			logger.error("Invalid input_fluid {%s}", recipe);
			return;
		}

		if (recipe.output_fluid == null || recipe.output_fluid.isInvalid())
		{
			logger.error("Invalid output_fluid {%s}", recipe);
			return;
		}

		Residue residue = null;
		if (recipe.residue != null)
		{
			residue = recipe.residue.asResidue();
			if (residue == null)
			{
				logger.error("Not a valid residue found for {%s}", recipe);
				return;
			}
		}


		final FluidStack inputFluidStack = recipe.input_fluid.asFluidStack();
		final FluidStack outputFluidStack = recipe.output_fluid.asFluidStack();

		logger.debug("Adding user brewing recipe {%s}", recipe);
		for (IMultiItemStacks item : recipe.item.getMultiItemStacks())
		{
			CellarRegistry.instance().brewing().addRecipe(inputFluidStack, item, outputFluidStack, recipe.time, residue);
		}
	}

	@Override
	public void postInit()
	{
		if (recipes != null)
		{
			if (recipes.data != null)
			{
				logger.debug("Adding %d user brewing recipes.", recipes.data.size());
				for (UserBrewingRecipe recipe : recipes.data) addBrewingRecipe(recipe);
			}
			else
			{
				logger.error("Recipes data is invalid!");
			}
		}
	}
}
