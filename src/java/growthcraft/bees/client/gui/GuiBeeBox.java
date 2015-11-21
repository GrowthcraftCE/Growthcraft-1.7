package growthcraft.bees.client.gui;

import growthcraft.bees.common.inventory.ContainerBeeBox;
import growthcraft.bees.common.tileentity.TileEntityBeeBox;
import growthcraft.core.client.gui.GrcGuiContainer;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiBeeBox extends GrcGuiContainer
{
	private static final ResourceLocation res = new ResourceLocation("grcbees" , "textures/guis/beebox_gui.png");
	private TileEntityBeeBox te;

	public GuiBeeBox(InventoryPlayer inv, TileEntityBeeBox beeBox)
	{
		super(new ContainerBeeBox(inv, beeBox), beeBox);
		this.te = beeBox;
		this.xSize = 176;
		this.ySize = 200;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		mc.getTextureManager().bindTexture(res);

		final int w = (width - xSize) / 2;
		final int h = (height - ySize) / 2;

		drawTexturedModalRect(w, h, 0, 0, this.xSize, this.ySize);
	}
}
