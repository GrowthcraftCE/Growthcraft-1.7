package growthcraft.bees.integration.waila;

import java.util.List;

import growthcraft.bees.tileentity.TileEntityBeeBox;
import growthcraft.bees.util.TagFormatterBeeBox;
import growthcraft.core.util.NBTHelper;

import cpw.mods.fml.common.Optional;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BeesDataProvider implements IWailaDataProvider
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
		if (accessor.getTileEntity() instanceof TileEntityBeeBox)
		{
			final NBTTagCompound tag = accessor.getNBTData();
			tooltip = TagFormatterBeeBox.INSTANCE.format(tooltip, tag);
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
		if (te instanceof TileEntityBeeBox)
		{
			final TileEntityBeeBox beeBox = (TileEntityBeeBox)te;
			tag.setBoolean("has_bonus", beeBox.hasBonus());
			tag.setInteger("honeycomb_count", beeBox.countCombs());
			tag.setInteger("honeycomb_max", beeBox.getSizeInventory());
			tag.setInteger("honey_count", beeBox.countHoney());
			tag.setTag("bee", NBTHelper.writeItemStackToNBT(beeBox.getBeeStack()));
		}
		return tag;
	}
}
