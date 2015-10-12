/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 IceDragon200
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
package growthcraft.core.common.definition;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockTypeDefinition<T extends Block> extends ObjectDefinition<T>
{
	public BlockTypeDefinition(@Nonnull T block)
	{
		super(block);
	}

	@Nonnull
	public T getBlock()
	{
		return (T)getObject();
	}

	public Item getItem()
	{
		return Item.getItemFromBlock(getBlock());
	}

	@Nonnull
	public ItemStack asStack(int size, int damage)
	{
		return new ItemStack(getBlock(), size, damage);
	}

	@Nonnull
	public ItemStack getItemAsStack(int size, int damage)
	{
		return new ItemStack(getItem(), size, damage);
	}

	/**
	 * Checks if the supplied block is equal to the given, this uses
	 * Block.isEqualTo to compare the blocks
	 *
	 * @param other - block to compare to
	 * @return true if blocks are equal, false otherwise
	 */
	public boolean equals(Block other)
	{
		return Block.isEqualTo(getBlock(), other);
	}

	/**
	 * Checks if the supplied block is the same as the given, this uses ==
	 * for comparison
	 *
	 * @param other - block to check
	 * @return true if block is the same, false otherwise
	 */
	public boolean isSameAs(Block other)
	{
		return getBlock() == other;
	}
}
