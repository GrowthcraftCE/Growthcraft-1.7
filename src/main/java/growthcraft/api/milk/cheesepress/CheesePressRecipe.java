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
package growthcraft.api.milk.cheesepress;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

public class CheesePressRecipe implements ICheesePressRecipe
{
	private ItemStack inputStack;
	private ItemStack outputStack;
	private int time;

	public CheesePressRecipe(@Nonnull ItemStack pInputStack, @Nonnull ItemStack pOutputStack, int pTime)
	{
		this.inputStack = pInputStack;
		this.outputStack = pOutputStack;
		this.time = pTime;
	}

	@Override
	public ItemStack getInputItemStack()
	{
		return inputStack;
	}

	@Override
	public ItemStack getOutputItemStack()
	{
		return outputStack;
	}

	@Override
	public int getTimeMax()
	{
		return time;
	}

	@Override
	public boolean isMatchingRecipe(@Nonnull ItemStack stack)
	{
		if (inputStack.isItemEqual(stack))
		{
			return stack.stackSize >= inputStack.stackSize;
		}
		return false;
	}

	@Override
	public String toString()
	{
		return String.format("CheesePressRecipe({%s} / %d = {%s})", getOutputItemStack(), time, getInputItemStack());
	}
}
