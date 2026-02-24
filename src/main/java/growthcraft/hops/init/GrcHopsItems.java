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
package growthcraft.hops.init;

import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.GrcModuleItems;
import growthcraft.hops.common.item.ItemHops;
import growthcraft.hops.common.item.ItemHopSeeds;

public class GrcHopsItems extends GrcModuleItems
{
	public ItemDefinition hops;
	public ItemDefinition hopSeeds;

	@Override
	public void preInit()
	{
		this.hops     = newDefinition(new ItemHops());
		this.hopSeeds = newDefinition(new ItemHopSeeds());
	}

	@Override
	public void register()
	{
		hops.register("grc.hops");
		hopSeeds.register("grc.hopSeeds");
	}
}
