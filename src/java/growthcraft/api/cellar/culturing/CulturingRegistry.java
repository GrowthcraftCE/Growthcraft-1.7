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
package growthcraft.api.cellar.culturing;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CulturingRegistry implements ICulturingRegistry
{
	private List<ICultureRecipe> recipes = new ArrayList<ICultureRecipe>();
	private ILogger logger = NullLogger.INSTANCE;

	@Override
	public void setLogger(@Nonnull ILogger l)
	{
		this.logger = l;
	}

	@Override
	public void addRecipe(@Nonnull ICultureRecipe recipe)
	{
		recipes.add(recipe);
		logger.debug("Adding new Culturing Recipe, {%s}.", recipe);
	}

	@Override
	public void addRecipe(@Nonnull FluidStack fluidStack, @Nonnull ItemStack itemStack, float requiredHeat, int time)
	{
		addRecipe(new CultureRecipe(fluidStack, itemStack, requiredHeat, time));
	}

	@Override
	public ICultureRecipe findRecipe(@Nullable FluidStack fluid, float heat)
	{
		for (ICultureRecipe recipe : recipes)
		{
			if (recipe.matchesRecipe(fluid, heat)) return recipe;
		}
		return null;
	}
}
