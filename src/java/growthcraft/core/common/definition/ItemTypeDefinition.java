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

import growthcraft.api.core.definition.ISubItemStackFactory;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemTypeDefinition<T extends Item> extends ObjectDefinition<T> implements ISubItemStackFactory
{
	public ItemTypeDefinition(@Nonnull T item)
	{
		super(item);
	}

	@Nonnull
	public T getItem()
	{
		return getObject();
	}

	@Nonnull
	@Override
	public ItemStack asStack(int size, int damage)
	{
		return new ItemStack(getItem(), size, damage);
	}

	public boolean equals(Item other)
	{
		if (other == null) return false;
		return getItem() == other;
	}

	/**
	 * @param name - item name
	 */
	public void register(String name)
	{
		GameRegistry.registerItem(getItem(), name);
	}
}
