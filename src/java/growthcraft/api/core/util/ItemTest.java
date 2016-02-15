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

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

public class ItemTest
{
	private ItemTest() {}

	public static boolean isValid(@Nonnull ItemStack stack)
	{
		if (stack == null) return false;
		if (stack.getItem() == null) return false;
		if (stack.stackSize <= 0) return false;
		return true;
	}

	public static boolean hasEnough(@Nonnull ItemStack expected, @Nullable ItemStack actual)
	{
		if (actual == null) return false;
		if (!expected.isItemEqual(actual)) return false;
		if (actual.stackSize < expected.stackSize) return false;
		return true;
	}

	public static boolean isValidAndExpected(@Nonnull List<ItemStack> expectedItems, @Nonnull List<ItemStack> givenItems)
	{
		if (expectedItems.size() != givenItems.size()) return false;
		for (int i = 0; i < expectedItems.size(); ++i)
		{
			final ItemStack expected = expectedItems.get(i);
			final ItemStack actual = givenItems.get(i);
			if (expected != null)
			{
				if (!isValid(actual)) return false;
				if (!expected.isItemEqual(actual)) return false;
			}
			else
			{
				if (actual != null) return false;
			}
		}
		return true;
	}
}
