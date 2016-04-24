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

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.fermenting.IFermentationRecipe;
import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.cellar.client.gui.GuiFermentBarrel;
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
import net.minecraftforge.fluids.FluidStack;

public class RecipeHandlerFermentBarrel extends TemplateRecipeHandler
{
	public class CachedFermentationRecipe extends CachedRecipe
	{
		public IFermentationRecipe fermentationRecipe;
		public FluidStack outputFluidStack;
		public FluidStack inputFluidStack;

		protected PositionedStack ingredient;
		public CachedFermentationRecipe(@Nonnull IFermentationRecipe recipe)
		{
			super();
			this.fermentationRecipe = recipe;
			this.ingredient = new PositionedStack(fermentationRecipe.getFermentingItemStack().getItemStacks(), 38, 42);

			this.inputFluidStack = fermentationRecipe.getInputFluidStack().copy();
			inputFluidStack.amount = GrowthCraftCellar.getConfig().fermentBarrelMaxCap;

			this.outputFluidStack = fermentationRecipe.getInputFluidStack().copy();
			outputFluidStack.amount = GrowthCraftCellar.getConfig().fermentBarrelMaxCap;
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
	}

	@Override
	public String getGuiTexture()
	{
		return GrcCellarResources.INSTANCE.textureGuiFermentBarrel.toString();
	}

	@Override
	public String getRecipeName()
	{
		return GrcI18n.translate("grc.recipe_handler.ferment_barrel");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Class<? extends GuiContainer> getGuiClass()
	{
		return GuiFermentBarrel.class;
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		final List<IFermentationRecipe> recipes = CellarRegistry.instance().fermenting().findRecipes(ingredient);
		for (IFermentationRecipe recipe : recipes)
		{
			arecipes.add(new CachedFermentationRecipe(recipe));
		}
	}

	protected void drawOutputFluidStack(CachedRecipe recipe)
	{
		if (recipe instanceof CachedFermentationRecipe)
		{
			TemplateRenderHelper.drawFluidStack(58, 6, 50, 52, ((CachedFermentationRecipe)recipe).inputFluidStack, GrowthCraftCellar.getConfig().fermentBarrelMaxCap);
			//TemplateRenderHelper.drawFluidStack(58, 6, 50, 52, ((CachedFermentationRecipe)recipe).outputFluidStack, GrowthCraftCellar.getConfig().fermentBarrelMaxCap);
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
		drawProgressBar(34, 10, 188, 0, 9, 29, 240, TemplateRenderHelper.PROGRESS_UP);
		drawProgressBar(44, 9, 176, 0, 12, 29, 20, TemplateRenderHelper.PROGRESS_UP);
	}
}
