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
package growthcraft.api.cellar.booze.effect;

import java.util.Random;
import java.util.List;

import growthcraft.api.core.effect.AbstractEffect;
import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.api.core.stats.IAchievement;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EffectTipsy extends AbstractEffect
{
	public static Potion potionTipsy;
	public static IAchievement achievement;
	private boolean hasTipsyEffect;
	private float tipsyChance;
	private int tipsyTime;

	public EffectTipsy clear()
	{
		this.hasTipsyEffect = false;
		this.tipsyChance = 0.0f;
		this.tipsyTime = 0;
		return this;
	}

	public EffectTipsy setTipsy(float chance, int time)
	{
		this.hasTipsyEffect = true;
		this.tipsyChance = MathHelper.clamp_float(chance, 0.1F, 1.0F);
		this.tipsyTime = time;
		return this;
	}

	public boolean canCauseTipsy()
	{
		return hasTipsyEffect;
	}

	public float getTipsyChance()
	{
		return tipsyChance;
	}

	public int getTipsyTime()
	{
		return tipsyTime;
	}

	@Override
	public void apply(World world, Entity entity, Random random, Object data)
	{
		if (entity instanceof EntityLivingBase)
		{
			final EntityLivingBase entitylb = (EntityLivingBase)entity;
			if (!canCauseTipsy()) return;
			if (world.rand.nextFloat() > getTipsyChance()) return;

			int amplifier = 0;
			int time = 1200;
			if (entitylb.isPotionActive(potionTipsy))
			{
				amplifier = entitylb.getActivePotionEffect(potionTipsy).getAmplifier() + 1;
				if (amplifier > 4)
				{
					amplifier = 4;
				}
			}

			switch (amplifier)
			{
				case 1: time = 3000; break;
				case 2: time = 6750; break;
				case 3: time = 12000; break;
				case 4: time = 24000; break;
				default:
					break;
			}

			entitylb.addPotionEffect(new PotionEffect(potionTipsy.id, time, amplifier));

			if (entitylb instanceof EntityPlayer)
			{
				final EntityPlayer player = (EntityPlayer)entitylb;
				if (amplifier >= 4) achievement.addStat(player, 1);
			}
		}
	}

	@Override
	public void getDescription(List<String> list)
	{
		final PotionEffect nausea = new PotionEffect(Potion.confusion.id, getTipsyTime(), 0);
		final String p = GrcI18n.translate("grc.cellar.format.tipsy_chance", Math.round(getTipsyChance() * 100));

		String n = "";
		if (nausea.getDuration() > 20)
		{
			n = "(" + Potion.getDurationString(nausea) + ")";
		}
		list.add(EnumChatFormatting.GRAY + p + EnumChatFormatting.GRAY + " " + n);
	}

	@Override
	protected void readFromNBT(NBTTagCompound data)
	{
		this.hasTipsyEffect = data.getBoolean("has_tipsy_effect");
		this.tipsyChance = data.getFloat("tipsy_chance");
		this.tipsyTime = data.getInteger("tipsy_time");
	}

	@Override
	protected void writeToNBT(NBTTagCompound data)
	{
		data.setBoolean("has_tipsy_effect", hasTipsyEffect);
		data.setFloat("tipsy_chance", tipsyChance);
		data.setInteger("tipsy_time", tipsyTime);
	}
}
