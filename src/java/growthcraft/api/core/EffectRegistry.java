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
package growthcraft.api.core;

import javax.annotation.Nonnull;

import growthcraft.api.core.effect.EffectAddPotionEffect;
import growthcraft.api.core.effect.EffectChance;
import growthcraft.api.core.effect.EffectList;
import growthcraft.api.core.effect.EffectNull;
import growthcraft.api.core.effect.EffectRandomList;
import growthcraft.api.core.effect.EffectRemovePotionEffect;
import growthcraft.api.core.effect.EffectWeightedRandomList;
import growthcraft.api.core.effect.IEffect;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.NullLogger;

import net.minecraft.nbt.NBTTagCompound;

public class EffectRegistry extends AbstractClassRegistry<IEffect> implements IEffectRegistry
{
	private ILogger logger = NullLogger.INSTANCE;

	public EffectRegistry initialize()
	{
		register("add_potion_effect", EffectAddPotionEffect.class);
		register("chance", EffectChance.class);
		register("list", EffectList.class);
		register("null", EffectNull.class);
		register("random_list", EffectRandomList.class);
		register("remove_potion_effect", EffectRemovePotionEffect.class);
		register("weighted_random_list", EffectWeightedRandomList.class);
		return this;
	}

	@Override
	public void setLogger(@Nonnull ILogger l)
	{
		this.logger = l;
	}

	@Override
	public IEffect loadEffectFromNBT(@Nonnull NBTTagCompound data, @Nonnull String name)
	{
		return loadObjectFromNBT(data, name);
	}
}
