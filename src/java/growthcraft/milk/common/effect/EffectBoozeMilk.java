/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 IceDragon200
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
package growthcraft.milk.common.effect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import growthcraft.api.core.effect.IEffect;
import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.api.core.nbt.NBTHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/**
 * This is an effect similar to drinking milk, however it can blacklist certain
 * potions effects.
 */
public class EffectBoozeMilk implements IEffect
{
	// A list of effects that should not be removed by this effect
	private Set<Integer> blacklist = new HashSet<Integer>();

	public EffectBoozeMilk clearBlacklist()
	{
		blacklist.clear();
		return this;
	}

	public EffectBoozeMilk blacklistPotions(Potion... potions)
	{
		for (Potion potion : potions)
		{
			blacklist.add(potion.id);
		}
		return this;
	}

	public void apply(World _w, Entity entity, Random _r, Object _d)
	{
		if (entity instanceof EntityLivingBase)
		{
			final EntityLivingBase elb = (EntityLivingBase)entity;
			final List<Integer> effectsToRemove = new ArrayList<Integer>();
			for (Object e : elb.getActivePotionEffects())
			{
				if (e instanceof PotionEffect)
				{
					final PotionEffect eff = (PotionEffect)e;
					final int id = eff.getPotionID();
					if (!blacklist.contains(id))
					{
						// to prevent concurrent modifications, cache the
						// effect ids
						effectsToRemove.add(id);
					}
				}
			}
			for (int id : effectsToRemove)
			{
				elb.removePotionEffect(id);
			}
		}
	}

	public void getDescription(List<String> list)
	{
		list.add(GrcI18n.translate("grc.effect.booze_milk"));
	}

	/**
	 * Creates a new EffectBoozeMilk, and initializes the blacklist with the
	 * given potions
	 *
	 * @param potions - list of potions to add to the blacklist
	 * @return effect
	 */
	public static EffectBoozeMilk create(Potion... potions)
	{
		final EffectBoozeMilk eff = new EffectBoozeMilk();
		return eff.blacklistPotions(potions);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt, String name)
	{
		blacklist.clear();
		if (nbt.hasKey(name))
		{
			final NBTTagCompound tag = nbt.getCompoundTag(name);
			final NBTTagCompound blacklistTag = tag.getCompoundTag("blacklist");
			NBTHelper.readIntegerCollection(blacklist, blacklistTag);
		}
		else
		{
			// log error
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, String name)
	{
		final NBTTagCompound tag = new NBTTagCompound();
		final NBTTagCompound blacklistTag = new NBTTagCompound();
		NBTHelper.writeIntegerCollection(blacklistTag, blacklist);
		tag.setTag("blacklist", blacklistTag);
		nbt.setTag(name, tag);
	}
}
