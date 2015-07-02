package growthcraft.cellar.gui;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.container.ContainerBrewKettle;
import growthcraft.cellar.network.PacketClearTankButtonWByte;
import growthcraft.cellar.network.PacketSwitchTankButton;
import growthcraft.cellar.tileentity.CellarTank;
import growthcraft.cellar.tileentity.TileEntityBrewKettle;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

public class GuiBrewKettle extends GuiCellar
{
	private static final ResourceLocation res = new ResourceLocation("grccellar" , "textures/guis/brewkettle_gui.png");
	private TileEntityBrewKettle te;
	private GuiButtonDiscard button0;
	private GuiButtonDiscard button1;
	private GuiButtonSwitch button2;

	public GuiBrewKettle(InventoryPlayer inv, TileEntityBrewKettle te)
	{
		super(new ContainerBrewKettle(inv, te));
		this.te = te;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.add(this.button0 = new GuiButtonDiscard(this.res, 1, this.guiLeft + 27, this.guiTop + 54));
		this.button0.enabled = false;
		this.buttonList.add(this.button1 = new GuiButtonDiscard(this.res, 1, this.guiLeft + 133, this.guiTop + 54));
		this.button1.enabled = false;
		this.buttonList.add(this.button2 = new GuiButtonSwitch(this.res, 1, this.guiLeft + 133, this.guiTop + 37));
		this.button2.enabled = false;
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
		this.button0.enabled = this.te.isFluidTankFilled(0);
		this.button1.enabled = this.te.isFluidTankFilled(1);
		//this.button2.enabled = this.te.isFluidTankFilled(1) && this.te.isFluidTankEmpty(0);
		this.button2.enabled = this.te.isFluidTankFilled(0) || this.te.isFluidTankFilled(1);
	}

	protected void actionPerformed(GuiButton button)
	{
		if (button == this.button0)
		{
			GrowthCraftCellar.packetPipeline.sendToServer(new PacketClearTankButtonWByte(this.te.xCoord, this.te.yCoord, this.te.zCoord, (byte)0));
		}
		else if (button == this.button1)
		{
			GrowthCraftCellar.packetPipeline.sendToServer(new PacketClearTankButtonWByte(this.te.xCoord, this.te.yCoord, this.te.zCoord, (byte)1));
		}
		else if (button == this.button2)
		{
			GrowthCraftCellar.packetPipeline.sendToServer(new PacketSwitchTankButton(this.te.xCoord, this.te.yCoord, this.te.zCoord));
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String s = this.te.hasCustomInventoryName() ? this.te.getInventoryName() : I18n.format(this.te.getInventoryName());
		this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);

		if (this.te.isFluidTankEmpty(0) == false)
		{
			s = String.valueOf(this.te.getFluidAmount(0));
			this.fontRendererObj.drawStringWithShadow(s, this.xSize - 113 - this.fontRendererObj.getStringWidth(s), this.ySize - 104, 0xFFFFFF);
		}

		if (this.te.isFluidTankEmpty(1) == false)
		{
			s = String.valueOf(this.te.getFluidAmount(1));
			this.fontRendererObj.drawStringWithShadow(s, this.xSize - 45 - this.fontRendererObj.getStringWidth(s), this.ySize - 104, 0xFFFFFF);
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

		i = this.te.getBrewProgressScaled(28);
		this.drawTexturedModalRect(w + 98, h + 30, 176, 0, 9, i);

		if (this.te.hasFire())
		{
			this.drawTexturedModalRect(w + 67, h + 53, 176, 28, 14, 14);
		}

		if (this.te.getFluidAmountScaled(52, 0) > 0)
		{
			drawTank(w, h, 46, 17, 16, this.te.getFluidAmountScaled(52, 0), this.te.getFluidStack(0), this.te.getFluidTank(0));
			this.mc.getTextureManager().bindTexture(res);
		}

		if (this.te.getFluidAmountScaled(52, 1) > 0)
		{
			drawTank(w, h, 114, 17, 16, this.te.getFluidAmountScaled(52, 1), this.te.getFluidStack(1), this.te.getFluidTank(1));
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

		if ((par1 > w + 46) && (par2 > h + 17) && (par1 < w + 46 + 16) && (par2 < h + 17 + 52))
		{
			ArrayList toolTip = new ArrayList();

			if (this.te.isFluidTankFilled(0))
			{
				Fluid fluid = this.te.getFluid(0);
				toolTip.add(fluid.getLocalizedName());
				if (CellarRegistry.instance().isFluidBooze(fluid))
				{
					if (CellarRegistry.instance().isAlternateBooze(fluid))
					{
						toolTip.add(EnumChatFormatting.GRAY + I18n.format(CellarRegistry.instance().getAlternateBooze(fluid).getUnlocalizedName() + ".modifier"));
					}
					else
					{
						toolTip.add(EnumChatFormatting.GRAY + I18n.format(fluid.getUnlocalizedName() + ".modifier"));
					}
				}
			}

			drawText(toolTip, par1, par2, this.fontRendererObj);
		}
		else if ((par1 > w + 114) && (par2 > h + 17) && (par1 < w + 114 + 16) && (par2 < h + 17 + 52))
		{
			ArrayList toolTip = new ArrayList();

			if (this.te.isFluidTankFilled(1))
			{
				Fluid fluid = this.te.getFluid(1);
				toolTip.add(fluid.getLocalizedName());
				if (CellarRegistry.instance().isFluidBooze(fluid))
				{
					if (CellarRegistry.instance().isAlternateBooze(fluid))
					{
						toolTip.add(EnumChatFormatting.GRAY + I18n.format(CellarRegistry.instance().getAlternateBooze(fluid).getUnlocalizedName() + ".modifier"));
					}
					else
					{
						toolTip.add(EnumChatFormatting.GRAY + I18n.format(fluid.getUnlocalizedName() + ".modifier"));
					}
				}
			}

			drawText(toolTip, par1, par2, this.fontRendererObj);
		}		
		else if ((par1 > w + 27) && (par2 > h + 54) && (par1 < w + 27 + 16) && (par2 < h + 54 + 16))
		{
			ArrayList toolTip = new ArrayList();
			toolTip.add(I18n.format("gui.grc.discard"));
			drawText(toolTip, par1, par2, this.fontRendererObj);
		}
		else if ((par1 > w + 133) && (par2 > h + 54) && (par1 < w + 133 + 16) && (par2 < h + 54 + 16))
		{
			ArrayList toolTip = new ArrayList();
			toolTip.add(I18n.format("gui.grc.discard"));
			drawText(toolTip, par1, par2, this.fontRendererObj);
		}
		else if ((par1 > w + 133) && (par2 > h + 37) && (par1 < w + 133 + 16) && (par2 < h + 37 + 16))
		{
			ArrayList toolTip = new ArrayList();
			toolTip.add(I18n.format("gui.grc.switch"));
			drawText(toolTip, par1, par2, this.fontRendererObj);
		}
	}
}
