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
package growthcraft.api.cellar.fermenting.user;

import java.io.BufferedReader;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.core.definition.IMultiFluidStacks;
import growthcraft.api.core.definition.IMultiItemStacks;
import growthcraft.api.core.schema.FluidStackSchema;
import growthcraft.api.core.schema.ItemKeySchema;
import growthcraft.api.core.schema.MultiFluidStackSchema;
import growthcraft.api.core.user.AbstractUserJSONConfig;
import growthcraft.api.core.util.MultiStacksUtil;

import net.minecraftforge.fluids.FluidStack;

public class UserFermentingRecipesConfig extends AbstractUserJSONConfig
{
	protected UserFermentingRecipes defaultRecipes = new UserFermentingRecipes();
	protected UserFermentingRecipes recipes;

	public void addDefaultSchemas(ItemKeySchema item, MultiFluidStackSchema inputFluid, FluidStackSchema outputFluid, int time)
	{
		addDefault(new UserFermentingRecipe(item, inputFluid, outputFluid, time));
	}

	public void addDefault(UserFermentingRecipe recipe)
	{
		defaultRecipes.data.add(recipe);
	}

	public void addDefault(Object stack, Object inputFluid, FluidStack outputFluid, int time)
	{
		for (ItemKeySchema itemKey : ItemKeySchema.createMulti(stack))
		{
			addDefaultSchemas(
				itemKey,
				new MultiFluidStackSchema(MultiStacksUtil.toMultiFluidStacks(inputFluid)),
				new FluidStackSchema(outputFluid),
				time
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
		this.recipes = gson.fromJson(reader, UserFermentingRecipes.class);
	}

	private void addRecipe(UserFermentingRecipe recipe)
	{
		if (recipe == null)
		{
			logger.error("Recipe is invalid!");
			return;
		}

		if (recipe.item == null || !recipe.item.isValid())
		{
			logger.error("Recipe item is invalid! {%s}", recipe);
			return;
		}

		if (recipe.input_fluid == null || !recipe.input_fluid.isValid())
		{
			logger.error("Recipe input_fluid is invalid! {%s}", recipe);
			return;
		}

		if (recipe.output_fluid == null || !recipe.output_fluid.isValid())
		{
			logger.error("Recipe output_fluid is invalid! {%s}", recipe);
			return;
		}

		logger.info("Adding Fermenting Recipe {%s}", recipe);
		for (IMultiItemStacks item : recipe.item.getMultiItemStacks())
		{
			for (IMultiFluidStacks inputFluid : recipe.input_fluid.getMultiFluidStacks())
			{
				CellarRegistry.instance().fermenting().addRecipe(
					recipe.output_fluid.asFluidStack(),
					inputFluid,
					item,
					recipe.time
				);
			}
		}
	}

	@Override
	public void postInit()
	{
		if (recipes != null)
		{
			if (recipes.data != null)
			{
				logger.info("Registering %d fermenting recipes.", recipes.data.size());
				for (UserFermentingRecipe recipe : recipes.data) addRecipe(recipe);
			}
			else
			{
				logger.error("Fermenting Recipes data is invalid!");
			}
		}
	}
}
