package growthcraft.core;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class Utils
{
	public static void debug(String msg)
	{
		boolean flag = true;

		if (flag) { System.out.println(msg); }
	}

	public static boolean isIDInList(int id, String list)
	{
		String[] itemArray = list.split(";");
		for (int i = 0; i < itemArray.length; i++)
		{
			String[] values = itemArray[i].split(",");
			int tempID = parseInt(values[0], 2147483647);

			if (tempID != 2147483647)
			{
				if (tempID == id)
					return true;
			}
		}
		return false;
	}

	public static int parseInt(String string, int defaultValue)
	{
		try
		{
			return Integer.parseInt(string.trim());
		}
		catch (NumberFormatException ex) {
		}
		return defaultValue;
	}

	public static void decreaseStack(ItemStack itemstack, EntityPlayer player)
	{
		if (!player.capabilities.isCreativeMode)
		{
			--itemstack.stackSize;

			if (itemstack.stackSize <= 0)
			{
				player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
			}
		}
	}

	public static void addStack(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, boolean bool)
	{
		boolean flag = bool ? !player.capabilities.isCreativeMode : true;

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

	public static final boolean isIntegerInRange(int i, int floor, int ceiling)
	{
		if (i < floor || i > ceiling)
		{
			return false;
		}
		return true;
	}

	public static ItemStack consumeItem(ItemStack itemstack)
	{
		if (itemstack.stackSize == 1)
		{
			if (itemstack.getItem().hasContainerItem(itemstack))
			{
				return itemstack.getItem().getContainerItem(itemstack);
			}
			else
			{
				return null;
			}
		}
		else
		{
			itemstack.splitStack(1);
			return itemstack;
		}
	}

	public static boolean fillTank(World world, int x, int y, int z, IFluidHandler tank, ItemStack held, EntityPlayer player)
	{
		if (held != null)
		{
			FluidStack heldContents = FluidContainerRegistry.getFluidForFilledItem(held);

			if (heldContents != null)
			{
				int used = tank.fill(ForgeDirection.UNKNOWN, heldContents, true);

				if (used > 0)
				{
					ItemStack consumed = held.getItem().getContainerItem(held);
					if (!player.inventory.addItemStackToInventory(consumed))
					{
						world.spawnEntityInWorld(new EntityItem(world, (double)x + 0.5D, (double)y + 1.5D, (double)z + 0.5D, consumed));
					}
					else if (player instanceof EntityPlayerMP)
					{
						((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
					}

					if (!player.capabilities.isCreativeMode)
					{
						if (--held.stackSize <= 0)
						{
							player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
						}
					}

					return true;
				}
			}
		}

		return false;
	}

	public static boolean drainTank(World world, int x, int y, int z, IFluidHandler tank, ItemStack held, EntityPlayer player, boolean expbool, int amount, float exp)
	{
		if (held != null)
		{
			FluidStack heldContents = FluidContainerRegistry.getFluidForFilledItem(held);
			FluidStack available = tank.drain(ForgeDirection.UNKNOWN, Integer.MAX_VALUE, false);

			if (available != null)
			{
				ItemStack filled = FluidContainerRegistry.fillFluidContainer(available, held);
				heldContents = FluidContainerRegistry.getFluidForFilledItem(filled);

				if (heldContents != null)
				{
					if (!player.inventory.addItemStackToInventory(filled))
					{
						world.spawnEntityInWorld(new EntityItem(world, (double)x + 0.5D, (double)y + 1.5D, (double)z + 0.5D, filled));
					}
					else if (player instanceof EntityPlayerMP)
					{
						((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
					}

					if (!player.capabilities.isCreativeMode)
					{
						if (--held.stackSize <= 0)
						{
							player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
						}
					}

					if (expbool)
					{
						spawnExp(amount * heldContents.amount / tank.getTankInfo(ForgeDirection.UNKNOWN)[0].capacity, exp, player);
					}
					tank.drain(ForgeDirection.UNKNOWN, heldContents.amount, true);

					return true;
				}
			}
		}

		return false;
	}

	public static void spawnExp(int amount, float exp, EntityPlayer player)
	{
		int j;

		if (exp == 0.0F)
		{
			amount = 0;
		}
		else if (exp < 1.0F)
		{
			j = MathHelper.floor_float((float)amount * exp);

			if (j < MathHelper.ceiling_float_int((float)amount * exp) && (float)Math.random() < (float)amount * exp - (float)j)
			{
				++j;
			}

			amount = j;
		}

		while (amount > 0)
		{
			j = EntityXPOrb.getXPSplit(amount);
			amount -= j;
			player.worldObj.spawnEntityInWorld(new EntityXPOrb(player.worldObj, player.posX, player.posY + 0.5D, player.posZ + 0.5D, j));
		}
	}
}
