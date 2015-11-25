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

import java.util.Random;
import java.util.List;
import java.util.Collection;

import javax.annotation.Nonnull;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.core.effect.IPotionEffectFactory;

import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class BoozePotionEffectFactory implements IPotionEffectFactory
{
	private int id;
	private int time;
	private int level;
	private Fluid booze;

	public BoozePotionEffectFactory(@Nonnull Fluid b, int i, int tm, int lvl)
	{
		this.booze = b;
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

	public PotionEffect createPotionEffect(World world, Entity entity, Random random, Object data)
	{
		final BoozeRegistry reg = CellarRegistry.instance().booze();
		final Collection<BoozeTag> tags = reg.getTags(booze);

		if (tags != null)
		{
			int tm = getTime();
			int lv = getLevel();
			for (BoozeTag tag : tags)
			{
				final IModifierFunction func = tag.getModifierFunction();
				if (func != null)
				{
					tm = func.applyTime(tm);
					lv = func.applyLevel(lv);
				}
			}
			return new PotionEffect(getID(), tm, lv);
		}
		return null;
	}

	public void getDescription(List<String> list)
	{
		final PotionEffect pe = createPotionEffect(null, null, null, null);

		String s = StatCollector.translateToLocal(pe.getEffectName()).trim();
		if (pe.getAmplifier() > 0)
		{
			s = s + " " + StatCollector.translateToLocal("potion.potency." + pe.getAmplifier()).trim();
		}

		if (pe.getDuration() > 20)
		{
			s = s + " (" + Potion.getDurationString(pe) + ")";
		}
		list.add(EnumChatFormatting.GRAY + s);
	}
}
