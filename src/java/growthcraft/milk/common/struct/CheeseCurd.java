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
package growthcraft.milk.common.struct;

import growthcraft.milk.common.item.EnumCheeseType;
import growthcraft.api.core.nbt.INBTSerializableContext;

import io.netty.buffer.ByteBuf;

import net.minecraft.nbt.NBTTagCompound;

public class CheeseCurd implements INBTSerializableContext
{
	public boolean needClientUpdate;
	private EnumCheeseType cheese = EnumCheeseType.CHEDDAR;
	private int age;
	private int ageMax = 1200;
	private boolean dried;

	public EnumCheeseType getType()
	{
		return cheese;
	}

	public int getId()
	{
		return cheese.meta;
	}

	public float getAgeProgress()
	{
		return (float)age / ageMax;
	}

	public int getRenderColor()
	{
		return cheese.getColor();
	}

	public boolean isDried()
	{
		return dried;
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		cheese.writeToNBT(nbt);
		nbt.setBoolean("dried", dried);
		nbt.setInteger("age", age);
	}

	public void writeToNBT(NBTTagCompound nbt, String name)
	{
		final NBTTagCompound target = new NBTTagCompound();
		writeToNBT(target);
		nbt.setTag(name, target);
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		this.cheese = EnumCheeseType.loadFromNBT(nbt);
		this.dried = nbt.getBoolean("dried");
		this.age = nbt.getInteger("age");
	}

	public void readFromNBT(NBTTagCompound nbt, String name)
	{
		if (nbt.hasKey(name))
		{
			readFromNBT(nbt.getCompoundTag(name));
		}
		else
		{
			// WARN
		}
	}

	public void readFromStream(ByteBuf stream)
	{
		this.cheese = EnumCheeseType.loadFromStream(stream);
		this.dried = stream.readBoolean();
		this.age = stream.readInt();
	}

	public void writeToStream(ByteBuf stream)
	{
		cheese.writeToStream(stream);
		stream.writeBoolean(dried);
		stream.writeInt(age);
	}

	public void update()
	{
		if (dried)
		{
			if (age != ageMax)
			{
				this.age = ageMax;
				this.needClientUpdate = true;
			}
		}
		else
		{
			if (this.age < ageMax)
			{
				this.age += 1;
			}
			else
			{
				this.dried = true;
				this.needClientUpdate = true;
			}
		}
	}
}
