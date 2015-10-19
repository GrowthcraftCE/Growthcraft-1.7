package growthcraft.core.integration.waila;

import java.util.List;

import growthcraft.core.GrowthCraftCore;
import growthcraft.core.block.ICropDataProvider;
import growthcraft.core.util.ItemUtils;

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
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class CropDataProvider implements IWailaDataProvider
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
		if (itemStack != null)
		{
			if (GrowthCraftCore.getConfig().useAmazingStick)
			{
				if (ItemUtils.isAmazingStick(itemStack))
				{
					tooltip.add("So, I heard you didn't have a wrench");
				}
			}
		}
		if (block instanceof ICropDataProvider)
		{
			final ICropDataProvider	prov = (ICropDataProvider)block;
			final MovingObjectPosition pos = accessor.getPosition();
			final float growth = prov.getGrowthProgress(accessor.getWorld(), pos.blockX, pos.blockY, pos.blockZ, accessor.getMetadata());
			String content = EnumChatFormatting.GRAY + StatCollector.translateToLocal("grc.format.crop.growth_prefix") + " " + EnumChatFormatting.WHITE;
			if (growth >= 1.0f)
			{
				content += StatCollector.translateToLocal("grc.format.crop.mature");
			}
			else
			{
				content += StatCollector.translateToLocalFormatted("grc.format.crop.progress_format", (int)(growth * 100));
			}
			tooltip.add(content);
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
		return tag;
	}
}
