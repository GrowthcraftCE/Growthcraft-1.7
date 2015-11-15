package growthcraft.cellar.client.gui;

import growthcraft.cellar.common.tileentity.TileEntityFermentJar;
import growthcraft.cellar.common.inventory.ContainerFermentJar;

import net.minecraft.entity.player.InventoryPlayer;

public class GuiFermentJar extends GuiCellar
{
	private TileEntityFermentJar te;

	public GuiFermentJar(InventoryPlayer inv, TileEntityFermentJar fermentJar)
	{
		super(new ContainerFermentJar(inv, fermentJar));
		this.te = fermentJar;
	}
}
