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

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.pressing.PressingRecipe;
import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.cellar.client.gui.GuiFruitPress;
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
public class RecipeHandlerFruitPress extends TemplateRecipeHandler
{
	public class CachedPressingRecipe extends CachedRecipe
	{
		public PressingRecipe pressingRecipe;
		protected PositionedStack ingredient;
		protected PositionedStack otherStack;

		public CachedPressingRecipe(@Nonnull PressingRecipe recipe)
		{
			super();
			this.pressingRecipe = recipe;
			this.ingredient = new PositionedStack(pressingRecipe.getInput().getItemStacks(), 40, 24);
			if (recipe.hasResidue())
				this.otherStack = new PositionedStack(pressingRecipe.getResidue().residueItem, 111, 6);
		}

		@Override
		public PositionedStack getIngredient()
		{
			return ingredient;
		}

		@Override
		public PositionedStack getResult()
		{
			return null;
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
		return GrcCellarResources.INSTANCE.textureGuiFruitPress.toString();
	}

	@Override
	public String getRecipeName()
	{
		return GrcI18n.translate("grc.recipe_handler.fruit_press");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Class<? extends GuiContainer> getGuiClass()
	{
		return GuiFruitPress.class;
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		final PressingRecipe recipe = CellarRegistry.instance().pressing().getPressingRecipe(ingredient);
		if (recipe != null)
		{
			arecipes.add(new CachedPressingRecipe(recipe));
		}
	}

	public void drawOutputFluidStack(CachedRecipe recipe)
	{
		if (recipe instanceof CachedPressingRecipe)
		{
			final PressingRecipe pressingRecipe = ((CachedPressingRecipe)recipe).pressingRecipe;
			TemplateRenderHelper.drawFluidStack(84, 5, 16, 52, pressingRecipe.getFluidStack(), GrowthCraftCellar.getConfig().fruitPressMaxCap);
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
		drawProgressBar(58, 24, 176, 0, 25, 16, 40, TemplateRenderHelper.PROGRESS_RIGHT);
	}
}
