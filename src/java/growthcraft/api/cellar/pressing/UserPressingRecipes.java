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
package growthcraft.api.cellar.pressing;

import java.io.BufferedReader;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.common.Residue;
import growthcraft.api.core.schema.FluidStackSchema;
import growthcraft.api.core.schema.ItemKeySchema;
import growthcraft.api.core.schema.ResidueSchema;
import growthcraft.api.core.util.JsonConfigDef;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * This allows users to define their own pressing recipes using existing items
 * and fluids. Growthcraft WILL NOT create new fluids or items for you, THEY
 * MUST EXIST, or we will not register your recipe.
 */
public class UserPressingRecipes extends JsonConfigDef
{
	public static class UserPressingRecipeEntry
	{
		public ItemKeySchema item;
		public FluidStackSchema fluid;
		public int time;
		public ResidueSchema residue;

		public String toString()
		{
			return "" + item + " + " + time + " = "  + fluid + " + " + residue;
		}
	}

	private static final UserPressingRecipeEntry[] DEFAULT_ENTRIES = {};
	private UserPressingRecipeEntry[] recipes;

	@Override
	protected String getDefault()
	{
		return gson.toJson(DEFAULT_ENTRIES, UserPressingRecipeEntry[].class);
	}

	@Override
	protected void loadFromBuffer(BufferedReader reader)
	{
		this.recipes = gson.fromJson(reader, UserPressingRecipeEntry[].class);
	}

	protected void addPressingRecipe(UserPressingRecipeEntry recipe)
	{
		if (recipe == null)
		{
			logger.error("NULL RECIPE");
			return;
		}

		if (recipe.item == null)
		{
			logger.error("No pressing item for recipe %s", recipe);
			return;
		}

		final ItemStack item = recipe.item.asStack();
		if (item == null)
		{
			logger.error("Invalid item for recipe %s", recipe);
			return;
		}

		if (recipe.fluid == null)
		{
			logger.error("No result fluid for recipe %s", recipe);
			return;
		}

		final FluidStack fluidStack = recipe.fluid.asFluidStack();
		if (fluidStack == null)
		{
			logger.error("Invalid fluid for recipe %s", recipe);
			return;
		}

		Residue residue = null;
		if (recipe.residue == null)
		{
			logger.warn("No residue specified for %s", recipe);
			residue = Residue.newDefault(1.0f);
		}
		else
		{
			residue = recipe.residue.asResidue();
		}

		if (residue == null)
		{
			logger.error("Not a valid residue found for %s", recipe);
			return;
		}

		logger.info("Adding pressing recipe %s", recipe);
		CellarRegistry.instance().pressing().addPressing(
			item.getItem(), item.getItemDamage(),
			fluidStack.getFluid(), recipe.time, fluidStack.amount,
			residue
		);
	}

	@Override
	public void postInit()
	{
		if (recipes != null)
		{
			logger.info("Adding %d pressing recipes.", recipes.length);
			for (UserPressingRecipeEntry recipe : recipes) addPressingRecipe(recipe);
		}
	}
}
