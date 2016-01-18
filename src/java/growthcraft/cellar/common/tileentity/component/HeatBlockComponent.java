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
package growthcraft.cellar.common.tileentity.component;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.heatsource.IHeatSourceBlock;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Component for handling heat source blocks for a tile entity
 */
public class HeatBlockComponent
{
	private TileEntity tileEntity;
	private ForgeDirection sourceDir = ForgeDirection.DOWN;

	public HeatBlockComponent(TileEntity te)
	{
		this.tileEntity = te;
	}

	private World getWorld()
	{
		return tileEntity.getWorldObj();
	}

	public float getHeatMultiplier()
	{
		final int x = tileEntity.xCoord + sourceDir.offsetX;
		final int y = tileEntity.yCoord + sourceDir.offsetY;
		final int z = tileEntity.zCoord + sourceDir.offsetZ;

		final Block block = getWorld().getBlock(x, y, z);
		final int meta = getWorld().getBlockMetadata(x, y, z);

		final IHeatSourceBlock heatSource = CellarRegistry.instance().heatSource().getHeatSource(block, meta);

		if (heatSource != null) return heatSource.getHeat(getWorld(), x, y, z);
		return 0.0f;
	}
}
