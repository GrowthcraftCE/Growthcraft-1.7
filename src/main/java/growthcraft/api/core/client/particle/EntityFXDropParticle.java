// Copied from Buildcraft core and edited for Growthcraft
package growthcraft.api.core.client.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;

@SideOnly(Side.CLIENT)
public class EntityFXDropParticle extends EntityFX
{
	/**
	 * The height of the current bob
	 */
	private int bobTimer;

	public EntityFXDropParticle(World world, double x, double y, double z, float particleRed, float particleGreen, float particleBlue)
	{
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);

		this.particleRed = particleRed;
		this.particleGreen = particleGreen;
		this.particleBlue = particleBlue;

		setParticleTextureIndex(113);
		setSize(0.01F, 0.01F);
		this.particleGravity = 0.06F;
		this.bobTimer = 40;
		this.particleMaxAge = (int) (64.0D / (Math.random() * 0.8D + 0.2D));
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate()
	{
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		this.motionY -= this.particleGravity;

		if (this.bobTimer-- > 0)
		{
			this.motionX *= 0.02D;
			this.motionY *= 0.02D;
			this.motionZ *= 0.02D;
			setParticleTextureIndex(113);
		}
		else
		{
			setParticleTextureIndex(112);
		}

		moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9800000190734863D;
		this.motionY *= 0.9800000190734863D;
		this.motionZ *= 0.9800000190734863D;

		if (this.particleMaxAge-- <= 0)
		{
			setDead();
		}

		if (this.onGround)
		{
			setParticleTextureIndex(114);

			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		}

		final int x = MathHelper.floor_double(this.posX);
		final int y = MathHelper.floor_double(this.posY);
		final int z = MathHelper.floor_double(this.posZ);
		final Block block = worldObj.getBlock(x, y, z);

		final Material material = block.getMaterial();

		if ((material.isLiquid() || material.isSolid()) && block instanceof IFluidBlock)
		{
			final double d0 = MathHelper.floor_double(this.posY) + 1 -
				((IFluidBlock)block).getFilledPercentage(worldObj, x, y, z);

			if (this.posY < d0)
			{
				setDead();
			}
		}
	}
}
