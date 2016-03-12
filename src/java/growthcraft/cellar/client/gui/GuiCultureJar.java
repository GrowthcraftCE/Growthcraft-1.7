package growthcraft.cellar.client.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;

import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.cellar.client.gui.widget.GuiButtonDiscard;
import growthcraft.cellar.client.resource.GrcCellarResources;
import growthcraft.cellar.common.inventory.ContainerCultureJar;
import growthcraft.cellar.common.tileentity.TileEntityCultureJar;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.network.PacketClearTankButton;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiCultureJar extends GuiCellar
{
	private ResourceLocation fermentJarResource = GrcCellarResources.INSTANCE.textureGuiCultureJar;
	private TileEntityCultureJar te;
	private GuiButtonDiscard discardButton;

	public GuiCultureJar(InventoryPlayer inv, TileEntityCultureJar fermentJar)
	{
		super(new ContainerCultureJar(inv, fermentJar), fermentJar);
		this.te = fermentJar;
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void initGui()
	{
		super.initGui();
		if (GrowthCraftCellar.getConfig().enableDiscardButton)
		{
			this.discardButton = new GuiButtonDiscard(fermentJarResource, 1, guiLeft + 116, guiTop + 54);
			discardButton.enabled = false;
			buttonList.add(discardButton);
		}

		addTooltipIndex("fluid_tank.primary", 36, 17, 16, 58);
		if (discardButton != null) addTooltipIndex("fluid_tank.primary.discard", 16, 52, 16, 16);
	}

	@Override
	protected void actionPerformed(GuiButton butn)
	{
		GrowthCraftCellar.packetPipeline.sendToServer(new PacketClearTankButton(te.xCoord, te.yCoord, te.zCoord));
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
		if (discardButton != null) discardButton.enabled = te.isFluidTankFilled(0);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(fermentJarResource);
		final int x = (width - xSize) / 2;
		final int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		final int progressWidth = te.getProgressScaled(22);
		drawTexturedModalRect(x + 55, y + 35, 176, 0, progressWidth, 17);

		final int fluidScaled = te.getFluidAmountScaled(58, 0);
		if (fluidScaled > 0)
		{
			drawTank(x, y, 36, 14, 16, 58, fluidScaled, te.getFluidStack(0), te.getFluidTank(0));
			mc.getTextureManager().bindTexture(fermentJarResource);
			// Rule overlay
			drawTexturedModalRect(x + 36, y + 14, 176, 53, 16, 58);
		}

		final int iconHeight = te.getHeatScaled(14);
		final int offY = 14 - iconHeight;
		drawTexturedModalRect(x + 82, y + 56 + offY, 176, 17 + offY, 14, iconHeight);

		if (!te.isCulturing())
		{
			// Red Ring around Yeast Slot
			drawTexturedModalRect(x + 77, y + 32, 176, 31, 22, 22);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y)
	{
		super.drawGuiContainerForegroundLayer(x, y);

		if (!te.isFluidTankEmpty(0))
		{
			final String s = String.valueOf(te.getFluidAmount(0));
			fontRendererObj.drawStringWithShadow(s, xSize - 124 - fontRendererObj.getStringWidth(s), ySize - 104, 0xFFFFFF);
		}
	}

	@Override
	protected void addTooltips(String handle, List<String> tooltip)
	{
		switch (handle)
		{
			case "fluid_tank.primary":
				addFluidTooltips(te.getFluidStack(0), tooltip);
				break;
			case "fluid_tank.primary.discard":
				tooltip.add(GrcI18n.translate("gui.grc.discard"));
				break;
			default:
				break;
		}
	}
}
