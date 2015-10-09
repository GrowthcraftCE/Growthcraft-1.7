package growthcraft.core.utils;

import buildcraft.api.tools.IToolWrench;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Utility class for item handling.
 */
public class ItemUtils
{
	/**
	 * Checks if the provided item is a Wrench
	 *
	 * @param item - an item to check
	 * @return true if the item is a wrench, false otherwise
	 */
	public static boolean isWrench(ItemStack item)
	{
		if (item == null) return false;
		return (item.getItem() instanceof IToolWrench);
	}

	/**
	 * Returns the item as a wrench, or null
	 *
	 * @param item - an item
	 * @return IToolWrench if item was a wrench, null otherwise
	 */
	public static IToolWrench asWrench(ItemStack item)
	{
		if (isWrench(item))
		{
			return (IToolWrench)item.getItem();
		}
		else
		{
			return null;
		}
	}

	public static boolean canWrench(ItemStack item, EntityPlayer player, int x, int y, int z)
	{
		final IToolWrench wrench = asWrench(item);
		if (wrench == null) return false;
		return wrench.canWrench(player, x, y, z);
	}

	public static void wrenchUsed(ItemStack item, EntityPlayer player, int x, int y, int z)
	{
		final IToolWrench wrench = asWrench(item);
		if (wrench == null) return;
		wrench.wrenchUsed(player, x, y, z);
	}
}
