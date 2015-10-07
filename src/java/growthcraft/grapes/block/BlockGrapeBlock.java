package growthcraft.grapes.block;

import java.util.Random;

import growthcraft.core.utils.BlockFlags;
import growthcraft.grapes.GrowthCraftGrapes;
import growthcraft.grapes.renderer.RenderGrape;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockGrapeBlock extends Block
{
	@SideOnly(Side.CLIENT)
	public static IIcon[] tex;

	public BlockGrapeBlock()
	{
		super(Material.plants);
		//this.setTickRandomly(true);
		this.setHardness(0.0F);
		this.setStepSound(soundTypeGrass);
		this.setBlockName("grc.grapeBlock");
		this.setCreativeTab(null);
		this.setBlockBounds(0.1875F, 0.5F, 0.1875F, 0.8125F, 1.0F, 0.8125F);
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
			fellBlockAsItem(world, x, y, z);
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
			fellBlockAsItem(world, x, y, z);
		}
		return true;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block par5)
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
		return world.getBlock(x, y + 1, z) == GrowthCraftGrapes.grapeLeaves;
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return GrowthCraftGrapes.grapes;
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
		return GrowthCraftGrapes.grapes;
	}

	@Override
	public int quantityDropped(Random random)
	{
		return random.nextInt(3) == 0 ? 2 : 1;
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		tex = new IIcon[1];

		tex[0] = reg.registerIcon("grcgrapes:grape");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return this.tex[0];
	}

	/************
	 * RENDER
	 ************/
	@Override
	public int getRenderType()
	{
		return RenderGrape.id;
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
		return null;
	}
}
