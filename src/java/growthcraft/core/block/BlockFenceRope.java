package growthcraft.core.block;

import growthcraft.core.GrowthCraftCore;
import growthcraft.core.render.RenderFenceRope;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFenceRope extends Block implements IBlockRope
{
	@SideOnly(Side.CLIENT)
	public static IIcon[] tex;

	public BlockFenceRope() 
	{
		super(Material.wood);
		this.setHardness(2.0F);
		this.setResistance(5.0F);
		this.setStepSound(soundTypeWood);
		this.setBlockName("grc.fenceRope");
		this.setCreativeTab(null);
	}

	/************
	 * TRIGGERS
	 ************/	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int dir, float par7, float par8, float par9)
	{
		if (world.isRemote)
		{
			return true;
		}
		else
		{
			if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem() == GrowthCraftCore.rope) { return false; }
			else
			{
				world.setBlock(x, y, z, Blocks.fence);
				this.dropBlockAsItem(world, x, y, z, new ItemStack(GrowthCraftCore.rope));
			}

			return true;
		}
	}

	/************
	 * STUFF
	 ************/	
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return Item.getItemFromBlock(Blocks.fence);
	}

	@Override
	public boolean canConnectRopeTo(IBlockAccess world, int x, int y, int z) 
	{
		return world.getBlock(x, y, z) instanceof IBlockRope;
	}

	/************
	 * DROPS
	 ************/	
	@Override
	public Item getItemDropped(int meta, Random random, int par3)
	{
		return null;
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

		ret.add(new ItemStack(Blocks.fence));
		ret.add(new ItemStack(GrowthCraftCore.rope));
		return ret;
	}

	/************
	 * TEXTURES
	 ************/	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		tex = new IIcon[3];

		tex[0] = reg.registerIcon("planks_oak");
		tex[1] = reg.registerIcon("grccore:rope_0");
		tex[2] = reg.registerIcon("grccore:rope_1");
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
		return RenderFenceRope.id;
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

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int par5)
	{
		return true;
	}

	/************
	 * BOXES
	 ************/
	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity)
	{
		boolean flag = this.canConnectRopeTo(world, x, y, z - 1);
		boolean flag1 = this.canConnectRopeTo(world, x, y, z + 1);
		boolean flag2 = this.canConnectRopeTo(world, x - 1, y, z);
		boolean flag3 = this.canConnectRopeTo(world, x + 1, y, z);
		float f = 0.375F;
		float f1 = 0.625F;
		float f2 = 0.375F;
		float f3 = 0.625F;

		if (flag)
		{
			f2 = 0.0F;
		}

		if (flag1)
		{
			f3 = 1.0F;
		}

		if (flag || flag1)
		{
			this.setBlockBounds(f, 0.4375F, f2, f1, 0.5625F, f3);
			super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
		}

		f2 = 0.375F;
		f3 = 0.625F;

		if (flag2)
		{
			f = 0.0F;
		}

		if (flag3)
		{
			f1 = 1.0F;
		}

		if (flag2 || flag3 || !flag && !flag1)
		{
			this.setBlockBounds(f, 0.4375F, f2, f1, 0.5625F, f3);
			super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
		}

		if (flag)
		{
			f2 = 0.0F;
		}

		if (flag1)
		{
			f3 = 1.0F;
		}

		this.setBlockBoundsBasedOnState(world, x, y, z);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		boolean flag = this.canConnectRopeTo(world, x, y, z - 1);
		boolean flag1 = this.canConnectRopeTo(world, x, y, z + 1);
		boolean flag2 = this.canConnectRopeTo(world, x - 1, y, z);
		boolean flag3 = this.canConnectRopeTo(world, x + 1, y, z);
		float f = 0.375F;
		float f1 = 0.625F;
		float f2 = 0.375F;
		float f3 = 0.625F;

		if (flag)
		{
			f2 = 0.0F;
		}

		if (flag1)
		{
			f3 = 1.0F;
		}

		if (flag2)
		{
			f = 0.0F;
		}

		if (flag3)
		{
			f1 = 1.0F;
		}

		this.setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
	}
}
