/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015, 2016 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.core.eventhandler;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

import growthcraft.core.GrowthCraftCore;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;

public class EventHandlerBucketFill
{
	public static interface IBucketEntry
	{
		ItemStack getItemStack();
		boolean matches(@Nonnull World world, @Nonnull MovingObjectPosition pos);
		void commit(@Nonnull EntityPlayer player, @Nonnull World world, @Nonnull MovingObjectPosition pos);
	}

	public static class GenericBucketEntry implements IBucketEntry
	{
		private final Block block;
		private final ItemStack itemStack;

		public GenericBucketEntry(Block blk, ItemStack stack)
		{
			this.block = blk;
			this.itemStack = stack;
		}

		public ItemStack getItemStack()
		{
			return itemStack;
		}

		public boolean matches(@Nonnull World world, @Nonnull MovingObjectPosition pos)
		{
			final Block srcBlock = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);

			if (block.equals(srcBlock))
			{
				return world.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ) == 0;
			}
			return false;
		}

		public void commit(@Nonnull EntityPlayer player, @Nonnull World world, @Nonnull MovingObjectPosition pos)
		{
			world.setBlockToAir(pos.blockX, pos.blockY, pos.blockZ);
		}

		public String toString()
		{
			return String.format("GenericBucketEntry{ block: {%s}, item_stack: {%s} }", block, itemStack);
		}
	}

	private static EventHandlerBucketFill INSTANCE = new EventHandlerBucketFill();
	private List<IBucketEntry> buckets = new ArrayList<IBucketEntry>();

	public static EventHandlerBucketFill instance()
	{
		return INSTANCE;
	}

	public void addEntry(@Nonnull IBucketEntry entry)
	{
		buckets.add(entry);
		GrowthCraftCore.getLogger().debug("Added new Bucket Entry {%s}", entry);
	}

	public EventHandlerBucketFill register(Block block, ItemStack stack)
	{
		addEntry(new GenericBucketEntry(block, stack));
		return this;
	}

	public EventHandlerBucketFill register(Block block, Item item)
	{
		return register(block, new ItemStack(item, 1));
	}

	private ItemStack fillCustomBucket(FillBucketEvent event)
	{
		for (IBucketEntry entry : buckets)
		{
			if (entry.matches(event.world, event.target))
			{
				entry.commit(event.entityPlayer, event.world, event.target);
				return entry.getItemStack();
			}
		}
		return null;
	}

	@SubscribeEvent
	public void onBucketFill(FillBucketEvent event)
	{
		if (event.world.isRemote ||
			event.result != null ||
			event.getResult() != Result.DEFAULT)
		{
			return;
		}

		final ItemStack result = fillCustomBucket(event);

		if (result != null)
		{
			event.result = result.copy();
			event.setResult(Result.ALLOW);
		}
	}
}
