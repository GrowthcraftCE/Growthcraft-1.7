package growthcraft.nether.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemNetherLilyPad extends ItemBlock
{
	private Block lilypadBlock;

	public ItemNetherLilyPad(Block block)
	{
		super(block);
		this.lilypadBlock = block;
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
	{
		final MovingObjectPosition pos = this.getMovingObjectPositionFromPlayer(world, player, true);

		if (pos == null)
		{
			return itemstack;
		}
		else
		{
			if (pos.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
			{
				final int x = pos.blockX;
				final int y = pos.blockY;
				final int z = pos.blockZ;

				if (!world.canMineBlock(player, x, y, z))
				{
					return itemstack;
				}

				if (!player.canPlayerEdit(x, y, z, pos.sideHit, itemstack))
				{
					return itemstack;
				}

				if (world.getBlock(x, y, z).getMaterial() == Material.lava && world.getBlockMetadata(x, y, z) == 0 && world.isAirBlock(x, y + 1, z))
				{
					world.setBlock(x, y + 1, z, lilypadBlock);

					if (!player.capabilities.isCreativeMode)
					{
						--itemstack.stackSize;
					}
				}
			}

			return itemstack;
		}
	}
}
