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

import java.util.Random;

import growthcraft.api.core.util.BBox;
import growthcraft.api.core.util.BlockFlags;
import growthcraft.api.core.util.CuboidI;
import growthcraft.core.integration.AppleCore;
import growthcraft.core.logic.FlowerSpread;
import growthcraft.core.logic.ISpreadablePlant;
import growthcraft.milk.GrowthCraftMilk;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

public class BlockThistle extends BlockBush implements ISpreadablePlant, IGrowable
{
	public static class ThistleStage
	{
		public static final int SEEDLING = 0;
		public static final int BUD = 1;
		public static final int GROWN = 2;
		public static final int FLOWER = 3;

		private ThistleStage() {}
	}

	private FlowerSpread spreadLogic;

	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	public BlockThistle()
	{
		super(Material.plants);
		setTickRandomly(true);
		setBlockName("grcmilk.Thistle");
		setStepSound(soundTypeGrass);
		setCreativeTab(GrowthCraftMilk.creativeTab);
		final BBox bb = BBox.newCube(2f, 0f, 2f, 12f, 16f, 12f).scale(1f / 16f);
		setBlockBounds(bb.x0(), bb.y0(), bb.z0(), bb.x1(), bb.y1(), bb.z1());
		this.spreadLogic = new FlowerSpread(new CuboidI(-1, -1, -1, 2, 2, 2));
	}

	@Override
	public boolean canSpreadTo(World world, int x, int y, int z)
	{
		if (world.isAirBlock(x, y, z) && canBlockStay(world, x, y, z))
		{
			return true;
		}
		return false;
	}

	private void runSpread(World world, int x, int y, int z, Random random)
	{
		spreadLogic.run(this, 0, world, x, y, z, random);
	}

	private void incrementGrowth(World world, int x, int y, int z, int meta)
	{
		world.setBlockMetadataWithNotify(x, y, z, meta + 1, BlockFlags.SYNC);
		AppleCore.announceGrowthTick(this, world, x, y, z, meta);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		super.updateTick(world, x, y, z, random);
		if (!world.isRemote)
		{
			final int meta = world.getBlockMetadata(x, y, z);
			if (meta >= ThistleStage.FLOWER)
			{
				final int spreadChance = GrowthCraftMilk.getConfig().thistleSpreadChance;
				if (spreadChance > 0)
				{
					if (random.nextInt(spreadChance) == 0)
					{
						runSpread(world, x, y, z, random);
					}
				}
			}
			else
			{
				final int growthChance = GrowthCraftMilk.getConfig().thistleGrowthChance;
				final Event.Result allowGrowthResult = AppleCore.validateGrowthTick(this, world, x, y, z, random);
				if (allowGrowthResult == Event.Result.DENY)
				{
					return;
				}

				if (allowGrowthResult == Event.Result.ALLOW || (growthChance > 0 && random.nextInt(growthChance) == 0))
				{
					if (meta < ThistleStage.FLOWER)
					{
						incrementGrowth(world, x, y, z, meta);
					}
				}
			}
		}
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
	{
		return EnumPlantType.Plains;
	}

	/* IGrowable interface
	 *	Check: http://www.minecraftforge.net/forum/index.php?topic=22571.0
	 *	if you have no idea what this stuff means
	 */

	/* Can this accept bonemeal? */
	@Override
	public boolean func_149851_a(World world, int x, int y, int z, boolean isClient)
	{
		return true;
	}

	/* SideOnly(Side.SERVER) Can this apply bonemeal effect? */
	@Override
	public boolean func_149852_a(World world, Random random, int x, int y, int z)
	{
		return true;
	}

	@Override
	public Item getItemDropped(int meta, Random random, int fortune)
	{
		if (meta < ThistleStage.FLOWER)
		{
			if (GrowthCraftMilk.items.seedThistle != null)
			{
				return GrowthCraftMilk.items.seedThistle.getItem();
			}
			return null;
		}
		return super.getItemDropped(meta, random, fortune);
	}

	/* Apply bonemeal effect */
	@Override
	public void func_149853_b(World world, Random random, int x, int y, int z)
	{
		final int meta = world.getBlockMetadata(x, y, z);
		if (meta < ThistleStage.FLOWER)
		{
			final int growthChance = GrowthCraftMilk.getConfig().thistleGrowthChance;
			if (growthChance > 0)
			{
				if (random.nextInt(growthChance) != 0) return;
			}
			incrementGrowth(world, x, y, z, meta);
		}
		else
		{
			runSpread(world, x, y, z, random);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		this.icons = new IIcon[4];

		icons[0] = reg.registerIcon("grcmilk:thistle/stage1");
		icons[1] = reg.registerIcon("grcmilk:thistle/stage2");
		icons[2] = reg.registerIcon("grcmilk:thistle/stage3");
		icons[3] = reg.registerIcon("grcmilk:thistle/stage4");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if (meta < 0 || meta >= icons.length)
		{
			return icons[3];
		}
		return this.icons[meta];
	}
}
