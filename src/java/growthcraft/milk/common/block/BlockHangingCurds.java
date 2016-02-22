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
package growthcraft.milk.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import growthcraft.api.core.util.BBox;
import growthcraft.api.core.util.BlockFlags;
import growthcraft.api.core.util.RenderType;
import growthcraft.core.common.block.GrcBlockContainer;
import growthcraft.core.util.BlockCheck;
import growthcraft.milk.common.item.EnumCheeseType;
import growthcraft.milk.common.item.ItemBlockHangingCurds;
import growthcraft.milk.common.tileentity.TileEntityHangingCurds;
import growthcraft.milk.GrowthCraftMilk;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockHangingCurds extends GrcBlockContainer
{
	public BlockHangingCurds()
	{
		// placeholder
		super(Material.wood);
		setTickRandomly(true);
		setBlockName("grcmilk.HangingCurds");
		setCreativeTab(GrowthCraftMilk.creativeTab);
		setTileEntityType(TileEntityHangingCurds.class);
		final BBox bb = BBox.newCube(4f, 0f, 4f, 8f, 16f, 8f).scale(1f / 16f);
		setBlockBounds(bb.x0(), bb.y0(), bb.z0(), bb.x1(), bb.y1(), bb.z1());
	}

	public void fellBlockAsItem(World world, int x, int y, int z)
	{
		dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
		world.setBlockToAir(x, y, z);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float par7, float par8, float par9)
	{
		if (super.onBlockActivated(world, x, y, z, player, meta, par7, par8, par9)) return true;
		if (!player.isSneaking())
		{
			final TileEntityHangingCurds hangingCurd = getTileEntity(world, x, y, z);
			if (hangingCurd != null)
			{
				if (hangingCurd.isDried())
				{
					fellBlockAsItem(world, x, y, z);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack)
	{
		final Item item = stack.getItem();
		if (item instanceof ItemBlockHangingCurds)
		{
			final ItemBlockHangingCurds cheeseBlock = (ItemBlockHangingCurds)item;
			final TileEntityHangingCurds teHangingCurds = getTileEntity(world, x, y, z);
			if (teHangingCurds != null)
			{
				teHangingCurds.readFromNBTForItem(cheeseBlock.getTileData(stack));
			}
		}
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		if (item instanceof ItemBlockHangingCurds)
		{
			final ItemBlockHangingCurds ib = (ItemBlockHangingCurds)item;
			for (EnumCheeseType cheese : EnumCheeseType.VALUES)
			{
				if (cheese.hasCurdBlock())
				{
					final ItemStack stack = new ItemStack(item, 1, cheese.meta);
					ib.getTileData(stack);
					list.add(stack);
				}
			}
		}
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player)
	{
		final TileEntityHangingCurds teHangingCurds = getTileEntity(world, x, y, z);
		if (teHangingCurds != null)
		{
			return teHangingCurds.asItemStack();
		}
		return super.getPickBlock(target, world, x, y, z, player);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		final ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		final TileEntityHangingCurds teHangingCurds = getTileEntity(world, x, y, z);
		if (teHangingCurds != null)
		{
			teHangingCurds.populateDrops(ret);
		}
		return ret;
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		return BlockCheck.isBlockPlacableOnSide(world, x, y + 1, z, ForgeDirection.DOWN);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		return super.canPlaceBlockAt(world, x, y, z) && canBlockStay(world, x, y, z);
	}


	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
	{
		if (!this.canBlockStay(world, x, y, z))
		{
			fellBlockAsItem(world, x, y, z);
		}
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		super.updateTick(world, x, y, z, random);
		if (!world.isRemote)
		{
			if (!canBlockStay(world, x, y, z))
			{
				dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
				world.setBlock(x, y, z, Blocks.air, 0, BlockFlags.UPDATE_AND_SYNC);
			}
		}
	}

	@Override
	public int getRenderType()
	{
		return RenderType.NONE;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
	{
		return true;
	}
}
