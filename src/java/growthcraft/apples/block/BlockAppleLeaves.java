package growthcraft.apples.block;

import java.util.ArrayList;
import java.util.Random;

import growthcraft.apples.GrowthCraftApples;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

public class BlockAppleLeaves extends BlockLeavesBase implements IShearable
{
	//Constants
	private final int growth = GrowthCraftApples.appleLeaves_growth;

	@SideOnly(Side.CLIENT)
	public static IIcon[] tex;
	//public static Boolean graphicFlag;
	int[] adjacentTreeBlocks;

	public BlockAppleLeaves()
	{
		super(Material.leaves, false);
		this.setTickRandomly(true);
		this.setHardness(0.2F);
		this.setLightOpacity(1);
		this.setStepSound(soundTypeGrass);
		this.setBlockName("grc.appleLeaves");
		this.setCreativeTab(null);
	}

	/************
	 * TICK
	 ************/
	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		if (!world.isRemote)
		{
			int meta = world.getBlockMetadata(x, y, z);

			if ((meta & 4) == 0)
			{
				// Spawn Apple
				if (world.rand.nextInt(this.growth) == 0)
				{
					if (world.isAirBlock(x, y - 1, z))
					{
						world.setBlock(x, y - 1, z, GrowthCraftApples.appleBlock);
					}
				}
			}

			if ((meta & 8) != 0 && (meta & 4) == 0)
			{
				byte b0 = 4;
				int i1 = b0 + 1;
				byte b1 = 32;
				int j1 = b1 * b1;
				int k1 = b1 / 2;

				if (this.adjacentTreeBlocks == null)
				{
					this.adjacentTreeBlocks = new int[b1 * b1 * b1];
				}

				int l1;

				if (world.checkChunksExist(x - i1, y - i1, z - i1, x + i1, y + i1, z + i1))
				{
					int i2;
					int j2;

					for (l1 = -b0; l1 <= b0; ++l1)
					{
						for (i2 = -b0; i2 <= b0; ++i2)
						{
							for (j2 = -b0; j2 <= b0; ++j2)
							{
								Block block = world.getBlock(x + l1, y + i2, z + j2);

								if (!block.canSustainLeaves(world, x + l1, y + i2, z + j2))
								{
									if (block.isLeaves(world, x + l1, y + i2, z + j2))
									{
										this.adjacentTreeBlocks[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -2;
									}
									else
									{
										this.adjacentTreeBlocks[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -1;
									}
								}
								else
								{
									this.adjacentTreeBlocks[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = 0;
								}
							}
						}
					}

					for (l1 = 1; l1 <= 4; ++l1)
					{
						for (i2 = -b0; i2 <= b0; ++i2)
						{
							for (j2 = -b0; j2 <= b0; ++j2)
							{
								for (int k2 = -b0; k2 <= b0; ++k2)
								{
									if (this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1] == l1 - 1)
									{
										if (this.adjacentTreeBlocks[(i2 + k1 - 1) * j1 + (j2 + k1) * b1 + k2 + k1] == -2)
										{
											this.adjacentTreeBlocks[(i2 + k1 - 1) * j1 + (j2 + k1) * b1 + k2 + k1] = l1;
										}

										if (this.adjacentTreeBlocks[(i2 + k1 + 1) * j1 + (j2 + k1) * b1 + k2 + k1] == -2)
										{
											this.adjacentTreeBlocks[(i2 + k1 + 1) * j1 + (j2 + k1) * b1 + k2 + k1] = l1;
										}

										if (this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1 - 1) * b1 + k2 + k1] == -2)
										{
											this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1 - 1) * b1 + k2 + k1] = l1;
										}

										if (this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1 + 1) * b1 + k2 + k1] == -2)
										{
											this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1 + 1) * b1 + k2 + k1] = l1;
										}

										if (this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1) * b1 + (k2 + k1 - 1)] == -2)
										{
											this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1) * b1 + (k2 + k1 - 1)] = l1;
										}

										if (this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1 + 1] == -2)
										{
											this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1 + 1] = l1;
										}
									}
								}
							}
						}
					}
				}

				l1 = this.adjacentTreeBlocks[k1 * j1 + k1 * b1 + k1];

				if (l1 >= 0)
				{
					world.setBlockMetadataWithNotify(x, y, z, meta & -9, 4);
				}
				else
				{
					this.removeLeaves(world, x, y, z);
				}
			}
		}
	}

	private void removeLeaves(World world, int x, int y, int z)
	{
		this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
		world.setBlockToAir(x, y, z);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random)
	{
		super.randomDisplayTick(world, x, y, z, random);
		if (world.canLightningStrikeAt(x, y + 1, z) && !World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && random.nextInt(15) == 1)
		{
			double d0 = (double)((float)x + random.nextFloat());
			double d1 = (double)y - 0.05D;
			double d2 = (double)((float)z + random.nextFloat());
			world.spawnParticle("dripWater", d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	/************
	 * TRIGGERS
	 ************/
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int par6)
	{
		byte b0 = 1;
		int i1 = b0 + 1;

		if (world.checkChunksExist(x - i1, y - i1, z - i1, x + i1, y + i1, z + i1))
		{
			for (int j1 = -b0; j1 <= b0; ++j1)
			{
				for (int k1 = -b0; k1 <= b0; ++k1)
				{
					for (int l1 = -b0; l1 <= b0; ++l1)
					{
						Block block2 = world.getBlock(x + j1, y + k1, z + l1);
						if (block2.isLeaves(world, x + j1, y + k1, z + l1))
						{
							block2.beginLeavesDecay(world, x + j1, y + k1, z + l1);
						}
					}
				}
			}
		}
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int par6)
	{
		super.harvestBlock(world, player, x, y, z, par6);
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return Item.getItemFromBlock(Blocks.leaves);
	}

	@Override
	public void beginLeavesDecay(World world, int x, int y, int z)
	{
		world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) | 8, 4);
	}

	@Override
	public boolean isLeaves(IBlockAccess world, int x, int y, int z)
	{
		return true;
	}

	@Override
	public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata)
	{
		return false;
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int meta, Random random, int par3)
	{
		return Item.getItemFromBlock(GrowthCraftApples.appleSapling);
	}

	@Override
	public int quantityDropped(Random random)
	{
		return random.nextInt(20) == 0 ? 1 : 0;
	}

	@Override
	public void dropBlockAsItemWithChance(World world, int x, int y, int z, int meta, float par6, int fortune)
	{
		if (!world.isRemote)
		{
			int random = 20;

			if (fortune > 0)
			{
				random -= 2 << fortune;

				if (random < 10)
				{
					random = 10;
				}
			}

			if (world.rand.nextInt(random) == 0)
			{
				this.dropBlockAsItem(world, x, y, z, new ItemStack(this.getItemDropped(meta, world.rand, fortune), 1, 0));
			}
		}
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		tex = new IIcon[2];

		tex[0] = reg.registerIcon("leaves_oak");
		tex[1] = reg.registerIcon("leaves_oak_opaque");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		//this.graphicFlag = ((BlockLeaves)Block.blocksList[Block.leaves.blockID]).graphicsLevel;
		//return this.graphicFlag ? this.tex[0] : this.tex[1];
		return this.tex[this.isOpaqueCube() ? 1 : 0];
	}

	/************
	 * RENDERS
	 ************/
	@Override
	public boolean isOpaqueCube()
	{
		//this.graphicFlag = ((BlockLeaves)Block.blocksList[Block.leaves.blockID]).graphicsLevel;
		//return !this.graphicFlag;
		return Blocks.leaves.isOpaqueCube();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
	{
		//int id = world.getBlockId(x, y, z);
		//this.graphicFlag = ((BlockLeaves)Block.blocksList[Block.leaves.blockID]).graphicsLevel;
		//return !this.graphicFlag && id == this.blockID ? false : super.shouldSideBeRendered(world, x, y, z, side);
		return true;
	}

	/************
	 * COLORS
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockColor()
	{
		double d0 = 0.5D;
		double d1 = 1.0D;
		return ColorizerFoliage.getFoliageColor(d0, d1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int meta)
	{
		return ColorizerFoliage.getFoliageColorBasic();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);

		int r = 0;
		int g = 0;
		int b = 0;

		for (int l1 = -1; l1 <= 1; ++l1)
		{
			for (int i2 = -1; i2 <= 1; ++i2)
			{
				int j2 = world.getBiomeGenForCoords(x + i2, z + l1).getBiomeFoliageColor(x + i2, y, z + l1);
				r += (j2 & 16711680) >> 16;
			g += (j2 & 65280) >> 8;
				b += j2 & 255;
			}
		}

		return (r / 9 & 255) << 16 | (g / 9 & 255) << 8 | b / 9 & 255;
	}

	/************
	 * SHEARS
	 ************/
	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z)
	{
		return true;
	}

	@Override
	public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(Blocks.leaves, 1, world.getBlockMetadata(x, y, z) & 3));
		return ret;
	}
}
