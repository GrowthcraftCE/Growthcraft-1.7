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
package growthcraft.cellar.init;

import growthcraft.cellar.common.item.ItemChievDummy;
import growthcraft.cellar.common.item.ItemWaterBag;
import growthcraft.cellar.common.item.ItemYeast;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.GrcModuleItems;

public class GrcCellarItems extends GrcModuleItems
{
	public ItemDefinition yeast;
	public ItemDefinition waterBag;
	public ItemDefinition chievItemDummy;

	@Override
	public void preInit()
	{
		this.yeast = new ItemDefinition(new ItemYeast());
		this.waterBag = new ItemDefinition(new ItemWaterBag());
		this.chievItemDummy = new ItemDefinition(new ItemChievDummy());
	}

	@Override
	public void register()
	{
		yeast.register("grc.yeast");
		waterBag.register("grc.waterBag");
		chievItemDummy.register("grc.chievItemDummy");
	}
}
