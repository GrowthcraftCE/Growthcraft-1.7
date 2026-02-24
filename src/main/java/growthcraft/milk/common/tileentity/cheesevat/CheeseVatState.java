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
package growthcraft.milk.common.tileentity.cheesevat;

import java.util.Locale;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public enum CheeseVatState
{
	IDLE,
	PREPARING_RICOTTA,
	PREPARING_CHEESE,
	PREPARING_CURDS;

	public static final BiMap<String, CheeseVatState> stateMap = HashBiMap.create();
	private static final CheeseVatState[] VALUES = { IDLE, PREPARING_RICOTTA, PREPARING_CHEESE, PREPARING_CURDS };
	public final String name;
	static
	{
		for (CheeseVatState state : VALUES)
		{
			stateMap.put(state.name, state);
		}
	}

	private CheeseVatState()
	{
		this.name = name().toLowerCase(Locale.ENGLISH);
	}

	public static CheeseVatState getStateSafe(String name)
	{
		final CheeseVatState state = stateMap.get(name);
		if (state != null) return state;
		return IDLE;
	}
}
