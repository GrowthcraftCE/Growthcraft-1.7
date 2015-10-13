package growthcraft.bees.gui;

import growthcraft.bees.inventory.ContainerBeeBox;
import growthcraft.bees.tileentity.TileEntityBeeBox;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandlerBees implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		final TileEntity te = world.getTileEntity(x, y, z);

		if (te instanceof TileEntityBeeBox)
		{
			return new ContainerBeeBox(player.inventory, (TileEntityBeeBox)te);
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		final TileEntity te = world.getTileEntity(x, y, z);

		if (te instanceof TileEntityBeeBox)
		{
			return new GuiBeeBox(player.inventory, (TileEntityBeeBox)te);
		}

		return null;
	}
}
