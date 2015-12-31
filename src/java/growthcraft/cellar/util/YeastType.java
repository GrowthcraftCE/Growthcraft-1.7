package growthcraft.cellar.util;

import growthcraft.cellar.GrowthCraftCellar;

import net.minecraft.item.ItemStack;

public enum YeastType
{
	BREWERS,
	LAGER,
	BAYANUS,
	ETHEREAL,
	ORIGIN;

	public static final int length = values().length;

	/**
	 * Convience method for creating the corresponding yeast stack
	 *   example: YeastType.BREWERS.asStack(size)
	 *
	 * @param size - size of the stack to create
	 * @return yeast stack
	 */
	public ItemStack asStack(int size)
	{
		return GrowthCraftCellar.yeast.asStack(size, ordinal());
	}

	/**
	 * @return yeast stack, size: 1
	 */
	public ItemStack asStack()
	{
		return asStack(1);
	}
}
