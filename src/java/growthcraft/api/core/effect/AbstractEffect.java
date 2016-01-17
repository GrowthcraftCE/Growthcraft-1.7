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
package growthcraft.api.core.effect;

import growthcraft.api.core.CoreRegistry;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Because sometimes you want an Effect that does ABSOLUTELY NOTHING.
 */
public abstract class AbstractEffect implements IEffect
{
	protected abstract void readFromNBT(NBTTagCompound data);

	@Override
	public void readFromNBT(NBTTagCompound data, String name)
	{
		if (data.hasKey(name))
		{
			final NBTTagCompound effectData = data.getCompoundTag(name);
			readFromNBT(effectData);
		}
		else
		{
			// LOG error
		}
	}

	protected abstract void writeToNBT(NBTTagCompound data);

	@Override
	public void writeToNBT(NBTTagCompound data, String name)
	{
		final NBTTagCompound target = new NBTTagCompound();
		final String effectName = CoreRegistry.instance().getEffectsRegistry().getName(this.getClass());
		// This is a VERY important field, this is how the effects will reload their correct class.
		target.setString("__name__", effectName);
		writeToNBT(target);

		data.setTag(name, target);
	}
}
