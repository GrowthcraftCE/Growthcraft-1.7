package growthcraft.fishtrap.gui;

import growthcraft.fishtrap.block.ContainerFishTrap;
import growthcraft.fishtrap.entity.TileEntityFishTrap;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiFishTrap extends GuiContainer
{
	private static final ResourceLocation res = new ResourceLocation("grcfishtrap" , "textures/guis/fishtrap_gui.png");
	private TileEntityFishTrap te;

	public GuiFishTrap(InventoryPlayer inv, TileEntityFishTrap fishTrap)
	{
		super(new ContainerFishTrap(inv, fishTrap));
		this.te = fishTrap;
		this.ySize = 133;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		final String s = this.te.hasCustomInventoryName() ? this.te.getInventoryName() : I18n.format(this.te.getInventoryName());
		this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(res);
		final int w = (this.width - this.xSize) / 2;
		final int h = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(w, h, 0, 0, this.xSize, this.ySize);
	}
}
