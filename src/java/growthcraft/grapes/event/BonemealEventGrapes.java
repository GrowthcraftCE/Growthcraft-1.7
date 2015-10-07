package growthcraft.grapes.event;

import growthcraft.core.GrowthCraftCore;
import growthcraft.grapes.GrowthCraftGrapes;
import growthcraft.grapes.block.BlockGrapeVine0;
import growthcraft.grapes.block.BlockGrapeVine1;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.BonemealEvent;

public class BonemealEventGrapes
{
	@SubscribeEvent
	public void onUseBonemeal(BonemealEvent event)
	{
		if (event.block == GrowthCraftGrapes.grapeVine0)
		{
			BlockGrapeVine0 vine = (BlockGrapeVine0)event.block;
			int meta = event.world.getBlockMetadata(event.x, event.y, event.z);

			if (!event.world.isRemote)
			{
				int i = MathHelper.getRandomIntegerInRange(event.world.rand, 1, 2);
				if (meta == 0)
				{
					if (i == 1)
					{
						vine.incrementGrowth(event.world, event.x, event.y, event.z, meta);
					}
					else if (i == 2)
					{
						event.world.setBlock(event.x, event.y, event.z, GrowthCraftGrapes.grapeVine1, 0, 2);
					}
				}
				else if (meta == 1)
				{
					if (i == 1)
					{
						event.world.setBlock(event.x, event.y, event.z, GrowthCraftGrapes.grapeVine1, 0, 2);
					}
					else if (i == 2)
					{
						if (event.world.getBlock(event.x, event.y + 1, event.z) == GrowthCraftCore.ropeBlock)
						{
							event.world.setBlock(event.x, event.y, event.z, GrowthCraftGrapes.grapeVine1, 1, 2);
							event.world.setBlock(event.x, event.y + 1, event.z, GrowthCraftGrapes.grapeLeaves, 0, 2);
						}
						else
						{
							event.world.setBlock(event.x, event.y, event.z, GrowthCraftGrapes.grapeVine1, 0, 2);
						}
					}
				}
			}
			event.setResult(Result.ALLOW);
		}
		else if (event.block == GrowthCraftGrapes.grapeVine1)
		{
			BlockGrapeVine1 vine = (BlockGrapeVine1)event.block;
			int meta = event.world.getBlockMetadata(event.x, event.y, event.z);
			if (meta == 0 && event.world.getBlock(event.x, event.y + 1, event.z) == GrowthCraftCore.ropeBlock)
			{
				if (!event.world.isRemote)
				{
					vine.incrementGrowth(event.world, event.x, event.y, event.z, meta);
					event.world.setBlock(event.x, event.y + 1, event.z, GrowthCraftGrapes.grapeLeaves, 0, 2);
				}
				event.setResult(Result.ALLOW);
			}
			if (meta == 0 && event.world.isAirBlock(event.x, event.y + 1, event.z))
			{
				if (!event.world.isRemote)
				{
					vine.incrementGrowth(event.world, event.x, event.y, event.z, meta);
					event.world.setBlock(event.x, event.y + 1, event.z, GrowthCraftGrapes.grapeVine1, 0, 2);
				}
				event.setResult(Result.ALLOW);
			}
			else if (meta == 0 && event.world.getBlock(event.x, event.y + 1, event.z) ==  GrowthCraftGrapes.grapeLeaves)
			{
				if (!event.world.isRemote)
				{
					vine.incrementGrowth(event.world, event.x, event.y, event.z, meta);
				}
				event.setResult(Result.ALLOW);
			}
			else
			{
				event.setResult(Result.DENY);
			}
		}
		else if (event.block == GrowthCraftGrapes.grapeLeaves)
		{
			boolean flag = !checkValidity(event.world, event.x, event.y, event.z - 1);
			boolean flag1 = !checkValidity(event.world, event.x, event.y, event.z + 1);
			boolean flag2 = !checkValidity(event.world, event.x - 1, event.y, event.z);
			boolean flag3 = !checkValidity(event.world, event.x + 1, event.y, event.z);

			if (flag1 && flag2 && flag3 && flag)
			{
				if (event.world.isAirBlock(event.x, event.y - 1, event.z))
				{
					if (!event.world.isRemote)
					{
						event.world.setBlock(event.x, event.y - 1, event.z, GrowthCraftGrapes.grapeBlock);
					}
					event.setResult(Result.ALLOW);
				}
				else
				{
					event.setResult(Result.DENY);
				}
			}
			else
			{
				if (!event.world.isRemote)
				{
					int r = event.world.rand.nextInt(4);

					if (r == 0 && checkValidity(event.world, event.x, event.y, event.z - 1))
					{
						event.world.setBlock(event.x, event.y, event.z - 1, GrowthCraftGrapes.grapeLeaves);
						return;
					}

					if (r == 1 && checkValidity(event.world, event.x, event.y, event.z + 1))
					{
						event.world.setBlock(event.x, event.y, event.z + 1, GrowthCraftGrapes.grapeLeaves);
						return;
					}

					if (r == 2 && checkValidity(event.world, event.x - 1, event.y, event.z))
					{
						event.world.setBlock(event.x - 1, event.y, event.z, GrowthCraftGrapes.grapeLeaves);
						return;
					}

					if (r == 3 && checkValidity(event.world, event.x + 1, event.y, event.z))
					{
						event.world.setBlock(event.x + 1, event.y, event.z, GrowthCraftGrapes.grapeLeaves);
						return;
					}
				}

				event.setResult(Result.ALLOW);
			}
		}
	}

	private boolean checkValidity(World world, int x, int y, int z)
	{
		if (world.getBlock(x, y, z) == GrowthCraftCore.ropeBlock)
		{
			boolean flag = world.getBlock(x + 1, y, z) == GrowthCraftGrapes.grapeLeaves;
			boolean flag1 = world.getBlock(x - 1, y, z) == GrowthCraftGrapes.grapeLeaves;
			boolean flag2 = world.getBlock(x, y, z + 1) == GrowthCraftGrapes.grapeLeaves;
			boolean flag3 = world.getBlock(x, y, z - 1) == GrowthCraftGrapes.grapeLeaves;

			if (!flag && !flag1 && !flag2 && !flag3) return false;

			if (flag && isTrunk(world, x + 1, y - 1, z)) return true;
			if (flag1 && isTrunk(world, x - 1, y - 1, z)) return true;
			if (flag2 && isTrunk(world, x, y - 1, z + 1)) return true;
			if (flag3 && isTrunk(world, x, y - 1, z - 1)) return true;

			if (flag && isTrunk(world, x + 2, y - 1, z)) return true;
			if (flag1 && isTrunk(world, x - 2, y - 1, z)) return true;
			if (flag2 && isTrunk(world, x, y - 1, z + 2)) return true;
			if (flag3 && isTrunk(world, x, y - 1, z - 2)) return true;
		}
		return false;
	}

	private boolean isTrunk(World world, int x, int y, int z)
	{
		return world.getBlock(x, y, z) == GrowthCraftGrapes.grapeVine1;
	}
}
