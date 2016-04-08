package growthcraft.api.cellar.fermenting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.core.fluids.FluidTest;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;
import growthcraft.api.core.util.MultiStacksUtil;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FermentingRegistry implements IFermentingRegistry
{
	private List<IFermentationRecipe> recipes = new ArrayList<IFermentationRecipe>();
	private Set<Fluid> fermentableFluids = new HashSet<Fluid>();
	private ILogger logger = NullLogger.INSTANCE;

	@Override
	public void setLogger(@Nonnull ILogger l)
	{
		this.logger = l;
	}

	@Override
	public void addRecipe(@Nonnull IFermentationRecipe recipe)
	{
		recipes.add(recipe);
		final FluidStack fs = recipe.getInputFluidStack();
		fermentableFluids.add(fs.getFluid());
		logger.info("Added Fermentation recipe={%s}", recipe);
	}

	@Override
	public void addRecipe(@Nonnull FluidStack result, @Nonnull FluidStack booze, @Nonnull Object fermenter, int time)
	{
		addRecipe(new FermentationRecipe(booze, MultiStacksUtil.toMultiItemStacks(fermenter), result, time));
	}

	@Override
	public IFermentationRecipe findRecipe(@Nullable FluidStack booze, @Nullable ItemStack fermenter)
	{
		if (booze == null || fermenter == null) return null;
		for (IFermentationRecipe recipe : recipes)
		{
			if (recipe.matchesRecipe(booze, fermenter)) return recipe;
		}
		return null;
	}

	@Override
	public boolean canFerment(@Nullable FluidStack booze)
	{
		if (FluidTest.isValid(booze))
		{
			return fermentableFluids.contains(booze.getFluid());
		}
		return false;
	}
}
