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
package growthcraft.api.core.item;

import java.util.Locale;
import com.google.common.base.CaseFormat;

import growthcraft.api.core.definition.IItemStackFactory;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * Sometimes you honestly can't remember them all, but hey, enum!
 */
public enum EnumDye implements IItemStackFactory
{
	BLACK,
	RED,
	GREEN,
	BROWN,
	BLUE,
	PURPLE,
	CYAN,
	LIGHT_GRAY,
	GRAY,
	PINK,
	LIME,
	YELLOW,
	LIGHT_BLUE,
	MAGENTA,
	ORANGE,
	WHITE;

	// Aliases
	public static final EnumDye INK_SAC = BLACK;
	public static final EnumDye CACTUS_GREEN = GREEN;
	public static final EnumDye COCOA_BEANS = BROWN;
	public static final EnumDye LAPIS_LAZULI = BLUE;
	public static final EnumDye BONE_MEAL = WHITE;

	public final int meta;
	public final String name;

	private EnumDye()
	{
		this.name = name().toLowerCase(Locale.ENGLISH);
		this.meta = ordinal();
	}

	public ItemStack asStack(int size)
	{
		return new ItemStack(Items.dye, size, meta);
	}

	public ItemStack asStack()
	{
		return asStack(1);
	}

	public String getOreName()
	{
		return String.format("dye%s", CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name()));
	}
}
