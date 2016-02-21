/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.core.common.block;

import java.util.Random;
import javax.annotation.Nonnull;

import growthcraft.core.common.tileentity.ICustomDisplayName;
import growthcraft.core.common.tileentity.IItemHandler;
import growthcraft.api.core.util.BlockFlags;
import growthcraft.core.util.ItemUtils;
import growthcraft.core.Utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * Base class for machines and the like
 */
public abstract class GrcBlockContainer extends BlockContainer implements IDroppableBlock, IRotatableBlock, IWrenchable
{
	protected Random rand = new Random();
	protected Class<? extends TileEntity> tileEntityType;

	public GrcBlockContainer(@Nonnull Material material)
	{
		super(material);
		this.isBlockContainer = true;
	}

	protected void setTileEntityType(Class<? extends TileEntity> klass)
	{
		this.tileEntityType = klass;
	}

	/**
	 * Drops the block as an item and replaces it with air
	 *
	 * @param world - world to drop in
	 * @param x - x Coord
	 * @param y - y Coord
	 * @param z - z Coord
	 */
	public void fellBlockAsItem(World world, int x, int y, int z)
	{
		this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
		world.setBlockToAir(x, y, z);
	}

	/* IRotatableBlock */
	public boolean isRotatable(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		return false;
	}

	public void doRotateBlock(World world, int x, int y, int z, ForgeDirection side)
	{
		final int meta = world.getBlockMetadata(x, y, z);
		final ForgeDirection current = ForgeDirection.getOrientation(meta);
		ForgeDirection newDirection = current;
		if (current == side)
		{
			switch (current)
			{
				case UP:
					newDirection = ForgeDirection.NORTH;
					break;
				case DOWN:
					newDirection = ForgeDirection.SOUTH;
					break;
				case NORTH:
				case EAST:
					newDirection = ForgeDirection.UP;
					break;
				case SOUTH:
				case WEST:
					newDirection = ForgeDirection.DOWN;
					break;
				default:
					// some invalid state
					break;
			}
		}
		else
		{
			switch (current)
			{
				case UP:
					newDirection = ForgeDirection.DOWN;
					break;
				case DOWN:
					newDirection = ForgeDirection.UP;
					break;
				case WEST:
					newDirection = ForgeDirection.SOUTH;
					break;
				case EAST:
					newDirection = ForgeDirection.NORTH;
					break;
				case NORTH:
					newDirection = ForgeDirection.WEST;
					break;
				case SOUTH:
					newDirection = ForgeDirection.EAST;
					break;
				default:
					// yet another invalid state
					break;
			}
		}
		if (newDirection != current)
		{
			world.setBlockMetadataWithNotify(x, y, z, newDirection.ordinal(), BlockFlags.UPDATE_AND_SYNC);
		}
	}

	public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection side)
	{
		if (isRotatable(world, x, y, z, side))
		{
			doRotateBlock(world, x, y, z, side);
			world.markBlockForUpdate(x, y, z);
			return true;
		}
		return false;
	}

	public boolean wrenchBlock(World world, int x, int y, int z, EntityPlayer player, ItemStack wrench)
	{
		if (player != null)
		{
			if (ItemUtils.canWrench(wrench, player, x, y, z))
			{
				if (player.isSneaking())
				{
					fellBlockAsItem(world, x, y, z);
					ItemUtils.wrenchUsed(wrench, player, x, y, z);
					return true;
				}
			}
		}
		return false;
	}

	public boolean tryWrenchItem(EntityPlayer player, World world, int x, int y, int z)
	{
		if (player == null) return false;
		final ItemStack is = player.inventory.getCurrentItem();
		return wrenchBlock(world, x, y, z, player, is);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
	{
		if (stack.hasDisplayName())
		{
			final TileEntity te = getTileEntity(world, x, y, z);
			if (te instanceof ICustomDisplayName)
			{
				((ICustomDisplayName)te).setGuiDisplayName(stack.getDisplayName());
			}
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int par6)
	{
		final TileEntity te = getTileEntity(world, x, y, z);
		if (te instanceof IInventory)
		{
			final IInventory inventory = (IInventory)te;

			if (inventory != null)
			{
				for (int index = 0; index < inventory.getSizeInventory(); ++index)
				{
					final ItemStack stack = inventory.getStackInSlot(index);

					ItemUtils.spawnBrokenItemStack(world, x, y, z, stack, rand);
				}

				world.func_147453_f(x, y, z, block);
			}
		}
		super.breakBlock(world, x, y, z, block, par6);
	}

	protected boolean playerFillTank(World world, int x, int y, int z, IFluidHandler fh, ItemStack is, EntityPlayer player)
	{
		return Utils.playerFillTank(world, x, y, z, fh, is, player);
	}

	protected boolean playerDrainTank(World world, int x, int y, int z, IFluidHandler fh, ItemStack is, EntityPlayer player)
	{
		final FluidStack fs = Utils.playerDrainTank(world, x, y, z, fh, is, player);
		return fs != null && fs.amount > 0;
	}

	private boolean handleIFluidHandler(World world, int x, int y, int z, EntityPlayer player, int meta)
	{
		final TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof IFluidHandler)
		{
			final IFluidHandler fh = (IFluidHandler)te;
			final ItemStack is = player.inventory.getCurrentItem();

			boolean needUpdate = false;

			if (!player.isSneaking())
			{
				// While not sneaking, draining is given priority
				if (playerDrainTank(world, x, y, z, fh, is, player) ||
					playerFillTank(world, x, y, z, fh, is, player)) needUpdate = true;
			}
			else
			{
				// Otherwise filling is given priority
				if (playerFillTank(world, x, y, z, fh, is, player) ||
					playerDrainTank(world, x, y, z, fh, is, player)) needUpdate = true;
			}
			if (needUpdate)
			{
				world.markBlockForUpdate(x, y, z);
				return true;
			}
		}
		return false;
	}

	private boolean handleOnUseItem(World world, int x, int y, int z, EntityPlayer player, int meta)
	{
		final TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof IItemHandler)
		{
			final IItemHandler ih = (IItemHandler)te;
			final ItemStack is = player.inventory.getCurrentItem();

			boolean needUpdate = false;
			if (ih.tryPlaceItem(player, is))
			{
				needUpdate = true;
			}
			else if (ih.tryTakeItem(player, is))
			{
				needUpdate = true;
			}

			if (needUpdate)
			{
				world.markBlockForUpdate(x, y, z);
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float par7, float par8, float par9)
	{
		if (world.isRemote) return true;
		if (tryWrenchItem(player, world, x, y, z)) return true;
		if (handleIFluidHandler(world, x, y, z, player, meta)) return true;
		if (handleOnUseItem(world, x, y, z, player, meta)) return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public <T extends TileEntity> T getTileEntity(IBlockAccess world, int x, int y, int z)
	{
		final TileEntity te = world.getTileEntity(x, y, z);
		if (te != null)
		{
			if (tileEntityType.isInstance(te))
			{
				return (T)te;
			}
			else
			{
				// warn
			}
		}
		return null;
	}

	public TileEntity createNewTileEntity(World world, int unused)
	{
		if (tileEntityType != null)
		{
			try
			{
				return tileEntityType.newInstance();
			}
			catch (InstantiationException e)
			{
				throw new IllegalStateException("Failed to create a new instance of an illegal class " + this.tileEntityType, e);
			}
			catch (IllegalAccessException e)
			{
				throw new IllegalStateException("Failed to create a new instance of " + this.tileEntityType + ", because lack of permissions", e);
			}
		}
		return null;
	}
}
