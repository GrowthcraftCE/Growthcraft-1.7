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

import growthcraft.api.core.CoreRegistry;
import growthcraft.api.core.fluids.FluidTag;

public class BoozeTag
{
	// the booze is young and probably has no effects
	public static final FluidTag YOUNG = CoreRegistry.instance().fluidTags().createTag("young");
	// the booze is fermented
	public static final FluidTag FERMENTED = CoreRegistry.instance().fluidTags().createTag("fermented");
	// the booze has its effect time extended
	public static final FluidTag EXTENDED = CoreRegistry.instance().fluidTags().createTag("extended");
	// the booze has a stronger effect, but limited time
	public static final FluidTag POTENT = CoreRegistry.instance().fluidTags().createTag("potent");
	// the booze has the extended + potent effect
	public static final FluidTag HYPER_EXTENDED = CoreRegistry.instance().fluidTags().createTag("hyper_extended");
	// the booze WILL kill the player on overdose
	public static final FluidTag DEADLY = CoreRegistry.instance().fluidTags().createTag("deadly");
	// the booze is poisoned, most likely by using netherrash
	public static final FluidTag POISONED = CoreRegistry.instance().fluidTags().createTag("poisoned");
	// the booze was fermented using Lager yeast
	public static final FluidTag CHILLED = CoreRegistry.instance().fluidTags().createTag("chilled");
	// the booze is heavily intoxicating
	public static final FluidTag INTOXICATED = CoreRegistry.instance().fluidTags().createTag("intoxicated");
	// the booze has a base in magic
	public static final FluidTag MAGICAL = CoreRegistry.instance().fluidTags().createTag("magical");
	// the booze has been reinforced, (a stronger version of potent)
	public static final FluidTag FORTIFIED = CoreRegistry.instance().fluidTags().createTag("fortified");
	// the booze has been brewed with hops
	public static final FluidTag HOPPED = CoreRegistry.instance().fluidTags().createTag("hopped");
	// the booze is some form of wine
	public static final FluidTag WINE = CoreRegistry.instance().fluidTags().createTag("wine");
	// the booze is some form of cider
	public static final FluidTag CIDER = CoreRegistry.instance().fluidTags().createTag("cider");

	private BoozeTag() {}
}
