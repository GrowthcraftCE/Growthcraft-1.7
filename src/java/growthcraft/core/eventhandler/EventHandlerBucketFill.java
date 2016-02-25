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
	private Map<Block, ItemStack> buckets = new HashMap<Block, ItemStack>();

	public static EventHandlerBucketFill instance()
	{
		return INSTANCE;
	}

	public EventHandlerBucketFill register(Block block, ItemStack stack)
	{
		buckets.put(block, stack);
		return this;
	}

	public EventHandlerBucketFill register(Block block, Item item)
	{
		return register(block, item);
	}

	private ItemStack fillCustomBucket(World world, MovingObjectPosition pos)
	{
		final Block block = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);
		final ItemStack bucket = buckets.get(block);

		if (bucket != null && world.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ) == 0)
		{
			world.setBlockToAir(pos.blockX, pos.blockY, pos.blockZ);
			return bucket.copy();
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
