package growthcraft.bamboo.block;

import java.util.Random;

import growthcraft.core.block.ICropDataProvider;
import growthcraft.core.utils.BlockCheck;
import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.bamboo.world.WorldGenBamboo;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockBambooShoot extends BlockBush implements ICropDataProvider
{
	@SideOnly(Side.CLIENT)
	public static IIcon[] tex;

	//constants
	private final int growth = GrowthCraftBamboo.getConfig().bambooShootGrowthRate;

	public BlockBambooShoot()
	{
		super(Material.plants);
		this.setStepSound(soundTypeGrass);
		this.setHardness(0.0F);
		this.setTickRandomly(true);
		float f = 0.4F;
		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
		this.setCreativeTab(null);
		this.setBlockName("grc.bambooShoot");
	}

	public float getGrowthProgress(IBlockAccess world, int x, int y, int z, int meta)
	{
		return (float)(meta / 1.0);
	}

	/************
	 * TICK
	 ************/
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		if (!world.isRemote)
		{
			super.updateTick(world, x, y, z, rand);

			if (world.getBlockLightValue(x, y + 1, z) >= 9 && rand.nextInt(this.growth) == 0)
			{
				this.markOrGrowMarked(world, x, y, z, rand);
			}
		}
	}

	/************
	 * EVENTS
	 ************/
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block par5)
	{
		super.onNeighborBlockChange(world, x, y, z, par5);
		this.checkShootChange(world, x, y, z);
	}

	protected final void checkShootChange(World world, int x, int y, int z)
	{
		if (!this.canBlockStay(world, x, y, z))
		{
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			world.setBlockToAir(x, y, z);
		}
	}

	/************
	 * CONDITIONS
	 ************/

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		return super.canPlaceBlockAt(world, x, y, z) && canBlockStay(world, x, y, z);
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		Block soil = world.getBlock(x, y - 1, z);
		return (world.getFullBlockLightValue(x, y, z) >= 8 || world.canBlockSeeTheSky(x, y, z)) &&
			BlockCheck.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this);
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return GrowthCraftBamboo.bambooShootFood.getItem();
	}

	public void markOrGrowMarked(World world, int x, int y, int z, Random random)
	{
		int meta = world.getBlockMetadata(x, y, z);

		if (meta == 0)
		{
			world.setBlockMetadataWithNotify(x, y, z, 1, 4);
		}
		else
		{
			this.growBamboo(world, x, y, z, random);
		}
	}

	public void growBamboo(World world, int x, int y, int z, Random rand)
	{
		if (!TerrainGen.saplingGrowTree(world, rand, x, y, z)) return;

		int meta = world.getBlockMetadata(x, y, z) & 3;
		WorldGenerator generator = new WorldGenBamboo(true);
		int i1 = 0;
		int j1 = 0;

		world.setBlock(x, y, z, Blocks.air, 0, 4);

		if (!generator.generate(world, rand, x + i1, y, z + j1))
		{
			world.setBlock(x, y, z, this, meta, 4);
		}
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
	public Item getItemDropped(int meta, Random par2Random, int par3)
	{
		return GrowthCraftBamboo.bambooShootFood.getItem();
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		tex = new IIcon[1];
		this.tex[0] = reg.registerIcon("grcbamboo:shoot");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return this.tex[0];
	}

	/************
	 * RENDERS
	 ************/
	@Override
	public int getRenderType()
	{
		return 1;
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
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		return null;
	}
}
