package growthcraft.cellar.integration.waila;

import java.util.List;

import growthcraft.core.utils.TagFormatterFluidHandler;
import growthcraft.core.utils.ConstID;
import growthcraft.cellar.block.BlockBrewKettle;
import growthcraft.cellar.block.BlockFermentBarrel;
import growthcraft.cellar.block.BlockFruitPress;

import cpw.mods.fml.common.Optional;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidTankInfo;
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
		TileEntity te = accessor.getTileEntity();
		if (te instanceof IFluidHandler)
		{
			final NBTTagCompound tag = accessor.getNBTData();
			tooltip = TagFormatterFluidHandler.INSTANCE.format(tooltip, tag);
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
		if (te instanceof IFluidHandler)
		{
			final IFluidHandler fluidHandler = (IFluidHandler)te;
			final NBTTagList tankTagList = new NBTTagList();
			int tankId = 0;
			for (FluidTankInfo tankInfo : fluidHandler.getTankInfo(ForgeDirection.UNKNOWN))
			{
				final NBTTagCompound tankTag = new NBTTagCompound();
				tankTag.setInteger("tank_id", tankId);
				tankTag.setInteger("capacity", tankInfo.capacity);
				if (tankInfo.fluid != null)
				{
					tankTag.setInteger("fluid_id", tankInfo.fluid.getFluidID());
					tankTag.setInteger("amount", tankInfo.fluid.amount);
				}
				else
				{
					// no fluid
					tankTag.setInteger("fluid_id", ConstID.NO_FLUID);
					tankTag.setInteger("amount", 0);
				}
				tankTagList.appendTag(tankTag);
				++tankId;
			}
			tag.setTag("tanks", tankTagList);
			tag.setInteger("tank_count", tankId);
		}
		return tag;
	}
}
