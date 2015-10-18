package growthcraft.core.utils;

import growthcraft.core.GrowthCraftCore;
import growthcraft.core.item.IGrcWrench;
import growthcraft.core.item.AmazingStickWrench;

import buildcraft.api.tools.IToolWrench;

import cpw.mods.fml.common.Loader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

/**
 * Utility class for item handling.
 */
public class ItemUtils
{
	public static final IGrcWrench amazingStickWrench = new AmazingStickWrench();

	private static boolean hasIToolWrench;

	private ItemUtils() {}

	public static void init()
	{
		// There should be a better way to check this
		if (Loader.isModLoaded("BuildCraft|Core"))
		{
			hasIToolWrench = true;
		}
	}

	public static ItemStack increaseStack(ItemStack itemstack, int amount)
	{
		itemstack.stackSize = MathHelper.clamp_int(itemstack.stackSize + amount, 0, itemstack.getMaxStackSize());
		if (itemstack.stackSize == 0)
		{
			final Item item = itemstack.getItem();
			if (item.hasContainerItem(itemstack))
			{
				return item.getContainerItem(itemstack);
			}
			else
			{
				return null;
			}
		}
		return itemstack;
	}

	public static ItemStack increaseStack(ItemStack itemstack)
	{
		return increaseStack(itemstack, 1);
	}

	public static ItemStack consumeItem(ItemStack itemstack, int amount)
	{
		return increaseStack(itemstack, -amount);
	}

	public static ItemStack consumeItem(ItemStack itemstack)
	{
		return consumeItem(itemstack, 1);
	}

	public static ItemStack mergeStacks(ItemStack a, ItemStack b)
	{
		if (a == null && b == null)
		{
			return null;
		}
		else if (a == null && b != null)
		{
			final ItemStack result = b.copy();
			b.stackSize = 0;
			return result;
		}
		else if (a != null && b == null)
		{
			return a.copy();
		}
		else
		{
			if (a.isItemEqual(b))
			{
				final int newSize = MathHelper.clamp_int(a.stackSize + b.stackSize, 0, a.getMaxStackSize());
				final ItemStack result = a.copy();
				b.stackSize -= newSize - a.stackSize;
				result.stackSize = newSize;
				return result;
			}
		}
		return null;
	}

	/**
	 * Is this an amazing stick of waaaaaaat
	 *
	 * @param item - item to check
	 * @return true, if the item is an amazing stick, false otherwise
	 */
	public static boolean isAmazingStick(ItemStack item)
	{
		if (item == null) return false;
		if (GrowthCraftCore.getConfig().useAmazingStick)
		{
			return item.getItem() == Items.stick;
		}
		return false;
	}

	/**
	 * Determines if item is an IToolWrench
	 *
	 * @param item - an item to check
	 * @return true if the item is a IToolWrench, false otherwise
	 */
	public static boolean isIToolWrench(ItemStack item)
	{
		if (item == null) return false;
		if (hasIToolWrench)
		{
			return item.getItem() instanceof IToolWrench;
		}
		return false;
	}

	public static boolean isIGrcWrench(ItemStack item)
	{
		if (item == null) return false;
		return item.getItem() instanceof IGrcWrench;
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
		if (isAmazingStick(item)) return true;
		return isIGrcWrench(item) || isIToolWrench(item);
	}

	/**
	 * Returns the IGrcWrench for the given item, if it is an IGrcWrench
	 *
	 * @param item - wrench item
	 * @return IGrcWrench if item is a wrench, null otherwise
	 */
	public static IGrcWrench asIGrcWrench(ItemStack item)
	{
		if (isAmazingStick(item))
		{
			return amazingStickWrench;
		}
		else if (isIGrcWrench(item))
		{
			return (IGrcWrench)item.getItem();
		}
		return null;
	}

	public static boolean canWrench(ItemStack item, EntityPlayer player, int x, int y, int z)
	{
		if (item == null) return false;
		final IGrcWrench wrench = asIGrcWrench(item);
		if (wrench != null)
		{
			return wrench.canWrench(player, x, y, z);
		}
		else if (isIToolWrench(item))
		{
			return ((IToolWrench)item.getItem()).canWrench(player, x, y, z);
		}
		return false;
	}

	public static void wrenchUsed(ItemStack item, EntityPlayer player, int x, int y, int z)
	{
		if (item == null) return;
		final IGrcWrench wrench = asIGrcWrench(item);
		if (wrench != null)
		{
			wrench.wrenchUsed(player, x, y, z);
		}
		else if (isIToolWrench(item))
		{
			((IToolWrench)item.getItem()).wrenchUsed(player, x, y, z);
		}
	}
}
