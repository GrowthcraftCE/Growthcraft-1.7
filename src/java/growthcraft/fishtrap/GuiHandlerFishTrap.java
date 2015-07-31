package growthcraft.fishtrap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandlerFishTrap implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(x, y, z);

		if(te instanceof TileEntityFishTrap)
		{
			return new ContainerFishTrap(player.inventory, (TileEntityFishTrap)te);
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(x, y, z);

		if(te instanceof TileEntityFishTrap)
		{
			return new GuiFishTrap(player.inventory, (TileEntityFishTrap)te);
		}

		return null;
	}
}
