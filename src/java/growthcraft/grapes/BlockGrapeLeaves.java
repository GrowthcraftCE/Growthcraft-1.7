package growthcraft.grapes;

import growthcraft.core.GrowthCraftCore;
import growthcraft.core.block.IBlockRope;

import java.util.Random;

import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockGrapeLeaves extends BlockLeavesBase implements IBlockRope 
{
	//Constants
	private final int growth = GrowthCraftGrapes.grapeLeaves_growth;
	private final int growth2 = GrowthCraftGrapes.grapeLeaves_growth2;

	@SideOnly(Side.CLIENT)
	public static IIcon[] tex;
	//public static Boolean graphicFlag;

	protected BlockGrapeLeaves() 
	{
		super(Material.leaves, false);
		this.setTickRandomly(true);
		this.setHardness(0.2F);
		this.setLightOpacity(1);
		this.setStepSound(soundTypeGrass);
		this.setBlockName("grc.grapeLeaves");
		this.setCreativeTab(null);
	}

	/************
	 * TICK
	 ************/
	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		if (!this.canBlockStay(world, x, y, z))
		{
			world.setBlock(x, y, z, GrowthCraftCore.ropeBlock);
		}
		else
		{
			grow(world, x, y, z, random);
		}
	}

	private void grow(World world, int x, int y, int z, Random random)
	{
		boolean flag = !checkValidity(world, x, y, z - 1);
		boolean flag1 = !checkValidity(world, x, y, z + 1);
		boolean flag2 = !checkValidity(world, x - 1, y, z);
		boolean flag3 = !checkValidity(world, x + 1, y, z);

		if (flag1 && flag2 && flag3 && flag)
		{
			if (world.isAirBlock(x, y - 1, z) && (world.rand.nextInt(this.growth2) == 0))
			{
				world.setBlock(x, y - 1, z, GrowthCraftGrapes.grapeBlock);
			}
			else
			{
				return;
			}
		}
		else
		{
			if ((world.rand.nextInt(this.growth) == 0))
			{
				int r = random.nextInt(4);

				if (r == 0 && checkValidity(world, x, y, z - 1))
				{
					world.setBlock(x, y, z - 1, this);
					return;
				}

				if (r == 1 && checkValidity(world, x, y, z + 1))
				{
					world.setBlock(x, y, z + 1, this);
					return;
				}

				if (r == 2 && checkValidity(world, x - 1, y, z))
				{
					world.setBlock(x - 1, y, z, this);
					return;
				}

				if (r == 3 && checkValidity(world, x + 1, y, z))
				{
					world.setBlock(x + 1, y, z, this);
					return;
				}
			}
		}		
	}

	private boolean checkValidity(World world, int x, int y, int z)
	{
		if (world.getBlock(x, y, z) == GrowthCraftCore.ropeBlock)
		{
			boolean flag = world.getBlock(x + 1, y, z) == this;
			boolean flag1 = world.getBlock(x - 1, y, z) == this;
			boolean flag2 = world.getBlock(x, y, z + 1) == this;
			boolean flag3 = world.getBlock(x, y, z - 1) == this;

			if (!flag && !flag1 && !flag2 && !flag3) return false;

			if (flag && isTrunk(world, x + 1, y - 1, z)) return true;
			if (flag1 && isTrunk(world, x - 1, y - 1, z)) return true;
			if (flag2 && isTrunk(world, x, y - 1, z + 1)) return true;
			if (flag3 && isTrunk(world, x, y - 1, z - 1)) return true;

			if (flag && isTrunk(world, x + 2, y - 1, z)) return true;
			if (flag1 && isTrunk(world, x - 2, y - 1, z)) return true;
			if (flag2 && isTrunk(world, x, y - 1, z + 2)) return true;
			if (flag3 && isTrunk(world, x, y - 1, z - 2)) return true;
		}
		return false;
	}

	private boolean isTrunk(World world, int x, int y, int z)
	{
		return world.getBlock(x, y, z) == GrowthCraftGrapes.grapeVine1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random)
	{
		if (world.canLightningStrikeAt(x, y + 1, z) && !World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && random.nextInt(15) == 1)
		{
			double d0 = (double)((float)x + random.nextFloat());
			double d1 = (double)y - 0.05D;
			double d2 = (double)((float)z + random.nextFloat());
			world.spawnParticle("dripWater", d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	/************
	 * CONDITIONS
	 ************/
	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		if (this.isSupportedByTrunk(world, x, y, z))
		{
			return true;
		}
		else
		{
			int loop = 1;

			while (loop < 4)
			{
				if (world.getBlock(x, y, z - loop) != GrowthCraftGrapes.grapeLeaves)
				{
					break;
				}
				else
				{
					if (this.isSupportedByTrunk(world, x, y, z - loop))
					{
						return true;
					}
					else
					{
						loop++;
					}
				}
			}

			loop = 1;

			while (loop < 4)
			{
				if (world.getBlock(x, y, z + loop) != GrowthCraftGrapes.grapeLeaves)
				{
					break;
				}
				else
				{
					if (this.isSupportedByTrunk(world, x, y, z + loop))
					{
						return true;
					}
					else
					{
						loop++;
					}
				}
			}

			loop = 1;

			while (loop < 4)
			{
				if (world.getBlock(x - loop, y, z) != GrowthCraftGrapes.grapeLeaves)
				{
					break;
				}
				else
				{
					if (this.isSupportedByTrunk(world, x - loop, y, z))
					{
						return true;
					}
					else
					{
						loop++;
					}
				}
			}

			loop = 1;

			while (loop < 4)
			{
				if (world.getBlock(x + loop, y, z) != GrowthCraftGrapes.grapeLeaves)
				{
					break;
				}
				else
				{
					if (this.isSupportedByTrunk(world, x + loop, y, z))
					{
						return true;
					}
					else
					{
						loop++;
					}
				}
			}

			return false;
		}
	}

	private boolean isSupportedByTrunk(World world, int x, int y, int z)
	{
		return world.getBlock(x, y - 1, z) == GrowthCraftGrapes.grapeVine1;
	}

	/************
	 * STUFF
	 ************/	
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return GrowthCraftGrapes.grapeSeeds;
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

	@Override
	public boolean canConnectRopeTo(IBlockAccess world, int x, int y, int z) 
	{
		if (world.getBlock(x, y, z) instanceof IBlockRope)
		{
			return true;
		}
		return false;
	}

	/*public boolean canConnectVineTo(World world, int x, int y, int z)
	{
		return world.getBlockId(x, y, z) == this.blockID;
	}*/

	/************
	 * DROPS
	 ************/	
	@Override
	public Item getItemDropped(int par1, Random par2Random, int par3)
	{
		return GrowthCraftCore.rope;
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
		tex = new IIcon[4];

		tex[0] = reg.registerIcon("grcgrapes:leaves");
		tex[1] = reg.registerIcon("grcgrapes:leaves_opaque");
		tex[2] = reg.registerIcon("grccore:rope_1");
		tex[3] = reg.registerIcon("grcgrapes:leaves_half");
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
	public int getRenderType()
	{
		return RenderGrapeLeaves.id;
	}

	@Override
	public boolean isOpaqueCube()
	{
		//this.graphicFlag = ((BlockLeaves)Block.blocksList[Block.leaves.blockID]).graphicsLevel;
		//return !this.graphicFlag;
		return Blocks.leaves.isOpaqueCube();
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
}
