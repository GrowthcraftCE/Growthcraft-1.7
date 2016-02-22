/**
 * Gracefully taken from BuildCraft
 */
package growthcraft.core.eventhandler;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;

public class EventHandlerBucketFill
{
	private static EventHandlerBucketFill INSTANCE = new EventHandlerBucketFill();
	private Map<Block, Item> buckets = new HashMap<Block, Item>();

	public static EventHandlerBucketFill instance()
	{
		return INSTANCE;
	}

	public void register(Block block, Item item)
	{
		buckets.put(block, item);
	}

	private ItemStack fillCustomBucket(World world, MovingObjectPosition pos)
	{
		final Block block = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);
		final Item bucket = buckets.get(block);

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

	@SubscribeEvent
	public void onBucketFill(FillBucketEvent event)
	{
		final ItemStack result = fillCustomBucket(event.world, event.target);

		if (result == null) return;

		event.result = result;
		event.setResult(Result.ALLOW);
	}
}
