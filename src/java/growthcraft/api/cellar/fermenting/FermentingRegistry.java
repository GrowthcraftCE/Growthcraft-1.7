package growthcraft.api.cellar.fermenting;

import java.util.HashMap;
import javax.annotation.Nonnull;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;
import growthcraft.api.core.util.ItemKey;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FermentingRegistry implements IFermentingRegistry
{
	static class FluidModifierMap extends HashMap<ItemKey, FermentationRecipe>
	{
		public static final long serialVersionUID = 1L;
	}

	static class FluidModifierTree extends HashMap<Fluid, FluidModifierMap>
	{
		public static final long serialVersionUID = 1L;
	}

	// This maps Fluids to Items (in a ItemKey) to FluidStacks
	// The lookup works like this: First you search for a Booze, then you
	// look at its modifiers and return its resultant FluidStack
	// Why didn't I use a List? The linear lookup and the lack of type safety
	private FluidModifierTree fermentTree = new FluidModifierTree();
	private ILogger logger = NullLogger.INSTANCE;

	@Override
	public void setLogger(@Nonnull ILogger l)
	{
		this.logger = l;
	}

	@Nonnull
	private Fluid boozeToKey(@Nonnull FluidStack booze)
	{
		return CellarRegistry.instance().booze().maybeAlternateBooze(booze.getFluid());
	}

	@Nonnull
	private ItemKey stackToKey(@Nonnull ItemStack stack)
	{
		return new ItemKey(stack);
	}

	@Override
	public void addFermentingRecipe(@Nonnull FluidStack result, @Nonnull FluidStack booze, @Nonnull ItemStack fermenter, int time)
	{
		final Fluid key = boozeToKey(booze);
		if (!fermentTree.containsKey(key))
		{
			fermentTree.put(key, new FluidModifierMap());
		}
		fermentTree.get(key).put(stackToKey(fermenter), new FermentationRecipe(booze, fermenter, result, time, null));
	}

	@Override
	public FermentationRecipe getFermentationRecipe(FluidStack booze, ItemStack fermenter)
	{
		if (booze == null || fermenter == null) return null;

		final FluidModifierMap map = fermentTree.get(boozeToKey(booze));
		if (map != null)
		{
			return map.get(stackToKey(fermenter));
		}
		return null;
	}

	@Override
	public boolean canFerment(FluidStack booze)
	{
		if (booze == null) return false;
		return fermentTree.containsKey(boozeToKey(booze));
	}
}
