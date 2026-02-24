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
package growthcraft.api.cellar.brewing;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.cellar.common.ProcessingRecipe;
import growthcraft.api.cellar.common.Residue;
import growthcraft.api.core.fluids.FluidTest;
import growthcraft.api.core.item.ItemTest;
import growthcraft.api.core.definition.IMultiItemStacks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class BrewingRecipe extends ProcessingRecipe
{
	private IMultiItemStacks inputItemStack;
	private FluidStack inputFluidStack;

	public BrewingRecipe(@Nonnull FluidStack pInputFluid, @Nonnull IMultiItemStacks pInputItem, @Nonnull FluidStack pOutputFluid, int pTime, @Nullable Residue pResidue)
	{
		super(pOutputFluid, pTime, pResidue);
		this.inputItemStack = pInputItem;
		this.inputFluidStack = pInputFluid;
	}

	public IMultiItemStacks getInputItemStack()
	{
		return inputItemStack;
	}

	public FluidStack getInputFluidStack()
	{
		return inputFluidStack;
	}

	public boolean matchesRecipe(@Nullable FluidStack fluidStack, @Nullable ItemStack itemStack)
	{
		if (fluidStack != null && itemStack != null)
		{
			if (!FluidTest.hasEnough(inputFluidStack, fluidStack)) return false;
			if (!ItemTest.hasEnough(inputItemStack, itemStack)) return false;
			return true;
		}
		return false;
	}

	public boolean matchesIngredient(@Nullable FluidStack fluidStack)
	{
		return FluidTest.fluidMatches(inputFluidStack, fluidStack);
	}

	public boolean matchesIngredient(@Nullable ItemStack stack)
	{
		return ItemTest.itemMatches(inputItemStack, stack);
	}

	public boolean isItemIngredient(@Nullable ItemStack stack)
	{
		if (stack != null)
		{
			if (inputItemStack.containsItemStack(stack)) return true;
		}
		return false;
	}
}
