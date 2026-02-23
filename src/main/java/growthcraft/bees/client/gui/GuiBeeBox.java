package growthcraft.bees.client.gui;

import growthcraft.bees.common.inventory.ContainerBeeBox;
import growthcraft.bees.common.tileentity.TileEntityBeeBox;
import growthcraft.core.client.gui.GrcGuiContainer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiBeeBox extends GrcGuiContainer<ContainerBeeBox, TileEntityBeeBox>
{
	private static final ResourceLocation res = new ResourceLocation("grcbees" , "textures/guis/beebox_gui.png");
	private TileEntityBeeBox te;

	public GuiBeeBox(InventoryPlayer inv, TileEntityBeeBox beeBox)
	{
		super(res, new ContainerBeeBox(inv, beeBox), beeBox);
		this.te = beeBox;
		this.xSize = 176;
		this.ySize = 200;
	}
}
