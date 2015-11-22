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
package growthcraft.core.util;

import javax.annotation.Nullable;

import com.google.common.base.Joiner;

import growthcraft.api.cellar.CellarRegistry;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * Utility class for formatting data into strings for descriptions, item names
 * and othre nicities
 */
public class UnitFormatter
{
	public static final String fractionSeparator = EnumChatFormatting.GRAY + " / ";

	private UnitFormatter() {}

	public static String fraction(String... args)
	{
		return Joiner.on(fractionSeparator).join(args);
	}

	public static String fractionNum(int a, int b)
	{
		return fraction("" + EnumChatFormatting.WHITE + a, "" + EnumChatFormatting.WHITE + b);
	}

	public static String fluidBucketName(Fluid fluid)
	{
		return StatCollector.translateToLocal("item.bucket." + fluid.getUnlocalizedName() + ".name");
	}

	/**
	 * @param fluid - fluid to get a modifier for
	 * @return localized format of the boolean
	 */
	@Nullable
	public static String fluidModifier(Fluid fluid)
	{
		final Fluid alt = CellarRegistry.instance().booze().maybeAlternateBooze(fluid);
		if (CellarRegistry.instance().booze().isFluidBooze(alt))
		{
			return EnumChatFormatting.GREEN + StatCollector.translateToLocal(alt.getUnlocalizedName() + ".modifier");
		}
		else
		{
			return null;
		}
	}

	@Nullable
	public static String fluidModifier(FluidStack fluid)
	{
		return fluidModifier(fluid.getFluid());
	}

	@Nullable
	public static String fluidName(FluidStack fluidStack)
	{
		if (fluidStack != null)
		{
			final FluidStack altStack = CellarRegistry.instance().booze().maybeAlternateBoozeStack(fluidStack);
			final Fluid fluid = altStack.getFluid();
			final String modifier = fluidModifier(fluid);

			if (modifier != null)
			{
				return StatCollector.translateToLocalFormatted("grc.format.booze.name",
					EnumChatFormatting.WHITE + altStack.getLocalizedName(), modifier);
			}
			else
			{
				return StatCollector.translateToLocalFormatted("grc.format.fluid.name",
						EnumChatFormatting.WHITE + altStack.getLocalizedName());
			}
		}
		return null;
	}

	/**
	 * Formats a fluid name, handling booze and their modifiers specially
	 *
	 * @param fluidStack - fluid source
	 * @return localized format of the fluid name + its modifiers if any
	 */
	@Nullable
	public static String fluidNameForContainer(FluidStack fluidStack)
	{
		final String name = fluidName(fluidStack);
		if (name != null) return name;
		return invalidFluid();
	}

	/**
	 * @return localized format of the boolean
	 */
	public static String noItem()
	{
		return EnumChatFormatting.GRAY + StatCollector.translateToLocal("grc.format.itemslot.empty");
	}

	/**
	 * @return localized format of the boolean
	 */
	public static String invalidItem()
	{
		return EnumChatFormatting.RED + StatCollector.translateToLocal("grc.format.itemslot.invalid");
	}

	/**
	 * @return localized format of the boolean
	 */
	public static String noFluid()
	{
		return EnumChatFormatting.GRAY + StatCollector.translateToLocal("grc.format.tank.empty");
	}

	/**
	 * @return localized format of the boolean
	 */
	public static String invalidFluid()
	{
		return EnumChatFormatting.RED + StatCollector.translateToLocal("grc.format.invalid_fluid");
	}

	/**
	 * @param b - boolean to format
	 * @return localized format of the boolean
	 */
	public static String booleanAsState(boolean b)
	{
		return StatCollector.translateToLocal("grc.format.state." + b);
	}

	/**
	 * @param b - boolean to format
	 * @return localized format of the boolean
	 */
	public static String booleanAsValue(boolean b)
	{
		return StatCollector.translateToLocal("grc.format.value." + b);
	}
}
