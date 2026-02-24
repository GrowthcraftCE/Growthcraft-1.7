package growthcraft.bamboo.common.item;

import java.util.List;

import growthcraft.bamboo.common.entity.EntityBambooRaft;
import growthcraft.bamboo.GrowthCraftBamboo;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemBambooRaft extends ItemBoat
{
	public ItemBambooRaft()
	{
		super();
		this.maxStackSize = 1;
		setUnlocalizedName("grc.bambooRaft");
		setCreativeTab(GrowthCraftBamboo.creativeTab);
	}

	/************
	 * MAIN
	 ************/
	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		final float f = 1.0F;
		final float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
		final float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
		final double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double)f;
		final double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double)f + 1.62D - (double)player.yOffset;
		final double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)f;
		final Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
		final float f3 = MathHelper.cos(-f2 * 0.017453292F - (float)Math.PI);
		final float f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
		final float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		final float f6 = MathHelper.sin(-f1 * 0.017453292F);
		final float f7 = f4 * f5;
		final float f8 = f3 * f5;
		final double d3 = 5.0D;
		final Vec3 vec31 = vec3.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
		final MovingObjectPosition movingobjectposition = world.rayTraceBlocks(vec3, vec31, true);

		if (movingobjectposition == null)
		{
			return stack;
		}
		else
		{
			final Vec3 vec32 = player.getLook(f);
			boolean flag = false;
			final float f9 = 1.0F;
			final List list = world.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.addCoord(vec32.xCoord * d3, vec32.yCoord * d3, vec32.zCoord * d3).expand((double)f9, (double)f9, (double)f9));
			int i;

			for (i = 0; i < list.size(); ++i)
			{
				final Entity entity = (Entity)list.get(i);

				if (entity.canBeCollidedWith())
				{
					final float f10 = entity.getCollisionBorderSize();
					final AxisAlignedBB axisalignedbb = entity.boundingBox.expand((double)f10, (double)f10, (double)f10);

					if (axisalignedbb.isVecInside(vec3))
					{
						flag = true;
					}
				}
			}

			if (flag)
			{
				return stack;
			}
			else
			{
				if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
				{
					i = movingobjectposition.blockX;
					int j = movingobjectposition.blockY;
					final int k = movingobjectposition.blockZ;

					if (world.getBlock(i, j, k) == Blocks.snow)
					{
						--j;
					}

					final EntityBambooRaft raft = new EntityBambooRaft(world, (double)((float)i + 0.5F), (double)((float)j + 1.0F), (double)((float)k + 0.5F));
					raft.rotationYaw = (float)(((MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);

					if (!world.getCollidingBoundingBoxes(raft, raft.boundingBox.expand(-0.1D, -0.1D, -0.1D)).isEmpty())
					{
						return stack;
					}

					if (!world.isRemote)
					{
						world.spawnEntityInWorld(raft);
					}

					if (!player.capabilities.isCreativeMode)
					{
						--stack.stackSize;
					}
				}

				return stack;
			}
		}
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg)
	{
		this.itemIcon = reg.registerIcon("grcbamboo:raft");
	}
}
