package growthcraft.cellar.handler;

import growthcraft.cellar.client.gui.GuiBrewKettle;
import growthcraft.cellar.client.gui.GuiFermentBarrel;
import growthcraft.cellar.client.gui.GuiCultureJar;
import growthcraft.cellar.client.gui.GuiFruitPress;
import growthcraft.cellar.common.inventory.ContainerBrewKettle;
import growthcraft.cellar.common.inventory.ContainerFermentBarrel;
import growthcraft.cellar.common.inventory.ContainerCultureJar;
import growthcraft.cellar.common.inventory.ContainerFruitPress;
import growthcraft.cellar.common.tileentity.TileEntityBrewKettle;
import growthcraft.cellar.common.tileentity.TileEntityFermentBarrel;
import growthcraft.cellar.common.tileentity.TileEntityCultureJar;
import growthcraft.cellar.common.tileentity.TileEntityFruitPress;

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

		if (te instanceof TileEntityCultureJar)
		{
			return new ContainerCultureJar(player.inventory, (TileEntityCultureJar)te);
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

		if (te instanceof TileEntityCultureJar)
		{
			return new GuiCultureJar(player.inventory, (TileEntityCultureJar)te);
		}

		return null;
	}
}
