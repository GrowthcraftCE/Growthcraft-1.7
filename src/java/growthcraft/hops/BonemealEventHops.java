package growthcraft.hops;

import growthcraft.core.GrowthCraftCore;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.BonemealEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BonemealEventHops
{
	@SubscribeEvent
	public void onUseBonemeal(BonemealEvent event)
	{
		if (event.block == GrowthCraftHops.hopVine)
		{
			int meta = event.world.getBlockMetadata(event.x, event.y, event.z);
			if ((meta == 2 || meta == 3) && event.world.getBlock(event.x, event.y + 1, event.z) == GrowthCraftCore.ropeBlock&& this.canBlockStay(event.world, event.x, event.y + 1, event.z))
			{
				if (!event.world.isRemote)
				{
					event.world.setBlock(event.x, event.y + 1, event.z, GrowthCraftHops.hopVine, 2, 3);
				}
				event.setResult(Result.ALLOW);
			}
			else if (meta < 3)
			{
				if (!event.world.isRemote)
				{
					++meta;
					event.world.setBlockMetadataWithNotify(event.x, event.y, event.z, meta, 3);
				}
				event.setResult(Result.ALLOW);
			}
			else
			{
				event.setResult(Result.DENY);
			}
		}
	}

	private boolean canBlockStay(World world, int x, int y, int z)
	{
		if (world.getBlock(x, y - 1, z) == Blocks.farmland)
		{
			return true;
		}
		else
		{
			int loop = 1;

			while (loop < 5)
			{
				if (world.getBlock(x, y - loop, z) != GrowthCraftHops.hopVine)
				{
					return false;
				}

				if (isVineRoot(world, x, y - loop, z))
				{
					return true;
				}
				loop++;
			}

			return false;
		}
	}

	private boolean isVineRoot(World world, int x, int y, int z)
	{
		return world.getBlock(x, y, z) == GrowthCraftHops.hopVine && world.getBlock(x, y - 1, z) == Blocks.farmland && (world.getBlockMetadata(x, y, z) == 2 || world.getBlockMetadata(x, y, z) == 3);
	}
}
