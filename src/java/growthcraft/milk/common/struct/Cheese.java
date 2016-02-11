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

import growthcraft.api.core.util.TickUtils;
import growthcraft.milk.common.item.EnumCheeseType;
import growthcraft.milk.common.item.EnumCheeseStage;

import io.netty.buffer.ByteBuf;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class Cheese
{
	public boolean needClientUpdate;

	private int age;
	private int ageMax = TickUtils.minutes(1);
	private int slices = 8;
	private EnumCheeseType cheese = EnumCheeseType.CHEDDAR;
	private EnumCheeseStage cheeseStage = EnumCheeseType.CHEDDAR.stages.get(0);

	public EnumCheeseType getType()
	{
		return cheese;
	}

	public EnumCheeseStage getStage()
	{
		return cheeseStage;
	}

	public int getId()
	{
		return cheese.meta;
	}

	public int getStageId()
	{
		return cheeseStage.index;
	}

	public int getSlices()
	{
		return slices;
	}

	public float getAgeProgress()
	{
		return (float)age / ageMax;
	}

	public boolean isAged()
	{
		return cheeseStage == EnumCheeseStage.AGED;
	}

	public ItemStack asFullStack()
	{
		final int quantity = getSlices() * 9;
		if (quantity > 0)
		{
			return cheese.asStack(quantity);
		}
		return null;
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		cheese.writeToNBT(nbt);
		cheeseStage.writeToNBT(nbt);
		nbt.setInteger("age", age);
		nbt.setInteger("slices", slices);
	}

	/**
	 * When the tileentity is reloaded from an ItemStack
	 *
	 * @param nbt  tag compound to read
	 */
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.cheese = EnumCheeseType.loadFromNBT(nbt);
		this.cheeseStage = EnumCheeseStage.loadFromNBT(nbt);
		this.age = nbt.getInteger("age");
		this.slices = nbt.getInteger("slices");
	}

	public void readFromStream(ByteBuf stream)
	{
		this.cheese = EnumCheeseType.loadFromStream(stream);
		this.cheeseStage = EnumCheeseStage.loadFromStream(stream);
		this.age = stream.readInt();
		this.slices = stream.readInt();
	}

	public void writeToStream(ByteBuf stream)
	{
		cheese.writeToStream(stream);
		cheeseStage.writeToStream(stream);
		stream.writeInt(age);
		stream.writeInt(slices);
	}

	public void update()
	{
		if (isAged())
		{
		}
		else
		{
			if (this.age < this.ageMax)
			{
				this.age += 1;
			}
			else
			{
				this.cheeseStage = EnumCheeseStage.AGED;
				this.needClientUpdate = true;
			}
		}
	}
}
