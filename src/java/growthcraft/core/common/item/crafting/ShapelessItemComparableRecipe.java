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
package growthcraft.milk.common.item.crafting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;

import growthcraft.api.core.item.IItemStackComparator;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ShapelessItemComparableRecipe implements IRecipe
{
	public final List<ItemStack> recipeItems;
	private final ItemStack recipeOutput;
	private final IItemStackComparator comparator;

	public ShapelessItemComparableRecipe(@Nonnull IItemStackComparator pComparator, ItemStack result, List<ItemStack> list)
	{
		this.comparator = pComparator;
		this.recipeOutput = result;
		this.recipeItems = list;
	}

	public ItemStack getRecipeOutput()
	{
		return this.recipeOutput;
	}

	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	public boolean matches(InventoryCrafting crafting, World world)
	{
		final List<ItemStack> recipeList = new ArrayList<ItemStack>(this.recipeItems);
		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 3; ++j)
			{
				final ItemStack actual = crafting.getStackInRowAndColumn(j, i);
				if (actual != null)
				{
					boolean flag = false;
					final Iterator<ItemStack> iterator = recipeList.iterator();
					while (iterator.hasNext())
					{
						final ItemStack expected = iterator.next();
						if (comparator.equals(expected, actual))
						{
							flag = true;
							recipeList.remove(expected);
							break;
						}
					}
					if (!flag)
					{
						return false;
					}
				}
			}
		}
		return recipeList.isEmpty();
	}

	/**
	 * Returns an Item that is the result of this recipe
	 */
	public ItemStack getCraftingResult(InventoryCrafting crafting)
	{
		return this.recipeOutput.copy();
	}

	/**
	 * Returns the size of the recipe area
	 */
	public int getRecipeSize()
	{
		return this.recipeItems.size();
	}
}
