package growthcraft.apples;

import net.minecraftforge.event.entity.player.BonemealEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BonemealEventApples
{
	@SubscribeEvent
	public void onUseBonemeal(BonemealEvent event)
	{
		if (event.block == GrowthCraftApples.appleSapling)
		{
			if (!event.world.isRemote)
			{
				if ((double)event.world.rand.nextFloat() < 0.45D)
				{
					((BlockAppleSapling)GrowthCraftApples.appleSapling).growTree(event.world, event.x, event.y, event.z, event.world.rand);
				}
			}
			event.setResult(Result.ALLOW);
		}
		else if (event.block == GrowthCraftApples.appleLeaves)
		{
			int meta = event.world.getBlockMetadata(event.x, event.y, event.z);
			if ((meta & 3) != 0)
			{
				event.setResult(Result.DENY);
			}
			else if (!event.world.isAirBlock(event.x, event.y - 1, event.z))
			{
				event.setResult(Result.DENY);
			}
			else
			{
				if (!event.world.isRemote)
				{
					event.world.setBlock(event.x, event.y - 1, event.z, GrowthCraftApples.appleBlock);
				}

				event.setResult(Result.ALLOW);
			}
		}
	}
}
