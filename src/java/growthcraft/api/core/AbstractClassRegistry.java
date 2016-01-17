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
package growthcraft.api.core;

import javax.annotation.Nonnull;

import growthcraft.api.core.nbt.INBTSerializable;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Don't even try to understand this, save yourself the trouble.
 */
public abstract class AbstractClassRegistry<T extends INBTSerializable> implements IClassRegistry<T>
{
	/**
	 * Error raised when an attempt is made to register an effect under an existing name
	 */
	public static class ClassRegisteredException extends RuntimeException
	{
		public static final long serialVersionUID = 1L;

		public ClassRegisteredException(@Nonnull String msg)
		{
			super(msg);
		}

		public ClassRegisteredException() {}
	}

	private BiMap<String, Class<T>> effects = HashBiMap.create();

	public Class<T> getClass(@Nonnull String name)
	{
		return effects.get(name);
	}

	public String getName(@Nonnull Class<?> klass)
	{
		return effects.inverse().get(klass);
	}

	public void register(@Nonnull String name, @Nonnull Class<T> klass)
	{
		if (effects.containsKey(name))
		{
			final Class<T> effect = getClass(name);
			throw new ClassRegisteredException("Cannot register " + klass + ", Effect " + effect + " is already registered to " + name);
		}
		else
		{
			effects.put(name, klass);
		}
	}

	/**
	 * Mother of hacks batman!
	 *
	 * @param data - nbt data to load from
	 * @param name - key to load data from
	 * @return T an instance of the class to reload
	 */
	public T loadObjectFromNBT(@Nonnull NBTTagCompound data, @Nonnull String name)
	{
		final NBTTagCompound effectData = data.getCompoundTag(name);
		final String factoryName = data.getString("__name__");
		final Class<T> klass = getClass(factoryName);

		T instance = null;

		// This should be a utility method in the future or something, its used so much now...
		try
		{
			instance = klass.newInstance();
		}
		catch (InstantiationException e)
		{
			throw new IllegalStateException("Failed to create a new instance of an illegal class " + klass, e);
		}
		catch (IllegalAccessException e)
		{
			throw new IllegalStateException("Failed to create a new instance of " + klass + ", because lack of permissions", e);
		}

		instance.readFromNBT(data, name);

		return instance;
	}
}
