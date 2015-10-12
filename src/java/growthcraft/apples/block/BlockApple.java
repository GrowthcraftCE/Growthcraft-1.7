package growthcraft.apples.block;

import java.util.Random;

import growthcraft.apples.GrowthCraftApples;
import growthcraft.apples.renderer.RenderApple;
import growthcraft.core.block.ICropDataProvider;
import growthcraft.core.integration.AppleCore;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockApple extends Block implements IGrowable, ICropDataProvider
{
	@SideOnly(Side.CLIENT)
	public static IIcon[] tex;

	private final int growth = GrowthCraftApples.getConfig().appleGrowthRate;
	private final boolean dropRipeApples = GrowthCraftApples.getConfig().dropRipeApples;
	private final int dropChance = GrowthCraftApples.getConfig().appleDropChance;

	public BlockApple()
	{
		super(Material.plants);
		this.setTickRandomly(true);
		this.setHardness(0.2F);
		this.setResistance(5.0F);
		this.setStepSound(soundTypeWood);
		this.setBlockName("grc.appleBlock");
		this.setCreativeTab(null);
	}

	public float getGrowthProgress(IBlockAccess world, int x, int y, int z, int meta)
	{
		return (float)(meta / 2.0);
	}

	void incrementGrowth(World world, int x, int y, int z, int meta)
	{
		final int previousMetadata = meta;
		++meta;
		world.setBlockMetadataWithNotify(x, y, z, meta, 3);
		AppleCore.announceGrowthTick(this, world, x, y, z, previousMetadata);
	}

	/* IGrowable interface
	 *	Check: http://www.minecraftforge.net/forum/index.php?topic=22571.0
	 *	if you have no idea what this stuff means
	 */

	/* Can the Apple grow anymore? */
	public boolean func_149851_a(World world, int x, int y, int z, boolean p_149851_5_)
	{
		return world.getBlockMetadata(x, y, z) < 2;
	}

	/* Can the Apple accept bonemeal? */
	public boolean func_149852_a(World world, Random random, int x, int y, int z)
	{
		return !world.isRemote;
	}

	/* Apply bonemeal effect */
	public void func_149853_b(World world, Random random, int x, int y, int z)
	{
		incrementGrowth(world, x, y, z, world.getBlockMetadata(x, y, z));
	}

	/**
	 * Drops the block as an item and replaces it with air
	 * @param world - world to drop in
	 * @param x - x Coord
	 * @param y - y Coord
	 * @param z - z Coord
	 */
	public void fellBlockAsItem(World world, int x, int y, int z)
	{
		this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
		world.setBlockToAir(x, y, z);
	}

	/************
	 * TICK
	 ************/
	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		if (!this.canBlockStay(world, x, y, z))
		{
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			world.setBlock(x, y, z, Blocks.air, 0, 3);
		}
		else
		{
			final Event.Result allowGrowthResult = AppleCore.validateGrowthTick(this, world, x, y, z, random);
			if (allowGrowthResult == Event.Result.DENY)
				return;

			final boolean continueGrowth = world.rand.nextInt(this.growth) == 0;
			if (allowGrowthResult == Event.Result.ALLOW || continueGrowth)
			{
				final int meta = world.getBlockMetadata(x, y, z);
				if (meta < 2)
				{
					incrementGrowth(world, x, y, z, meta);
				}
				else if (meta >= 2 && this.dropRipeApples && world.rand.nextInt(this.dropChance) == 0)
				{
					fellBlockAsItem(world, x, y, z);
				}
			}
		}
	}

	/************
	 * TRIGGERS
	 ************/
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int dir, float par7, float par8, float par9)
	{
		if (!world.isRemote)
		{
			if (world.getBlockMetadata(x, y, z) >= 2)
			{
				fellBlockAsItem(world, x, y, z);
			}
		}
		return true;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
	{
		if (!this.canBlockStay(world, x, y, z))
		{
			fellBlockAsItem(world, x, y, z);
		}
	}

	/************
	 * CONDITIONS
	 ************/
	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		return GrowthCraftApples.appleLeaves.getBlock() == world.getBlock(x, y + 1, z) &&
			(world.getBlockMetadata(x, y + 1, z) & 3) == 0;
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return Items.apple;
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int meta, Random random, int par3)
	{
		return meta == 2 ? Items.apple : null;
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}

	@Override
	public void dropBlockAsItemWithChance(World world, int x, int y, int z, int par5, float par6, int par7)
	{
		super.dropBlockAsItemWithChance(world, x, y, z, par5, par6, 0);
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		tex = new IIcon[3];

		tex[0] = reg.registerIcon("grcapples:apples_1");
		tex[1] = reg.registerIcon("grcapples:apples_2");
		tex[2] = reg.registerIcon("grcapples:apples_3");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return this.tex[meta];
	}

	/************
	 * RENDER
	 ************/
	@Override
	public int getRenderType()
	{
		return RenderApple.id;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	/************
	 * BOXES
	 ************/
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		final int meta = world.getBlockMetadata(x, y, z);
		final float f = 0.0625F;

		if (meta == 0)
		{
			this.setBlockBounds(6*f, 11*f, 6*f, 10*f, 15*f, 10*f);
		}
		else if (meta == 1)
		{
			this.setBlockBounds((float)(5.5*f), 10*f, (float)(5.5*f), (float)(10.5*f), 15*f, (float)(10.5*f));
		}
		else
		{
			this.setBlockBounds(5*f, 9*f, 5*f, 11*f, 15*f, 11*f);
		}
	}
}
