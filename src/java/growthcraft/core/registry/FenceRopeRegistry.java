/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 IceDragon200
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
package growthcraft.core.registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import growthcraft.api.core.item.ItemKey;
import growthcraft.api.core.util.BlockKey;

import net.minecraft.block.Block;

public class FenceRopeRegistry
{
	public static class FenceRopeEntry
	{
		private BlockKey fenceBlock;
		private BlockKey fenceRopeBlock;

		public FenceRopeEntry(BlockKey pFenceBlock, BlockKey pFenceRopeBlock)
		{
			this.fenceBlock = pFenceBlock;
			this.fenceRopeBlock = pFenceRopeBlock;
		}

		public BlockKey getFenceBlockKey()
		{
			return fenceBlock;
		}

		public BlockKey getFenceRopeBlockKey()
		{
			return fenceRopeBlock;
		}

		public Block getFenceBlock()
		{
			return fenceBlock.getBlock();
		}

		public int getFenceBlockMetadata()
		{
			return fenceBlock.getMetadata();
		}

		public Block getFenceRopeBlock()
		{
			return fenceRopeBlock.getBlock();
		}

		public int getFenceRopeBlockMetadata()
		{
			return fenceRopeBlock.getMetadata();
		}
	}

	private static final FenceRopeRegistry INSTANCE = new FenceRopeRegistry();
	private final BiMap<BlockKey, FenceRopeEntry> entries = HashBiMap.create();

	public void addEntry(@Nonnull FenceRopeEntry entry)
	{
		entries.put(entry.getFenceBlockKey(), entry);
	}

	public void addEntry(@Nonnull BlockKey fence, @Nonnull BlockKey fenceRope)
	{
		addEntry(new FenceRopeEntry(fence, fenceRope));
	}

	public void addEntry(@Nonnull Block fence, @Nonnull Block fenceRope)
	{
		addEntry(new BlockKey(fence, ItemKey.WILDCARD_VALUE), new BlockKey(fenceRope, ItemKey.WILDCARD_VALUE));
	}

	public FenceRopeEntry getEntry(@Nonnull BlockKey key)
	{
		return entries.get(key);
	}

	public FenceRopeEntry getEntry(@Nullable Block block, int meta)
	{
		if (block == null) return null;
		final FenceRopeEntry entry = getEntry(new BlockKey(block, meta));
		if (entry != null) return entry;
		return getEntry(new BlockKey(block, ItemKey.WILDCARD_VALUE));
	}

	public static FenceRopeRegistry instance()
	{
		return INSTANCE;
	}
}
