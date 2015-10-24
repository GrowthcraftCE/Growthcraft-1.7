package growthcraft.cellar.client.gui;

import java.util.ArrayList;
import java.util.Set;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.common.inventory.ContainerFermentBarrel;
import growthcraft.cellar.common.tileentity.CellarTank;
import growthcraft.cellar.common.tileentity.TileEntityFermentBarrel;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.network.PacketClearTankButton;
import growthcraft.core.util.UnitFormatter;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class GuiFermentBarrel extends GuiCellar
{
	private final ResourceLocation res = new ResourceLocation("grccellar" , "textures/guis/fermentbarrel_gui.png");
	private TileEntityFermentBarrel te;
	private GuiButtonDiscard button;

	public GuiFermentBarrel(InventoryPlayer inv, TileEntityFermentBarrel fermentBarrel)
	{
		super(new ContainerFermentBarrel(inv, fermentBarrel));
		this.te = fermentBarrel;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.button = new GuiButtonDiscard(this.res, 1, this.guiLeft + 116, this.guiTop + 54);
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
			this.fontRendererObj.drawStringWithShadow(s, this.xSize - 62 - this.fontRendererObj.getStringWidth(s), this.ySize - 104, 0xFFFFFF);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(res);
		final int w = (this.width - this.xSize) / 2;
		final int h = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(w, h, 0, 0, this.xSize, this.ySize);
		int i;

		if (this.te.getTime() > 0)
		{
			i = this.te.getFermentProgressScaled(29);
			if (i > 0)
			{
				this.drawTexturedModalRect(w + 49, h + 20 + 29 - i, 176, 29 - i, 12, i);
			}

			final int k1 = (this.te.getTime() / 2) % 7;

			switch (k1)
			{
				case 0:
					i = 0;
					break;
				case 1:
					i = 6;
					break;
				case 2:
					i = 11;
					break;
				case 3:
					i = 16;
					break;
				case 4:
					i = 20;
					break;
				case 5:
					i = 24;
					break;
				case 6:
					i = 29;
					break;
				default:
					i = 29;
					break;
			}

			if (i > 0)
			{
				this.drawTexturedModalRect(w + 39, h + 21 + 29 - i, 188, 29 - i, 9, i);
			}
		}

		i = this.te.getFluidAmountScaled(52, 0);
		if (i > 0)
		{
			final FluidStack fluid = this.te.getFluidStack(0);
			drawTank(w, h, 63, 17, 50, i, fluid, this.te.getFluidTank(0));
			this.mc.getTextureManager().bindTexture(res);

			itemRender.zLevel = 100.0F;

			// render active modifiers
			final Set<String> tags = CellarRegistry.instance().booze().getTags(fluid);
			if (tags != null)
			{
				if (tags.contains("fermented"))
				{
					itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.renderEngine, new ItemStack(Items.nether_wart), w + 114, h + 16);
				}
				if (tags.contains("extended"))
				{
					itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.renderEngine, new ItemStack(Items.redstone), w + 114, h + 32);
				}
				if (tags.contains("potent"))
				{
					itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.renderEngine, new ItemStack(Items.glowstone_dust), w + 130, h + 32);
				}
			}
			itemRender.zLevel = 0.0F;
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

		this.drawTexturedModelRectFromIcon(w + wp, h + hp + 52 - amount, icon, width, amount);

		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}

	@Override
	protected void drawToolTipAtMousePos(int par1, int par2)
	{
		final int w = (this.width - this.xSize) / 2;
		final int h = (this.height - this.ySize) / 2;

		if ((par1 > w + 63) && (par2 > h + 17) && (par1 < w + 63 + 50) && (par2 < h + 17 + 52))
		{
			final ArrayList<String> tooltip = new ArrayList<String>();

			if (this.te.isFluidTankFilled(0))
			{
				if (CellarRegistry.instance().booze().isFluidBooze(this.te.getFluidStack(0)))
				{
					final FluidStack fluid = CellarRegistry.instance().booze().maybeAlternateBoozeStack(this.te.getFluidStack(0));
					tooltip.add(fluid.getLocalizedName());
					final String s = UnitFormatter.fluidModifier(fluid);
					if (s != null) tooltip.add(s);

					if (this.te.getBoozeMeta() > 3)
					{
						tooltip.add("");
						tooltip.add(EnumChatFormatting.RED + I18n.format("gui.grc.cantferment"));
					}
				}
				else
				{
					tooltip.add(this.te.getFluidStack(0).getLocalizedName());
					tooltip.add("");
					tooltip.add(EnumChatFormatting.RED + I18n.format("gui.grc.cantferment"));
				}
			}

			drawText(tooltip, par1, par2, this.fontRendererObj);
		}
		else if ((par1 > w + 116) && (par2 > h + 54) && (par1 < w + 116 + 16) && (par2 < h + 54 + 16))
		{
			final ArrayList<String> tooltip = new ArrayList<String>();
			tooltip.add(I18n.format("gui.grc.discard"));
			drawText(tooltip, par1, par2, this.fontRendererObj);
		}
	}
}
