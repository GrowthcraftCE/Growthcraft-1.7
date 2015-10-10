package growthcraft.cellar.block;

import growthcraft.core.block.IDroppableBlock;
import growthcraft.core.block.IRotatableBlock;
import growthcraft.core.utils.BlockFlags;
import growthcraft.core.utils.ItemUtils;
import growthcraft.core.Utils;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Base class for Cellar machines and the like
 */
public abstract class BlockCellarContainer extends BlockContainer implements IDroppableBlock, IRotatableBlock
{
	public BlockCellarContainer(Material material)
	{
		super(material);
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

	public boolean isRotatable()
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
					break;
			}
		}
		if (newDirection != current)
		{
			world.setBlockMetadataWithNotify(x, y, z, newDirection.ordinal(), BlockFlags.UPDATE_CLIENT);
		}
	}

	public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection side)
	{
		if (isRotatable())
		{
			doRotateBlock(world, x, y, z, side);
			world.markBlockForUpdate(x, y, z);
			return true;
		}
		return false;
	}

	public boolean useWrenchItem(EntityPlayer player, World world, int x, int y, int z)
	{
		if (player != null)
		{
			final ItemStack is = player.inventory.getCurrentItem();
			if (ItemUtils.canWrench(is, player, x, y, z))
			{
				if (player.isSneaking())
				{
					fellBlockAsItem(world, x, y, z);
					ItemUtils.wrenchUsed(is, player, x, y, z);
					return true;
				}
			}
		}
		return false;
	}
}
