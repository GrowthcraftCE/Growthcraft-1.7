package growthcraft.core.utils;

import growthcraft.core.GrowthCraftCore;

import buildcraft.api.tools.IToolWrench;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Utility class for item handling.
 */
public class ItemUtils
{
	/**
	 * In place of a missing wrench, you can use a stick, which results in
	 * this amazing wrench, which does amazing things, no really.
	 */
	public static class AmazingStickWrench implements IToolWrench
	{
		public boolean canWrench(EntityPlayer player, int x, int y, int z)
		{
			return true;
		}

		public void wrenchUsed(EntityPlayer player, int x, int y, int z) {}
	}

	public static final IToolWrench amazingStickWrench = new AmazingStickWrench();

	/**
	 * Is this an amazing stick of waaaaaaat
	 *
	 * @param item - item to check
	 * @return true, if the item is an amazing stick, false otherwise
	 */
	public static boolean isAmazingStick(ItemStack item)
	{
		if (item == null) return false;
		return item.getItem() == Items.stick;
	}

	/**
	 * Checks if the provided item is a Wrench
	 *
	 * @param item - an item to check
	 * @return true if the item is a wrench, false otherwise
	 */
	public static boolean isWrench(ItemStack item)
	{
		if (item == null) return false;
		if (GrowthCraftCore.getConfig().useAmazingStick)
		{
			if (isAmazingStick(item)) return true;
		}
		return (item.getItem() instanceof IToolWrench);
	}

	/**
	 * Returns the item as a wrench, or null
	 *
	 * @param itemStack - an item stack
	 * @return IToolWrench if item was a wrench, null otherwise
	 */
	public static IToolWrench asWrench(ItemStack itemStack)
	{
		if (isWrench(itemStack))
		{
			if (isAmazingStick(itemStack))
			{
				return amazingStickWrench;
			}
			else
			{
				return (IToolWrench)itemStack.getItem();
			}
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
