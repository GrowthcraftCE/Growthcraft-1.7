/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 IceDragon200
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

import java.util.Random;
import java.util.List;

import growthcraft.api.core.CoreRegistry;
import growthcraft.api.core.description.Describer;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class SimplePotionEffectFactory implements IPotionEffectFactory
{
	private int id;
	private int time;
	private int level;

	public SimplePotionEffectFactory(int i, int tm, int lvl)
	{
		this.id = i;
		this.time = tm;
		this.level = lvl;
	}

	public int getID()
	{
		return id;
	}

	public int getTime()
	{
		return time;
	}

	public int getLevel()
	{
		return level;
	}

	@Override
	public PotionEffect createPotionEffect(World world, Entity entity, Random random, Object data)
	{
		return new PotionEffect(getID(), getTime(), getLevel());
	}

	@Override
	public void getDescription(List<String> list)
	{
		final PotionEffect pe = createPotionEffect(null, null, null, null);
		Describer.getPotionEffectDescription(list, pe);
	}

	private void readFromNBT(NBTTagCompound data)
	{
		this.id = data.getInteger("id");
		this.time = data.getInteger("time");
		this.level = data.getInteger("level");
	}

	@Override
	public void readFromNBT(NBTTagCompound data, String name)
	{
		if (data.hasKey(name))
		{
			final NBTTagCompound subData = data.getCompoundTag(name);
			readFromNBT(subData);
		}
		else
		{
			// LOG error
		}
	}

	private void writeToNBT(NBTTagCompound data)
	{
		data.setInteger("id", getID());
		data.setInteger("time", getTime());
		data.setInteger("level", getLevel());
	}

	@Override
	public void writeToNBT(NBTTagCompound data, String name)
	{
		final NBTTagCompound target = new NBTTagCompound();
		final String factoryName = CoreRegistry.instance().getPotionEffectFactoryRegistry().getName(this.getClass());

		target.setString("__name__", factoryName);
		writeToNBT(target);

		data.setTag(name, target);
	}
}
