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
package growthcraft.cellar.common.block;

import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.common.block.GrcBlockContainer;
import growthcraft.core.common.tileentity.feature.IInteractionObject;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Base class for Cellar machines and the like
 */
public abstract class BlockCellarContainer extends GrcBlockContainer
{
	public BlockCellarContainer(Material material)
	{
		super(material);
	}

	protected boolean openGui(EntityPlayer player, World world, int x, int y, int z)
	{
		final TileEntity te = getTileEntity(world, x, y, z);
		if (te instanceof IInteractionObject)
		{
			player.openGui(GrowthCraftCellar.instance, 0, world, x, y, z);
			return true;
		}
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float par7, float par8, float par9)
	{
		if (super.onBlockActivated(world, x, y, z, player, meta, par7, par8, par9)) return true;
		return !player.isSneaking() && openGui(player, world, x, y, z);
	}
}
