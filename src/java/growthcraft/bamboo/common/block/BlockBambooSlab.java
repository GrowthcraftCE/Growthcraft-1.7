package growthcraft.bamboo.common.block;

import java.util.List;
import java.util.Random;

import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.core.GrowthCraftCore;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockBambooSlab extends BlockSlab
{
	public BlockBambooSlab(boolean par2)
	{
		super(par2, Material.wood);
		this.useNeighborBrightness = true;
		this.setStepSound(soundTypeWood);
		this.setResistance(5.0F);
		this.setHardness(2.0F);
		this.setCreativeTab(GrowthCraftCore.tab);
		this.setBlockName("grc.bambooSlab");
	}

	/************
	 * STUFF
	 ************/
	@SuppressWarnings("rawtypes")
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		if (item != Item.getItemFromBlock(GrowthCraftBamboo.bambooDoubleSlab.getBlock()))
		{
			list.add(new ItemStack(item, 1, 0));
		}
	}

	@Override
	public String func_150002_b(int par1)
	{
		return super.getUnlocalizedName();
	}

	@SideOnly(Side.CLIENT)
	private static boolean isBlockSingleSlab(Block block)
	{
		return block == GrowthCraftBamboo.bambooSingleSlab.getBlock();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World par1World, int par2, int par3, int par4)
	{
		return Item.getItemFromBlock(GrowthCraftBamboo.bambooSingleSlab.getBlock());
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int par1, Random par2Random, int par3)
	{
		return Item.getItemFromBlock(GrowthCraftBamboo.bambooSingleSlab.getBlock());
	}

	protected ItemStack createStackedBlock(int par1)
	{
		return new ItemStack(GrowthCraftBamboo.bambooSingleSlab.getBlock(), 2, 0);
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return GrowthCraftBamboo.bambooBlock.getBlock().getIcon(side, meta);
	}
}
