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

import java.util.List;
import java.util.Collection;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.api.core.util.Point3;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CropBasicGrowthCraft extends AbstractForestryCrop
{
	private final Block block;
	private final int meta;
	private final boolean isRice;
	private final boolean isGrape;

	public CropBasicGrowthCraft(World pworld, Block pblock, int pmeta, Point3 pos, boolean pisRice, boolean pisGrape)
	{
		super(pworld, pos);
		this.block = pblock;
		this.meta = pmeta;
		this.isRice = pisRice;
		this.isGrape = pisGrape;
	}

	@Override
	protected boolean isCrop(Point3 pos)
	{
		return getBlock(pos) == block && getBlockMeta(pos) == meta;
	}

	@Override
	protected Collection<ItemStack> harvestBlock(Point3 pos)
	{
		final List<ItemStack> harvest = block.getDrops(world, pos.x, pos.y, pos.z, meta, 0);
		if (harvest.size() > 1)
		{
			// Hops have rope as first drop.
			harvest.remove(0);
		}

		// Need to replace this later
		//Proxies.common.addBlockDestroyEffects(world, pos.x, pos.y, pos.z, block, 0);

		if (isGrape)
		{
			world.setBlockToAir(pos.x, pos.y, pos.z);
		}
		else
		{
			world.setBlockMetadataWithNotify(pos.x, pos.y, pos.z, 0, BlockFlags.SYNC);
		}

		if (isRice) {
			world.setBlockMetadataWithNotify(pos.x, pos.y - 1, pos.z, 7, BlockFlags.SYNC);
		}

		return harvest;
	}

	@Override
	public String toString()
	{
		return String.format("CropBasicGrowthCraft [ position: [ %s ]; block: %s; meta: %s ]", position.toString(), block.getUnlocalizedName(), meta);
	}
}
