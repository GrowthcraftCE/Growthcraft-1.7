package growthcraft.nether.common.block;

import growthcraft.nether.GrowthCraftNether;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class BlockNetherCinderrot extends BlockNetherFungusBase
{
	private final float cinderrotSpreadRate = GrowthCraftNether.getConfig().cinderrotSpreadRate;

	public BlockNetherCinderrot()
	{
		super();
		setBlockName("grcnether.netherCinderrot");
		setCreativeTab(GrowthCraftNether.tab);
		setBlockTextureName("grcnether:cinderrot");
		setBlockBounds(0.375F, 0.0F, 0.375F, 0.625F, 0.375F, 0.625F);
	}

	@Override
	protected float getSpreadRate(World world, int x, int y, int z)
	{
		return cinderrotSpreadRate;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		if (world.isRemote) return;
		entity.setFire(15);
	}
}
