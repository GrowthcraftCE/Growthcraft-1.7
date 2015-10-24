package growthcraft.api.cellar.fermenting;

import growthcraft.api.core.util.ItemKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;

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
	private Map<BiomeDictionary.Type, List<ItemStack>> biomeToYeast = new HashMap<BiomeDictionary.Type, List<ItemStack>>();
	private Map<ItemKey, List<BiomeDictionary.Type>> yeastToBiome = new HashMap<ItemKey, List<BiomeDictionary.Type>>();

	@Nonnull
	private Fluid boozeToKey(@Nonnull FluidStack booze)
	{
		return booze.getFluid();
	}

	private ItemKey stackToKey(@Nonnull ItemStack stack)
	{
		return new ItemKey(stack);
	}

	public void addYeastToBiomeType(@Nonnull ItemStack yeast, BiomeDictionary.Type type)
	{
		if (!biomeToYeast.containsKey(type))
		{
			biomeToYeast.put(type, new ArrayList<ItemStack>());
		}
		final ItemKey yeastKey = stackToKey(yeast);
		if (!yeastToBiome.containsKey(yeastKey))
		{
			yeastToBiome.put(yeastKey, new ArrayList<BiomeDictionary.Type>());
		}
		biomeToYeast.get(type).add(yeast);
		yeastToBiome.get(yeastKey).add(type);
	}

	public List<ItemStack> getYeastListForBiomeType(BiomeDictionary.Type type)
	{
		return biomeToYeast.get(type);
	}

	public List<BiomeDictionary.Type> getBiomeTypesForYeast(ItemStack yeast)
	{
		return yeastToBiome.get(stackToKey(yeast));
	}

	public boolean canYeastFormInBiome(ItemStack yeast, BiomeGenBase biome)
	{
		if (yeast == null || biome == null) return false;

		List<BiomeDictionary.Type> yeastBiomeList = getBiomeTypesForYeast(yeast);
		if (yeastBiomeList == null) return false;

		for (BiomeDictionary.Type t : BiomeDictionary.getTypesForBiome(biome))
		{
			if (yeastBiomeList.contains(t)) return true;
		}

		return false;
	}

	public void addFermentation(@Nonnull FluidStack result, @Nonnull FluidStack booze, @Nonnull ItemStack fermenter, int time)
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
