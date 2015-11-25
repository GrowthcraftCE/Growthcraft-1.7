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

import java.util.List;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.entity.Entity;

/**
 * Has a random chance of applying its sub effect to the target
 */
public class EffectChance implements IEffect
{
	private float chance;
	private IEffect effect;

	public EffectChance(IEffect effekt)
	{
		this.effect = effekt;
	}

	public EffectChance() {}

	public EffectChance setChance(float chan)
	{
		this.chance = chan;
		return this;
	}

	public float getChance()
	{
		return chance;
	}

	public IEffect getEffect()
	{
		return effect;
	}

	public EffectChance setEffect(IEffect effekt)
	{
		this.effect = effekt;
		return this;
	}

	public void apply(World world, Entity entity, Random random, Object data)
	{
		if (effect != null)
		{
			if ((random.nextInt(2000) / 2000.0f) < chance)
			{
				effect.apply(world, entity, random, data);
			}
		}
	}

	public void getDescription(List<String> list)
	{
		// TODO: say that the following effect has a x% chance of being applied
		if (effect != null) effect.getDescription(list);
		//list.add(I18n.format("grc.effect.null"));
	}
}
