package growthcraft.grapes.event;

import growthcraft.core.utils.BlockCheck;
import growthcraft.core.utils.BlockFlags;
import growthcraft.grapes.GrowthCraftGrapes;
import growthcraft.grapes.block.BlockGrapeVine0;
import growthcraft.grapes.block.BlockGrapeVine1;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.BonemealEvent;

public class BonemealEventGrapes
{
	private void bonemealGrapeVine0(BonemealEvent event)
	{
		final BlockGrapeVine0 vine = (BlockGrapeVine0)event.block;
		final int meta = event.world.getBlockMetadata(event.x, event.y, event.z);

		if (!event.world.isRemote)
		{
			final int i = MathHelper.getRandomIntegerInRange(event.world.rand, 1, 2);
			if (meta == 0)
			{
				if (i == 1)
				{
					vine.incrementGrowth(event.world, event.x, event.y, event.z, meta);
				}
				else if (i == 2)
				{
					event.world.setBlock(event.x, event.y, event.z, GrowthCraftGrapes.grapeVine1.getBlock(), 0, BlockFlags.UPDATE_CLIENT);
				}
			}
			else if (meta == 1)
			{
				if (i == 1)
				{
					event.world.setBlock(event.x, event.y, event.z, GrowthCraftGrapes.grapeVine1.getBlock(), 0, BlockFlags.UPDATE_CLIENT);
				}
				else if (i == 2)
				{
					if (BlockCheck.isRope(event.world.getBlock(event.x, event.y + 1, event.z)))
					{
						event.world.setBlock(event.x, event.y, event.z, GrowthCraftGrapes.grapeVine1.getBlock(), 1, BlockFlags.UPDATE_CLIENT);
						event.world.setBlock(event.x, event.y + 1, event.z, GrowthCraftGrapes.grapeLeaves.getBlock(), 0, BlockFlags.UPDATE_CLIENT);
					}
					else
					{
						event.world.setBlock(event.x, event.y, event.z, GrowthCraftGrapes.grapeVine1.getBlock(), 0, BlockFlags.UPDATE_CLIENT);
					}
				}
			}
		}
		event.setResult(Result.ALLOW);
	}

	private void bonemealGrapeVine1(BonemealEvent event)
	{
		final BlockGrapeVine1 vine = (BlockGrapeVine1)event.block;
		final int meta = event.world.getBlockMetadata(event.x, event.y, event.z);
		if (meta == 0 && BlockCheck.isRope(event.world.getBlock(event.x, event.y + 1, event.z)))
		{
			if (!event.world.isRemote)
			{
				vine.incrementGrowth(event.world, event.x, event.y, event.z, meta);
				event.world.setBlock(event.x, event.y + 1, event.z, GrowthCraftGrapes.grapeLeaves.getBlock(), 0, BlockFlags.UPDATE_CLIENT);
			}
			event.setResult(Result.ALLOW);
		}
		if (meta == 0 && event.world.isAirBlock(event.x, event.y + 1, event.z))
		{
			if (!event.world.isRemote)
			{
				vine.incrementGrowth(event.world, event.x, event.y, event.z, meta);
				event.world.setBlock(event.x, event.y + 1, event.z, GrowthCraftGrapes.grapeVine1.getBlock(), 0, BlockFlags.UPDATE_CLIENT);
			}
			event.setResult(Result.ALLOW);
		}
		else if (meta == 0 && event.world.getBlock(event.x, event.y + 1, event.z) ==  GrowthCraftGrapes.grapeLeaves.getBlock())
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

	private void bonemealGrapeLeaves(BonemealEvent event)
	{
		final boolean flag = !checkValidity(event.world, event.x, event.y, event.z - 1);
		final boolean flag1 = !checkValidity(event.world, event.x, event.y, event.z + 1);
		final boolean flag2 = !checkValidity(event.world, event.x - 1, event.y, event.z);
		final boolean flag3 = !checkValidity(event.world, event.x + 1, event.y, event.z);

		if (flag1 && flag2 && flag3 && flag)
		{
			if (event.world.isAirBlock(event.x, event.y - 1, event.z))
			{
				if (!event.world.isRemote)
				{
					event.world.setBlock(event.x, event.y - 1, event.z, GrowthCraftGrapes.grapeBlock.getBlock(), 0, BlockFlags.UPDATE_CLIENT);
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
				final int r = event.world.rand.nextInt(4);

				if (r == 0 && checkValidity(event.world, event.x, event.y, event.z - 1))
				{
					event.world.setBlock(event.x, event.y, event.z - 1, GrowthCraftGrapes.grapeLeaves.getBlock(), 0, BlockFlags.UPDATE_CLIENT);
					return;
				}

				if (r == 1 && checkValidity(event.world, event.x, event.y, event.z + 1))
				{
					event.world.setBlock(event.x, event.y, event.z + 1, GrowthCraftGrapes.grapeLeaves.getBlock(), 0, BlockFlags.UPDATE_CLIENT);
					return;
				}

				if (r == 2 && checkValidity(event.world, event.x - 1, event.y, event.z))
				{
					event.world.setBlock(event.x - 1, event.y, event.z, GrowthCraftGrapes.grapeLeaves.getBlock(), 0, BlockFlags.UPDATE_CLIENT);
					return;
				}

				if (r == 3 && checkValidity(event.world, event.x + 1, event.y, event.z))
				{
					event.world.setBlock(event.x + 1, event.y, event.z, GrowthCraftGrapes.grapeLeaves.getBlock(), 0, BlockFlags.UPDATE_CLIENT);
					return;
				}
			}
			event.setResult(Result.ALLOW);
		}
	}

	@SubscribeEvent
	public void onUseBonemeal(BonemealEvent event)
	{
		if (GrowthCraftGrapes.grapeVine0.getBlock() == event.block)
		{
			bonemealGrapeVine0(event);
		}
		else if (GrowthCraftGrapes.grapeVine1.getBlock() == event.block)
		{
			bonemealGrapeVine1(event);
		}
		else if (GrowthCraftGrapes.grapeLeaves.getBlock() == event.block)
		{
			bonemealGrapeLeaves(event);
		}
	}

	private boolean checkValidity(World world, int x, int y, int z)
	{
		if (BlockCheck.isRope(world.getBlock(x, y, z)))
		{
			final Block grapeLeaves = GrowthCraftGrapes.grapeLeaves.getBlock();
			final boolean flag = world.getBlock(x + 1, y, z) == grapeLeaves;
			final boolean flag1 = world.getBlock(x - 1, y, z) == grapeLeaves;
			final boolean flag2 = world.getBlock(x, y, z + 1) == grapeLeaves;
			final boolean flag3 = world.getBlock(x, y, z - 1) == grapeLeaves;

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
		return GrowthCraftGrapes.grapeVine1.getBlock() == world.getBlock(x, y, z);
	}
}
