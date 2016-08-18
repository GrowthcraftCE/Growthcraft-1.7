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
package growthcraft.core.common;

import java.util.LinkedList;
import java.util.List;

import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.common.definition.BlockTypeDefinition;

import net.minecraft.block.Block;

public class GrcModuleBlocks extends GrcModuleBase
{
	// All items that had defintions created via the interface
	public final List<BlockTypeDefinition<? extends Block>> all = new LinkedList<BlockTypeDefinition<? extends Block>>();

	/**
	 * Creates a basic BlockDefintion from the given block
	 *
	 * @param block the block to wrap
	 * @return definition
	 */
	public BlockDefinition newDefinition(Block block)
	{
		final BlockDefinition def = new BlockDefinition(block);
		all.add(def);
		return def;
	}

	/**
	 * Creates a BlockTypeDefintion from the given block
	 *
	 * @param block the block to wrap and type by
	 * @return typed definition
	 */
	public <T extends Block> BlockTypeDefinition<T> newTypedDefinition(T block)
	{
		final BlockTypeDefinition<T> def = new BlockTypeDefinition<T>(block);
		all.add(def);
		return def;
	}
}
