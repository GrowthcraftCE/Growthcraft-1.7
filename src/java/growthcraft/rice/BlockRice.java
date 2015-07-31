package growthcraft.rice;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockRice extends Block implements IPaddyCrop
{
	//Constants
	private final float growth = GrowthCraftRice.riceBlock_growth;

	@SideOnly(Side.CLIENT)
	public static IIcon[] tex;

	public BlockRice()
	{
		super(Material.plants);
		this.setHardness(0.0F);
		this.setTickRandomly(true);
		this.setCreativeTab(null);
		this.setBlockName("grc.riceBlock");
		this.setStepSound(soundTypeGrass);
	}

	/************
	 * TICK
	 ************/
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		this.checkCropChange(world, x, y, z);

		if (world.getBlockLightValue(x, y + 1, z) >= 9 && world.getBlockMetadata(x, y - 1, z) > 0)
		{
			int meta = world.getBlockMetadata(x, y, z);

			if (meta < 7)
			{
				float f = this.getGrowthRate(world, x, y, z);

				if (rand.nextInt((int)(this.growth / f) + 1) == 0)
				{
					++meta;
					world.setBlockMetadataWithNotify(x, y, z, meta, 2);
					world.setBlockMetadataWithNotify(x, y - 1, z, world.getBlockMetadata(x, y - 1, z) - 1, 2);
				}
			}
		}
	}

	private float getGrowthRate(World world, int x, int y, int z)
	{
		float f = 1.0F;
		Block l = world.getBlock(x, y, z - 1);
		Block i1 = world.getBlock(x, y, z + 1);
		Block j1 = world.getBlock(x - 1, y, z);
		Block k1 = world.getBlock(x + 1, y, z);
		Block l1 = world.getBlock(x - 1, y, z - 1);
		Block i2 = world.getBlock(x + 1, y, z - 1);
		Block j2 = world.getBlock(x + 1, y, z + 1);
		Block k2 = world.getBlock(x - 1, y, z + 1);
		boolean flag = j1 == this || k1 == this;
		boolean flag1 = l == this || i1 == this;
		boolean flag2 = l1 == this || i2 == this || j2 == this || k2 == this;

		for (int loop_i = x - 1; loop_i <= x + 1; ++loop_i)
		{
			for (int loop_k = z - 1; loop_k <= z + 1; ++loop_k)
			{
				Block soil = world.getBlock(loop_i, y - 1, loop_k);
				float f1 = 0.0F;

				if (soil != null && soil == GrowthCraftRice.paddyField)
				{
					f1 = 1.0F;

					if (world.getBlockMetadata(loop_i, y - 1, loop_k) > 0)
					{
						f1 = 3.0F;
					}
				}

				if (loop_i != x || loop_k != z)
				{
					f1 /= 4.0F;
				}

				f += f1;
			}
		}

		if (flag2 || flag && flag1)
		{
			f /= 2.0F;
		}

		return f;
	}

	protected final void checkCropChange(World world, int x, int y, int z)
	{
		if (!this.canBlockStay(world, x, y, z))
		{
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			world.setBlockToAir(x, y, z);
		}
	}

	/************
	 * TRIGGERS
	 ************/
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block par5)
	{
		this.checkCropChange(world, x, y, z);
	}

	/************
	 * CONDITIONS
	 ************/
	protected boolean canThisPlantGrowOnThisBlockID(Block block)
	{
		return block == GrowthCraftRice.paddyField;
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		return (world.getFullBlockLightValue(x, y, z) >= 8 || world.canBlockSeeTheSky(x, y, z)) && this.canThisPlantGrowOnThisBlockID(world.getBlock(x, y - 1, z));
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return GrowthCraftRice.rice;
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int meta, Random random, int par3)
	{
		return GrowthCraftRice.rice;
	}

	@Override
	public int quantityDropped(Random par1Random)
	{
		return 1;
	}

	@Override
	public void dropBlockAsItemWithChance(World world, int x, int y, int z, int par5, float par6, int par7)
	{
		super.dropBlockAsItemWithChance(world, x, y, z, par5, par6, 0);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		ArrayList<ItemStack> ret = super.getDrops(world, x, y, z, metadata, fortune);

		if (metadata >= 7)
		{
			for (int n = 0; n < 3 + fortune; n++)
			{
				if (world.rand.nextInt(15) <= metadata)
				{
					ret.add(new ItemStack(GrowthCraftRice.rice, 1, 0));
				}
			}
		}

		return ret;
	}

	/************
	 * TEXTURE
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		tex = new IIcon[5];

		tex[0] = reg.registerIcon("grcrice:rice_0");
		tex[1] = reg.registerIcon("grcrice:rice_1");
		tex[2] = reg.registerIcon("grcrice:rice_2");
		tex[3] = reg.registerIcon("grcrice:rice_3");
		tex[4] = reg.registerIcon("grcrice:rice_4");

	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if (meta < 0 || meta > 7)
		{
			meta = 7;
		}

		int i = 0;
		switch (meta)
		{
		case 0: case 1: i = 0; break;
		case 2: case 3: i = 1; break;
		case 4: case 5: case 7: i = 2; break;
		case 6: i = 3; break;
		}

		return tex[i];
	}

	/************
	 * RENDERS
	 ************/
	@Override
	public int getRenderType()
	{
		return RenderRice.id;
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

	/************
	 * BOXES
	 ************/
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}
}
