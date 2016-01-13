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

import growthcraft.api.core.effect.IEffect;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class EffectRegistry implements IEffectRegistry
{
	/**
	 * Error raised when an attempt is made to register an effect under an existing name
	 */
	public static class EffectRegisteredException extends RuntimeException
	{
		public static final long serialVersionUID = 1L;

		public EffectRegisteredException(@Nonnull String msg)
		{
			super(msg);
		}

		public EffectRegisteredException() {}
	}

	private BiMap<String, Class<IEffect>> effects = HashBiMap.create();

	public IEffectRegistry register(@Nonnull String name, @Nonnull Class<IEffect> effectClass)
	{
		if (effects.containsKey(name))
		{
			final Class<IEffect> effect = getClass(name);
			throw new EffectRegisteredException("Cannot register " + effectClass + ", Effect " + effect + " is already registered to " + name);
		}
		else
		{
			effects.put(name, effectClass);
		}
		return this;
	}

	public Class<IEffect> getClass(@Nonnull String name)
	{
		return effects.get(name);
	}

	public String getName(@Nonnull Class<IEffect> effectClass)
	{
		return effects.inverse().get(effectClass);
	}
}
