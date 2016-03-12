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
package growthcraft.api.cellar.culturing;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.core.fluids.FluidTest;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CultureRecipe implements ICultureRecipe
{
	private FluidStack inputFluidStack;
	private ItemStack outputItemStack;
	private float requiredHeat;
	private int time;

	public CultureRecipe(@Nonnull FluidStack pInputFluidStack, @Nonnull ItemStack pOutputItemStack, float pRequiredHeat, int pTime)
	{
		this.inputFluidStack = pInputFluidStack;
		this.outputItemStack = pOutputItemStack;
		this.requiredHeat = pRequiredHeat;
		this.time = pTime;
	}

	@Override
	public ItemStack getOutputItemStack()
	{
		return outputItemStack;
	}

	@Override
	public FluidStack getInputFluidStack()
	{
		return inputFluidStack;
	}

	@Override
	public float getRequiredHeat()
	{
		return requiredHeat;
	}

	@Override
	public int getTime()
	{
		return time;
	}

	@Override
	public boolean matchesRecipe(@Nullable FluidStack fluid, float heat)
	{
		if (FluidTest.hasEnough(inputFluidStack, fluid))
		{
			return heat >= requiredHeat;
		}
		return false;
	}
}
