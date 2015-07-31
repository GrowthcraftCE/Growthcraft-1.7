package growthcraft.rice;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.BonemealEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BonemealEventRice
{
	@SubscribeEvent
	public void onUseBonemeal(BonemealEvent event)
	{
		if (event.block == GrowthCraftRice.riceBlock)
		{
			int meta = event.world.getBlockMetadata(event.x, event.y, event.z);

			if (meta == 7)
			{
				event.setResult(Result.DENY);
			}
			else
			{
				if (!event.world.isRemote)
				{
					this.applyBoneMeal(event.world, event.world.rand, event.x, event.y, event.z);
				}
				event.setResult(Result.ALLOW);
			}

		}
	}

	private void applyBoneMeal(World world, Random rand, int x, int y, int z)
	{
		int r =  MathHelper.getRandomIntegerInRange(rand, 2, 5);
		int mplus;
		int mminus;

		for (int loop_i = x - 1; loop_i <= x + 1; ++loop_i)
		{
			for (int loop_k = z - 1; loop_k <= z + 1; ++loop_k)
			{
				boolean flag1 = (world.getBlock(loop_i, y, loop_k) == GrowthCraftRice.riceBlock) && (world.getBlockMetadata(loop_i, y, loop_k) != 7);
				boolean flag2 = (world.getBlock(loop_i, y - 1, loop_k) == GrowthCraftRice.paddyField) && (world.getBlockMetadata(loop_i, y - 1, loop_k) != 0);

				if (flag1 && flag2)
				{
					mplus = world.getBlockMetadata(loop_i, y, loop_k) + r;
					mminus = world.getBlockMetadata(loop_i, y - 1, loop_k) - r;
					if (mplus > 7)
					{
						mplus = 7;
					}
					if (mminus < 0)
					{
						mminus = 0;
					}
					world.setBlockMetadataWithNotify(loop_i, y, loop_k, mplus, 2);
					world.setBlockMetadataWithNotify(loop_i, y - 1, loop_k, mminus, 2);
					world.playAuxSFX(2005, loop_i, y, loop_k, 0);
					world.notifyBlockChange(loop_i, y, loop_k, Block.getBlockById(0));
					world.notifyBlockChange(loop_i, y - 1, loop_k, Block.getBlockById(0));
				}
			}
		}
	}
}
