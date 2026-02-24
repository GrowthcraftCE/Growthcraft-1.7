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
package growthcraft.bamboo.init;

import growthcraft.bamboo.common.item.ItemBamboo;
import growthcraft.bamboo.common.item.ItemBambooCoal;
import growthcraft.bamboo.common.item.ItemBambooDoor;
import growthcraft.bamboo.common.item.ItemBambooRaft;
import growthcraft.bamboo.common.item.ItemBambooShoot;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.GrcModuleItems;

public class GrcBambooItems extends GrcModuleItems
{
	public ItemDefinition bamboo;
	public ItemDefinition bambooDoorItem;
	public ItemDefinition bambooRaft;
	public ItemDefinition bambooCoal;
	public ItemDefinition bambooShootFood;

	@Override
	public void preInit()
	{
		this.bamboo = newDefinition(new ItemBamboo());
		this.bambooDoorItem = newDefinition(new ItemBambooDoor());
		this.bambooRaft = newDefinition(new ItemBambooRaft());
		this.bambooCoal = newDefinition(new ItemBambooCoal());
		this.bambooShootFood = newDefinition(new ItemBambooShoot());
	}

	@Override
	public void register()
	{
		bamboo.register("grc.bamboo");
		bambooDoorItem.register("grc.bambooDoorItem");
		bambooRaft.register("grc.bambooRaft");
		bambooCoal.register("grc.bambooCoal");
		bambooShootFood.register("grc.bambooShootFood");
	}
}
