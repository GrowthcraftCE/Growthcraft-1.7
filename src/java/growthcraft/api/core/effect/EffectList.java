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

import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.api.core.description.Describer;

import net.minecraft.world.World;
import net.minecraft.entity.Entity;

/**
 * An EffectList, as its name implies, is a list of IEffects, when it is applied
 * it will apply all of its children effects.
 * When the description is retrieved, it will join them together.
 */
public class EffectList extends AbstractEffectList
{
	/**
	 * Performs a shallow copy of the EffectList
	 *
	 * @return new effect list
	 */
	public EffectList copy()
	{
		return new EffectList().concat(effects);
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
		for (IEffect effect : effects)
		{
			effect.apply(world, entity, random, data);
		}
	}

	protected void addDescriptionHead(List<String> list)
	{
		list.add(GrcI18n.translate("grc.effect.list.head"));
	}

	/**
	 * Adds the description of all the internal effects
	 *
	 * @param list - list to add description lines to
	 */
	@Override
	protected void getActualDescription(List<String> list)
	{
		addDescriptionHead(list);
		for (IEffect effect : effects)
		{
			final List<String> tempList = new ArrayList<String>();
			effect.getDescription(tempList);
			Describer.addAllIndented(list, tempList);
		}
	}
}
