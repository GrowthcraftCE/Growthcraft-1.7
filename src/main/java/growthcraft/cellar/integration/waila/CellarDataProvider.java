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
package growthcraft.cellar.integration.waila;

import java.util.List;

import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.cellar.common.block.BlockFruitPresser;
import growthcraft.cellar.common.tileentity.TileEntityBrewKettle;
import growthcraft.cellar.common.tileentity.TileEntityFermentBarrel;
import growthcraft.cellar.common.tileentity.TileEntityCultureJar;
import growthcraft.cellar.common.tileentity.TileEntityFruitPress;
import growthcraft.cellar.util.TagFormatterBrewKettle;
import growthcraft.cellar.util.TagFormatterFermentBarrel;
import growthcraft.cellar.util.TagFormatterCultureJar;
import growthcraft.cellar.util.TagFormatterFruitPress;
import growthcraft.api.core.nbt.NBTHelper;

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

public class CellarDataProvider implements IWailaDataProvider
{
	@Override
	@Optional.Method(modid="Waila")
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
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
		final Block block = accessor.getBlock();
		final TileEntity te = accessor.getTileEntity();
		if (block instanceof BlockFruitPresser)
		{
			tooltip.add(EnumChatFormatting.GRAY + GrcI18n.translate("grc.cellar.fruit_presser.state_prefix") + " " +
				EnumChatFormatting.WHITE + GrcI18n.translate("grc.cellar.fruit_presser.state." +
					((BlockFruitPresser)block).getPressStateName(accessor.getMetadata())));
		}
		final NBTTagCompound tag = accessor.getNBTData();
		if (config.getConfig("FermentBarrelExtras"))
		{
			if (te instanceof TileEntityFermentBarrel)
			{
				tooltip = TagFormatterFermentBarrel.INSTANCE.format(tooltip, tag);
			}
		}
		if (config.getConfig("CultureJarExtras"))
		{
			if (te instanceof TileEntityCultureJar)
			{
				tooltip = TagFormatterCultureJar.INSTANCE.format(tooltip, tag);
			}
		}
		if (config.getConfig("BrewKettleExtras"))
		{
			if (te instanceof TileEntityBrewKettle)
			{
				tooltip = TagFormatterBrewKettle.INSTANCE.format(tooltip, tag);
			}
		}
		if (config.getConfig("FruitPressExtras"))
		{
			if (te instanceof TileEntityFruitPress)
			{
				tooltip = TagFormatterFruitPress.INSTANCE.format(tooltip, tag);
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

	private void getBrewKettleData(TileEntityBrewKettle brewKettle, NBTTagCompound tag)
	{
		tag.setBoolean("can_brew", brewKettle.canBrew());
		tag.setTag("item_brew", NBTHelper.writeItemStackToNBT(brewKettle.getStackInSlot(0), new NBTTagCompound()));
		tag.setTag("item_residue", NBTHelper.writeItemStackToNBT(brewKettle.getStackInSlot(1), new NBTTagCompound()));
	}

	private void getFruitPressData(TileEntityFruitPress fruitPress, NBTTagCompound tag)
	{
		tag.setTag("item_press", NBTHelper.writeItemStackToNBT(fruitPress.getStackInSlot(0), new NBTTagCompound()));
		tag.setTag("item_residue", NBTHelper.writeItemStackToNBT(fruitPress.getStackInSlot(1), new NBTTagCompound()));
	}

	private void getCultureJarData(TileEntityCultureJar fermentJar, NBTTagCompound tag)
	{
		tag.setTag("item_yeast", NBTHelper.writeItemStackToNBT(fermentJar.getStackInSlot(0), new NBTTagCompound()));
	}

	private void getFermentBarrelData(TileEntityFermentBarrel fermentBarrel, NBTTagCompound tag)
	{
		tag.setTag("item_modifier", NBTHelper.writeItemStackToNBT(fermentBarrel.getStackInSlot(0), new NBTTagCompound()));
	}

	@Override
	@Optional.Method(modid="Waila")
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z)
	{
		if (te instanceof TileEntityBrewKettle) getBrewKettleData((TileEntityBrewKettle)te, tag);
		if (te instanceof TileEntityFruitPress) getFruitPressData((TileEntityFruitPress)te, tag);
		if (te instanceof TileEntityFermentBarrel) getFermentBarrelData((TileEntityFermentBarrel)te, tag);
		if (te instanceof TileEntityCultureJar) getCultureJarData((TileEntityCultureJar)te, tag);
		return tag;
	}
}
