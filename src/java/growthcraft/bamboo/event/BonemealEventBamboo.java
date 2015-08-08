package growthcraft.bamboo.event;

import java.util.Random;

import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.bamboo.block.BlockBambooShoot;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.BonemealEvent;

public class BonemealEventBamboo
{
	@SubscribeEvent
	public void onUseBonemeal(BonemealEvent event)
	{
		if (event.block == GrowthCraftBamboo.bambooShoot)
		{
			if (!event.world.isRemote)
			{
				if ((double)event.world.rand.nextFloat() < 0.45D)
				{
					((BlockBambooShoot)GrowthCraftBamboo.bambooShoot).markOrGrowMarked(event.world, event.x, event.y, event.z, event.world.rand);
				}
			}
			event.setResult(Result.ALLOW);
		}
		else if (event.block == GrowthCraftBamboo.bambooStalk)
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
						Random rand = new Random();
						boolean flag = false;
						int size = 1;

						while (!flag)
						{
							int x = event.x - size, y = event.y, z = event.z - size;
							x = x + rand.nextInt(size * 2 + 1);
							y = y + rand.nextInt(2) - rand.nextInt(2);
							z = z + rand.nextInt(size * 2 + 1);

							if (event.world.isAirBlock(x, y, z) && GrowthCraftBamboo.bambooShoot.canBlockStay(event.world, x, y, z))
							{
								event.world.setBlock(x, y, z, GrowthCraftBamboo.bambooShoot, 0, 3);
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
		boolean flag = world.getBlock(x, y - 1, z) == Blocks.grass || world.getBlock(x, y - 1, z) == Blocks.dirt;
		return world.getBlock(x, y, z) == GrowthCraftBamboo.bambooStalk && flag;
	}

	private int countNearbyValidSoil(World world, int x, int y, int z, int b)
	{
		int count = 0;
		int x1 = x, y1 = y, z1 = z;

		for (x1 = x - b; x1 <= x + b; ++x1)
		{
			for (z1 = z - b; z1 <= z + b; ++z1)
			{
				for (y1 = y - 1; y1 <= y + 1; ++y1)
				{
					boolean flag = world.isAirBlock(x1, y1, z1) && GrowthCraftBamboo.bambooShoot.canBlockStay(world, x1, y1, z1);
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
