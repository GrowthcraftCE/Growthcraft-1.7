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
package growthcraft.cellar.common.itemblock;

import java.util.List;

import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.core.common.item.GrcItemTileBlockBase;
import growthcraft.core.lib.GrcCoreState;
import growthcraft.core.util.UnitFormatter;
import growthcraft.cellar.GrowthCraftCellar;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class ItemBlockFermentBarrel extends GrcItemTileBlockBase
{
	public ItemBlockFermentBarrel(Block block)
	{
		super(block);
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced)
	{
		super.addInformation(stack, player, list, advanced);
		if (GrcCoreState.showDetailedInformation())
		{
			final NBTTagCompound tag = getTileTagCompound(stack);
			if (tag != null)
			{
				if (tag.hasKey("Tank0"))
				{
					final NBTTagCompound tank = tag.getCompoundTag("Tank0");
					if (!tank.hasKey("Empty"))
					{
						final FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(tank);
						final String fluidName = UnitFormatter.fluidName(fluidStack);
						if (fluidName != null)
						{
							list.add(GrcI18n.translate("grc.cellar.format.fluid_container.contents",
								fluidName,
								fluidStack.amount,
								GrowthCraftCellar.getConfig().fermentBarrelMaxCap));
						}
					}
				}
			}
		}
	}
}
