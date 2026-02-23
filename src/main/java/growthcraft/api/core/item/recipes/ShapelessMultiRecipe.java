package growthcraft.api.core.item.recipes;

import growthcraft.api.core.definition.IMultiFluidStacks;
import growthcraft.api.core.definition.IMultiItemStacks;
import growthcraft.api.core.item.MultiItemStacks;
import growthcraft.api.core.item.OreItemStacks;
import growthcraft.api.core.util.MultiStacksUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class ShapelessMultiRecipe implements IRecipe
{

	private ItemStack output;
	private ArrayList<IMultiItemStacks> input = new ArrayList<IMultiItemStacks>();
	private ArrayList<IMultiFluidStacks> fluids = new ArrayList<IMultiFluidStacks>();

	public ShapelessMultiRecipe(Block result, Object... recipe)
	{
		this(new ItemStack(result), recipe);
	}

	public ShapelessMultiRecipe(Item result, Object... recipe)
	{
		this(new ItemStack(result), recipe);
	}

	public ShapelessMultiRecipe(ItemStack result, Object... recipe)
	{
		output = result.copy();
		for (Object in : recipe)
		{
			if (in instanceof ItemStack)
			{
				input.add(new MultiItemStacks(((ItemStack) in).copy()));
			} else if (in instanceof Item)
			{
				input.add(new MultiItemStacks(new ItemStack((Item) in)));
			} else if (in instanceof Block)
			{
				input.add(new MultiItemStacks(new ItemStack((Block) in)));
			} else if (in instanceof String)
			{
				input.add(new OreItemStacks((String) in));
			} else if (in instanceof IMultiItemStacks)
			{
				input.add((IMultiItemStacks) in);
			} else if (in instanceof FluidStack)
			{
				fluids.add(MultiStacksUtil.toMultiFluidStacks(in));
			} else if (in instanceof IMultiFluidStacks)
			{
				fluids.add((IMultiFluidStacks) in);
			} else
			{
				String ret = "Invalid shapeless multi recipe: ";
				for (Object tmp : recipe)
				{
					ret += tmp + ", ";
				}
				ret += output;
				throw new RuntimeException(ret);
			}
		}
	}

	/**
	 * Returns the size of the recipe area
	 */
	@Override
	public int getRecipeSize()
	{
		return input.size();
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return output;
	}

	/**
	 * Returns an Item that is the result of this recipe
	 */
	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1)
	{
		return output.copy();
	}

	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	@Override
	public boolean matches(InventoryCrafting var1, World world)
	{
		final Map<IMultiFluidStacks, Integer> aggregateFluids = new HashMap<IMultiFluidStacks, Integer>();
		for (IMultiFluidStacks fluid : fluids)
		{
			aggregateFluids.put(fluid, fluid.getAmount());
		}

		final ArrayList<IMultiItemStacks> required = new ArrayList<IMultiItemStacks>(input);

		for (int x = 0; x < var1.getSizeInventory(); x++)
		{
			final ItemStack slot = var1.getStackInSlot(x);

			if (slot != null)
			{
				boolean inRecipe = false;
				final Iterator<IMultiItemStacks> req = required.iterator();

				while (req.hasNext())
				{
					boolean match = false;

					final IMultiItemStacks next = req.next();
					final Iterator<ItemStack> itr = next.getItemStacks().iterator();
					while (itr.hasNext() && !match)
					{
						match = OreDictionary.itemMatches(itr.next(), slot,
								false);
					}

					if (match)
					{
						inRecipe = true;
						required.remove(next);
						break;
					}
				}

				if (!inRecipe)
				{
					boolean fluidConsumed = false;

					if (FluidContainerRegistry.isFilledContainer(slot))
					{
						final FluidStack containerFluid = FluidContainerRegistry
								.getFluidForFilledItem(slot).copy();

						for (IMultiFluidStacks fluidStacks : fluids)
						{
							final int aggregateAmount = aggregateFluids
									.containsKey(fluidStacks) ? aggregateFluids
									.get(fluidStacks) : 0;

							if (fluidStacks.containsFluidStack(containerFluid)
									&& aggregateAmount > 0)
							{
								final int leftover = Math.max(0, aggregateAmount
										- containerFluid.amount);
								if (leftover <= 1)
								{
									aggregateFluids.remove(fluidStacks);
								} else
								{
									aggregateFluids.put(fluidStacks, leftover);
								}
								fluidConsumed = true;
							}
						}
					}

					if (!fluidConsumed)
					{
						return false;
					}
				}
			}
		}

		return required.isEmpty() && aggregateFluids.isEmpty();
	}

	/**
	 * Returns the input for this recipe, any mod accessing this value should
	 * never manipulate the values in this array as it will effect the recipe
	 * itself.
	 * 
	 * @return The recipes input vales.
	 */
	public ArrayList<IMultiItemStacks> getInput()
	{
		return this.input;
	}

	public List<IMultiFluidStacks> getFluids()
	{
		return fluids;
	}
}
