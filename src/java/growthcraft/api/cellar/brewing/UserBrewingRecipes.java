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
package growthcraft.api.cellar.brewing;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.common.Residue;
import growthcraft.api.core.schema.FluidStackSchema;
import growthcraft.api.core.schema.ICommentable;
import growthcraft.api.core.schema.ItemKeySchema;
import growthcraft.api.core.schema.ResidueSchema;
import growthcraft.api.core.util.JsonConfigDef;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * This allows users to define new brewing recipes.
 */
public class UserBrewingRecipes extends JsonConfigDef
{
	public static class UserBrewingRecipe implements ICommentable
	{
		public String comment = "";
		public ItemKeySchema item;
		public FluidStackSchema input_fluid;
		public FluidStackSchema output_fluid;
		public ResidueSchema residue;
		public int time;

		public UserBrewingRecipe(ItemKeySchema itm, FluidStackSchema inp, FluidStackSchema out, ResidueSchema res, int tm)
		{
			this.item = itm;
			this.input_fluid = inp;
			this.output_fluid = out;
			this.residue = res;
			this.time = tm;
		}

		public UserBrewingRecipe() {}

		@Override
		public String toString()
		{
			return "" + item + " + " + input_fluid + " @" + time + " = " + output_fluid + " + " + residue;
		}

		@Override
		public void setComment(String comm)
		{
			this.comment = comm;
		}

		@Override
		public String getComment()
		{
			return comment;
		}
	}

	private final List<UserBrewingRecipe> defaultEntries = new ArrayList<UserBrewingRecipe>();
	private UserBrewingRecipe[] recipes;

	public void addDefault(UserBrewingRecipe recipe)
	{
		defaultEntries.add(recipe);
	}

	public void addDefault(ItemStack stack, FluidStack inp, FluidStack out, Residue residue, int time)
	{
		addDefault(
			new UserBrewingRecipe(
				new ItemKeySchema(stack),
				new FluidStackSchema(inp),
				new FluidStackSchema(out),
				new ResidueSchema(residue),
				time
			)
		);
	}

	@Override
	protected String getDefault()
	{
		final UserBrewingRecipe[] ary = defaultEntries.toArray(new UserBrewingRecipe[defaultEntries.size()]);
		return gson.toJson(ary, UserBrewingRecipe[].class);
	}

	@Override
	protected void loadFromBuffer(BufferedReader reader)
	{
		this.recipes = gson.fromJson(reader, UserBrewingRecipe[].class);
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
			logger.error("Invalid item for recipe %s", recipe);
			return;
		}

		if (recipe.input_fluid == null || recipe.input_fluid.isInvalid())
		{
			logger.error("Invalid input_fluid %s", recipe);
			return;
		}

		if (recipe.output_fluid == null || recipe.output_fluid.isInvalid())
		{
			logger.error("Invalid output_fluid %s", recipe);
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

		final FluidStack inputFluidStack = recipe.input_fluid.asFluidStack();
		final FluidStack outputFluidStack = recipe.output_fluid.asFluidStack();

		logger.info("Adding brewing recipe %s", recipe);
		for (ItemStack item : recipe.item.getItemStacks())
		{
			CellarRegistry.instance().brewing().addBrewing(inputFluidStack, item, outputFluidStack, recipe.time, residue);
		}
	}

	@Override
	public void postInit()
	{
		if (recipes != null)
		{
			logger.info("Adding %d brewing recipes.", recipes.length);
			for (UserBrewingRecipe recipe : recipes) addBrewingRecipe(recipe);
		}
	}
}
