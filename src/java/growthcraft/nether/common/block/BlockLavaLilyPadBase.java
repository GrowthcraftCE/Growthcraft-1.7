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
package growthcraft.nether.common.block;

import java.util.List;

import growthcraft.api.core.util.RenderType;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockLavaLilyPadBase extends BlockBush
{
	public BlockLavaLilyPadBase()
	{
		super();
		setHardness(0.0F);
		setStepSound(Block.soundTypeGrass);
		final float var1 = 0.5F;
		final float var2 = 0.015625F;
		setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, var2, 0.5F + var1);
	}

	@Override
	public int getRenderType()
	{
		return RenderType.LILYPAD;
	}

	protected boolean func_149854_a(Block block)
	{
		return Blocks.lava == block;
	}

	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB bb, List list, Entity entity)
	{
		if (entity == null || !(entity instanceof EntityBoat))
		{
			super.addCollisionBoxesToList(world, x, y, z, bb, list, entity);
		}
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		return AxisAlignedBB.getBoundingBox((double)x + getBlockBoundsMinX(), (double)y + getBlockBoundsMinY(), (double)z + getBlockBoundsMinZ(),
			(double)x + getBlockBoundsMaxX(), (double)y + getBlockBoundsMaxY(), (double)z + getBlockBoundsMaxZ());
	}

	public boolean canBlockStay(World world, int x, int y, int z)
	{
		return y >= 0 && y < 256 ? world.getBlock(x, y - 1, z).getMaterial() == Material.lava && world.getBlockMetadata(x, y - 1, z) == 0 : false;
	}
}
