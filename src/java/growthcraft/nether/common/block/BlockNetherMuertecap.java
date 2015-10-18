package growthcraft.nether.common.block;

import java.util.Random;

import growthcraft.nether.GrowthCraftNether;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class BlockNetherMuertecap extends BlockNetherFungusBase
{
	private final float muertecapSpreadRate = GrowthCraftNether.getConfig().muertecapSpreadRate;

	public BlockNetherMuertecap()
	{
		super();
		setBlockName("grcnether.netherMuertecap");
		setBlockTextureName("grcnether:muertecap");
		setBlockBounds(0.375F, 0.0F, 0.375F, 0.625F, 0.375F, 0.625F);
		setCreativeTab(null);
	}

	@Override
	protected float getSpreadRate(World world, int x, int y, int z)
	{
		return muertecapSpreadRate;
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
}
