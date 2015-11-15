package growthcraft.cellar.client.gui;

import java.util.ArrayList;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.client.gui.widget.GuiButtonDiscard;
import growthcraft.cellar.client.gui.widget.GuiButtonSwitch;
import growthcraft.cellar.common.inventory.ContainerBrewKettle;
import growthcraft.cellar.common.tileentity.CellarTank;
import growthcraft.cellar.common.tileentity.TileEntityBrewKettle;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.network.PacketClearTankButtonWByte;
import growthcraft.cellar.network.PacketSwitchTankButton;
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
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiBrewKettle extends GuiCellar
{
	protected static final ResourceLocation brewKettleResource = new ResourceLocation("grccellar" , "textures/guis/brewkettle_gui.png");
	private TileEntityBrewKettle te;
	private GuiButtonDiscard button0;
	private GuiButtonDiscard button1;
	private GuiButtonSwitch button2;

	public GuiBrewKettle(InventoryPlayer inv, TileEntityBrewKettle brewKettle)
	{
		super(new ContainerBrewKettle(inv, brewKettle));
		this.te = brewKettle;
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void initGui()
	{
		super.initGui();
		this.button0 = new GuiButtonDiscard(brewKettleResource, 1, this.guiLeft + 27, this.guiTop + 54);
		this.button0.enabled = false;

		this.button1 = new GuiButtonDiscard(brewKettleResource, 1, this.guiLeft + 133, this.guiTop + 54);
		this.button1.enabled = false;

		this.button2 = new GuiButtonSwitch(brewKettleResource, 1, this.guiLeft + 133, this.guiTop + 37);
		this.button2.enabled = false;

		this.buttonList.add(this.button0);
		this.buttonList.add(this.button1);
		this.buttonList.add(this.button2);
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

		if (!this.te.isFluidTankEmpty(0))
		{
			s = String.valueOf(this.te.getFluidAmount(0));
			this.fontRendererObj.drawStringWithShadow(s, this.xSize - 113 - this.fontRendererObj.getStringWidth(s), this.ySize - 104, 0xFFFFFF);
		}

		if (!this.te.isFluidTankEmpty(1))
		{
			s = String.valueOf(this.te.getFluidAmount(1));
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

		if (this.te.hasFire())
		{
			final int iconHeight = this.te.getHeatScaled(14);
			final int offY = 14 - iconHeight;
			this.drawTexturedModalRect(w + 67, h + 53 + offY, 176, 28 + offY, 14, iconHeight);
		}

		if (this.te.getFluidAmountScaled(52, 0) > 0)
		{
			drawTank(w, h, 46, 17, 16, this.te.getFluidAmountScaled(52, 0), this.te.getFluidStack(0), this.te.getFluidTank(0));
			this.mc.getTextureManager().bindTexture(brewKettleResource);
		}

		if (this.te.getFluidAmountScaled(52, 1) > 0)
		{
			drawTank(w, h, 114, 17, 16, this.te.getFluidAmountScaled(52, 1), this.te.getFluidStack(1), this.te.getFluidTank(1));
			this.mc.getTextureManager().bindTexture(brewKettleResource);
		}
	}

	@Override
	protected void drawToolTipAtMousePos(int par1, int par2)
	{
		final int w = (this.width - this.xSize) / 2;
		final int h = (this.height - this.ySize) / 2;

		if ((par1 > w + 46) && (par2 > h + 17) && (par1 < w + 46 + 16) && (par2 < h + 17 + 52))
		{
			final ArrayList<String> tooltip = new ArrayList<String>();

			if (this.te.isFluidTankFilled(0))
			{
				FluidStack fluid = this.te.getFluidStack(0);
				tooltip.add(fluid.getLocalizedName());
				if (CellarRegistry.instance().booze().isFluidBooze(fluid))
				{
					fluid = CellarRegistry.instance().booze().maybeAlternateBoozeStack(fluid);
					tooltip.add(UnitFormatter.fluidModifier(fluid));
				}
			}

			drawText(tooltip, par1, par2, this.fontRendererObj);
		}
		else if ((par1 > w + 114) && (par2 > h + 17) && (par1 < w + 114 + 16) && (par2 < h + 17 + 52))
		{
			final ArrayList<String> tooltip = new ArrayList<String>();

			if (this.te.isFluidTankFilled(1))
			{
				FluidStack fluid = this.te.getFluidStack(1);
				tooltip.add(fluid.getLocalizedName());
				if (CellarRegistry.instance().booze().isFluidBooze(fluid))
				{
					fluid = CellarRegistry.instance().booze().maybeAlternateBoozeStack(fluid);
					tooltip.add(UnitFormatter.fluidModifier(fluid));
				}
			}

			drawText(tooltip, par1, par2, this.fontRendererObj);
		}
		else if ((par1 > w + 27) && (par2 > h + 54) && (par1 < w + 27 + 16) && (par2 < h + 54 + 16))
		{
			final ArrayList<String> tooltip = new ArrayList<String>();
			tooltip.add(I18n.format("gui.grc.discard"));
			drawText(tooltip, par1, par2, this.fontRendererObj);
		}
		else if ((par1 > w + 133) && (par2 > h + 54) && (par1 < w + 133 + 16) && (par2 < h + 54 + 16))
		{
			final ArrayList<String> tooltip = new ArrayList<String>();
			tooltip.add(I18n.format("gui.grc.discard"));
			drawText(tooltip, par1, par2, this.fontRendererObj);
		}
		else if ((par1 > w + 133) && (par2 > h + 37) && (par1 < w + 133 + 16) && (par2 < h + 37 + 16))
		{
			final ArrayList<String> tooltip = new ArrayList<String>();
			tooltip.add(I18n.format("gui.grc.switch"));
			drawText(tooltip, par1, par2, this.fontRendererObj);
		}
	}
}
