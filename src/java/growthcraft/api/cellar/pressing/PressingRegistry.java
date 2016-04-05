package growthcraft.api.cellar.pressing;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.cellar.common.Residue;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class PressingRegistry implements IPressingRegistry
{
	private ILogger logger = NullLogger.INSTANCE;
	private List<PressingRecipe> recipes = new ArrayList<PressingRecipe>();

	@Override
	public void setLogger(@Nonnull ILogger l)
	{
		this.logger = l;
	}

	@Override
	public void addRecipe(@Nonnull PressingRecipe recipe)
	{
		recipes.add(recipe);
		logger.debug("Added new Pressing Recipe recipe={%s}", recipe);
	}

	@Override
	public void addRecipe(@Nonnull ItemStack stack, @Nonnull FluidStack resultFluid, int time, @Nullable Residue residue)
	{
		addRecipe(new PressingRecipe(stack, resultFluid, time, residue));
	}

	@Override
	public PressingRecipe getPressingRecipe(ItemStack itemstack)
	{
		if (itemstack == null) return null;

		for (PressingRecipe recipe : recipes)
		{
			if (recipe.matchesRecipe(itemstack)) return recipe;
		}
		return null;
	}

	@Override
	public boolean hasPressingRecipe(ItemStack itemstack)
	{
		return getPressingRecipe(itemstack) != null;
	}
}
