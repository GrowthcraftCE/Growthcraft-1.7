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
package growthcraft.api.cellar.booze;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import growthcraft.api.core.effect.IEffect;
import growthcraft.api.core.effect.EffectList;
import growthcraft.api.cellar.booze.effect.EffectTipsy;

import net.minecraft.world.World;
import net.minecraft.entity.Entity;

public class BoozeEffect implements IEffect
{
	private EffectTipsy tipsyEffect;
	private EffectList effects = new EffectList();

	public EffectTipsy getTipsyEffect()
	{
		return tipsyEffect;
	}

	public BoozeEffect setTipsyEffect(EffectTipsy tipsy)
	{
		this.tipsyEffect = tipsy;
		return this;
	}

	public BoozeEffect setTipsy(float chance, int time)
	{
		setTipsyEffect(new EffectTipsy().setTipsy(chance, time));
		return this;
	}

	public BoozeEffect clearTipsy()
	{
		this.tipsyEffect = null;
		return this;
	}

	public EffectList getEffects()
	{
		return effects;
	}

	public boolean canCauseTipsy()
	{
		return tipsyEffect != null && tipsyEffect.canCauseTipsy();
	}

	public boolean hasEffects()
	{
		return effects.size() > 0;
	}

	public boolean isValid()
	{
		return canCauseTipsy() || hasEffects();
	}

	public void apply(World world, Entity entity, Random random, Object data)
	{
		if (tipsyEffect != null) tipsyEffect.apply(world, entity, random, data);
		effects.apply(world, entity, random, data);
	}

	public void getDescription(List<String> list)
	{
		if (tipsyEffect != null) tipsyEffect.getDescription(list);
		effects.getDescription(list);
	}
}
