/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
/**
 * NOTICE:
 *   This file has been modified from its original source for use in
 *   Growthcraft CE.
 */
package growthcraft.core.integration.forestry;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.api.core.util.Point3;
import growthcraft.core.util.ItemUtils;

import forestry.api.farming.ICrop;
import forestry.api.farming.IFarmable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * This was taken from Forestry
 */
public class FarmableBasicGrowthCraft implements IFarmable
{
	private final Block block;
	private final int matureMeta;
	private final boolean isRice;
	private final boolean isGrape;

	public FarmableBasicGrowthCraft(Block pblock, int pmatureMeta, boolean pisRice, boolean pisGrape)
	{
		this.block = pblock;
		this.matureMeta = pmatureMeta;
		this.isRice = pisRice;
		this.isGrape = pisGrape;
	}

	@Override
	public boolean isSaplingAt(World world, int x, int y, int z)
	{
		return world.getBlock(x, y, z) == block;
	}

	@Override
	public ICrop getCropAt(World world, int x, int y, int z)
	{
		if (world.getBlock(x, y, z) != block) return null;
		if (world.getBlockMetadata(x, y, z) != matureMeta) return null;
		return new CropBasicGrowthCraft(world, block, matureMeta, new Point3(x, y, z), isRice, isGrape);
	}

	@Override
	public boolean isGermling(ItemStack stack)
	{
		return ItemUtils.equals(block, stack);
	}

	@Override
	public boolean plantSaplingAt(EntityPlayer player, ItemStack germling, World world, int x, int y, int z)
	{
		return world.setBlock(x, y, z, block, 0, BlockFlags.SYNC);
	}

	@Override
	public boolean isWindfall(ItemStack stack)
	{
		return false;
	}
}
