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
package growthcraft.core.integration.forestry;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public enum EnumForestryWoodType
{
	LARCH("larch"),
	TEAK("teak"),
	ACACIA("acacia"),
	LIME("lime"),
	CHESTNUT("chestnut"),
	WENGE("wenge"),
	BAOBAB("baobab"),
	SEQUOIA("sequoia", 4.0f),
	KAPOK("kapok"),
	EBONY("ebony"),
	MAHOGANY("mahogany"),
	BALSA("balsa", 1.0f),
	WILLOW("willow"),
	WALNUT("walnut"),
	GREENHEART("greenheart", 7.5f),
	CHERRY("cherry"),
	MAHOE("mahoe"),
	POPLAR("poplar"),
	PALM("palm"),
	PAPAYA("papaya"),
	PINE("pine", 3.0f),
	PLUM("plum"),
	MAPLE("maple"),
	CITRUS("citrus"),
	GIGANTEUM("giganteum"),
	IPE("ipe"),
	PADAUK("padauk"),
	COCOBOLO("cocobolo"),
	ZEBRAWOOD("zebrawood");

	public static final EnumForestryWoodType[] VALUES = values();

	public final String name;
	public final float hardness;

	private EnumForestryWoodType(String n, float h)
	{
		this.name = n;
		this.hardness = h;
	}

	private EnumForestryWoodType(String n)
	{
		this(n, 2.0f);
	}

	public float getHardness()
	{
		return hardness;
	}

	public NBTTagCompound newWoodCompoundTag()
	{
		final NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("WoodType", ordinal());
		return tag;
	}

	public ItemStack getForestryWoodStack(String blockname)
	{
		final Block block = GameRegistry.findBlock(ForestryPlatform.MOD_ID, blockname);
		if (block != null)
		{
			final ItemStack result = new ItemStack(block);
			result.setTagCompound(newWoodCompoundTag());
			return result;
		}
		return null;
	}

	public ItemStack getPlanksItemStack()
	{
		return getForestryWoodStack("planks");
	}

	public ItemStack getFireproofPlanksItemStack()
	{
		return getForestryWoodStack("planksFireproof");
	}
}
