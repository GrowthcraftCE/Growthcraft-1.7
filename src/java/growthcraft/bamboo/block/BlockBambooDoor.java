package growthcraft.bamboo.block;

import java.util.Random;

import growthcraft.bamboo.GrowthCraftBamboo;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBambooDoor extends BlockDoor
{
	private static final String[] doorIconNames = new String[] {"grcbamboo:door_lower", "grcbamboo:door_upper"};

	@SideOnly(Side.CLIENT)
	private IIcon[] tex;

	public BlockBambooDoor()
	{
		super(Material.wood);
		this.setStepSound(soundTypeWood);
		this.setHardness(3.0F);
		this.disableStats();
		this.setCreativeTab(null);
		this.setBlockName("grc.bambooDoor");
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World par1World, int par2, int par3, int par4)
	{
		return GrowthCraftBamboo.bambooDoorItem.getItem();
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int meta, Random par2Random, int par3)
	{
		return (meta & 8) != 0 ? null : GrowthCraftBamboo.bambooDoorItem.getItem();
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		this.tex = new IIcon[doorIconNames.length * 2];

		for (int i = 0; i < doorIconNames.length; ++i)
		{
			this.tex[i] = reg.registerIcon(doorIconNames[i]);
			this.tex[i + doorIconNames.length] = new IconFlipped(this.tex[i], true, false);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2)
	{
		return this.tex[0];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		if (side != 1 && side != 0)
		{
			final int meta = this.func_150012_g(world, x, y, z);
			final int j1 = meta & 3;
			final boolean flag = (meta & 4) != 0;
			final boolean flag2 = (meta & 8) != 0;
			boolean flag1 = false;

			if (flag)
			{
				if (j1 == 0 && side == 2)
				{
					flag1 = !flag1;
				}
				else if (j1 == 1 && side == 5)
				{
					flag1 = !flag1;
				}
				else if (j1 == 2 && side == 3)
				{
					flag1 = !flag1;
				}
				else if (j1 == 3 && side == 4)
				{
					flag1 = !flag1;
				}
			}
			else
			{
				if (j1 == 0 && side == 5)
				{
					flag1 = !flag1;
				}
				else if (j1 == 1 && side == 3)
				{
					flag1 = !flag1;
				}
				else if (j1 == 2 && side == 4)
				{
					flag1 = !flag1;
				}
				else if (j1 == 3 && side == 2)
				{
					flag1 = !flag1;
				}

				if ((meta & 16) != 0)
				{
					flag1 = !flag1;
				}
			}

			return this.tex[0 + (flag1 ? doorIconNames.length : 0) + (flag2 ? 1 : 0)];
		}
		else
		{
			return this.tex[0];
		}
	}
}
