package growthcraft.core.utils;

import growthcraft.api.cellar.Booze;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class UnitFormatter
{
	public static String fraction(String a, String b)
	{
		return a + EnumChatFormatting.GRAY + " / " + b;
	}

	public static String fractionNum(int a, int b)
	{
		return fraction("" + EnumChatFormatting.WHITE + a, "" + EnumChatFormatting.WHITE + b);
	}

	/**
	 * @param fluid - fluid to get a modifier for
	 * @return localized format of the boolean
	 */
	public static String fluidModifier(Fluid fluid)
	{
		return EnumChatFormatting.GREEN + StatCollector.translateToLocal(fluid.getUnlocalizedName() + ".modifier");
	}

	/**
	 * Formats a fluid name, handling booze and their modifiers specially
	 *
	 * @param fluidStack - fluid source
	 * @return localized format of the boolean
	 */
	public static String fluidName(FluidStack fluidStack)
	{
		if (fluidStack != null)
		{
			final Fluid fluid = fluidStack.getFluid();
			if (fluid instanceof Booze)
			{
				return StatCollector.translateToLocalFormatted("grc.format.booze.name",
					EnumChatFormatting.WHITE + fluidStack.getLocalizedName(),
					fluidModifier(fluid));
			}
			else
			{
				return EnumChatFormatting.WHITE + fluidStack.getLocalizedName();
			}
		}
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
