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

import javax.annotation.Nonnull;

import growthcraft.api.cellar.culturing.ICultureRecipe;
import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.cellar.client.gui.GuiCultureJar;
import growthcraft.cellar.client.resource.GrcCellarResources;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.integration.nei.TemplateRenderHelper;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.lib.gui.GuiDraw;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

public class RecipeHandlerCultureJar extends TemplateRecipeHandler
{
	public class CachedCultureRecipe extends CachedRecipe
	{
		public ICultureRecipe culturingRecipe;
		protected PositionedStack result;

		public CachedCultureRecipe(@Nonnull ICultureRecipe recipe)
		{
			super();
			this.culturingRecipe = recipe;
			this.result = new PositionedStack(culturingRecipe.getOutputItemStack(), 38, 42);
		}

		@Override
		public PositionedStack getResult()
		{
			return result;
		}

		@Override
		public PositionedStack getIngredient()
		{
			return null;
		}
	}

	@Override
	public String getGuiTexture()
	{
		return GrcCellarResources.INSTANCE.textureGuiCultureJar.toString();
	}

	@Override
	public String getRecipeName()
	{
		return GrcI18n.translate("grc.recipe_handler.culture_jar");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Class<? extends GuiContainer> getGuiClass()
	{
		return GuiCultureJar.class;
	}

	@Override
	public void loadCraftingRecipes(ItemStack ingredient)
	{
		//final List<ICultureRecipe> recipes = CellarRegistry.instance().culturing().findRecipesBy(ingredient);
		//for (ICultureRecipe recipe : recipes)
		//{
		//	arecipes.add(new CachedCultureRecipe(recipe));
		//}
	}

	protected void drawOutputFluidStack(CachedRecipe recipe)
	{
		if (recipe instanceof CachedCultureRecipe)
		{
			final ICultureRecipe culturingRecipe = ((CachedCultureRecipe)recipe).culturingRecipe;
			TemplateRenderHelper.drawFluidStack(54, 6, 50, 52, culturingRecipe.getInputFluidStack(), GrowthCraftCellar.getConfig().cultureJarMaxCap);
		}
	}

	@Override
	public void drawExtras(int recipe)
	{
		final CachedRecipe crecipe = arecipes.get(recipe);
		if (crecipe != null)
		{
			drawOutputFluidStack(crecipe);
		}
		GuiDraw.changeTexture(getGuiTexture());
	}
}
