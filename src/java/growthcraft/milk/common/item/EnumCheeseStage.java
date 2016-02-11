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
package growthcraft.milk.common.item;

import io.netty.buffer.ByteBuf;

import net.minecraft.nbt.NBTTagCompound;

public enum EnumCheeseStage
{
	UNAGED("unaged"),
	AGED("aged"),
	CUT("cut"),
	UNWAXED("unwaxed");

	public static final EnumCheeseStage[] VALUES = values();

	public final String name;
	public final int index;

	private EnumCheeseStage(String s)
	{
		this.name = s;
		this.index = ordinal();
	}

	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		tag.setInteger("cheese_stage_id", index);
		return tag;
	}

	public void writeToStream(ByteBuf stream)
	{
		stream.writeInt(index);
	}

	/**
	 * Returns a EnumCheeseStage given an id, if the id is invalid, returns CHEDDAR
	 *
	 * @param id  cheese id
	 * @return cheese
	 */
	public static EnumCheeseStage getSafeById(int id)
	{
		if (id >= 0 && id < VALUES.length) return VALUES[id];
		return UNAGED;
	}

	public static EnumCheeseStage loadFromStream(ByteBuf stream)
	{
		final int id = stream.readInt();
		return getSafeById(id);
	}

	public static EnumCheeseStage loadFromNBT(NBTTagCompound nbt)
	{
		final int id = nbt.getInteger("cheese_stage_id");
		return getSafeById(id);
	}
}
