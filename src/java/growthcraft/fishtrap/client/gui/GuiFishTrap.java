package growthcraft.fishtrap.client.gui;

import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.core.client.gui.GrcGuiContainer;
import growthcraft.fishtrap.common.inventory.ContainerFishTrap;
import growthcraft.fishtrap.common.tileentity.TileEntityFishTrap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiFishTrap extends GrcGuiContainer<ContainerFishTrap, TileEntityFishTrap>
{
	private static final ResourceLocation res = new ResourceLocation("grcfishtrap" , "textures/guis/fishtrap_gui.png");
	private TileEntityFishTrap te;

	public GuiFishTrap(InventoryPlayer inv, TileEntityFishTrap fishTrap)
	{
		super(res, new ContainerFishTrap(inv, fishTrap), fishTrap);
		this.ySize = 133;
	}
}
