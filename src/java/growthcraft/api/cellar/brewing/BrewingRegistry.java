package growthcraft.api.cellar.brewing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.common.Residue;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;
import growthcraft.api.core.item.ItemKey;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class BrewingRegistry implements IBrewingRegistry
{
	private List<BrewingRecipe> recipes = new ArrayList<BrewingRecipe>();
	private Set<ItemKey> itemIngredients = new HashSet<ItemKey>();
	private ILogger logger = NullLogger.INSTANCE;

	private Fluid boozeToKey(Fluid f)
	{
		return CellarRegistry.instance().booze().maybeAlternateBooze(f);
	}

	@Override
	public void setLogger(@Nonnull ILogger l)
	{
		this.logger = l;
	}

	private void addRecipe(@Nonnull BrewingRecipe recipe)
	{
		final ItemStack is = recipe.getInputItemStack();
		recipes.add(recipe);
		itemIngredients.add(new ItemKey(is));
	}

	@Override
	public void addBrewing(@Nonnull FluidStack sourceFluid, @Nonnull ItemStack raw, @Nonnull FluidStack resultFluid, int time, @Nullable Residue residue)
	{
		addRecipe(new BrewingRecipe(sourceFluid, raw, resultFluid, time, residue));
	}

	@Override
	public BrewingRecipe getBrewingRecipe(@Nullable FluidStack fluidstack, @Nullable ItemStack itemstack)
	{
		if (itemstack == null || fluidstack == null) return null;

		for (BrewingRecipe recipe : recipes)
		{
			if (recipe.matchesRecipe(fluidstack, itemstack)) return recipe;
		}
		return null;
	}

	@Override
	public boolean isBrewingRecipe(@Nullable FluidStack fluidstack, @Nullable ItemStack itemstack)
	{
		return getBrewingRecipe(fluidstack, itemstack) != null;
	}

	@Override
	public boolean isItemBrewingIngredient(@Nullable ItemStack itemstack)
	{
		if (itemstack == null) return false;

		return itemIngredients.contains(new ItemKey(itemstack)) ||
			itemIngredients.contains(new ItemKey(itemstack.getItem(), ItemKey.WILDCARD_VALUE));
	}
}
