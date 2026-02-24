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

public class BlockBeeBoxThaumcraft extends BlockBeeBox
{
	public BlockBeeBoxThaumcraft()
	{
		super();
		this.setBlockName("grc.BeeBox.Thaumcraft");
	}

	@Override
	public String getMetaname(int meta)
	{
		if (meta >= 0 && meta < EnumBeeBoxThaumcraft.VALUES.length)
		{
			return EnumBeeBoxThaumcraft.VALUES[meta].name;
		}
		return super.getMetaname(meta);
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void getSubBlocks(Item block, CreativeTabs tab, List list)
	{
		list.add(new ItemStack(block, 1, EnumBeeBoxThaumcraft.GREATWOOD.meta));
		list.add(new ItemStack(block, 1, EnumBeeBoxThaumcraft.SILVERWOOD.meta));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		this.icons = new IIcon[2 * 4];
		registerBeeBoxIcons(reg, "/thaumcraft/greatwood/", EnumBeeBoxThaumcraft.GREATWOOD.meta);
		registerBeeBoxIcons(reg, "/thaumcraft/silverwood/", EnumBeeBoxThaumcraft.SILVERWOOD.meta);
	}
}
