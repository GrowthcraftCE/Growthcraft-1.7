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
package growthcraft.bees.common.block;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockBeeBoxForestry extends BlockBeeBox
{
	private final EnumBeeBoxForestry[] beeboxTypes;
	private final boolean isFireproofFlag;
	private final int metaOffset;
	private final int subIndex;

	public BlockBeeBoxForestry(EnumBeeBoxForestry[] types, int offset, int index, boolean fireproof)
	{
		super();
		this.beeboxTypes = types;
		this.metaOffset = offset;
		this.subIndex = index;
		this.isFireproofFlag = fireproof;
		setHardness(2f);
		setBlockName(String.format("grc.BeeBox.Forestry.%d.%s", subIndex, isFireproofFlag ? "Fireproof" : "Normal"));
	}

	public EnumBeeBoxForestry getBeeBoxType(World world, int x, int y, int z)
	{
		final int meta = world.getBlockMetadata(x, y, z);
		return beeboxTypes[MathHelper.clamp_int(meta, 0, beeboxTypes.length)];
	}

	@Override
	public float getBlockHardness(World world, int x, int y, int z)
	{
		return getBeeBoxType(world, x, y, z).getHardness();
	}

	public boolean isFireproof()
	{
		return isFireproofFlag;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void getSubBlocks(Item block, CreativeTabs tab, List list)
	{
		for (EnumBeeBoxForestry type : beeboxTypes)
		{
			list.add(new ItemStack(block, 1, type.col));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		this.icons = new IIcon[4 * beeboxTypes.length];
		for (EnumBeeBoxForestry type : beeboxTypes)
		{
			registerBeeBoxIcons(reg, String.format("/forestry/%s/", type.name), type.col);
		}
	}
}
