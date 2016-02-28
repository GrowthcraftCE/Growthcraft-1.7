package growthcraft.bamboo.event;

import java.util.Random;

import growthcraft.bamboo.GrowthCraftBamboo;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.BonemealEvent;

public class BonemealEventBamboo
{
	@SubscribeEvent
	public void onUseBonemeal(BonemealEvent event)
	{
		if (GrowthCraftBamboo.blocks.bambooStalk.getBlock() == event.block)
		{
			if (!this.isBambooOnGround(event.world, event.x, event.y, event.z))
			{
				event.setResult(Result.DENY);
			}
			else if ((this.countNearbyValidSoil(event.world, event.x, event.y, event.z, 1)) == 0)
			{
				event.setResult(Result.DENY);
			}
			else if (event.world.getBlockMetadata(event.x, event.y, event.z) != 0)
			{
				event.setResult(Result.DENY);
			}
			else
			{
				if (!event.world.isRemote)
				{
					if (isBambooOnGround(event.world, event.x, event.y, event.z))
					{
						final Random rand = new Random();
						boolean flag = false;
						final int size = 1;

						while (!flag)
						{
							int x = event.x - size;
							int y = event.y;
							int z = event.z - size;
							x = x + rand.nextInt(size * 2 + 1);
							y = y + rand.nextInt(2) - rand.nextInt(2);
							z = z + rand.nextInt(size * 2 + 1);

							if (event.world.isAirBlock(x, y, z) && GrowthCraftBamboo.blocks.bambooShoot.getBlock().canBlockStay(event.world, x, y, z))
							{
								event.world.setBlock(x, y, z, GrowthCraftBamboo.blocks.bambooShoot.getBlock(), 0, 3);
								flag = true;
							}
						}
					}
				}

				event.setResult(Result.ALLOW);
			}
		}
	}

	private boolean isBambooOnGround(World world, int x, int y, int z)
	{
		return GrowthCraftBamboo.blocks.bambooStalk.getBlock().isBambooOnGround(world, x, y, z);
	}

	private int countNearbyValidSoil(World world, int x, int y, int z, int b)
	{
		int count = 0;
		int x1 = x;
		int y1 = y;
		int z1 = z;

		for (x1 = x - b; x1 <= x + b; ++x1)
		{
			for (z1 = z - b; z1 <= z + b; ++z1)
			{
				for (y1 = y - 1; y1 <= y + 1; ++y1)
				{
					final boolean flag = world.isAirBlock(x1, y1, z1) &&
						GrowthCraftBamboo.blocks.bambooShoot.getBlock().canBlockStay(world, x1, y1, z1);
					if (flag)
					{
						++count;
					}
				}
			}
		}
		return count;
	}
}
