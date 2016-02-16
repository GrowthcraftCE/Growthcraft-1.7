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

import java.util.Random;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.api.core.util.RenderType;
import growthcraft.core.common.block.ICropDataProvider;
import growthcraft.core.integration.AppleCore;
import growthcraft.nether.GrowthCraftNether;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockNetherPepper extends BlockBush implements ICropDataProvider, IGrowable
{
	public static class PepperStages
	{
		public static final int SEEDLING = 0;
		public static final int YOUNG = 1;
		public static final int FULL = 2;
		public static final int FRUIT = 3;
		public static final int COUNT = 4;

		private PepperStages() {}
	}

	@SideOnly(Side.CLIENT)
	protected IIcon[] icons;

	private int minPepperPicked = GrowthCraftNether.getConfig().minPepperPicked;
	private int maxPepperPicked = GrowthCraftNether.getConfig().maxPepperPicked;

	public BlockNetherPepper()
	{
		super(Material.plants);
		setTickRandomly(true);
		setBlockTextureName("grcnether:pepper");
		setBlockName("grcnether.netherPepper");
	}

	private void incrementGrowth(World world, int x, int y, int z, int meta)
	{
		world.setBlockMetadataWithNotify(x, y, z, meta + 1, BlockFlags.SYNC);
		AppleCore.announceGrowthTick(this, world, x, y, z, meta);
	}

	public boolean isFullyGrown(World world, int x, int y, int z)
	{
		return world.getBlockMetadata(x, y, z) >= PepperStages.FRUIT;
	}

	public boolean canGrow(World world, int x, int y, int z)
	{
		return world.getBlockMetadata(x, y, z) < PepperStages.FRUIT;
	}

	/* IGrowable: can this grow anymore */
	@Override
	public boolean func_149851_a(World world, int x, int y, int z, boolean b)
	{
		return !isFullyGrown(world, x, y, z);
	}

	/* IGrowable: does this accept bonemeal */
	@Override
	public boolean func_149852_a(World world, Random random, int x, int y, int z)
	{
		return canGrow(world, x, y, z);
	}

	public boolean onUseBonemeal(World world, int x, int y, int z)
	{
		if (canGrow(world, x, y, z))
		{
			if (!world.isRemote)
			{
				incrementGrowth(world, x, y, z, world.getBlockMetadata(x, y, z));
			}
			return true;
		}
		return false;
	}

	/* IGrowable: Apply bonemeal effect */
	public void func_149853_b(World world, Random random, int x, int y, int z)
	{
		onUseBonemeal(world, x, y, z);
	}

	@Override
	public float getGrowthProgress(IBlockAccess world, int x, int y, int z, int meta)
	{
		return (float)meta / (float)PepperStages.FRUIT;
	}

	protected boolean func_149854_a(Block block)
	{
		return Blocks.soul_sand == block;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		final Event.Result result = AppleCore.validateGrowthTick(this, world, x, y, z, random);
		if (Event.Result.DENY == result) return;

		if (canGrow(world, x, y, z))
		{
			if (Event.Result.ALLOW == result || random.nextInt(10) == 0)
			{
				incrementGrowth(world, x, y, z, world.getBlockMetadata(x, y, z));
			}
		}
		super.updateTick(world, x, y, z, random);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return GrowthCraftNether.items.netherPepper.getItem();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9)
	{
		if (isFullyGrown(world, x, y, z))
		{
			if (!world.isRemote)
			{
				world.setBlockMetadataWithNotify(x, y, z, PepperStages.FULL, BlockFlags.SYNC);
				final int count = minPepperPicked + world.rand.nextInt(maxPepperPicked - minPepperPicked);
				dropBlockAsItem(world, x, y, z, GrowthCraftNether.items.netherPepper.asStack(count));
			}
			return true;
		}
		return false;
	}

	@Override
	public int getRenderType()
	{
		return RenderType.CROPS;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		this.icons = new IIcon[PepperStages.COUNT];

		for (int stage = 0; stage < icons.length; ++stage)
		{
			icons[stage] = reg.registerIcon(getTextureName() + "_stage_" + stage);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if (meta >= PepperStages.SEEDLING && meta <= PepperStages.FRUIT)
		{
			return icons[meta];
		}
		return icons[PepperStages.FRUIT];
	}
}
