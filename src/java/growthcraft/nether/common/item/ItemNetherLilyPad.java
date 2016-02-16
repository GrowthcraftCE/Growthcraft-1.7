/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015, 2016 IceDragon200
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
