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
package growthcraft.core.common.item;

import java.util.List;

import growthcraft.api.core.effect.IEffect;
import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.core.lib.GrcCoreState;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class GrcItemFoodBase extends ItemFood
{
	private IEffect effect;

	public GrcItemFoodBase(int hunger, float saturation, boolean isWolfFav)
	{
		super(hunger, saturation, isWolfFav);
	}

	public GrcItemFoodBase(int hunger, boolean isWolfFav)
	{
		super(hunger, isWolfFav);
	}

	public GrcItemFoodBase setEffect(IEffect ef)
	{
		this.effect = ef;
		return this;
	}

	public IEffect getEffect()
	{
		return effect;
	}

	protected void applyIEffects(ItemStack itemStack, World world, EntityPlayer player)
	{
		if (effect != null)
		{
			effect.apply(world, player, world.rand, itemStack);
		}
	}

	@Override
	protected void onFoodEaten(ItemStack itemStack, World world, EntityPlayer player)
	{
		super.onFoodEaten(itemStack, world, player);
		applyIEffects(itemStack, world, player);
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean showAdvanced)
	{
		super.addInformation(stack, player, list, showAdvanced);
		GrcItemBase.addDescription(this, stack, player, list, showAdvanced);
		if (GrcCoreState.showDetailedInformation())
		{
			if (effect != null)
			{
				effect.getDescription((List<String>)list);
			}
		}
		else
		{
			list.add(EnumChatFormatting.GRAY +
					GrcI18n.translate("grc.tooltip.detailed_information",
						EnumChatFormatting.WHITE + GrcCoreState.detailedKey + EnumChatFormatting.GRAY));
		}
	}
}
