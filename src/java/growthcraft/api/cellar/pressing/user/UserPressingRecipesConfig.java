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
package growthcraft.api.cellar.pressing.user;

import java.io.BufferedReader;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.common.Residue;
import growthcraft.api.core.schema.FluidStackSchema;
import growthcraft.api.core.schema.ItemKeySchema;
import growthcraft.api.core.schema.ResidueSchema;
import growthcraft.api.core.user.AbstractUserJSONConfig;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * This allows users to define their own pressing recipes using existing items
 * and fluids. Growthcraft WILL NOT create new fluids or items for you, THEY
 * MUST EXIST, or we will not register your recipe.
 */
public class UserPressingRecipesConfig extends AbstractUserJSONConfig
{
	private final UserPressingRecipes defaultRecipes = new UserPressingRecipes();
	private UserPressingRecipes recipes;

	public void addDefault(UserPressingRecipe recipe)
	{
		defaultRecipes.data.add(recipe);
	}

	public void addDefault(ItemKeySchema itm, FluidStackSchema fl, int tm, ResidueSchema res)
	{
		addDefault(new UserPressingRecipe(itm, fl, tm, res));
	}

	public void addDefault(ItemStack stack, FluidStack fluid, int time, Residue res)
	{
		addDefault(
			new ItemKeySchema(stack),
			new FluidStackSchema(fluid),
			time,
			res == null ? null : new ResidueSchema(res)
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
		this.recipes = gson.fromJson(reader, UserPressingRecipes.class);
	}

	protected void addPressingRecipe(UserPressingRecipe recipe)
	{
		if (recipe == null)
		{
			logger.error("NULL RECIPE");
			return;
		}

		if (recipe.item == null || recipe.item.isInvalid())
		{
			logger.error("Item is invalid for recipe {%s}", recipe);
			return;
		}

		if (recipe.fluid == null)
		{
			logger.error("No result fluid for recipe {%s}", recipe);
			return;
		}

		final FluidStack fluidStack = recipe.fluid.asFluidStack();
		if (fluidStack == null)
		{
			logger.error("Invalid fluid for recipe {%s}", recipe);
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

		logger.info("Adding pressing recipe {%s}", recipe);
		for (ItemStack item : recipe.item.getItemStacks())
		{
			CellarRegistry.instance().pressing().addPressingRecipe(item, fluidStack, recipe.time, residue);
		}
	}

	@Override
	public void postInit()
	{
		if (recipes != null)
		{
			if (recipes.data != null)
			{
				logger.info("Adding %d user pressing recipes.", recipes.data.size());
				for (UserPressingRecipe recipe : recipes.data) addPressingRecipe(recipe);
			}
			else
			{
				logger.error("Recipe data is invalid!");
			}
		}
	}
}
