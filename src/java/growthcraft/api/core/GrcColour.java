/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 AlgorithmX2
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
/* The above is the original license, and to continue with the open source
 * spirit, the license will remain as such
 */

package growthcraft.api.core;

import java.util.Arrays;
import java.util.List;

import net.minecraft.util.StatCollector;

/**
 * Pretty much stolen from Applied Energistics 2, thanks AlgorithmX2.
 */
public enum GrcColour
{
	White("grc.colour.White", 0xBEBEBE, 0xDBDBDB, 0xFAFAFA),
	Orange("grc.colour.Orange", 0xF99739, 0xFAAE44, 0xF4DEC3),
	Magenta("grc.colour.Magenta", 0x821E82, 0xB82AB8, 0xC598C8),
	LightBlue("grc.colour.LightBlue", 0x628DCB, 0x82ACE7, 0xD8F6FF),
	Yellow("grc.colour.Yellow", 0xFFF7AA, 0xF8FF4A, 0xFFFFE8),
	Lime("grc.colour.Lime", 0x7CFF4A, 0xBBFF51, 0xE7F7D7),
	Pink("grc.colour.Pink", 0xDC8DB5, 0xF8B5D7, 0xF7DEEB),
	Gray("grc.colour.Gray", 0x7C7C7C, 0xA0A0A0, 0xC9C9C9),
	LightGray("grc.colour.LightGray", 0x9D9D9D, 0xCDCDCD, 0xEFEFEF),
	Cyan("grc.colour.Cyan", 0x2F9BA5, 0x51AAC6, 0xAEDDF4),
	Purple("grc.colour.Purple", 0x8230B2, 0xA453CE, 0xC7A3CC),
	Blue("grc.colour.Blue", 0x2D29A0, 0x514AFF, 0xDDE6FF),
	Brown("grc.colour.Brown", 0x724E35, 0xB7967F, 0xE0D2C8),
	Green("grc.colour.Green", 0x45A021, 0x60E32E, 0xE3F2E3),
	Red("grc.colour.Red", 0xA50029, 0xFF003C, 0xFFE6ED),
	Black("grc.colour.Black", 0x2B2B2B, 0x565656, 0x848484),
	Transparent("grc.colour.Transparent", 0x1B2344, 0x895CA8, 0xD7BBEC);

	public static final List<GrcColour> ALL_COLORS = Arrays.asList(White, Orange, Magenta, LightBlue, Yellow, Lime, Pink, Gray, LightGray, Cyan, Purple, Blue, Brown, Green, Red, Black, Transparent);
	public static final List<GrcColour> VALID_COLORS = Arrays.asList(White, Orange, Magenta, LightBlue, Yellow, Lime, Pink, Gray, LightGray, Cyan, Purple, Blue, Brown, Green, Red, Black);

	/**
	 * Unlocalized name for color.
	 */
	public final String unlocalizedName;

	/**
	 * Darkest Variant of the color, nearly black; as a RGB HEX Integer
	 */
	public final int blackVariant;

	/**
	 * The Variant of the color that is used to represent the color normally; as a RGB HEX Integer
	 */
	public final int mediumVariant;

	/**
	 * Lightest Variant of the colour, nearly white; as a RGB HEX Integer
	 */
	public final int whiteVariant;

	GrcColour(String unLocname, int blackHex, int medHex, int whiteHex)
	{
		this.unlocalizedName = unLocname;
		this.blackVariant = blackHex;
		this.mediumVariant = medHex;
		this.whiteVariant = whiteHex;
	}

	/**
	 * Logic to see which colours match each other.. special handle for Transparent
	 * @param colour - colour to check
	 * @return true if either colour is transparent, or if the colours are equal
	 */
	public boolean matches(GrcColour colour)
	{
		return this == Transparent || colour == Transparent || this == colour;
	}

	@Override
	public String toString()
	{
		return StatCollector.translateToLocal(this.unlocalizedName);
	}

	/**
	 * @param id - the ordinal value of the colour
	 * @return colour
	 */
	public static GrcColour toColour(int id)
	{
		return ALL_COLORS.get(id);
	}
}
