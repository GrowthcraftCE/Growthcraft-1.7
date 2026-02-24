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
package growthcraft.api.core.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.core.item.ItemKey;

import net.minecraft.block.Block;

/**
 * As the name implies, this class is used in place of a List for Block keys
 */
public class BlockKey extends HashKey
{
	public final Block block;
	public final int meta;

	public BlockKey(@Nonnull Block pBlock, int pMeta)
	{
		super();
		this.block = pBlock;
		this.meta = pMeta;
		generateHashCode();
	}

	public BlockKey(@Nonnull Block pBlock)
	{
		this(pBlock, 0);
	}

	public Block getBlock()
	{
		return block;
	}

	public int getMetadata()
	{
		return meta;
	}

	public boolean matches(@Nullable Block pBlock, int pMeta)
	{
		if (pBlock == null) return false;
		return pBlock == block && (meta == ItemKey.WILDCARD_VALUE || pMeta == meta);
	}

	public boolean matches(@Nonnull BlockKey key)
	{
		return matches(key.getBlock(), key.getMetadata());
	}

	private void generateHashCode()
	{
		this.hash = block.hashCode();
		this.hash = 31 * hash + meta;
	}
}
