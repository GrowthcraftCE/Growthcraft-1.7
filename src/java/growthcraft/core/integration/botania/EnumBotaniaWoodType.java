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
package growthcraft.core.integration.botania;

import java.util.Locale;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public enum EnumBotaniaWoodType
{
	LIVING_WOOD("livingwood"),
	DREAM_WOOD("dreamwood"),
	SHIMMER_WOOD("shimmerwoodPlanks");

	public static final EnumBotaniaWoodType[] VALUES = values();

	public final String plankName;
	public final String name;
	public final int meta;

	/**
	 * @param pn - plank block name
	 */
	private EnumBotaniaWoodType(String pn)
	{
		this.plankName = pn;
		this.name = name().toLowerCase(Locale.ENGLISH);
		this.meta = ordinal();
	}

	public ItemStack asPlanksItemStack(int size)
	{
		final Block block = GameRegistry.findBlock(BotaniaPlatform.MOD_ID, plankName);
		if (block != null)
		{
			final ItemStack result = new ItemStack(block, size, 0);
			return result;
		}
		return null;
	}

	public ItemStack asPlanksItemStack()
	{
		return asPlanksItemStack(1);
	}
}
