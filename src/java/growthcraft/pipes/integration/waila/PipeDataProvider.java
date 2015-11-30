package growthcraft.pipes.integration.waila;

import java.util.List;

import growthcraft.api.core.GrcColour;
import growthcraft.pipes.tileentity.TileEntityPipeBase;

import cpw.mods.fml.common.Optional;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PipeDataProvider implements IWailaDataProvider
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
		final NBTTagCompound tag = accessor.getNBTData();
		if (accessor.getTileEntity() instanceof TileEntityPipeBase)
		{
			tooltip.add("PipeState : " + tag.getInteger("pipe_state"));
			tooltip.add("Colour : " + GrcColour.toColour(tag.getInteger("colour")));
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
		if (te instanceof TileEntityPipeBase)
		{
			final TileEntityPipeBase pipeBase = (TileEntityPipeBase)te;
			tag.setInteger("pipe_state", pipeBase.getPipeRenderState());
			tag.setInteger("colour", pipeBase.getColour().ordinal());
		}
		return tag;
	}
}
