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
package growthcraft.dairy.common.effect;

import java.util.List;
import java.util.Random;

import growthcraft.api.core.effect.IEffect;
import growthcraft.api.core.effect.EffectList;
import growthcraft.api.core.effect.EffectChance;
import growthcraft.api.core.effect.EffectAddPotionEffect;
import growthcraft.api.core.effect.SimplePotionEffectFactory;

import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;

/**
 * If milk removes effects, then evil booze milk will add them.
 */
public class EffectEvilBoozeMilk implements IEffect
{
	private EffectList effects;

	public EffectEvilBoozeMilk()
	{
		this.effects = new EffectList();

		addEvilEffect(0.1f, Potion.blindness.id, 900, 0);
		addEvilEffect(0.1f, Potion.wither.id, 900, 0);
		addEvilEffect(0.2f, Potion.confusion.id, 900, 0);
		addEvilEffect(0.2f, Potion.digSlowdown.id, 900, 0);
		addEvilEffect(0.2f, Potion.poison.id, 900, 0);
		addEvilEffect(0.3f, Potion.hunger.id, 900, 0);
		addEvilEffect(0.5f, Potion.moveSlowdown.id, 900, 0);
		addEvilEffect(0.6f, Potion.harm.id, 20, 0);
		addEvilEffect(1.0f, Potion.weakness.id, 900, 0);
	}

	private void addEvilEffect(float chance, int id, int time, int lv)
	{
		effects.add(
			new EffectChance()
				.setChance(chance)
				.setEffect(
					new EffectAddPotionEffect()
						.setPotionFactory(new SimplePotionEffectFactory(id, time, lv))
				)
		);
	}

	@Override
	public void apply(World world, Entity en, Random r, Object d)
	{
		effects.apply(world, en, r, d);
	}

	@Override
	public void getDescription(List<String> list)
	{
		effects.getDescription(list);
	}
}
