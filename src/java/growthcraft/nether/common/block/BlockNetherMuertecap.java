package growthcraft.nether.common.block;

import java.util.Random;

import growthcraft.nether.GrowthCraftNether;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class BlockNetherMuertecap extends BlockBush
{
	public BlockNetherMuertecap()
	{
		super();
		setBlockName("grcnether.netherMuertecap");
		setBlockTextureName("grcnether:muertecap");
	}

	protected boolean func_149854_a(Block block)
	{
		return Blocks.netherrack == block;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return GrowthCraftNether.items.netherMuertecap.getItem();
	}

	@Override
	public Item getItemDropped(int meta, Random random, int par3)
	{
		return GrowthCraftNether.items.netherMuertecap.getItem();
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
}
