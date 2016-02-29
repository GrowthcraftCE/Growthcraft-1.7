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
package growthcraft.cellar.util;

import java.util.List;

import growthcraft.api.cellar.booze.BoozeEffect;
import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.core.CoreRegistry;
import growthcraft.api.core.description.Describer;
import growthcraft.core.util.UnitFormatter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class BoozeUtils
{
	private BoozeUtils() {}

	public static float alcoholToTipsy(float alcoholRate)
	{
		return alcoholRate * 4;
	}

	public static boolean isFermentedBooze(Fluid booze)
	{
		return CoreRegistry.instance().fluidDictionary().hasFluidTags(booze, BoozeTag.FERMENTED);
	}

	public static void addEffects(Fluid booze, ItemStack stack, World world, EntityPlayer player)
	{
		if (booze == null) return;

		final BoozeEffect effect = CellarRegistry.instance().booze().getEffect(booze);
		if (effect != null)
		{
			effect.apply(world, player, world.rand, null);
		}
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static void addInformation(Fluid booze, ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		if (booze == null) return;
		final String s = UnitFormatter.fluidModifier(booze);
		if (s != null) list.add(s);
		Describer.getDescription((List<String>)list, booze);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static void addEffectInformation(Fluid booze, ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		if (booze == null) return;
		final BoozeEffect effect = CellarRegistry.instance().booze().getEffect(booze);
		if (effect != null)
		{
			effect.getDescription((List<String>)list);
		}
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static void addBottleInformation(Fluid booze, ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		if (booze == null) return;
		addInformation(booze, stack, player, list, bool);
		addEffectInformation(booze, stack, player, list, bool);
	}

	public static boolean hasEffect(Fluid booze)
	{
		final BoozeEffect effect = CellarRegistry.instance().booze().getEffect(booze);
		if (effect != null) return effect.isValid();
		return false;
	}
}
