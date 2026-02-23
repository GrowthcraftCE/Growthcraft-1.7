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

import growthcraft.api.core.CoreRegistry;
import growthcraft.api.core.description.Describer;
import growthcraft.api.core.i18n.GrcI18n;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Has a random chance of applying its sub effect to the target
 */
public class EffectChance extends AbstractEffect
{
	private float chance;
	private IEffect effect;

	/**
	 * @param effekt - the effect to apply when the conditions are met
	 */
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

	@Override
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

	@Override
	protected void getActualDescription(List<String> list)
	{
		if (effect != null)
		{
			final List<String> tempList = new ArrayList<String>();
			effect.getDescription(tempList);
			if (tempList.size() > 0)
			{
				final String str = GrcI18n.translate("grc.effect.chance.format", (int)(chance * 100));
				Describer.compactDescription(str, list, tempList);
			}
		}
	}

	@Override
	protected void readFromNBT(NBTTagCompound data)
	{
		this.chance = data.getFloat("chance");
		if (data.hasKey("effect"))
		{
			this.effect = CoreRegistry.instance().getEffectsRegistry().loadEffectFromNBT(data, "effect");
		}
	}

	@Override
	protected void writeToNBT(NBTTagCompound data)
	{
		data.setFloat("chance", chance);
		if (effect != null)
		{
			effect.writeToNBT(data, "effect");
		}
	}
}
