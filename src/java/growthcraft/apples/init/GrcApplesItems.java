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
package growthcraft.apples.init;

import growthcraft.apples.common.item.ItemAppleSeeds;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.GrcModuleItems;
import net.minecraft.init.Items;
import net.minecraftforge.oredict.OreDictionary;

public class GrcApplesItems extends GrcModuleItems
{
	public ItemDefinition appleSeeds;

	@Override
	public void preInit()
	{
		appleSeeds = newDefinition(new ItemAppleSeeds());
	}

	@Override
	public void register()
	{
		appleSeeds.register("grc.appleSeeds");

		OreDictionary.registerOre("seedApple", appleSeeds.getItem());
		// For Pam's HarvestCraft
		// Uses the same OreDict. names as HarvestCraft
		OreDictionary.registerOre("listAllseed", appleSeeds.getItem());
		// Common
		OreDictionary.registerOre("foodApple", Items.apple);
		OreDictionary.registerOre("foodFruit", Items.apple);
	}
}
