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
package growthcraft.api.milk.churn;

import growthcraft.api.core.util.FluidFormatString;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ChurnRecipe
{
	private FluidStack inputFluid;
	private FluidStack outputFluid;
	private ItemStack outputItem;
	private int churns;

	public ChurnRecipe(FluidStack inFluid, FluidStack outFluid, ItemStack outItem, int ch)
	{
		this.inputFluid = inFluid;
		this.outputFluid = outFluid;
		this.outputItem = outItem;
		this.churns = ch;
	}

	public boolean isValidForRecipe(FluidStack stack)
	{
		if (stack == null) return false;
		if (!inputFluid.isFluidEqual(stack)) return false;
		if (stack.amount < inputFluid.amount) return false;
		return true;
	}

	public FluidStack getInputFluid()
	{
		return inputFluid;
	}

	public FluidStack getOutputFluid()
	{
		return outputFluid;
	}

	public ItemStack getOutputItem()
	{
		return outputItem;
	}

	public int getChurns()
	{
		return churns;
	}

	public String toString()
	{
		return String.format("ChurnRecipe(`%s` / %d = `%s` & `%s`)",
			FluidFormatString.format(inputFluid),
			churns,
			FluidFormatString.format(outputFluid),
			outputItem);
	}
}
