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
package growthcraft.api.cellar.heatsource.user;

import java.util.Map;
import java.util.HashMap;

import growthcraft.api.core.schema.ICommentable;
import growthcraft.api.core.util.ItemKey;

public class UserHeatSourceEntry implements ICommentable
{
	public String comment = "";
	public String mod_id;
	public String block_name;
	public Map<Integer, Float> states;

	public UserHeatSourceEntry() {}

	public UserHeatSourceEntry(String m, String b, Map<Integer, Float> s)
	{
		this.mod_id = m;
		this.block_name = b;
		this.states = s;
	}

	@Override
	public String toString()
	{
		String result = String.format("UserHeatSourceEntry(mod_id: '%s', block_name: '%s')", mod_id, block_name);
		if (states != null)
		{
			result += "~(";
			for (Map.Entry<Integer, Float> pair : states.entrySet())
			{
				result += pair.getKey() + "=>" + pair.getValue() + ", ";
			}
			result += ")";
		}
		return result;
	}

	@Override
	public void setComment(String comm)
	{
		this.comment = comm;
	}

	@Override
	public String getComment()
	{
		return comment;
	}

	public static Map<Integer, Float> newHeatPair(int i, float h)
	{
		final Map<Integer, Float> map = new HashMap<Integer, Float>();
		map.put(i, h);
		return map;
	}

	public static Map<Integer, Float> newWildcardHeat(float h)
	{
		return newHeatPair(ItemKey.WILDCARD_VALUE, h);
	}
}
