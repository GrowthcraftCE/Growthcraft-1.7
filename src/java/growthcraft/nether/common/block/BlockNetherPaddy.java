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

import javax.annotation.Nonnull;

import growthcraft.core.common.block.BlockPaddyBase;
import growthcraft.api.core.util.BlockFlags;
import growthcraft.nether.GrowthCraftNether;
import growthcraft.nether.util.NetherBlockCheck;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class BlockNetherPaddy extends BlockPaddyBase
{
	@SideOnly(Side.CLIENT)
	protected IIcon[] icons;

	private final int paddyFieldMax = GrowthCraftNether.getConfig().paddyFieldMax;
	private final boolean filledPaddy;

	public BlockNetherPaddy(boolean filled)
	{
		super(Material.sand);
		setHardness(0.5F);
		setBlockName("grcnether.netherPaddyField");
		setCreativeTab(null);
		this.filledPaddy = filled;
		if (filledPaddy)
		{
			setLightLevel(1.0F);
		}
		else
		{
			setStepSound(soundTypeSand);
		}
	}

	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		if (world.isRemote) return;

		entity.motionX *= 0.4D;
		entity.motionZ *= 0.4D;

		// set fire to the entity if they step into a filled lava paddy
		if (filledPaddy) entity.setFire(15);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random)
	{
		super.randomDisplayTick(world, x, y, z, random);
		if (filledPaddy)
		{
			if (world.getBlock(x, y + 1, z).getMaterial() == Material.air && !world.getBlock(x, y + 1, z).isOpaqueCube())
			{
				if (random.nextInt(100) == 0)
				{
					final double px = (double)((float)x + random.nextFloat());
					final double py = (double)y + getBlockBoundsMaxY();
					final double pz = (double)((float)z + random.nextFloat());
					world.spawnParticle("lava", px, py, pz, 0.0D, 0.0D, 0.0D);
					world.playSound(px, py, pz, "liquid.lavapop", 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
				}

				if (random.nextInt(200) == 0)
				{
					world.playSound((double)x, (double)y, (double)z, "liquid.lava", 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
				}
			}
		}
	}

	/**
	 * Returns the fluid block used to fill this paddy
	 *
	 * @return fluid block
	 */
	@Override
	@Nonnull public Block getFluidBlock()
	{
		return Blocks.lava;
	}

	@Override
	@Nonnull public Fluid getFillingFluid()
	{
		return FluidRegistry.LAVA;
	}

	@Override
	public int getMaxPaddyMeta(IBlockAccess world, int x, int y, int z)
	{
		return paddyFieldMax;
	}

	@Override
	public boolean isBelowFillingFluid(IBlockAccess world, int x, int y, int z)
	{
		return world.getBlock(x, y + 1, z).getMaterial() == Material.lava;
	}

	@Override
	public void drainPaddy(World world, int x, int y, int z)
	{
		final int meta = world.getBlockMetadata(x, y, z);
		if (meta > 1)
		{
			world.setBlockMetadataWithNotify(x, y, z, meta - 1, BlockFlags.SYNC);
		}
		else
		{
			final Block targetBlock = GrowthCraftNether.blocks.netherPaddyField.getBlock();
			if (this != targetBlock)
			{
				world.setBlock(x, y, z, targetBlock, 0, BlockFlags.SYNC);
			}
		}
	}

	@Override
	public void fillPaddy(World world, int x, int y, int z)
	{
		final Block targetBlock = GrowthCraftNether.blocks.netherPaddyFieldFilled.getBlock();
		if (this != targetBlock)
		{
			world.setBlock(x, y, z, targetBlock, getMaxPaddyMeta(world, x, y, z), BlockFlags.SYNC);
		}
		else
		{
			world.setBlockMetadataWithNotify(x, y, z, getMaxPaddyMeta(world, x, y, z), BlockFlags.SYNC);
		}
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return Item.getItemFromBlock(Blocks.soul_sand);
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int meta, Random random, int par3)
	{
		return Item.getItemFromBlock(Blocks.soul_sand);
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		icons = new IIcon[3];

		icons[0] = reg.registerIcon("soul_sand");
		icons[1] = reg.registerIcon("grcnether:soul_sand_paddy_dry");
		icons[2] = reg.registerIcon("grcnether:soul_sand_paddy_wet");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if (side == 1)
		{
			if (meta == 0)
			{
				return icons[1];
			}
			else
			{
				return icons[2];
			}
		}
		return icons[0];
	}

	public boolean canConnectPaddyTo(IBlockAccess world, int i, int j, int k, int m)
	{
		if (m > 0)
		{
			m = 1;
		}

		int meta = world.getBlockMetadata(i, j, k);

		if (meta > 0)
		{
			meta = 1;
		}

		return NetherBlockCheck.isPaddy(world.getBlock(i, j, k)) && meta == m;
	}
}
