package growthcraft.grapes.common.block;

import java.util.Random;

import growthcraft.cellar.util.YeastType;
import growthcraft.core.util.ItemUtils;
import growthcraft.grapes.client.renderer.RenderGrape;
import growthcraft.grapes.GrowthCraftGrapes;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockGrapeBlock extends Block
{
	protected Random rand = new Random();
	protected int bayanusDropRarity = GrowthCraftGrapes.getConfig().bayanusDropRarity;
	protected int grapesDropMin = GrowthCraftGrapes.getConfig().grapesDropMin;
	protected int grapesDropMax = GrowthCraftGrapes.getConfig().grapesDropMax;

	public BlockGrapeBlock()
	{
		super(Material.plants);
		setBlockTextureName("grcgrapes:grape");
		setHardness(0.0F);
		setStepSound(soundTypeGrass);
		setBlockName("grc.grapeBlock");
		setCreativeTab(null);
		setBlockBounds(0.1875F, 0.5F, 0.1875F, 0.8125F, 1.0F, 0.8125F);
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
		return GrowthCraftGrapes.grapeLeaves.getBlock() == world.getBlock(x, y + 1, z);
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return GrowthCraftGrapes.grapes.getItem();
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
	public void breakBlock(World world, int x, int y, int z, Block block, int meta)
	{
		if (world.rand.nextInt(bayanusDropRarity) == 0)
		{
			ItemUtils.spawnItemFromStack(world, x, y, z, YeastType.BAYANUS.asStack(1), rand);
		}
		super.breakBlock(world, x, y, z, block, meta);
	}

	@Override
	public Item getItemDropped(int meta, Random random, int par3)
	{
		return GrowthCraftGrapes.grapes.getItem();
	}

	@Override
	public int quantityDropped(Random random)
	{
		return grapesDropMin + random.nextInt(grapesDropMax - grapesDropMin);
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
