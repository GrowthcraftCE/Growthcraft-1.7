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
package growthcraft.core.integration.waila;

import java.util.List;

import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.api.core.nbt.NBTHelper;
import growthcraft.core.common.tileentity.ITileProgressiveDevice;
import growthcraft.core.common.tileentity.ITileHeatedDevice;
import growthcraft.core.common.tileentity.ITileNamedFluidTanks;
import growthcraft.core.util.TagFormatterFluidHandler;

import cpw.mods.fml.common.Optional;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidHandler;

public class CoreDataProvider implements IWailaDataProvider
{
	@Override
	@Optional.Method(modid = "Waila")
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return accessor.getStack();
	}

	@Override
	@Optional.Method(modid = "Waila")
	public List<String> getWailaHead(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return tooltip;
	}

	@Override
	@Optional.Method(modid = "Waila")
	public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		final Block block = accessor.getBlock();
		final TileEntity te = accessor.getTileEntity();
		final NBTTagCompound tag = accessor.getNBTData();
		if (config.getConfig("DisplayFluidContent"))
		{
			if (te instanceof IFluidHandler)
			{
				tooltip = TagFormatterFluidHandler.INSTANCE.format(tooltip, tag);
			}
		}

		if (te instanceof ITileHeatedDevice)
		{
			final boolean isHeated = tag.getBoolean("is_heated");
			final float heat = tag.getFloat("heat_multiplier");
			String result = EnumChatFormatting.GRAY + GrcI18n.translate("grccore.device.heated.prefix");
			if (isHeated)
			{
				result += EnumChatFormatting.WHITE + GrcI18n.translate("grccore.device.heated.multiplier.format", (int)(heat * 100));
			}
			else
			{
				result += EnumChatFormatting.WHITE + GrcI18n.translate("grccore.device.heated.state.false");
			}
			tooltip.add(result);
		}

		if (te instanceof ITileProgressiveDevice)
		{
			final float prog = tag.getFloat("device_progress");
			if (prog > 0)
			{
				final String result = EnumChatFormatting.GRAY + GrcI18n.translate("grccore.device.progress.prefix") +
					EnumChatFormatting.WHITE + GrcI18n.translate("grccore.device.progress.format", (int)(prog * 100));
				tooltip.add(result);
			}
		}
		return tooltip;
	}

	@Override
	@Optional.Method(modid = "Waila")
	public List<String> getWailaTail(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return tooltip;
	}

	@Override
	@Optional.Method(modid = "Waila")
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z)
	{
		if (te instanceof IFluidHandler) NBTHelper.writeIFluidHandlerToNBT((IFluidHandler)te, tag);
		if (te instanceof ITileNamedFluidTanks) ((ITileNamedFluidTanks)te).writeFluidTankNamesToTag(tag);
		if (te instanceof ITileProgressiveDevice)
		{
			final ITileProgressiveDevice device = (ITileProgressiveDevice)te;
			tag.setFloat("device_progress", device.getDeviceProgress());
		}
		if (te instanceof ITileHeatedDevice)
		{
			final ITileHeatedDevice device = (ITileHeatedDevice)te;
			tag.setBoolean("is_heated", device.isHeated());
			tag.setFloat("heat_multiplier", device.getHeatMultiplier());
		}
		return tag;
	}
}
