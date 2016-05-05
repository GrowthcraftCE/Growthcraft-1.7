package growthcraft.api.core.item;

import growthcraft.api.core.definition.IItemStackListProvider;
import growthcraft.api.core.definition.IMultiFluidStacks;
import growthcraft.api.core.definition.IMultiItemStacks;
import growthcraft.api.core.util.MultiStacksUtil;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CombinedMultiItemStacks implements IMultiItemStacks
{
	public int stackSize;
	private List<IItemStackListProvider> itemStacks;

	public CombinedMultiItemStacks(int amount, @Nonnull Object... items)
	{
		itemStacks = new ArrayList<IItemStackListProvider>();

		for (Object in : items)
		{
			if (in instanceof ItemStack)
			{
				itemStacks.add(new MultiItemStacks(((ItemStack) in).copy()));
			} else if (in instanceof Item)
			{
				itemStacks.add(new MultiItemStacks(new ItemStack((Item) in)));
			} else if (in instanceof Block)
			{
				itemStacks.add(new MultiItemStacks(new ItemStack((Block) in)));
			} else if (in instanceof String)
			{
				itemStacks.add(new OreItemStacks((String) in));
			} else if (in instanceof IMultiItemStacks)
			{
				itemStacks.add((IMultiItemStacks) in);
			} else if (in instanceof FluidStack)
			{
				itemStacks.add(MultiStacksUtil.toMultiFluidStacks(in));
			} else if (in instanceof IMultiFluidStacks)
			{
				itemStacks.add((IMultiFluidStacks) in);
			} else
			{
				String ret = "Invalid MultiOreItemStacks: ";
				for (Object tmp : items)
				{
					ret += tmp + ", ";
				}
				throw new RuntimeException(ret);
			}
		}
		this.stackSize = amount;
	}

	public CombinedMultiItemStacks(@Nonnull Object... items)
	{
		this(1, items);
	}

	@Override
	public int getStackSize()
	{
		return stackSize;
	}

	public List<ItemStack> getRawItemStacks()
	{
		final List<ItemStack> output = new ArrayList<ItemStack>();
		
		for (IItemStackListProvider itemList : itemStacks)
		{
			output.addAll(itemList.getItemStacks());
		}
		
		return output;
	}

	@Override
	public boolean isEmpty()
	{
		return getRawItemStacks().isEmpty();
	}

	@Override
	public List<ItemStack> getItemStacks()
	{
		final List<ItemStack> items = getRawItemStacks();
		final Set<ItemStack> result = new LinkedHashSet<ItemStack>();
		for (ItemStack stack : items)
		{
			final ItemStack newStack = stack.copy();
			if (newStack.stackSize <= 0) newStack.stackSize = 1;
			newStack.stackSize *= stackSize;
			result.add(newStack);
		}
		
		return new ArrayList<ItemStack>(result);
	}

	@Override
	public boolean containsItemStack(@Nullable ItemStack stack)
	{
		if (!ItemTest.isValid(stack)) return false;
		for (ItemStack content : getItemStacks())
		{
			if (content.isItemEqual(stack)) return true;
		}
		return false;
	}
}
