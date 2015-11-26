package growthcraft.cellar.client.gui;

import java.util.List;
import java.util.Collection;

import org.lwjgl.opengl.GL11;

import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.cellar.client.gui.widget.GuiButtonDiscard;
import growthcraft.cellar.common.inventory.ContainerFermentBarrel;
import growthcraft.cellar.common.tileentity.TileEntityFermentBarrel;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.network.PacketClearTankButton;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

@SideOnly(Side.CLIENT)
public class GuiFermentBarrel extends GuiCellar
{
	protected static final ResourceLocation fermentBarrelResource = new ResourceLocation("grccellar" , "textures/guis/fermentbarrel_gui.png");
	private TileEntityFermentBarrel te;
	private GuiButtonDiscard button;

	public GuiFermentBarrel(InventoryPlayer inv, TileEntityFermentBarrel fermentBarrel)
	{
		super(new ContainerFermentBarrel(inv, fermentBarrel), fermentBarrel);
		this.te = fermentBarrel;
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void initGui()
	{
		super.initGui();
		this.button = new GuiButtonDiscard(fermentBarrelResource, 1, this.guiLeft + 116, this.guiTop + 54);
		this.buttonList.add(this.button);
		this.button.enabled = false;

		addTooltipIndex("fluidtank0", 63, 17, 50, 52);
		addTooltipIndex("discardtank0", 116, 54, 16, 16);
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
		this.button.enabled = this.te.isFluidTankFilled(0);
	}

	@Override
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
			this.fontRendererObj.drawStringWithShadow(s, this.xSize - 62 - this.fontRendererObj.getStringWidth(s), this.ySize - 104, 0xFFFFFF);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(fermentBarrelResource);
		final int w = (this.width - this.xSize) / 2;
		final int h = (this.height - this.ySize) / 2;
		drawTexturedModalRect(w, h, 0, 0, xSize, ySize);

		if (te.getTime() > 0)
		{
			int i = te.getFermentProgressScaled(29);
			if (i > 0)
			{
				drawTexturedModalRect(w + 39, h + 21 + 29 - i, 188, 29 - i, 9, i);
			}

			final int k1 = (te.getTime() / 2) % 60;
			i = k1 * 29 / 60;
			if (i > 0)
			{
				drawTexturedModalRect(w + 49, h + 20 + 29 - i, 176, 29 - i, 12, i);
			}
		}

		final int i = te.getFluidAmountScaled(52, 0);
		if (i > 0)
		{
			final FluidStack fluid = te.getFluidStack(0);
			drawTank(w, h, 63, 17, 50, i, fluid, te.getFluidTank(0));
			mc.getTextureManager().bindTexture(fermentBarrelResource);

			itemRender.zLevel = 100.0F;

			// render active modifiers
			final Collection<BoozeTag> tags = CellarRegistry.instance().booze().getTags(fluid);
			if (tags != null)
			{
				if (tags.contains(BoozeTag.FERMENTED))
				{
					itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.renderEngine, new ItemStack(Items.nether_wart), w + 114, h + 16);
				}
				if (tags.contains(BoozeTag.EXTENDED))
				{
					itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.renderEngine, new ItemStack(Items.redstone), w + 114, h + 32);
				}
				if (tags.contains(BoozeTag.POTENT))
				{
					itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.renderEngine, new ItemStack(Items.glowstone_dust), w + 130, h + 32);
				}
			}
			itemRender.zLevel = 0.0F;
		}
	}

	@Override
	protected void addTooltips(String handle, List<String> tooltip)
	{
		switch (handle)
		{
			case "fluidtank0":
				if (this.te.isFluidTankFilled(0))
				{
					addFermentTooltips(this.te.getFluidStack(0), tooltip);
				}
				break;
			case "discardtank0":
				tooltip.add(GrcI18n.translate("gui.grc.discard"));
				break;
			default:
				break;
		}
	}
}
