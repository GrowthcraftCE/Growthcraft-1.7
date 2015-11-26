package growthcraft.cellar.client.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;

import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.cellar.client.gui.widget.GuiButtonDiscard;
import growthcraft.cellar.common.inventory.ContainerFruitPress;
import growthcraft.cellar.common.tileentity.TileEntityFruitPress;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.network.PacketClearTankButton;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiFruitPress extends GuiCellar
{
	protected static final ResourceLocation fruitPressResource = new ResourceLocation("grccellar" , "textures/guis/fruitpress_gui.png");
	private TileEntityFruitPress te;
	private GuiButtonDiscard button;

	public GuiFruitPress(InventoryPlayer inv, TileEntityFruitPress fruitPress)
	{
		super(new ContainerFruitPress(inv, fruitPress), fruitPress);
		this.te = fruitPress;
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void initGui()
	{
		super.initGui();
		this.button = new GuiButtonDiscard(fruitPressResource, 1, this.guiLeft + 108, this.guiTop + 54);
		this.buttonList.add(this.button);
		this.button.enabled = false;

		addTooltipIndex("fluidtank0", 89, 17, 16, 52);
		addTooltipIndex("discardtank0", 108, 54, 16, 16);
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
		this.button.enabled = this.te.isFluidTankFilled(0);
	}

	protected void actionPerformed(GuiButton butn)
	{
		GrowthCraftCellar.packetPipeline.sendToServer(new PacketClearTankButton(this.te.xCoord, this.te.yCoord, this.te.zCoord));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		super.drawGuiContainerForegroundLayer(par1, par2);

		if (!this.te.isFluidTankEmpty(0))
		{
			final String s = String.valueOf(this.te.getFluidAmount(0));
			this.fontRendererObj.drawStringWithShadow(s, this.xSize - 70 - this.fontRendererObj.getStringWidth(s), this.ySize - 104, 0xFFFFFF);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(fruitPressResource);
		final int w = (this.width - this.xSize) / 2;
		final int h = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(w, h, 0, 0, this.xSize, this.ySize);
		final int i = this.te.getPressProgressScaled(24);
		this.drawTexturedModalRect(w + 63, h + 34, 176, 0, i + 1, 16);

		if (this.te.getFluidAmountScaled(52, 0) > 0)
		{
			drawTank(w, h, 89, 17, 16, this.te.getFluidAmountScaled(52, 0), this.te.getFluidStack(0), this.te.getFluidTank(0));
			this.mc.getTextureManager().bindTexture(fruitPressResource);
		}
	}

	@Override
	protected void addTooltips(String handle, List<String> tooltip)
	{
		switch (handle)
		{
			case "fluidtank0":
				addFluidTooltips(this.te.getFluidStack(0), tooltip);

				break;
			case "discardtank0":
				tooltip.add(GrcI18n.translate("gui.grc.discard"));

				break;
			default:
				break;
		}
	}
}
