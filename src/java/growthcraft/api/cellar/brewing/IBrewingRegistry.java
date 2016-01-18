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

import growthcraft.api.cellar.common.Residue;
import growthcraft.api.core.log.ILoggable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IBrewingRegistry extends ILoggable
{
	/**
	 * addBrewing()
	 *
	 * Example Usage:
	 * 	addBrewing(new FluidStack(FluidRegistry.WATER, 20), new ItemStack(Item.wheat), new FluidStack(hopAle_booze, 20), 37, Residue.newDefault(0.3f));
	 *
	 * @param sourceFluid - The source Fluid.
	 * @param raw         - The source/input ItemStack.
	 * @param resultFluid - The resulting Fluid.
	 * @param time        - The time needed for the item/block to be brewed.
	 * @param residue     - The residue that will be produced
	 */
	void addBrewing(FluidStack sourceFluid, ItemStack raw, FluidStack resultFluid, int time, Residue residue);

	BrewingRecipe getBrewingRecipe(FluidStack fluidstack, ItemStack itemstack);
	boolean isBrewingRecipe(FluidStack fluidstack, ItemStack itemstack);
	boolean isItemBrewingIngredient(ItemStack itemstack);
}
