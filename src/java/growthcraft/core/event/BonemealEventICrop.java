package growthcraft.core.event;

import growthcraft.core.block.ICropBlock;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;

public class BonemealEventICrop
{
	@SubscribeEvent
	public void onUseBonemeal(BonemealEvent event)
	{
		if (event.block instanceof ICropBlock)
		{
			final ICropBlock cropBlock = (ICropBlock)event.block;

			if (cropBlock.onUseBonemeal(event.world, event.x, event.y, event.z))
			{
				event.setResult(Result.ALLOW);
			}
			else
			{
				event.setResult(Result.DENY);
			}
		}
	}
}
