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
import java.util.Random;
import javax.annotation.Nonnull;

import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.api.core.description.Describer;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.util.WeightedRandom;

/**
 * A variation of the EffectRandomList, this version uses weights instead
 * linear distribution.
 */
public class EffectWeightedRandomList extends AbstractEffect
{
	public static class WeightedEffect extends WeightedRandom.Item implements IEffect
	{
		private IEffect effect;

		public WeightedEffect(int weight, @Nonnull IEffect eff)
		{
			super(weight);
			this.effect = eff;
		}

		/**
		 * Returns the underlying effect
		 *
		 * @return effect
		 */
		public IEffect getEffect()
		{
			return effect;
		}

		@Override
		public void apply(World world, Entity entity, Random random, Object data)
		{
			effect.apply(world, entity, random, data);
		}

		@Override
		public void getDescription(List<String> list)
		{
			effect.getDescription(list);
		}
	}

	private List<WeightedEffect> effects = new ArrayList<WeightedEffect>();

	/**
	 * Adds a new WeightedEffect
	 *
	 * @param weight - effect weight
	 * @param effect - the effect
	 * @return this
	 */
	public EffectWeightedRandomList add(int weight, @Nonnull IEffect effect)
	{
		effects.add(new WeightedEffect(weight, effect));
		return this;
	}

	/**
	 * Returns the weighted effect at the given index
	 *
	 * @param index - item index
	 * @return weighted effect
	 */
	public WeightedEffect getWeightedEffect(int index)
	{
		return effects.get(index);
	}

	/**
	 * Returns the weight of the item at the given index, returns 0 if
	 * no item was found
	 *
	 * @param index - item index
	 * @return item weight
	 */
	public int getItemWeight(int index)
	{
		final WeightedEffect effect = getWeightedEffect(index);
		if (effect != null) return effect.itemWeight;
		return 0;
	}

	/**
	 * Returns the underlying effect at the given index
	 *
	 * @param index - item index
	 * @return effect
	 */
	public IEffect getItemEffect(int index)
	{
		final WeightedEffect effect = getWeightedEffect(index);
		if (effect != null) return effect.getEffect();
		return null;
	}

	/**
	 * Merges another weighted effect list into the target list
	 *
	 * @param other - weighted effect list
	 * @return this
	 */
	public EffectWeightedRandomList concat(@Nonnull EffectWeightedRandomList other)
	{
		effects.addAll(other.effects);
		return this;
	}

	/**
	 * Merges a WeightedEffect list into the current weighted list
	 *
	 * @param other - weighted effect list
	 * @return this
	 */
	public EffectWeightedRandomList concat(@Nonnull List<WeightedEffect> other)
	{
		effects.addAll(other);
		return this;
	}

	/**
	 * Performs a shallow copy of the EffectList
	 *
	 * @return new effect list
	 */
	public EffectWeightedRandomList copy()
	{
		return new EffectWeightedRandomList().concat(effects);
	}

	/**
	 * Returns one of the weighted items
	 *
	 * @param random - rng
	 * @return a weighted effect
	 */
	public WeightedEffect getRandomItem(@Nonnull Random random)
	{
		return (WeightedEffect)WeightedRandom.getRandomItem(random, effects);
	}

	/**
	 * Returns the size of the list
	 *
	 * @return size
	 */
	public int size()
	{
		return effects.size();
	}

	/**
	 * Applies all of the internal effects to the targets
	 *
	 * @param world - world that the entity is currently present ing
	 * @param entity - entity to apply the effect to
	 * @param data - any extra data you want to pass along
	 */
	@Override
	public void apply(World world, Entity entity, Random random, Object data)
	{
		final IEffect effect = getRandomItem(random);
		if (effect != null) effect.apply(world, entity, random, data);
	}

	/**
	 * Adds the description of all the internal effects
	 *
	 * @param list - list to add description lines to
	 */
	@Override
	public void getDescription(List<String> list)
	{
		final int totalWeight = WeightedRandom.getTotalWeight(effects);
		final List<String> tempList = new ArrayList<String>();
		for (WeightedEffect effect : effects)
		{
			tempList.clear();
			effect.getDescription(tempList);
			if (tempList.size() > 0)
			{
				final float chance = totalWeight > 0 ? (float)effect.itemWeight / (float)totalWeight : 0f;
				final String head = GrcI18n.translate("grc.effect.weighted_random_list.format", (int)(chance * 100f));
				Describer.compactDescription(head, list, tempList);
			}
		}
	}
}
