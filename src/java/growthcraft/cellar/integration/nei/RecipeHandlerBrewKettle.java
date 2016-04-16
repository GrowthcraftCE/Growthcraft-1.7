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
package growthcraft.cellar.integration.nei;

import java.util.List;
import javax.annotation.Nonnull;

import growthcraft.api.cellar.brewing.BrewingRecipe;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.cellar.client.gui.GuiBrewKettle;
import growthcraft.cellar.client.resource.GrcCellarResources;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.integration.nei.TemplateRenderHelper;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

public class RecipeHandlerBrewKettle extends TemplateRecipeHandler
{
	public class CachedBrewingRecipe extends CachedRecipe
	{
		public BrewingRecipe brewingRecipe;
		protected PositionedStack ingredient;
		protected PositionedStack otherStack;

		public CachedBrewingRecipe(@Nonnull BrewingRecipe recipe)
		{
			super();
			this.brewingRecipe = recipe;
			this.ingredient = new PositionedStack(brewingRecipe.getInputItemStack().getItemStacks(), 75, 24);
			if (brewingRecipe.hasResidue())
				this.otherStack = new PositionedStack(brewingRecipe.getResidue().residueItem, 136, 6);
		}

		@Override
		public PositionedStack getResult()
		{
			return null;
		}

		@Override
		public PositionedStack getIngredient()
		{
			return ingredient;
		}

		@Override
		public PositionedStack getOtherStack()
		{
			return otherStack;
		}
	}

	@Override
	public String getGuiTexture()
	{
		return GrcCellarResources.INSTANCE.textureGuiBrewKettle.toString();
	}

	@Override
	public String getRecipeName()
	{
		return GrcI18n.translate("grc.recipe_handler.brew_kettle");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Class<? extends GuiContainer> getGuiClass()
	{
		return GuiBrewKettle.class;
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		final List<BrewingRecipe> recipes = CellarRegistry.instance().brewing().findRecipes(ingredient);
		for (BrewingRecipe recipe : recipes)
		{
			arecipes.add(new CachedBrewingRecipe(recipe));
		}
	}

	@SideOnly(Side.CLIENT)
	protected void drawOutputFluidStacks(CachedRecipe recipe)
	{
		if (recipe instanceof CachedBrewingRecipe)
		{
			final BrewingRecipe brewingRecipe = ((CachedBrewingRecipe)recipe).brewingRecipe;
			TemplateRenderHelper.drawFluidStack(41, 6, 16, 52, brewingRecipe.getInputFluidStack(), GrowthCraftCellar.getConfig().brewKettleMaxCap);
			TemplateRenderHelper.drawFluidStack(109, 6, 16, 52, brewingRecipe.getFluidStack(), GrowthCraftCellar.getConfig().brewKettleMaxCap);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void drawExtras(int recipe)
	{
		final CachedRecipe crecipe = arecipes.get(recipe);
		if (crecipe != null)
		{
			drawOutputFluidStacks(crecipe);
		}
		drawProgressBar(93, 19, 176, 0, 9, 28, 40, TemplateRenderHelper.PROGRESS_DOWN);
	}
}
