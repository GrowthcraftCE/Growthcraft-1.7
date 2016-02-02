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

import java.util.List;
import java.util.Random;

import growthcraft.api.core.i18n.GrcI18n;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/**
 * As its name implies, this Effect will REMOVE a Potion Effect from the target.
 */
public class EffectRemovePotionEffect extends AbstractEffect
{
	private int potionId;

	public EffectRemovePotionEffect(int potnId)
	{
		this.potionId = potnId;
	}

	public EffectRemovePotionEffect() {}

	public EffectRemovePotionEffect setPotionID(int id)
	{
		this.potionId = id;
		return this;
	}

	public int getPotionID()
	{
		return potionId;
	}

	/**
	 * Removes the potion effect from the entity, if the entity is a EntityLivingBase
	 *
	 * @param world - world that the entity is currently present ing
	 * @param entity - entity to apply the effect to
	 * @param data - any extra data you want to pass along
	 */
	@Override
	public void apply(World world, Entity entity, Random random, Object data)
	{
		if (potionId > 0)
		{
			if (entity instanceof EntityLivingBase)
			{
				((EntityLivingBase)entity).removePotionEffect(potionId);
			}
		}
	}

	@Override
	protected void getActualDescription(List<String> list)
	{
		final PotionEffect pe = new PotionEffect(getPotionID(), 1000, 0);
		final String potionName = GrcI18n.translate(pe.getEffectName()).trim();
		list.add(GrcI18n.translate("grc.effect.remove_potion_effect.format", potionName));
	}

	@Override
	protected void readFromNBT(NBTTagCompound data)
	{
		this.potionId = data.getInteger("potion_id");
	}

	@Override
	protected void writeToNBT(NBTTagCompound data)
	{
		data.setInteger("potion_id", potionId);
	}
}
