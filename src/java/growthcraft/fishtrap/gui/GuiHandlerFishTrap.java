package growthcraft.fishtrap.gui;

import growthcraft.fishtrap.block.ContainerFishTrap;
import growthcraft.fishtrap.entity.TileEntityFishTrap;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandlerFishTrap implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		final TileEntity te = world.getTileEntity(x, y, z);

		if (te instanceof TileEntityFishTrap)
		{
			return new ContainerFishTrap(player.inventory, (TileEntityFishTrap)te);
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		final TileEntity te = world.getTileEntity(x, y, z);

		if (te instanceof TileEntityFishTrap)
		{
			return new GuiFishTrap(player.inventory, (TileEntityFishTrap)te);
		}

		return null;
	}
}
