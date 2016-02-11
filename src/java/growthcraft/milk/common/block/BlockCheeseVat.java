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

import java.util.List;

import growthcraft.core.common.block.GrcBlockContainer;
import growthcraft.milk.client.render.RenderCheeseVat;
import growthcraft.milk.common.tileentity.TileEntityCheeseVat;
import growthcraft.milk.GrowthCraftMilk;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCheeseVat extends GrcBlockContainer
{
	public BlockCheeseVat()
	{
		super(Material.iron);
		setBlockName("grcmilk.CheeseVat");
		setCreativeTab(GrowthCraftMilk.creativeTab);
		setTileEntityType(TileEntityCheeseVat.class);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float par7, float par8, float par9)
	{
		if (super.onBlockActivated(world, x, y, z, player, meta, par7, par8, par9)) return true;
		if (!player.isSneaking())
		{
			final TileEntityCheeseVat cheeseVat = getTileEntity(world, x, y, z);
			if (cheeseVat != null)
			{
				//cheeseVat.doWork();
				return true;
			}
		}
		return false;
	}

	@Override
	public void setBlockBoundsForItemRender()
	{
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axis, List list, Entity entity)
	{
		final float unit = 1f / 16f;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, unit, 1.0F);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);

		this.setBlockBounds(0.0F, 0.0F, 0.0F, unit, 1.0F, 1.0F);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);

		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, unit);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);

		this.setBlockBounds(1.0F - unit, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);

		this.setBlockBounds(0.0F, 0.0F, 1.0F - unit, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);

		this.setBlockBoundsForItemRender();
	}

	@Override
	public int getRenderType()
	{
		return RenderCheeseVat.RENDER_ID;
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
