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
package growthcraft.core.integration.forestry.recipes;

import javax.annotation.Nullable;

import forestry.api.recipes.ISqueezerManager;
import forestry.api.recipes.ISqueezerRecipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.common.Optional;

@Optional.Interface(iface="forestry.api.recipes.ISqueezerManager", modid="ForestryAPI|recipes")
public class SqueezerManagerShim extends AbstractManagerShim<ISqueezerRecipe> implements ISqueezerManager
{
	@Override
	public void addRecipe(int timePerItem, ItemStack[] resources, FluidStack liquid, ItemStack remnants, int chance) {}

	@Override
	public void addRecipe(int timePerItem, ItemStack[] resources, FluidStack liquid) {}

	@Override
	public void addContainerRecipe(int timePerItem, ItemStack emptyContainer, @Nullable ItemStack remnants, float chance) {}
}
