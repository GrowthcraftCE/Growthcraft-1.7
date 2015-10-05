/**
 * Gracefully taken from BuildCraft
 */
package growthcraft.cellar.handler;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;

public final class BucketHandler
{
	private static BucketHandler INSTANCE = new BucketHandler();
	private Map<Block, Item> buckets = new HashMap<Block, Item>();

	private BucketHandler() {}

	public static BucketHandler instance()
	{
		return INSTANCE;
	}

	public static void init()
	{
		MinecraftForge.EVENT_BUS.register(INSTANCE);
	}

	public void register(Block block, Item item)
	{
		buckets.put(block, item);
	}

	@SubscribeEvent
	public void onBucketFill(FillBucketEvent event)
	{
		ItemStack result = fillCustomBucket(event.world, event.target);

		if (result == null)
		{
			return;
		}

		event.result = result;
		event.setResult(Result.ALLOW);
	}

	private ItemStack fillCustomBucket(World world, MovingObjectPosition pos)
	{
		Block block = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);

		Item bucket = buckets.get(block);

		if (bucket != null && world.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ) == 0)
		{
			world.setBlockToAir(pos.blockX, pos.blockY, pos.blockZ);
			return new ItemStack(bucket);
		}
		else
		{
			return null;
		}
	}
}
