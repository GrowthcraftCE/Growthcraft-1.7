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
package growthcraft.api.core.effect;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

import growthcraft.api.core.nbt.NBTHelper;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Base class for defining Effect lists
 */
public abstract class AbstractEffectList extends AbstractEffect
{
	protected List<IEffect> effects = new ArrayList<IEffect>();

	/**
	 * Clears all the effects in this list
	 *
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractEffectList> T clear()
	{
		effects.clear();
		return (T)this;
	}

	/**
	 * Adds the given list of effects to this EffectList
	 *
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractEffectList> T concat(@Nonnull List<IEffect> otherEffects)
	{
		effects.addAll(otherEffects);
		return (T)this;
	}

	/**
	 * Adds the given EffectList to this EffectList
	 *
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractEffectList> T concat(@Nonnull AbstractEffectList list)
	{
		effects.addAll(list.effects);
		return (T)this;
	}

	/**
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractEffectList> T add(@Nonnull IEffect effect)
	{
		effects.add(effect);
		return (T)this;
	}

	/**
	 * Retrieves an effect at the given position
	 *
	 * @param index - position to place the effect
	 * @return effect
	 */
	public IEffect get(int index)
	{
		return effects.get(index);
	}

	/**
	 * Sets an effect at the given index
	 *
	 * @param index - position to place the effect
	 * @param effect - effect to set
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractEffectList> T set(int index, @Nonnull IEffect effect)
	{
		effects.set(index, effect);
		return (T)this;
	}

	/**
	 * Returns the size of the EffectList
	 *
	 * @return size of the effect list
	 */
	public int size()
	{
		return effects.size();
	}

	@Override
	protected void readFromNBT(NBTTagCompound data)
	{
		effects.clear();
		NBTHelper.loadEffectsList(effects, data);
	}

	@Override
	protected void writeToNBT(NBTTagCompound data)
	{
		NBTHelper.writeEffectsList(data, effects);
	}
}
