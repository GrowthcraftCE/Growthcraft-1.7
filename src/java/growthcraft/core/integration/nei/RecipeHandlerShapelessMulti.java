package growthcraft.core.integration.nei;

import growthcraft.api.core.definition.IMultiFluidStacks;
import growthcraft.api.core.definition.IMultiItemStacks;
import growthcraft.api.core.fluids.FluidUtils;
import growthcraft.api.core.item.recipes.ShapelessMultiRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidStack;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapedRecipeHandler;

public class RecipeHandlerShapelessMulti extends ShapedRecipeHandler
{
	public int[][] stackorder = new int[][] { { 0, 0 }, { 1, 0 }, { 0, 1 },
			{ 1, 1 }, { 0, 2 }, { 1, 2 }, { 2, 0 }, { 2, 1 }, { 2, 2 } };

	public class CachedShapelessMultiRecipe extends CachedRecipe
	{
		public ArrayList<PositionedStack> ingredients;
		public PositionedStack result;
		
		public CachedShapelessMultiRecipe()
		{
			ingredients = new ArrayList<PositionedStack>();
		}

		public CachedShapelessMultiRecipe(ItemStack output)
		{
			this();
			setResult(output);
		}

		public CachedShapelessMultiRecipe(Object[] input, ItemStack output)
		{
			this(Arrays.asList(input), output);
		}

		public CachedShapelessMultiRecipe(List<?> input, ItemStack output)
		{
			this(output);
			setIngredients(input);
		}

		public void setIngredients(List<?> items)
		{
			ingredients.clear();
			for (int ingred = 0; ingred < items.size(); ingred++)
			{
				final PositionedStack stack = new PositionedStack(items.get(ingred),
						25 + stackorder[ingred][0] * 18,
						6 + stackorder[ingred][1] * 18);
				stack.setMaxSize(1);
				ingredients.add(stack);
			}
		}

		public void setResult(ItemStack output)
		{
			result = new PositionedStack(output, 119, 24);
		}

		@Override
		public List<PositionedStack> getIngredients()
		{
			return getCycledIngredients(cycleticks / 20, ingredients);
		}

		@Override
		public PositionedStack getResult()
		{
			return result;
		}
	}

	public String getRecipeName()
	{
		return NEIClientUtils.translate("recipe.shapeless");
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if (outputId.equals("crafting")
				&& getClass() == RecipeHandlerShapelessMulti.class)
		{
			@SuppressWarnings("unchecked")
			final List<IRecipe> allrecipes = CraftingManager.getInstance()
					.getRecipeList();
			for (IRecipe irecipe : allrecipes)
			{
				List<CachedShapelessMultiRecipe> recipes = null;
				if (irecipe instanceof ShapelessMultiRecipe)
					recipes = shapelessMultiRecipe((ShapelessMultiRecipe) irecipe);

				if (recipes == null)
					continue;

				for (CachedShapelessMultiRecipe cRecipe : recipes)
				{
					arecipes.add(cRecipe);
				}
			}
		} else
		{
			super.loadCraftingRecipes(outputId, results);
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result)
	{
		@SuppressWarnings("unchecked")
		final List<IRecipe> allrecipes = CraftingManager.getInstance()
				.getRecipeList();
		for (IRecipe irecipe : allrecipes)
		{
			if (NEIServerUtils.areStacksSameTypeCrafting(
					irecipe.getRecipeOutput(), result))
			{
				List<CachedShapelessMultiRecipe> recipes = null;
				if (irecipe instanceof ShapelessMultiRecipe)
					recipes = shapelessMultiRecipe((ShapelessMultiRecipe) irecipe);

				if (recipes == null)
					continue;

				for (CachedShapelessMultiRecipe cRecipe : recipes)
				{
					arecipes.add(cRecipe);
				}
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		@SuppressWarnings("unchecked")
		final List<IRecipe> allrecipes = CraftingManager.getInstance()
				.getRecipeList();
		for (IRecipe irecipe : allrecipes)
		{
			List<CachedShapelessMultiRecipe> recipes = null;
			if (irecipe instanceof ShapelessMultiRecipe)
				recipes = shapelessMultiRecipe((ShapelessMultiRecipe) irecipe);

			if (recipes == null)
				continue;

			for (CachedShapelessMultiRecipe cRecipe : recipes)
			{
				if (cRecipe.contains(cRecipe.ingredients, ingredient))
				{
					cRecipe.setIngredientPermutation(cRecipe.ingredients,
							ingredient);
					arecipes.add(cRecipe);
				}
			}
		}
	}

	public List<CachedShapelessMultiRecipe> shapelessMultiRecipe(
			ShapelessMultiRecipe multiRecipe)
	{
		final ArrayList<Object> items = new ArrayList<Object>();
		final List<IMultiFluidStacks> fluidStacks = multiRecipe.getFluids();
		final List<CachedShapelessMultiRecipe> cachedRecipes = new ArrayList<CachedShapelessMultiRecipe>();

		for (IMultiItemStacks item : multiRecipe.getInput())
		{
			if (item.isEmpty())
				return null;
			
			items.add(item.getItemStacks());
		}

		ArrayList<ArrayList<Object>> recipes = new ArrayList<ArrayList<Object>>();
		recipes.add(items);

		for (IMultiFluidStacks multiStack : fluidStacks)
		{
			final ArrayList<ArrayList<Object>> recipesFluidStacks = new ArrayList<ArrayList<Object>>();
			final Map<Integer, ArrayList<Object>> itemsFluidMap = new HashMap<Integer, ArrayList<Object>>();

			for (FluidStack fluidStack : multiStack.getFluidStacks())
			{
				final List<FluidContainerData> fluidData = FluidUtils.getFluidData()
						.get(fluidStack.getFluid());

				for (FluidContainerData data : fluidData)
				{
					final FluidStack fluid = data.fluid;

					final int amount = (int) Math.max(1,
							Math.ceil(multiStack.getAmount() / fluid.amount));

					if (!itemsFluidMap.containsKey(amount))
						itemsFluidMap.put(amount, new ArrayList<Object>());

					itemsFluidMap.get(amount).add(data.filledContainer);
				}
			}

			for (Map.Entry<Integer, ArrayList<Object>> entry : itemsFluidMap
					.entrySet())
			{
				final ArrayList<Object> itemFluid = new ArrayList<Object>();

				for (int i = 0; i < entry.getKey(); i++)
				{
					itemFluid.add(entry.getValue());
				}

				for (ArrayList<Object> recipe : recipes)
				{
					final ArrayList<Object> recipeCopy = new ArrayList<Object>(recipe);
					recipeCopy.addAll(itemFluid);
					recipesFluidStacks.add(recipeCopy);
				}
			}

			// There's no way to craft this recipe, abort
			if (recipesFluidStacks.size() == 0)
				return cachedRecipes;

			recipes = recipesFluidStacks;
		}

		for (ArrayList<Object> recipe : recipes)
		{
			// This recipe can't actually be crafted, exclude it
			if (recipe.size() > 9)
				continue;

			cachedRecipes.add(new CachedShapelessMultiRecipe(recipe,
					multiRecipe.getRecipeOutput()));
		}

		return cachedRecipes;
	}

	@Override
	public boolean isRecipe2x2(int recipe)
	{
		return getIngredientStacks(recipe).size() <= 4;
	}

}
