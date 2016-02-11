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
package growthcraft.milk.init;

import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.milk.common.item.EnumCheeseType;
import growthcraft.milk.common.item.ItemButter;
import growthcraft.milk.common.item.ItemCheese;
import growthcraft.milk.common.item.ItemCheeseCloth;
import growthcraft.milk.common.item.ItemIceCream;
import growthcraft.milk.common.item.ItemStarterCulture;
import growthcraft.milk.common.item.ItemStomach;
import growthcraft.milk.common.item.ItemYogurt;

//import net.minecraft.init.Items;
import net.minecraftforge.oredict.OreDictionary;

public class GrcMilkItems extends GrcModuleBase
{
	public ItemDefinition butter;
	public ItemDefinition cheese;
	public ItemDefinition cheeseCloth;
	public ItemDefinition iceCream;
	public ItemDefinition starterCulture;
	public ItemDefinition stomach;
	public ItemDefinition yogurt;

	@Override
	public void preInit()
	{
		this.butter = new ItemDefinition(new ItemButter());
		this.cheese = new ItemDefinition(new ItemCheese());
		this.cheeseCloth = new ItemDefinition(new ItemCheeseCloth());
		this.iceCream = new ItemDefinition(new ItemIceCream());
		this.yogurt = new ItemDefinition(new ItemYogurt());
		this.stomach = new ItemDefinition(new ItemStomach());
		this.starterCulture = new ItemDefinition(new ItemStarterCulture());
	}

	@Override
	public void register()
	{
		butter.register("grcmilk.Butter");
		cheese.register("grcmilk.Cheese");
		cheeseCloth.register("grcmilk.CheeseCloth");
		iceCream.register("grcmilk.IceCream");
		yogurt.register("grcmilk.Yogurt");
		stomach.register("grcmilk.Stomach");
		starterCulture.register("grcmilk.StarterCulture");
	}

	@Override
	public void init()
	{
		OreDictionary.registerOre("foodButter", butter.asStack());
		for (EnumCheeseType e : EnumCheeseType.VALUES)
		{
			OreDictionary.registerOre("foodCheese", e.asStack());
		}
	}
}
