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

import growthcraft.api.core.i18n.GrcI18n;

import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
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

		String s = GrcI18n.translate(pe.getEffectName()).trim();
		if (pe.getAmplifier() > 0)
		{
			s = s + " " + GrcI18n.translate("potion.potency." + pe.getAmplifier()).trim();
		}

		if (pe.getDuration() > 20)
		{
			s = s + " (" + Potion.getDurationString(pe) + ")";
		}
		list.add(EnumChatFormatting.GRAY + s);
	}
}
