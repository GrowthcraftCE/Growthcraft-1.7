package growthcraft.nether.common.block;

import growthcraft.nether.GrowthCraftNether;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class BlockNetherBaalsRot extends BlockNetherFungusBase
{
	private final float baalsRotSpreadRate = GrowthCraftNether.getConfig().baalsRotSpreadRate;

	public BlockNetherBaalsRot()
	{
		super();
		setBlockName("grcnether.netherBaalsRot");
		setBlockTextureName("grcnether:baals_rot");
		setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.8125F, 0.875F);
		setCreativeTab(GrowthCraftNether.tab);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		if (world.isRemote) return;
		if (entity instanceof EntityLivingBase)
		{
			if (world.rand.nextFloat() < 0.3F)
			{
				((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.wither.id, 20 * 20));
			}
		}
	}

	@Override
	protected float getSpreadRate(World world, int x, int y, int z)
	{
		return baalsRotSpreadRate;
	}
}
