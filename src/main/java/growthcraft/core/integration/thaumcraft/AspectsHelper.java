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
package growthcraft.core.integration.thaumcraft;

import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspect;

import cpw.mods.fml.common.Optional;

/**
 * Utility class for handling Thaumcraft Aspects and AspectLists
 */
public class AspectsHelper
{
	private AspectsHelper() {}

	/**
	 * @param list - Target AspectList
	 * @param scale - how much to scale the scale the aspects by
	 * @param aspects - the aspects to target
	 * @return list, the list that was provided for method chaining
	 */
	@Optional.Method(modid="Thaumcraft")
	public static AspectList scaleAspects(AspectList list, int scale, Aspect... aspects)
	{
		for (Aspect aspect : aspects)
		{
			final int a = list.getAmount(aspect) * scale;
			list.remove(aspect);
			if (a > 0) list.add(aspect, a);
		}
		return list;
	}
}
