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
package growthcraft.api.cellar.booze;

import java.util.Collection;

import growthcraft.api.core.log.ILoggable;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public interface IBoozeRegistry extends ILoggable
{
	BoozeEntry getBoozeEntry(Fluid fluid);
	BoozeEntry fetchBoozeEntry(Fluid fluid);
	BoozeEffect getEffect(Fluid fluid);
	boolean isFluidBooze(Fluid f);
	boolean isFluidBooze(FluidStack fluidStack);
	void registerBooze(Fluid fluid);

	/**
	 * addBoozeAlternative()
	 *
	 * Adds an alternative fluid to the mod that will act as an alternative for the booze.
	 * You will almost always want to use this if you dont want to go into the trouble of creating boozes.
	 *
	 * Example Usage:
	 *   addBoozeAlternative("short.mead", "grc.honeymead0");
	 *
	 * @param altfluid - The alternate fluid.
	 * @param fluid    - The main fluid/booze.
	 **/
	void addBoozeAlternative(Fluid altfluid, Fluid fluid);
	void addBoozeAlternative(Fluid altfluid, String fluid);
	void addBoozeAlternative(String altfluid, String fluid);

	boolean isAlternateBooze(Fluid f);
	Fluid getAlternateBooze(Fluid f);

	/**
	 * @param f - source fluid
	 * @return if an alternate booze exists, that will be returned, else returns the fluid passed in
	 */
	Fluid maybeAlternateBooze(Fluid f);
	FluidStack maybeAlternateBoozeStack(FluidStack stack);

	/* Tagging */
	void addTags(Fluid fluid, BoozeTag... tags);
	Collection<BoozeTag> getTags(Fluid fluid);
	Collection<BoozeTag> getTags(FluidStack stack);
	boolean hasTags(Fluid fluid, BoozeTag... tags);
	boolean hasTags(FluidStack fluid, BoozeTag... tags);
}
