package growthcraft.cellar.client.gui;

import java.util.ArrayList;

import growthcraft.cellar.common.inventory.ContainerFruitPress;
import growthcraft.cellar.common.tileentity.CellarTank;
import growthcraft.cellar.common.tileentity.TileEntityFruitPress;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.network.PacketClearTankButton;
import growthcraft.core.util.UnitFormatter;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiFruitPress extends GuiCellar
{
	protected static final ResourceLocation fruitPressResource = new ResourceLocation("grccellar" , "textures/guis/fruitpress_gui.png");
	private static final Logger logger = LogManager.getLogger();
	private TileEntityFruitPress te;
	private GuiButtonDiscard button;

	public GuiFruitPress(InventoryPlayer inv, TileEntityFruitPress fruitPress)
	{
		super(new ContainerFruitPress(inv, fruitPress));
		this.te = fruitPress;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void initGui()
	{
		super.initGui();
		this.button = new GuiButtonDiscard(fruitPressResource, 1, this.guiLeft + 108, this.guiTop + 54);
		this.buttonList.add(this.button);
		this.button.enabled = false;
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
		String s = this.te.hasCustomInventoryName() ? this.te.getInventoryName() : I18n.format(this.te.getInventoryName());
		this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);

		if (!this.te.isFluidTankEmpty(0))
		{
			s = String.valueOf(this.te.getFluidAmount(0));
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

	protected void drawTank(int w, int h, int wp, int hp, int width, int amount, FluidStack fluidstack, CellarTank tank)
	{
		if (fluidstack == null) { return; }

		final Fluid fluid = fluidstack.getFluid();
		final int color = fluid.getColor();

		IIcon icon = null;
		if (fluid != null && fluid.getStillIcon() != null)
		{
			icon = fluid.getStillIcon();
		}

		this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

		final float r = (float)(color >> 16 & 255) / 255.0F;
		final float g = (float)(color >> 8 & 255) / 255.0F;
		final float b = (float)(color & 255) / 255.0F;
		GL11.glColor4f(r, g, b, 1.0f);

		if (icon != null) drawTexturedModelRectFromIcon(w + wp, h + hp + 52 - amount, icon, width, amount);

		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}

	@Override
	protected void drawToolTipAtMousePos(int par1, int par2)
	{
		final int w = (this.width - this.xSize) / 2;
		final int h = (this.height - this.ySize) / 2;

		if ((par1 > w + 89) && (par2 > h + 17) && (par1 < w + 89 + 16) && (par2 < h + 17 + 52))
		{
			final ArrayList<String> tooltip = new ArrayList<String>();

			if (this.te.isFluidTankFilled(0))
			{
				tooltip.add(this.te.getFluidStack(0).getLocalizedName());

				final String s = UnitFormatter.fluidModifier(this.te.getFluid(0));
				if (s != null) tooltip.add(s);
			}

			drawText(tooltip, par1, par2, this.fontRendererObj);
		}
		else if ((par1 > w + 108) && (par2 > h + 54) && (par1 < w + 108 + 16) && (par2 < h + 54 + 16))
		{
			final ArrayList<String> tooltip = new ArrayList<String>();
			tooltip.add(I18n.format("gui.grc.discard"));
			drawText(tooltip, par1, par2, this.fontRendererObj);
		}
	}
}
