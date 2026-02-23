package growthcraft.api.core.item.recipes;

import growthcraft.api.core.definition.IItemStackListProvider;
import growthcraft.api.core.definition.IMultiItemStacks;
import growthcraft.api.core.fluids.FluidUtils;
import growthcraft.api.core.item.MultiItemStacks;
import growthcraft.api.core.item.OreItemStacks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class ShapedMultiRecipe implements IRecipe
{
	// Added in for future ease of change, but hard coded for now.
	private static final int MAX_CRAFT_GRID_WIDTH = 3;
	private static final int MAX_CRAFT_GRID_HEIGHT = 3;

	private ItemStack output;
	private ArrayList<IItemStackListProvider> input;
	private int width;
	private int height;
	private boolean mirrored = true;

	public ShapedMultiRecipe(Block result, Object... recipe)
	{
		this(new ItemStack(result), recipe);
	}

	public ShapedMultiRecipe(Item result, Object... recipe)
	{
		this(new ItemStack(result), recipe);
	}

	public ShapedMultiRecipe(ItemStack result, Object... recipe)
	{
		output = result.copy();

		String shape = "";
		int idx = 0;

		if (recipe[idx] instanceof Boolean)
		{
			mirrored = (Boolean) recipe[idx];
			if (recipe[idx + 1] instanceof Object[])
			{
				recipe = (Object[]) recipe[idx + 1];
			} else
			{
				idx = 1;
			}
		}

		if (recipe[idx] instanceof String[])
		{
			final String[] parts = (String[]) recipe[idx++];

			for (String s : parts)
			{
				width = s.length();
				shape += s;
			}

			height = parts.length;
		} else
		{
			while (recipe[idx] instanceof String)
			{
				final String s = (String) recipe[idx++];
				shape += s;
				width = s.length();
				height++;
			}
		}

		if (width * height != shape.length())
		{
			String ret = "Invalid shaped ore recipe: ";
			for (Object tmp : recipe)
			{
				ret += tmp + ", ";
			}
			ret += output;
			throw new RuntimeException(ret);
		}

		final HashMap<Character, IItemStackListProvider> itemMap = new HashMap<Character, IItemStackListProvider>();

		for (; idx < recipe.length; idx += 2)
		{
			final Character chr = (Character) recipe[idx];
			final Object in = recipe[idx + 1];

			if (in instanceof ItemStack)
			{
				itemMap.put(chr, new MultiItemStacks((ItemStack) in).copy());
			} else if (in instanceof Item)
			{
				itemMap.put(chr, new MultiItemStacks(new ItemStack((Item) in)));
			} else if (in instanceof Block)
			{
				itemMap.put(chr, new MultiItemStacks(new ItemStack((Block) in, 1,
						OreDictionary.WILDCARD_VALUE)));
			} else if (in instanceof String)
			{
				itemMap.put(chr, new OreItemStacks((String) in));
			} else if (in instanceof IItemStackListProvider)
			{
				itemMap.put(chr, (IItemStackListProvider) in);
			} else if (in instanceof FluidStack)
			{
				itemMap.put(chr, new MultiItemStacks(FluidUtils.getFluidContainers((FluidStack) in)));
			} else
			{
				String ret = "Invalid shaped multi recipe: ";
				for (Object tmp : recipe)
				{
					ret += tmp + ", ";
				}
				ret += output;
				throw new RuntimeException(ret);
			}
		}

		input = new ArrayList<IItemStackListProvider>(width * height);
		for (char chr : shape.toCharArray())
		{
			input.add(itemMap.get(chr));
		}
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
	 * Used to check if a recipe matches current crafting inventory
	 */
	@Override
	public boolean matches(InventoryCrafting inv, World world)
	{
		for (int x = 0; x <= MAX_CRAFT_GRID_WIDTH - width; x++)
		{
			for (int y = 0; y <= MAX_CRAFT_GRID_HEIGHT - height; ++y)
			{
				if (checkMatch(inv, x, y, false))
				{
					return true;
				}

				if (mirrored && checkMatch(inv, x, y, true))
				{
					return true;
				}
			}
		}

		return false;
	}

	private boolean checkMatch(InventoryCrafting inv, int startX, int startY,
			boolean mirror)
	{
		for (int x = 0; x < MAX_CRAFT_GRID_WIDTH; x++)
		{
			for (int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y++)
			{
				final int subX = x - startX;
				final int subY = y - startY;
				IItemStackListProvider target = null;

				if (subX >= 0 && subY >= 0 && subX < width && subY < height)
				{
					if (mirror)
					{
						target = input.get(width - subX - 1 + subY * width);
					} else
					{
						target = input.get(subX + subY * width);
					}
				}

				final ItemStack slot = inv.getStackInRowAndColumn(x, y);

				if (target instanceof IMultiItemStacks)
				{
					boolean matched = false;

					final Iterator<ItemStack> itr = target.getItemStacks().iterator();
					while (itr.hasNext() && !matched)
					{
						matched = OreDictionary.itemMatches(itr.next(), slot,
								false);
					}

					if (!matched)
					{
						return false;
					}
				} else if (target == null && slot != null)
				{
					return false;
				}
			}
		}

		return true;
	}

	public ShapedMultiRecipe setMirrored(boolean mirror)
	{
		mirrored = mirror;
		return this;
	}

	/**
	 * Returns the input for this recipe, any mod accessing this value should
	 * never manipulate the values in this array as it will effect the recipe
	 * itself.
	 * 
	 * @return The recipes input vales.
	 */
	public ArrayList<IItemStackListProvider> getInput()
	{
		return this.input;
	}
}