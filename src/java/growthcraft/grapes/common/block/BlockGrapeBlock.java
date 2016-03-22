package growthcraft.grapes.common.block;

import java.util.ArrayList;
import java.util.Random;

import growthcraft.cellar.common.item.EnumYeast;
import growthcraft.core.common.block.GrcBlockBase;
import growthcraft.grapes.client.renderer.RenderGrape;
import growthcraft.grapes.GrowthCraftGrapes;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockGrapeBlock extends GrcBlockBase
{
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
		setBlockBounds(0.1875F, 0.5F, 0.1875F, 0.8125F, 1.0F, 0.8125F);
		setCreativeTab(null);
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
		return GrowthCraftGrapes.blocks.grapeLeaves.getBlock() == world.getBlock(x, y + 1, z);
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return GrowthCraftGrapes.items.grapes.getItem();
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
		return GrowthCraftGrapes.items.grapes.getItem();
	}

	@Override
	public int quantityDropped(Random random)
	{
		return grapesDropMin + random.nextInt(grapesDropMax - grapesDropMin);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		final ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		final int count = quantityDropped(metadata, fortune, world.rand);
		for(int i = 0; i < count; ++i)
		{
			final Item item = getItemDropped(metadata, world.rand, fortune);
			if (item != null)
			{
				ret.add(new ItemStack(item, 1, damageDropped(metadata)));
			}
			if (world.rand.nextInt(bayanusDropRarity) == 0)
			{
				ret.add(EnumYeast.BAYANUS.asStack(1));
			}
		}
		return ret;
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
