package growthcraft.core.integration.nei;

import growthcraft.api.core.definition.IItemStackListProvider;
import growthcraft.api.core.item.recipes.ShapedMultiRecipe;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import codechicken.core.ReflectionManager;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.DefaultOverlayRenderer;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.api.IStackPositioner;
import codechicken.nei.recipe.RecipeInfo;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class RecipeHandlerShapedMulti extends TemplateRecipeHandler
{
	public class CachedShapedRecipe extends CachedRecipe
	{
		public ArrayList<PositionedStack> ingredients;
		public PositionedStack result;

		public CachedShapedRecipe(int width, int height, ArrayList<Object> items,
				ItemStack out)
		{
			result = new PositionedStack(out, 119, 24);
			ingredients = new ArrayList<PositionedStack>();
			setIngredients(width, height, items);
		}

		/**
		 * @param width
		 * @param height
		 * @param items
		 *            an ItemStack[] or ItemStack[][]
		 */
		public void setIngredients(int width, int height, ArrayList<Object> items)
		{
			for (int x = 0; x < width; x++)
			{
				for (int y = 0; y < height; y++)
				{
					if (items.get(y * width + x) == null)
						continue;

					final PositionedStack stack = new PositionedStack(items.get(y * width
							+ x), 25 + x * 18, 6 + y * 18, false);
					stack.setMaxSize(1);
					ingredients.add(stack);
				}
			}
		}

		@Override
		public List<PositionedStack> getIngredients()
		{
			return getCycledIngredients(cycleticks / 20, ingredients);
		}

		public PositionedStack getResult()
		{
			return result;
		}

		public void computeVisuals()
		{
			for (PositionedStack p : ingredients)
				p.generatePermutations();
		}
	}

	@Override
	public void loadTransferRects()
	{
		transferRects.add(new RecipeTransferRect(new Rectangle(84, 23, 24, 18),
				"crafting"));
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass()
	{
		return GuiCrafting.class;
	}

	@Override
	public String getRecipeName()
	{
		return NEIClientUtils.translate("recipe.shaped");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if (outputId.equals("crafting")
				&& getClass() == RecipeHandlerShapedMulti.class)
		{
			for (IRecipe irecipe : (List<IRecipe>) CraftingManager
					.getInstance().getRecipeList())
			{
				CachedShapedRecipe recipe = null;
				if (irecipe instanceof ShapedMultiRecipe)
					recipe = forgeShapedRecipe((ShapedMultiRecipe) irecipe);

				if (recipe == null)
					continue;

				recipe.computeVisuals();
				arecipes.add(recipe);
			}
		} else
		{
			super.loadCraftingRecipes(outputId, results);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadCraftingRecipes(ItemStack result)
	{
		for (IRecipe irecipe : (List<IRecipe>) CraftingManager.getInstance()
				.getRecipeList())
		{
			if (NEIServerUtils.areStacksSameTypeCrafting(
					irecipe.getRecipeOutput(), result))
			{
				CachedShapedRecipe recipe = null;
				if (irecipe instanceof ShapedMultiRecipe)
					recipe = forgeShapedRecipe((ShapedMultiRecipe) irecipe);

				if (recipe == null)
					continue;

				recipe.computeVisuals();
				arecipes.add(recipe);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		for (IRecipe irecipe : (List<IRecipe>) CraftingManager.getInstance()
				.getRecipeList())
		{
			CachedShapedRecipe recipe = null;
			if (irecipe instanceof ShapedMultiRecipe)
				recipe = forgeShapedRecipe((ShapedMultiRecipe) irecipe);

			if (recipe == null
					|| !recipe.contains(recipe.ingredients,
							ingredient.getItem()))
				continue;

			recipe.computeVisuals();
			if (recipe.contains(recipe.ingredients, ingredient))
			{
				recipe.setIngredientPermutation(recipe.ingredients, ingredient);
				arecipes.add(recipe);
			}
		}
	}

	public CachedShapedRecipe forgeShapedRecipe(ShapedMultiRecipe recipe)
	{
		int width;
		int height;
		try
		{
			width = ReflectionManager.getField(ShapedMultiRecipe.class,
					Integer.class, recipe, 4);
			height = ReflectionManager.getField(ShapedMultiRecipe.class,
					Integer.class, recipe, 5);
		} catch (Exception e)
		{
			NEIClientConfig.logger.error("Error loading recipe", e);
			return null;
		}

		final ArrayList<Object> items = new ArrayList<Object>();

		for (IItemStackListProvider item : recipe.getInput())
		{
			final List<ItemStack> stacks = item.getItemStacks();

			if (stacks.isEmpty())
				return null;

			items.add(stacks);
		}

		return new CachedShapedRecipe(width, height, items,
				recipe.getRecipeOutput());
	}

	@Override
	public String getGuiTexture()
	{
		return "textures/gui/container/crafting_table.png";
	}

	@Override
	public String getOverlayIdentifier()
	{
		return "crafting";
	}

	public boolean hasOverlay(GuiContainer gui, Container container, int recipe)
	{
		return super.hasOverlay(gui, container, recipe) || isRecipe2x2(recipe)
				&& RecipeInfo.hasDefaultOverlay(gui, "crafting2x2");
	}

	@Override
	public IRecipeOverlayRenderer getOverlayRenderer(GuiContainer gui,
			int recipe)
	{
		final IRecipeOverlayRenderer renderer = super.getOverlayRenderer(gui, recipe);
		if (renderer != null)
			return renderer;

		final IStackPositioner positioner = RecipeInfo.getStackPositioner(gui,
				"crafting2x2");
		if (positioner == null)
			return null;
		return new DefaultOverlayRenderer(getIngredientStacks(recipe),
				positioner);
	}

	@Override
	public IOverlayHandler getOverlayHandler(GuiContainer gui, int recipe)
	{
		final IOverlayHandler handler = super.getOverlayHandler(gui, recipe);
		if (handler != null)
			return handler;

		return RecipeInfo.getOverlayHandler(gui, "crafting2x2");
	}

	public boolean isRecipe2x2(int recipe)
	{
		for (PositionedStack stack : getIngredientStacks(recipe))
			if (stack.relx > 43 || stack.rely > 24)
				return false;

		return true;
	}
}
