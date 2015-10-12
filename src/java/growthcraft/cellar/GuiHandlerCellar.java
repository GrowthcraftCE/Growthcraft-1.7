package growthcraft.cellar;

import growthcraft.cellar.container.ContainerBrewKettle;
import growthcraft.cellar.container.ContainerFermentBarrel;
import growthcraft.cellar.container.ContainerFruitPress;
import growthcraft.cellar.gui.GuiBrewKettle;
import growthcraft.cellar.gui.GuiFermentBarrel;
import growthcraft.cellar.gui.GuiFruitPress;
import growthcraft.cellar.tileentity.TileEntityBrewKettle;
import growthcraft.cellar.tileentity.TileEntityFermentBarrel;
import growthcraft.cellar.tileentity.TileEntityFruitPress;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandlerCellar implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		final TileEntity te = world.getTileEntity(x, y, z);

		if (te instanceof TileEntityFruitPress)
		{
			return new ContainerFruitPress(player.inventory, (TileEntityFruitPress)te);
		}

		if (te instanceof TileEntityBrewKettle)
		{
			return new ContainerBrewKettle(player.inventory, (TileEntityBrewKettle)te);
		}

		if (te instanceof TileEntityFermentBarrel)
		{
			return new ContainerFermentBarrel(player.inventory, (TileEntityFermentBarrel)te);
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		final TileEntity te = world.getTileEntity(x, y, z);

		if (te instanceof TileEntityFruitPress)
		{
			return new GuiFruitPress(player.inventory, (TileEntityFruitPress)te);
		}

		if (te instanceof TileEntityBrewKettle)
		{
			return new GuiBrewKettle(player.inventory, (TileEntityBrewKettle)te);
		}

		if (te instanceof TileEntityFermentBarrel)
		{
			return new GuiFermentBarrel(player.inventory, (TileEntityFermentBarrel)te);
		}

		return null;
	}
}
