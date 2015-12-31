package growthcraft.cellar.integration.waila;

import java.util.List;

import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.cellar.common.block.BlockFruitPresser;
import growthcraft.cellar.common.tileentity.TileEntityBrewKettle;
import growthcraft.cellar.common.tileentity.TileEntityFermentBarrel;
import growthcraft.cellar.common.tileentity.TileEntityFermentJar;
import growthcraft.cellar.common.tileentity.TileEntityFruitPress;
import growthcraft.cellar.util.TagFormatterBrewKettle;
import growthcraft.cellar.util.TagFormatterFermentBarrel;
import growthcraft.cellar.util.TagFormatterFermentJar;
import growthcraft.cellar.util.TagFormatterFruitPress;
import growthcraft.core.util.ConstID;
import growthcraft.core.util.NBTHelper;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class CellarDataProvider implements IWailaDataProvider
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
		if (block instanceof BlockFruitPresser)
		{
			tooltip.add(EnumChatFormatting.GRAY + GrcI18n.translate("grc.cellar.fruitPresser.state_prefix") + " " +
				EnumChatFormatting.WHITE + GrcI18n.translate("grc.cellar.fruitPresser.state." +
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
		if (config.getConfig("FermentJarExtras"))
		{
			if (te instanceof TileEntityFermentJar)
			{
				tooltip = TagFormatterFermentJar.INSTANCE.format(tooltip, tag);
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
		if (config.getConfig("DisplayFluidContent"))
		{
			if (te instanceof IFluidHandler)
			{
				tooltip = TagFormatterFluidHandler.INSTANCE.format(tooltip, tag);
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

	private void getFermentJarData(TileEntityFermentJar fermentJar, NBTTagCompound tag)
	{
		tag.setTag("item_yeast", NBTHelper.writeItemStackToNBT(fermentJar.getStackInSlot(0), new NBTTagCompound()));
	}

	private void getFermentBarrelData(TileEntityFermentBarrel fermentBarrel, NBTTagCompound tag)
	{
		tag.setTag("item_modifier", NBTHelper.writeItemStackToNBT(fermentBarrel.getStackInSlot(0), new NBTTagCompound()));
		tag.setInteger("time", fermentBarrel.getTime());
		tag.setInteger("time_max", fermentBarrel.getTimeMax());
		final FluidStack fluidStack = fermentBarrel.getFluidStack(0);
		if (fluidStack != null)
		{
			tag.setInteger("booze_id", fluidStack.getFluidID());
		}
		else
		{
			tag.setInteger("booze_id", ConstID.NO_FLUID);
		}
	}

	@Override
	@Optional.Method(modid = "Waila")
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z)
	{
		if (te instanceof IFluidHandler) NBTHelper.writeIFluidHandlerToNBT((IFluidHandler)te, tag);
		if (te instanceof TileEntityBrewKettle) getBrewKettleData((TileEntityBrewKettle)te, tag);
		if (te instanceof TileEntityFruitPress) getFruitPressData((TileEntityFruitPress)te, tag);
		if (te instanceof TileEntityFermentBarrel) getFermentBarrelData((TileEntityFermentBarrel)te, tag);
		if (te instanceof TileEntityFermentJar) getFermentJarData((TileEntityFermentJar)te, tag);
		return tag;
	}
}
