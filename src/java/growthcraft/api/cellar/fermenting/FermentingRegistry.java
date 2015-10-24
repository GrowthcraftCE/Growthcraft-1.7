package growthcraft.api.cellar.fermenting;

import growthcraft.api.core.util.ItemKey;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FermentingRegistry
{
	static class FluidModifierMap extends HashMap<ItemKey, FermentationResult>
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

	private Fluid boozeToKey(FluidStack booze)
	{
		return booze.getFluid();
	}

	private ItemKey stackToKey(ItemStack stack)
	{
		return new ItemKey(stack);
	}

	public void addFermentation(FluidStack result, FluidStack booze, ItemStack fermenter, int time)
	{
		final Fluid key = boozeToKey(booze);
		if (!fermentTree.containsKey(key))
		{
			fermentTree.put(key, new FluidModifierMap());
		}
		fermentTree.get(key).put(stackToKey(fermenter), new FermentationResult(result, time, null));
	}

	public FermentationResult getFermentation(FluidStack booze, ItemStack fermenter)
	{
		if (booze == null || fermenter == null) return null;

		final FluidModifierMap map = fermentTree.get(boozeToKey(booze));
		if (map != null)
		{
			return map.get(stackToKey(fermenter));
		}
		return null;
	}

	public boolean canFerment(FluidStack booze)
	{
		return fermentTree.containsKey(boozeToKey(booze));
	}
}
