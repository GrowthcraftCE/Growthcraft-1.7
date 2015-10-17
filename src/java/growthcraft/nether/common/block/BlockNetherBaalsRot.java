package growthcraft.nether.common.block;

import growthcraft.core.utils.RenderType;
import growthcraft.nether.GrowthCraftNether;

import net.minecraft.block.BlockBush;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class BlockNetherBaalsRot extends BlockBush
{
	public BlockNetherBaalsRot()
	{
		super();
		setBlockName("grcnether.netherBaalsRot");
		setCreativeTab(GrowthCraftNether.tab);
		setBlockTextureName("grcnether:baals_rot");
	}

	@Override
	public int getRenderType()
	{
		return RenderType.CROPS;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		if (entity instanceof EntityLivingBase)
		{
			((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.wither.id, 5));
		}
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
}
