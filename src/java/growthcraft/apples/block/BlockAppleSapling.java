package growthcraft.apples.block;

import java.util.Random;

import growthcraft.apples.GrowthCraftApples;
import growthcraft.apples.world.WorldGenAppleTree;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.utils.BlockFlags;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;

public class BlockAppleSapling extends BlockBush implements IGrowable
{
	@SideOnly(Side.CLIENT)
	public static IIcon tex;

	private final int growth = GrowthCraftApples.getConfig().appleSaplingGrowthRate;

	public BlockAppleSapling()
	{
		super(Material.plants);
		this.setHardness(0.0F);
		this.setStepSound(soundTypeGrass);
		this.setBlockName("grc.appleSapling");
		this.setTickRandomly(true);
		this.setCreativeTab(GrowthCraftCore.tab);
		final float f = 0.4F;
		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
	}

	/************
	 * MAIN
	 ************/
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		if (!world.isRemote)
		{
			super.updateTick(world, x, y, z, random);

			if (world.getBlockLightValue(x, y + 1, z) >= 9 && random.nextInt(this.growth) == 0)
			{
				this.markOrGrowMarked(world, x, y, z, random);
			}
		}
	}

	public void markOrGrowMarked(World world, int x, int y, int z, Random random)
	{
		final int meta = world.getBlockMetadata(x, y, z);

		if ((meta & 8) == 0)
		{
			world.setBlockMetadataWithNotify(x, y, z, meta | 8, 4);
		}
		else
		{
			this.growTree(world, x, y, z, random);
		}
	}

	public void growTree(World world, int x, int y, int z, Random random)
	{
		if (!TerrainGen.saplingGrowTree(world, random, x, y, z)) return;

		final int meta = world.getBlockMetadata(x, y, z) & 3;
		final WorldGenerator generator = new WorldGenAppleTree(true);

		world.setBlock(x, y, z, Blocks.air, 0, BlockFlags.ALL);

		if (!generator.generate(world, random, x, y, z))
		{
			world.setBlock(x, y, z, this, meta, BlockFlags.ALL);
		}
	}

	/* Both side */
	@Override
	public boolean func_149851_a(World world, int x, int y, int z, boolean isClient)
	{
		return (world.getBlockMetadata(x, y, z) & 8) == 0;
	}

	/* SideOnly(Side.SERVER) Can this apply bonemeal effect? */
	@Override
	public boolean func_149852_a(World world, Random random, int x, int y, int z)
	{
		return true;
	}

	/* Apply bonemeal effect */
	@Override
	public void func_149853_b(World world, Random random, int x, int y, int z)
	{
		if (random.nextFloat() < 0.45D)
		{
			growTree(world, x, y, z, random);
		}
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		tex = reg.registerIcon("grcapples:apple_sapling");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return this.tex;
	}
}
