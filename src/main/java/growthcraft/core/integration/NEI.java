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
package growthcraft.core.integration;

import growthcraft.core.integration.nei.NEIPlatform;

import codechicken.nei.api.API;

import cpw.mods.fml.common.Optional;
import net.minecraft.item.ItemStack;

public class NEI
{
	private NEI() {}

	/**
	 * @return true if NEI is available, false otherwise
	 */
	public static boolean neiIsAvailable()
	{
		return NEIPlatform.isLoaded();
	}

	@Optional.Method(modid=NEIPlatform.MOD_ID)
	private static void hideItem_API(ItemStack itemStack)
	{
		API.hideItem(itemStack);
	}

	public static void hideItem(ItemStack itemStack)
	{
		if (neiIsAvailable()) hideItem_API(itemStack);
	}
}
