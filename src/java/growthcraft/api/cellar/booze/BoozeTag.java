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

import growthcraft.api.core.i18n.GrcI18n;

public class BoozeTag
{
	// the booze is young and probably has no effects
	public static final BoozeTag YOUNG = new BoozeTag("young");
	// the booze is fermented
	public static final BoozeTag FERMENTED = new BoozeTag("fermented");
	// the booze has its effect time extended
	public static final BoozeTag EXTENDED = new BoozeTag("extended");
	// the booze has a stronger effect, but limited time
	public static final BoozeTag POTENT = new BoozeTag("potent");
	// the booze has the extended + potent effect
	public static final BoozeTag HYPER_EXTENDED = new BoozeTag("hyper_extended");
	// the booze WILL kill the player on overdose
	public static final BoozeTag DEADLY = new BoozeTag("deadly");
	// the booze is poisoned, most likely by using netherrash
	public static final BoozeTag POISONED = new BoozeTag("poisoned");
	// the booze was fermented using Lager yeast
	public static final BoozeTag CHILLED = new BoozeTag("chilled");
	// the booze is heavily intoxicating
	public static final BoozeTag INTOXICATED = new BoozeTag("intoxicated");
	// the booze has a base in magic
	public static final BoozeTag MAGICAL = new BoozeTag("magical");

	private final String name;
	private IModifierFunction modifierFunction;

	public BoozeTag(String n)
	{
		this.name = n;
	}

	public String getName()
	{
		return name;
	}

	public String getUnlocalizedName()
	{
		return "grc.booze.modifier." + getName();
	}

	public String getLocalizedName()
	{
		return GrcI18n.translate(getUnlocalizedName());
	}

	public String toString()
	{
		return getName();
	}

	public IModifierFunction getModifierFunction()
	{
		return modifierFunction;
	}

	public BoozeTag setModifierFunction(IModifierFunction func)
	{
		this.modifierFunction = func;
		return this;
	}
}
