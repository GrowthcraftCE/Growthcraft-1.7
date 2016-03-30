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
package growthcraft.api.cellar.culturing.user;

import java.io.BufferedReader;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.core.schema.FluidStackSchema;
import growthcraft.api.core.schema.ItemKeySchema;
import growthcraft.api.core.user.AbstractUserJSONConfig;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * This allows users to define new culturing recipes.
 */
public class UserCultureRecipesConfig extends AbstractUserJSONConfig
{
	private final UserCultureRecipes defaultRecipes = new UserCultureRecipes();
	private UserCultureRecipes recipes;

	public void addDefault(UserCultureRecipe recipe)
	{
		defaultRecipes.data.add(recipe);
	}

	public void addDefault(FluidStack inp, ItemStack stack, float heat, int time)
	{
		addDefault(
			new UserCultureRecipe(
				new FluidStackSchema(inp),
				new ItemKeySchema(stack),
				heat,
				time
			)
		);
	}

	@Override
	protected String getDefault()
	{
		return gson.toJson(defaultRecipes);
	}

	@Override
	protected void loadFromBuffer(BufferedReader reader) throws IllegalStateException
	{
		this.recipes = gson.fromJson(reader, UserCultureRecipes.class);
	}

	private void addRecipe(UserCultureRecipe recipe)
	{
		if (recipe == null)
		{
			logger.error("Invalid recipe");
			return;
		}

		if (recipe.output_item == null || recipe.output_item.isInvalid())
		{
			logger.error("Invalid output_item for recipe {%s}", recipe);
			return;
		}

		if (recipe.input_fluid == null || recipe.input_fluid.isInvalid())
		{
			logger.error("Invalid input_fluid {%s}", recipe);
			return;
		}

		final FluidStack inputFluidStack = recipe.input_fluid.asFluidStack();

		logger.info("Adding user culturing recipe {%s}", recipe);
		CellarRegistry.instance().culturing().addRecipe(inputFluidStack, recipe.output_item.asStack(), recipe.required_heat, recipe.time);
	}

	@Override
	public void postInit()
	{
		if (recipes != null)
		{
			if (recipes.data != null)
			{
				logger.info("Adding %d user culturing recipes.", recipes.data.size());
				for (UserCultureRecipe recipe : recipes.data) addRecipe(recipe);
			}
			else
			{
				logger.error("Recipes data is invalid!");
			}
		}
	}
}
