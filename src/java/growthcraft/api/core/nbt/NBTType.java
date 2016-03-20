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
package growthcraft.api.core.nbt;

import java.util.Map;
import java.util.HashMap;

public enum NBTType
{
	END(0),
	BYTE(1),
	SHORT(2),
	INT(3),
	LONG(4),
	FLOAT(5),
	DOUBLE(6),
	BYTE_ARRAY(7),
	STRING(8),
	LIST(9),
	COMPOUND(10),
	INT_ARRAY(11);

	public static final Map<Integer, NBTType> MAPPING = new HashMap<Integer, NBTType>();
	static
	{
		END.register();
		BYTE.register();
		SHORT.register();
		INT.register();
		LONG.register();
		FLOAT.register();
		DOUBLE.register();
		BYTE_ARRAY.register();
		STRING.register();
		LIST.register();
		COMPOUND.register();
		INT_ARRAY.register();
	}

	public final int id;

	private NBTType(int i)
	{
		this.id = i;
	}

	private void register()
	{
		MAPPING.put(this.id, this);
	}

	public static NBTType byId(int id)
	{
		return MAPPING.get(id);
	}
}
