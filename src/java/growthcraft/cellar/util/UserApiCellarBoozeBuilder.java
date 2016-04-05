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
package growthcraft.cellar.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.cellar.common.Residue;
import growthcraft.api.cellar.util.CellarBoozeBuilder;
import growthcraft.api.cellar.util.ICellarBoozeBuilder;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * This is a variant to the CellarBoozeBuilder provided in the API
 * This version will route all its registrations to the User API instead
 * of the internal registries.
 */
public class UserApiCellarBoozeBuilder extends CellarBoozeBuilder
{
	private GrcCellarUserApis userApis;

	public UserApiCellarBoozeBuilder(@Nonnull GrcCellarUserApis apis, @Nonnull Fluid fluid)
	{
		super(fluid);
		this.userApis = apis;
	}

	@Override
	public ICellarBoozeBuilder brewsTo(@Nonnull FluidStack result, @Nonnull Object stack, int time, @Nullable Residue residue)
	{
		this.userApis.getUserBrewingRecipes().addDefault(stack, new FluidStack(fluid, result.amount), result, residue, time);
		return this;
	}

	@Override
	public ICellarBoozeBuilder brewsFrom(@Nonnull FluidStack src, @Nonnull Object stack, int time, @Nullable Residue residue)
	{
		this.userApis.getUserBrewingRecipes().addDefault(stack, src, new FluidStack(fluid, src.amount), residue, time);
		return this;
	}

	@Override
	public ICellarBoozeBuilder fermentsTo(@Nonnull FluidStack result, @Nonnull ItemStack stack, int time)
	{
		this.userApis.getUserFermentingRecipes().addDefault(stack, new FluidStack(fluid, result.amount), result, time);
		return this;
	}

	@Override
	public ICellarBoozeBuilder fermentsFrom(@Nonnull FluidStack src, @Nonnull ItemStack stack, int time)
	{
		this.userApis.getUserFermentingRecipes().addDefault(stack, src, new FluidStack(fluid, src.amount), time);
		return this;
	}

	@Override
	public ICellarBoozeBuilder pressesFrom(@Nonnull ItemStack stack, int time, int amount, @Nullable Residue residue)
	{
		this.userApis.getUserPressingRecipes().addDefault(stack, new FluidStack(fluid, amount), time, residue);
		return this;
	}

	@Override
	public ICellarBoozeBuilder culturesTo(int amount, @Nonnull ItemStack stack, float heat, int time)
	{
		this.userApis.getUserCultureRecipes().addDefault(new FluidStack(fluid, amount), stack, heat, time);
		return this;
	}
}
