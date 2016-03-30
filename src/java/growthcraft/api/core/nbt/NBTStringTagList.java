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
package growthcraft.api.core.nbt;

import java.util.List;
import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

/**
 * Utility class for wrapping a NBTTagList for Strings
 */
public class NBTStringTagList
{
	private NBTTagList parent;

	/**
	 * Pass an existing String list, or an empty one in
	 *
	 * @param list - the tag list to use as the parent
	 */
	public NBTStringTagList(@Nonnull NBTTagList list)
	{
		this.parent = list;
	}

	/**
	 * Initializes the NBTStringTagList with a default taglist
	 */
	public NBTStringTagList()
	{
		this(new NBTTagList());
	}

	/**
	 * @param list - a string list
	 */
	public NBTStringTagList(@Nonnull List<String> list)
	{
		this();
		for (String str : list)
		{
			add(str);
		}
	}

	/**
	 * How many tags are present?
	 *
	 * @return number of tags
	 */
	public int size()
	{
		return parent.tagCount();
	}

	/**
	 * Adds a string to the existing list
	 *
	 * @param str - the string to add
	 */
	public NBTStringTagList add(@Nonnull String str)
	{
		parent.appendTag(new NBTTagString(str));
		return this;
	}

	/**
	 * Returns the String present at the given index
	 *
	 * @param index - index of the string wanted
	 * @return the string at the index
	 */
	public String get(int index)
	{
		return parent.getStringTagAt(index);
	}

	/**
	 * Returns the parent NBTTagList
	 *
	 * @return parent list
	 */
	public NBTTagList getTag()
	{
		return parent;
	}
}
