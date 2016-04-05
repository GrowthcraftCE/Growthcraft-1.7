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
package growthcraft.api.cellar.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import growthcraft.api.cellar.booze.BoozeEffect;
import growthcraft.api.core.fluids.FluidTag;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.core.CoreRegistry;
import growthcraft.api.cellar.common.Residue;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * If you find yourself making some seriously gnarly spaghetti code, this may
 * save you.
 */
public class CellarBoozeBuilder implements ICellarBoozeBuilder
{
	protected Fluid fluid;

	public CellarBoozeBuilder(@Nonnull Fluid f)
	{
		this.fluid = f;
	}

	public Fluid getFluid()
	{
		return fluid;
	}

	@Override
	public ICellarBoozeBuilder tags(FluidTag... tags)
	{
		CoreRegistry.instance().fluidDictionary().addFluidTags(fluid, tags);
		return this;
	}

	@Override
	public ICellarBoozeBuilder brewsTo(@Nonnull FluidStack result, @Nonnull Object stack, int time, @Nullable Residue residue)
	{
		CellarRegistry.instance().brewing().addRecipe(new FluidStack(fluid, result.amount), stack, result, time, residue);
		return this;
	}

	@Override
	public ICellarBoozeBuilder brewsFrom(@Nonnull FluidStack src, @Nonnull Object stack, int time, @Nullable Residue residue)
	{
		CellarRegistry.instance().brewing().addRecipe(src, stack, new FluidStack(fluid, src.amount), time, residue);
		return this;
	}

	@Override
	public ICellarBoozeBuilder fermentsTo(@Nonnull FluidStack result, @Nonnull ItemStack stack, int time)
	{
		CellarRegistry.instance().fermenting().addRecipe(result, new FluidStack(fluid, result.amount), stack, time);
		return this;
	}

	@Override
	public ICellarBoozeBuilder fermentsFrom(@Nonnull FluidStack src, @Nonnull ItemStack stack, int time)
	{
		CellarRegistry.instance().fermenting().addRecipe(new FluidStack(fluid, src.amount), src, stack, time);
		return this;
	}

	@Override
	public ICellarBoozeBuilder pressesFrom(@Nonnull ItemStack stack, int time, int amount, @Nullable Residue residue)
	{
		CellarRegistry.instance().pressing().addPressingRecipe(stack, new FluidStack(fluid, amount), time, residue);
		return this;
	}

	@Override
	public ICellarBoozeBuilder culturesTo(int amount, @Nonnull ItemStack stack, float heat, int time)
	{
		CellarRegistry.instance().culturing().addRecipe(new FluidStack(fluid, amount), stack, heat, time);
		return this;
	}

	@Override
	public BoozeEffect getEffect()
	{
		return CellarRegistry.instance().booze().getEffect(fluid);
	}

	public static CellarBoozeBuilder create(Fluid f)
	{
		return new CellarBoozeBuilder(f);
	}
}
