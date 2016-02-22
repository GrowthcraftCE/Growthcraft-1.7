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
import net.minecraft.util.MathHelper;

public class Cheese
{
	public boolean needClientUpdate = true;

	private int age;
	private int ageMax = TickUtils.minutes(1);
	private int slices = 8;
	private int slicesMax = 8;
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

	public Cheese setStage(EnumCheeseStage stage)
	{
		this.cheeseStage = stage;
		this.needClientUpdate = true;
		return this;
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

	public int getSlicesMax()
	{
		return slicesMax;
	}

	public boolean hasSlices()
	{
		return getSlices() > 0;
	}

	public float getAgeProgress()
	{
		return (float)age / (float)ageMax;
	}

	public boolean canAge()
	{
		return cheeseStage == EnumCheeseStage.UNAGED;
	}

	public boolean isAged()
	{
		return cheeseStage == EnumCheeseStage.AGED ||
			cheeseStage == EnumCheeseStage.CUT;
	}

	public ItemStack yankSlices(int count, boolean doYank)
	{
		final int yankedCount = MathHelper.clamp_int(count, 0, getSlices());
		final int quantity = yankedCount * 9;
		if (quantity > 0)
		{
			if (doYank)
			{
				this.slices -= yankedCount;
				setStage(EnumCheeseStage.CUT);
			}
			return cheese.asStack(quantity);
		}
		return null;
	}

	public ItemStack asFullStack()
	{
		return yankSlices(getSlices(), false);
	}

	public boolean tryWaxing(ItemStack stack)
	{
		if (getType().canWax(stack))
		{
			setStage(EnumCheeseStage.UNAGED);
			return true;
		}
		return false;
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		cheese.writeToNBT(nbt);
		cheeseStage.writeToNBT(nbt);
		nbt.setInteger("age", age);
		nbt.setInteger("slices", slices);
		nbt.setInteger("slices_max", slicesMax);
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
		if (nbt.hasKey("age"))
		{
			this.age = nbt.getInteger("age");
		}
		if (nbt.hasKey("slices"))
		{
			this.slices = nbt.getInteger("slices");
		}
		if (nbt.hasKey("slices_max"))
		{
			this.slicesMax = nbt.getInteger("slices_max");
		}
	}

	public void readFromStream(ByteBuf stream)
	{
		this.cheese = EnumCheeseType.loadFromStream(stream);
		this.cheeseStage = EnumCheeseStage.loadFromStream(stream);
		this.age = stream.readInt();
		this.slices = stream.readInt();
		this.slicesMax = stream.readInt();
	}

	public void writeToStream(ByteBuf stream)
	{
		cheese.writeToStream(stream);
		cheeseStage.writeToStream(stream);
		stream.writeInt(age);
		stream.writeInt(slices);
		stream.writeInt(slicesMax);
	}

	public void update()
	{
		if (!isAged())
		{
			if (canAge())
			{
				if (this.age < this.ageMax)
				{
					this.age += 1;
				}
				else
				{
					setStage(EnumCheeseStage.AGED);
				}
			}
		}
	}
}
