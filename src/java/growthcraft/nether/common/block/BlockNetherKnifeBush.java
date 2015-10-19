package growthcraft.nether.common.block;

import growthcraft.nether.GrowthCraftNether;
import growthcraft.nether.util.DamageSources;

import net.minecraft.block.BlockBush;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class BlockNetherKnifeBush extends BlockBush
{
	public BlockNetherKnifeBush()
	{
		super();
		setBlockName("grcnether.netherKnifeBush");
		setCreativeTab(GrowthCraftNether.tab);
		setBlockTextureName("grcnether:knife_bush");
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		if (world.isRemote) return;
		if (entity instanceof EntityLivingBase)
		{
			((EntityLivingBase)entity).attackEntityFrom(DamageSources.knifeBush, 1.0F);
		}
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
}
