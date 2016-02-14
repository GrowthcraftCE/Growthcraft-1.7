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
package growthcraft.api.cellar.booze;

import java.util.Collection;
import javax.annotation.Nonnull;

import growthcraft.api.core.fluids.FluidTag;
import growthcraft.api.core.CoreRegistry;

import net.minecraftforge.fluids.Fluid;

public class BoozeEntry
{
	private final Fluid fluid;
	private final BoozeEffect effect;

	public BoozeEntry(@Nonnull Fluid flus)
	{
		this.fluid = flus;
		this.effect = new BoozeEffect(fluid);
	}

	public BoozeEffect getEffect()
	{
		return effect;
	}

	public Fluid getFluid()
	{
		return fluid;
	}

	public Collection<FluidTag> getTags()
	{
		return CoreRegistry.instance().fluidDictionary().getFluidTags(fluid);
	}

	public void addTags(FluidTag... newtags)
	{
		CoreRegistry.instance().fluidDictionary().addFluidTags(fluid, newtags);
	}

	public boolean hasTags(FluidTag... checktags)
	{
		return CoreRegistry.instance().fluidDictionary().hasFluidTags(fluid, checktags);
	}
}
