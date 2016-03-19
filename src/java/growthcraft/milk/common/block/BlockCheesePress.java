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

import growthcraft.api.core.util.BlockFlags;
import growthcraft.core.common.block.GrcBlockContainer;
import growthcraft.milk.client.render.RenderCheesePress;
import growthcraft.milk.common.tileentity.TileEntityCheesePress;
import growthcraft.milk.GrowthCraftMilk;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockCheesePress extends GrcBlockContainer
{
	public BlockCheesePress()
	{
		super(Material.wood);
		setResistance(5.0F);
		setHardness(2.0F);
		setBlockName("grcmilk.CheesePress");
		setCreativeTab(GrowthCraftMilk.creativeTab);
		setTileEntityType(TileEntityCheesePress.class);
		setBlockTextureName("grcmilk:cheese_press");
	}

	public boolean isRotatable(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		return true;
	}

	public void doRotateBlock(World world, int x, int y, int z, ForgeDirection side)
	{
		world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) ^ 1, BlockFlags.SYNC);
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		super.onBlockAdded(world, x, y, z);
		this.setDefaultDirection(world, x, y, z);
	}

	private void setDefaultDirection(World world, int x, int y, int z)
	{
		if (!world.isRemote)
		{
			final Block block = world.getBlock(x, y, z - 1);
			final Block block1 = world.getBlock(x, y, z + 1);
			final Block block2 = world.getBlock(x - 1, y, z);
			final Block block3 = world.getBlock(x + 1, y, z);
			byte meta = 3;

			if (block.func_149730_j() && !block1.func_149730_j())
			{
				meta = 3;
			}

			if (block1.func_149730_j() && !block.func_149730_j())
			{
				meta = 2;
			}

			if (block2.func_149730_j() && !block3.func_149730_j())
			{
				meta = 5;
			}

			if (block3.func_149730_j() && !block2.func_149730_j())
			{
				meta = 4;
			}

			world.setBlockMetadataWithNotify(x, y, z, meta, BlockFlags.UPDATE_AND_SYNC);
		}
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
	{
		final int a = MathHelper.floor_double((entity.rotationYaw * 4.0D / 360.0D) + 0.5D) & 3;
		if (a == 0 || a == 2)
		{
			world.setBlockMetadataWithNotify(x, y, z, 0, BlockFlags.SYNC);
		}
		else if (a == 1 || a == 3)
		{
			world.setBlockMetadataWithNotify(x, y, z, 1, BlockFlags.SYNC);
		}
		setupCustomDisplayName(world, x, y, z, stack);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float par7, float par8, float par9)
	{
		if (super.onBlockActivated(world, x, y, z, player, meta, par7, par8, par9)) return true;
		if (!player.isSneaking())
		{
			final TileEntityCheesePress cheesePress = getTileEntity(world, x, y, z);
			if (cheesePress != null)
			{
				cheesePress.toggle();
				world.playSoundEffect((double)x, (double)y, (double)z, "random.wood_click", 0.3f, 0.5f);
				return true;
			}
		}
		return false;
	}

	@Override
	public int getRenderType()
	{
		return RenderCheesePress.RENDER_ID;
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
