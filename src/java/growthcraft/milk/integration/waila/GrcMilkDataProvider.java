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
package growthcraft.milk.integration.waila;

import java.util.List;

import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.api.core.nbt.NBTHelper;
import growthcraft.milk.common.tileentity.TileEntityButterChurn;
import growthcraft.milk.common.tileentity.TileEntityCheeseBlock;
import growthcraft.milk.common.tileentity.TileEntityCheesePress;
import growthcraft.milk.common.tileentity.TileEntityHangingCurds;
import growthcraft.milk.util.TagFormatterButterChurn;
import growthcraft.milk.util.TagFormatterCheesePress;

import cpw.mods.fml.common.Optional;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class GrcMilkDataProvider implements IWailaDataProvider
{
	@Override
	@Optional.Method(modid="Waila")
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		final TileEntity te = accessor.getTileEntity();
		if (te instanceof TileEntityCheeseBlock)
		{
			return ((TileEntityCheeseBlock)te).asItemStack();
		}
		if (te instanceof TileEntityHangingCurds)
		{
			return ((TileEntityHangingCurds)te).asItemStack();
		}
		return accessor.getStack();
	}

	@Override
	@Optional.Method(modid="Waila")
	public List<String> getWailaHead(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return tooltip;
	}

	@Override
	@Optional.Method(modid="Waila")
	public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		final TileEntity te = accessor.getTileEntity();
		final NBTTagCompound nbt = accessor.getNBTData();
		if (te instanceof TileEntityButterChurn)
		{
			TagFormatterButterChurn.INSTANCE.format(tooltip, nbt);
		}
		if (te instanceof TileEntityCheesePress)
		{
			TagFormatterCheesePress.INSTANCE.format(tooltip, nbt);
		}
		if (te instanceof TileEntityCheeseBlock)
		{
			if (nbt.getBoolean("is_aged"))
			{
				tooltip.add(EnumChatFormatting.GRAY + GrcI18n.translate("grcmilk.cheese.slices.prefix") +
					EnumChatFormatting.WHITE + GrcI18n.translate("grcmilk.cheese.slices.value.format", nbt.getInteger("slices"), nbt.getInteger("slices_max")));
			}
			else
			{
				final float ageProgress = nbt.getFloat("age_progress");
				final String result = EnumChatFormatting.GRAY + GrcI18n.translate("grcmilk.cheese.aging.prefix") +
					EnumChatFormatting.WHITE + GrcI18n.translate("grcmilk.cheese.aging.progress.format", (int)(ageProgress * 100));
				tooltip.add(result);
			}
		}
		if (te instanceof TileEntityHangingCurds)
		{
			final float progress = nbt.getFloat("progress");
			if (progress < 1f)
			{
				final String result = EnumChatFormatting.GRAY + GrcI18n.translate("grcmilk.hanging_curds.drying.prefix") +
					EnumChatFormatting.WHITE + GrcI18n.translate("grcmilk.hanging_curds.drying.progress.format", (int)(progress * 100));
				tooltip.add(result);
			}
			if (nbt.hasKey("dried"))
			{
				final boolean dried = nbt.getBoolean("dried");
				if (dried)
				{
					tooltip.add(GrcI18n.translate("grcmilk.hanging_curds.dried"));
				}
			}
		}
		return tooltip;
	}

	@Override
	@Optional.Method(modid="Waila")
	public List<String> getWailaTail(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return tooltip;
	}

	private void getButterChurnData(TileEntityButterChurn te, NBTTagCompound nbt)
	{
		nbt.setTag("item", NBTHelper.writeItemStackToNBT(te.getStackInSlot(0), new NBTTagCompound()));
	}

	private void getCheesePressData(TileEntityCheesePress te, NBTTagCompound nbt)
	{
		nbt.setBoolean("pressed", te.isPressed());
		nbt.setTag("item", NBTHelper.writeItemStackToNBT(te.getStackInSlot(0), new NBTTagCompound()));
	}

	@Override
	@Optional.Method(modid="Waila")
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z)
	{
		if (te instanceof TileEntityButterChurn) getButterChurnData((TileEntityButterChurn)te, tag);
		if (te instanceof TileEntityCheesePress) getCheesePressData((TileEntityCheesePress)te, tag);
		if (te instanceof TileEntityCheeseBlock)
		{
			final TileEntityCheeseBlock cheeseBlock = (TileEntityCheeseBlock)te;
			tag.setBoolean("is_aged", cheeseBlock.getCheese().isAged());
			tag.setFloat("age_progress", cheeseBlock.getCheese().getAgeProgress());
			tag.setInteger("slices", cheeseBlock.getCheese().getSlices());
			tag.setInteger("slices_max", cheeseBlock.getCheese().getSlicesMax());
		}
		if (te instanceof TileEntityHangingCurds)
		{
			final TileEntityHangingCurds hangingCurds = (TileEntityHangingCurds)te;
			tag.setFloat("progress", hangingCurds.getProgress());
			tag.setBoolean("dried", hangingCurds.isDried());
		}
		return tag;
	}
}
