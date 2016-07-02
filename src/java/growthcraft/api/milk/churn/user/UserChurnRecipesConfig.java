/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 IceDragon200
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
package growthcraft.api.milk.churn.user;

import java.io.BufferedReader;

import growthcraft.api.core.schema.FluidStackSchema;
import growthcraft.api.core.schema.ItemKeySchema;
import growthcraft.api.core.user.AbstractUserJSONConfig;
import growthcraft.api.milk.churn.IChurnRecipe;
import growthcraft.api.milk.MilkRegistry;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * This allows users to define new brewing recipes.
 */
public class UserChurnRecipesConfig extends AbstractUserJSONConfig
{
	private final UserChurnRecipes defaultRecipes = new UserChurnRecipes();
	private UserChurnRecipes recipes;

	public void addDefault(UserChurnRecipe recipe)
	{
		defaultRecipes.data.add(recipe);
	}

	public void addDefault(FluidStack inp, FluidStack out, ItemStack stack, int churns)
	{
		addDefault(
			new UserChurnRecipe(
				new FluidStackSchema(inp),
				new FluidStackSchema(out),
				new ItemKeySchema(stack),
				churns
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
		this.recipes = gson.fromJson(reader, UserChurnRecipes.class);
	}

	private void addChurnRecipe(UserChurnRecipe recipe)
	{
		if (recipe == null)
		{
			logger.error("Invalid recipe");
			return;
		}

		if (recipe.input_fluid == null || recipe.input_fluid.isInvalid())
		{
			logger.error("Invalid input_fluid {%s}", recipe);
			return;
		}

		for (IChurnRecipe churnRecipe : recipe.toChurnRecipes())
		{
			logger.debug("Adding user churn recipe {%s}", churnRecipe);
			MilkRegistry.instance().churn().addRecipe(churnRecipe);
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
				for (UserChurnRecipe recipe : recipes.data) addChurnRecipe(recipe);
			}
			else
			{
				logger.error("Recipes data is invalid!");
			}
		}
	}
}
