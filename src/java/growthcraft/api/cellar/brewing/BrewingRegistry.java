package growthcraft.api.cellar.brewing;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.cellar.common.Residue;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;
import growthcraft.api.core.util.MultiStacksUtil;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class BrewingRegistry implements IBrewingRegistry
{
	private List<BrewingRecipe> recipes = new ArrayList<BrewingRecipe>();
	private ILogger logger = NullLogger.INSTANCE;

	@Override
	public void setLogger(@Nonnull ILogger l)
	{
		this.logger = l;
	}

	@Override
	public void addRecipe(@Nonnull BrewingRecipe recipe)
	{
		recipes.add(recipe);
		logger.debug("Added Brewing Recipe recipe={%s}", recipe);
	}

	@Override
	public void addRecipe(@Nonnull FluidStack sourceFluid, @Nonnull Object raw, @Nonnull FluidStack resultFluid, int time, @Nullable Residue residue)
	{
		addRecipe(new BrewingRecipe(sourceFluid, MultiStacksUtil.toMultiItemStacks(raw), resultFluid, time, residue));
	}

	@Override
	public BrewingRecipe findRecipe(@Nullable FluidStack fluidstack, @Nullable ItemStack itemstack)
	{
		if (itemstack == null || fluidstack == null) return null;

		for (BrewingRecipe recipe : recipes)
		{
			if (recipe.matchesRecipe(fluidstack, itemstack)) return recipe;
		}
		return null;
	}

	@Override
	public List<BrewingRecipe> findRecipes(@Nullable FluidStack fluid)
	{
		final List<BrewingRecipe> result = new ArrayList<BrewingRecipe>();
		if (fluid != null)
		{
			for (BrewingRecipe recipe : recipes)
			{
				if (recipe.matchesIngredient(fluid))
					result.add(recipe);
			}
		}
		return result;
	}

	@Override
	public List<BrewingRecipe> findRecipes(@Nullable ItemStack fermenter)
	{
		final List<BrewingRecipe> result = new ArrayList<BrewingRecipe>();
		if (fermenter != null)
		{
			for (BrewingRecipe recipe : recipes)
			{
				if (recipe.matchesIngredient(fermenter))
					result.add(recipe);
			}
		}
		return result;
	}

	@Override
	public boolean isBrewingRecipe(@Nullable FluidStack fluidstack, @Nullable ItemStack itemstack)
	{
		return findRecipe(fluidstack, itemstack) != null;
	}

	@Override
	public boolean isItemBrewingIngredient(@Nullable ItemStack itemstack)
	{
		if (itemstack == null) return false;

		for (BrewingRecipe recipe : recipes)
		{
			if (recipe.isItemIngredient(itemstack)) return true;
		}
		return false;
	}
}
