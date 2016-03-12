package growthcraft.cellar.client.gui;

import java.util.List;

import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.cellar.client.gui.widget.GuiButtonDiscard;
import growthcraft.cellar.client.gui.widget.GuiButtonSwitch;
import growthcraft.cellar.client.resource.GrcCellarResources;
import growthcraft.cellar.common.inventory.ContainerBrewKettle;
import growthcraft.cellar.common.tileentity.TileEntityBrewKettle;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.network.PacketClearTankButtonWByte;
import growthcraft.cellar.network.PacketSwitchTankButton;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiBrewKettle extends GuiCellar
{
	private final ResourceLocation brewKettleResource = GrcCellarResources.INSTANCE.textureGuiBrewKettle;
	private TileEntityBrewKettle te;
	private GuiButtonDiscard button0;
	private GuiButtonDiscard button1;
	private GuiButtonSwitch button2;

	public GuiBrewKettle(InventoryPlayer inv, TileEntityBrewKettle brewKettle)
	{
		super(new ContainerBrewKettle(inv, brewKettle), brewKettle);
		this.te = brewKettle;
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void initGui()
	{
		super.initGui();
		if (GrowthCraftCellar.getConfig().enableDiscardButton)
		{
			this.button0 = new GuiButtonDiscard(brewKettleResource, 1, this.guiLeft + 27, this.guiTop + 54);
			this.button0.enabled = false;

			this.button1 = new GuiButtonDiscard(brewKettleResource, 1, this.guiLeft + 133, this.guiTop + 54);
			this.button1.enabled = false;
		}

		this.button2 = new GuiButtonSwitch(brewKettleResource, 1, this.guiLeft + 133, this.guiTop + 37);
		this.button2.enabled = false;

		if (button0 != null) this.buttonList.add(this.button0);
		if (button1 != null) this.buttonList.add(this.button1);
		this.buttonList.add(this.button2);

		addTooltipIndex("fluidtank0", 46, 17, 16, 52);
		addTooltipIndex("fluidtank1", 114, 17, 16, 52);
		if (button0 != null) addTooltipIndex("discardtank0", 27, 54, 16, 16);
		if (button1 != null) addTooltipIndex("discardtank1", 133, 54, 16, 16);
		addTooltipIndex("switchtank", 133, 37, 16, 16);
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
		if (button0 != null)
		{
			this.button0.enabled = this.te.isFluidTankFilled(0);
		}

		if (button1 != null)
		{
			this.button1.enabled = this.te.isFluidTankFilled(1);
		}

		//this.button2.enabled = this.te.isFluidTankFilled(1) && this.te.isFluidTankEmpty(0);
		this.button2.enabled = this.te.isFluidTankFilled(0) || this.te.isFluidTankFilled(1);
	}

	protected void actionPerformed(GuiButton button)
	{
		if (button0 != null && button == button0)
		{
			GrowthCraftCellar.packetPipeline.sendToServer(new PacketClearTankButtonWByte(te.xCoord, te.yCoord, te.zCoord, (byte)0));
		}
		else if (button1 != null && button == button1)
		{
			GrowthCraftCellar.packetPipeline.sendToServer(new PacketClearTankButtonWByte(te.xCoord, te.yCoord, te.zCoord, (byte)1));
		}
		else if (button == button2)
		{
			GrowthCraftCellar.packetPipeline.sendToServer(new PacketSwitchTankButton(te.xCoord, te.yCoord, te.zCoord));
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		super.drawGuiContainerForegroundLayer(par1, par2);

		if (!this.te.isFluidTankEmpty(0))
		{
			final String s = String.valueOf(this.te.getFluidAmount(0));
			this.fontRendererObj.drawStringWithShadow(s, this.xSize - 113 - this.fontRendererObj.getStringWidth(s), this.ySize - 104, 0xFFFFFF);
		}

		if (!this.te.isFluidTankEmpty(1))
		{
			final String s = String.valueOf(this.te.getFluidAmount(1));
			this.fontRendererObj.drawStringWithShadow(s, this.xSize - 45 - this.fontRendererObj.getStringWidth(s), this.ySize - 104, 0xFFFFFF);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(brewKettleResource);
		final int w = (this.width - this.xSize) / 2;
		final int h = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(w, h, 0, 0, this.xSize, this.ySize);

		this.drawTexturedModalRect(w + 98, h + 30, 176, 0, 9, this.te.getBrewProgressScaled(28));

		if (this.te.isHeated())
		{
			final int iconHeight = this.te.getHeatScaled(14);
			final int offY = 14 - iconHeight;
			this.drawTexturedModalRect(w + 67, h + 53 + offY, 176, 28 + offY, 14, iconHeight);
		}

		if (this.te.getFluidAmountScaled(52, 0) > 0)
		{
			drawTank(w, h, 46, 17, 16, 52, this.te.getFluidAmountScaled(52, 0), this.te.getFluidStack(0), this.te.getFluidTank(0));
			this.mc.getTextureManager().bindTexture(brewKettleResource);
		}

		if (this.te.getFluidAmountScaled(52, 1) > 0)
		{
			drawTank(w, h, 114, 17, 16, 52, this.te.getFluidAmountScaled(52, 1), this.te.getFluidStack(1), this.te.getFluidTank(1));
			this.mc.getTextureManager().bindTexture(brewKettleResource);
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
			case "fluidtank1":
				addFluidTooltips(this.te.getFluidStack(1), tooltip);
				break;
			case "discardtank0":
				tooltip.add(GrcI18n.translate("gui.grc.discard"));
				break;
			case "discardtank1":
				tooltip.add(GrcI18n.translate("gui.grc.discard"));
				break;
			case "switchtank":
				tooltip.add(GrcI18n.translate("gui.grc.switch"));
				break;
			default:
				break;
		}
	}
}
