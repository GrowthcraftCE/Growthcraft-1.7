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
package growthcraft.api.cellar.fermenting;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;
import growthcraft.api.core.util.MultiStacksUtil;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class FermentingRegistry implements IFermentingRegistry
{
	private List<IFermentationRecipe> recipes = new ArrayList<IFermentationRecipe>();
	private ILogger logger = NullLogger.INSTANCE;

	@Override
	public void setLogger(@Nonnull ILogger l)
	{
		this.logger = l;
	}

	private void onRecipeAdded(@Nonnull IFermentationRecipe recipe)
	{
		logger.info("Added Fermentation recipe={%s}", recipe);
	}

	@Override
	public void addRecipe(@Nonnull IFermentationRecipe recipe)
	{
		recipes.add(recipe);
		onRecipeAdded(recipe);
	}

	@Override
	public void addRecipe(@Nonnull FluidStack result, @Nonnull Object booze, @Nonnull Object fermenter, int time)
	{
		addRecipe(new FermentationRecipe(MultiStacksUtil.toMultiFluidStacks(booze), MultiStacksUtil.toMultiItemStacks(fermenter), result, time));
	}

	@Override
	public IFermentationRecipe findRecipe(@Nullable FluidStack booze, @Nullable ItemStack fermenter)
	{
		if (booze == null || fermenter == null) return null;
		for (IFermentationRecipe recipe : recipes)
		{
			if (recipe.matchesRecipe(booze, fermenter)) return recipe;
		}
		return null;
	}

	@Override
	public List<IFermentationRecipe> findRecipes(@Nullable FluidStack fluid)
	{
		final List<IFermentationRecipe> result = new ArrayList<IFermentationRecipe>();
		if (fluid != null)
		{
			for (IFermentationRecipe recipe : recipes)
			{
				if (recipe.matchesIngredient(fluid))
					result.add(recipe);
			}
		}
		return result;
	}

	@Override
	public List<IFermentationRecipe> findRecipes(@Nullable ItemStack fermenter)
	{
		final List<IFermentationRecipe> result = new ArrayList<IFermentationRecipe>();
		if (fermenter != null)
		{
			for (IFermentationRecipe recipe : recipes)
			{
				if (recipe.matchesIngredient(fermenter))
					result.add(recipe);
			}
		}
		return result;
	}

	@Override
	public boolean canFerment(@Nullable FluidStack fluid)
	{
		final List<IFermentationRecipe> result = new ArrayList<IFermentationRecipe>();
		if (fluid != null)
		{
			for (IFermentationRecipe recipe : recipes)
			{
				if (recipe.matchesIngredient(fluid))
					return true;
			}
		}
		return false;
	}
}
