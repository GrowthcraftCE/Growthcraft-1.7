package growthcraft.core.util;

import java.util.Random;

import growthcraft.core.common.item.AmazingStickWrench;
import growthcraft.core.common.item.IGrcWrench;
import growthcraft.core.GrowthCraftCore;

import buildcraft.api.tools.IToolWrench;

import cpw.mods.fml.common.Loader;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

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

	public static ItemStack[] clearInventorySlots(ItemStack[] invSlots, int expectedSize)
	{
		// avoid reallocating an existing ItemStack array
		if (invSlots == null || invSlots.length != expectedSize)
		{
			return new ItemStack[expectedSize];
		}
		else
		{
			// clear it instead
			for (int i = 0; i < invSlots.length; ++i)
			{
				invSlots[i] = null;
			}
		}
		return invSlots;
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

	public static ItemStack consumeStack(ItemStack itemstack, int amount)
	{
		return increaseStack(itemstack, -amount);
	}

	public static ItemStack consumeStack(ItemStack itemstack)
	{
		return consumeStack(itemstack, 1);
	}

	/**
	 * Destructive version of mergeStacks, may mutate both item stacks
	 *
	 * @param a - primary stack to merge to
	 *   If the secondary stack is null, then this stack is returned as is
	 * @param b - secondary stack to merge from
	 *   If the primary stack is null, then a copy of this stack is returned and
	 *   the original's stackSize reduced to 0
	 * @return new ItemStack - the resulting ItemStack of null
	 */
	public static ItemStack mergeStacksBang(ItemStack a, ItemStack b)
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
			return a;
		}
		else
		{
			if (a.isItemEqual(b))
			{
				final int newSize = MathHelper.clamp_int(a.stackSize + b.stackSize, 0, a.getMaxStackSize());
				b.stackSize -= newSize - a.stackSize;
				a.stackSize = newSize;
				return a;
			}
		}
		return null;
	}

	/**
	 * Less-destructive version of mergeStacksBang
	 *
	 * @param a - destination stack, may be modified
	 * @param b - source stack, will not be modified
	 * @return new ItemStack
	 */
	public static ItemStack mergeStacks(ItemStack a, ItemStack b)
	{
		return mergeStacksBang(a, b != null ? b.copy() : b);
	}

	public static void decreaseStackOnPlayer(ItemStack itemstack, EntityPlayer player)
	{
		if (player.capabilities.isCreativeMode) return;

		final ItemStack result = consumeStack(itemstack);
		player.inventory.setInventorySlotContents(player.inventory.currentItem, result);
	}

	public static void addStackToPlayer(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, boolean checkCreative)
	{
		final boolean flag = checkCreative ? !player.capabilities.isCreativeMode : true;
		if (flag)
		{
			if (!player.inventory.addItemStackToInventory(itemstack))
			{
				world.spawnEntityInWorld(new EntityItem(world, (double)x + 0.5D, (double)y + 1.5D, (double)z + 0.5D, itemstack));
			}
			else if (player instanceof EntityPlayerMP)
			{
				((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
			}
		}
	}

	public static void addStackToPlayer(ItemStack itemstack, EntityPlayer player, World world, boolean checkCreative)
	{
		addStackToPlayer(itemstack, player, world, (int)player.posX, (int)player.posY, (int)player.posZ, checkCreative);
	}

	public static void spawnItemStack(World world, int x, int y, int z, ItemStack stack, Random random)
	{
		if (stack != null)
		{
			final float f = random.nextFloat() * 0.8F + 0.1F;
			final float f1 = random.nextFloat() * 0.8F + 0.1F;
			final float f2 = random.nextFloat() * 0.8F + 0.1F;

			final EntityItem entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), stack);
			final float f3 = 0.05F;
			entityitem.motionX = (double)((float)random.nextGaussian() * f3);
			entityitem.motionY = (double)((float)random.nextGaussian() * f3 + 0.2F);
			entityitem.motionZ = (double)((float)random.nextGaussian() * f3);
			world.spawnEntityInWorld(entityitem);
		}
	}

	public static void spawnBrokenItemStack(World world, int x, int y, int z, ItemStack stack, Random random)
	{
		if (stack != null)
		{
			while (stack.stackSize > 0)
			{
				int k1 = random.nextInt(21) + 10;

				if (k1 > stack.stackSize)
				{
					k1 = stack.stackSize;
				}

				final ItemStack entityStack = stack.splitStack(k1);

				spawnItemStack(world, x, y, z, entityStack, random);
			}
		}
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
