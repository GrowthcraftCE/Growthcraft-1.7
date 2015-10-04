package growthcraft.cellar.gui;

import java.util.ArrayList;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.container.ContainerFruitPress;
import growthcraft.cellar.network.PacketClearTankButton;
import growthcraft.cellar.tileentity.CellarTank;
import growthcraft.cellar.tileentity.TileEntityFruitPress;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class GuiFruitPress extends GuiCellar
{
	private static final Logger logger = LogManager.getLogger();
	private static final ResourceLocation res = new ResourceLocation("grccellar" , "textures/guis/fruitpress_gui.png");
	private TileEntityFruitPress te;
	private GuiButtonDiscard button;

	public GuiFruitPress(InventoryPlayer inv, TileEntityFruitPress te)
	{
		super(new ContainerFruitPress(inv, te));
		this.te = te;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.add(this.button = new GuiButtonDiscard(this.res, 1, this.guiLeft + 108, this.guiTop + 54));
		this.button.enabled = false;
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
		this.button.enabled = this.te.isFluidTankFilled();
	}

	protected void actionPerformed(GuiButton button)
	{
		GrowthCraftCellar.packetPipeline.sendToServer(new PacketClearTankButton(this.te.xCoord, this.te.yCoord, this.te.zCoord));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String s = this.te.hasCustomInventoryName() ? this.te.getInventoryName() : I18n.format(this.te.getInventoryName());
		this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);

		if (this.te.isFluidTankEmpty() == false)
		{
			s = String.valueOf(this.te.getFluidAmount());
			this.fontRendererObj.drawStringWithShadow(s, this.xSize - 70 - this.fontRendererObj.getStringWidth(s), this.ySize - 104, 0xFFFFFF);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(res);
		int w = (this.width - this.xSize) / 2;
		int h = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(w, h, 0, 0, this.xSize, this.ySize);
		int i;

		i = this.te.getPressProgressScaled(24);
		this.drawTexturedModalRect(w + 63, h + 34, 176, 0, i + 1, 16);

		if (this.te.getFluidAmountScaled(52) > 0)
		{
			drawTank(w, h, 89, 17, 16, this.te.getFluidAmountScaled(52), this.te.getFluidStack(), this.te.getFluidTank());
			this.mc.getTextureManager().bindTexture(res);
		}
	}

	protected void drawTank(int w, int h, int wp, int hp, int width, int amount, FluidStack fluidstack, CellarTank tank)
	{
		if (fluidstack == null) { return; }
		int start = 0;

		IIcon icon = null;
		Fluid fluid = fluidstack.getFluid();
		int color = fluid.getColor();
		if (fluid != null && fluid.getStillIcon() != null)
		{
			icon = fluid.getStillIcon();
		}
		this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		float r = (float)(color >> 16 & 255) / 255.0F;
		float g = (float)(color >> 8 & 255) / 255.0F;
		float b = (float)(color & 255) / 255.0F;
		GL11.glColor4f(r, g, b, 1.0f);
		this.drawTexturedModelRectFromIcon(w + wp, h + hp + 52 - amount, icon, width, amount);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}

	@Override
	protected void drawToolTipAtMousePos(int par1, int par2)
	{
		int w = (this.width - this.xSize) / 2;
		int h = (this.height - this.ySize) / 2;

		if ((par1 > w + 89) && (par2 > h + 17) && (par1 < w + 89 + 16) && (par2 < h + 17 + 52))
		{
			ArrayList toolTip = new ArrayList();

			if (this.te.isFluidTankFilled())
			{
				toolTip.add(this.te.getFluid().getLocalizedName());
				if (CellarRegistry.instance().booze().isFluidBooze(this.te.getFluid()))
				{
					Fluid fluid = CellarRegistry.instance().booze().maybeAlternateBooze(this.te.getFluid());
					toolTip.add(EnumChatFormatting.GRAY + I18n.format(fluid.getUnlocalizedName() + ".modifier"));
				}
			}

			drawText(toolTip, par1, par2, this.fontRendererObj);
		}
		else if ((par1 > w + 108) && (par2 > h + 54) && (par1 < w + 108 + 16) && (par2 < h + 54 + 16))
		{
			ArrayList toolTip = new ArrayList();
			toolTip.add(I18n.format("gui.grc.discard"));
			drawText(toolTip, par1, par2, this.fontRendererObj);
		}
	}
}
