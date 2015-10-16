package growthcraft.rice.event;

import java.util.Random;

import growthcraft.rice.GrowthCraftRice;
import growthcraft.core.utils.BlockFlags;
import growthcraft.core.utils.AuxFX;
import growthcraft.rice.utils.RiceBlockCheck;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.BonemealEvent;

public class BonemealEventRice
{
	private void appleBonemealEffect(World world, Random rand, int x, int y, int z)
	{
		final int r =  MathHelper.getRandomIntegerInRange(rand, 2, 5);
		int mplus;
		int mminus;

		for (int i = x - 1; i <= x + 1; ++i)
		{
			for (int k = z - 1; k <= z + 1; ++k)
			{
				final boolean isRiceBlock = (GrowthCraftRice.riceBlock.getBlock() == world.getBlock(i, y, k)) &&
					(world.getBlockMetadata(i, y, k) != 7);
				final boolean isPaddyBelow = RiceBlockCheck.isPaddy(world.getBlock(i, y - 1, k)) &&
					(world.getBlockMetadata(i, y - 1, k) != 0);

				if (isRiceBlock && isPaddyBelow)
				{
					mplus = world.getBlockMetadata(i, y, k) + r;
					mminus = world.getBlockMetadata(i, y - 1, k) - r;
					if (mplus > 7)
					{
						mplus = 7;
					}
					if (mminus < 0)
					{
						mminus = 0;
					}
					world.setBlockMetadataWithNotify(i, y, k, mplus, BlockFlags.SEND_TO_CLIENT);
					world.setBlockMetadataWithNotify(i, y - 1, k, mminus, BlockFlags.SEND_TO_CLIENT);
					world.playAuxSFX(AuxFX.BONEMEAL, i, y, k, 0);
					world.notifyBlockChange(i, y, k, Blocks.air);
					world.notifyBlockChange(i, y - 1, k, Blocks.air);
				}
			}
		}
	}

	@SubscribeEvent
	public void onUseBonemeal(BonemealEvent event)
	{
		if (GrowthCraftRice.riceBlock.getBlock() == event.block)
		{
			if (!event.world.isRemote)
			{
				this.appleBonemealEffect(event.world, event.world.rand, event.x, event.y, event.z);
			}
			event.setResult(Result.ALLOW);
		}
	}
}
