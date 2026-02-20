package growthcraft.cellar.client.gui;

import java.util.List;
import java.util.Collection;

import org.lwjgl.opengl.GL11;

import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.core.CoreRegistry;
import growthcraft.api.core.fluids.FluidTag;
import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.cellar.client.gui.widget.GuiButtonDiscard;
import growthcraft.cellar.client.resource.GrcCellarResources;
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
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

@SideOnly(Side.CLIENT)
public class GuiFermentBarrel extends GuiCellar<ContainerFermentBarrel, TileEntityFermentBarrel>
{
	private GuiButtonDiscard button;

	public GuiFermentBarrel(InventoryPlayer inv, TileEntityFermentBarrel fermentBarrel)
	{
		super(GrcCellarResources.INSTANCE.textureGuiFermentBarrel, new ContainerFermentBarrel(inv, fermentBarrel), fermentBarrel);
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void initGui()
	{
		super.initGui();
		if (GrowthCraftCellar.getConfig().enableDiscardButton)
		{
			this.button = new GuiButtonDiscard(guiResource, 1, this.guiLeft + 116, this.guiTop + 54);
			this.buttonList.add(this.button);
			this.button.enabled = false;
		}

		addTooltipIndex("fluidtank.primary", 63, 17, 50, 52);
		addTooltipIndex("progress_indicator", 42, 22, 3, 26);
		if (button != null) addTooltipIndex("discard.fluidtank.primary", 116, 54, 16, 16);
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
		if (button != null)
		{
			this.button.enabled = tileEntity.isFluidTankFilled(0);
		}
	}

	@Override
	protected void actionPerformed(GuiButton butn)
	{
		GrowthCraftCellar.packetPipeline.sendToServer(new PacketClearTankButton(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		super.drawGuiContainerBackgroundLayer(par1, par2, par3);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		bindGuiTexture();
		final int x = getGuiX();
		final int y = getGuiY();

		if (tileEntity.getTime() > 0)
		{
			int i = tileEntity.getDeviceProgressScaled(29);
			if (i > 0)
			{
				drawTexturedModalRect(x + 39, y + 21 + 29 - i, 188, 29 - i, 9, i);
			}

			final int k1 = (tileEntity.getTime() / 2) % 60;
			i = k1 * 29 / 60;
			if (i > 0)
			{
				drawTexturedModalRect(x + 49, y + 20 + 29 - i, 176, 29 - i, 12, i);
			}
		}

		final int i = tileEntity.getFluidAmountScaled(52, 0);
		if (i > 0)
		{
			final FluidStack fluid = tileEntity.getFluidStack(0);
			drawTank(x, y, 63, 17, 50, 52, i, fluid, tileEntity.getFluidTank(0));
			bindGuiTexture();

			itemRender.zLevel = 100.0F;

			// render active modifiers
			final Collection<FluidTag> tags = CoreRegistry.instance().fluidDictionary().getFluidTags(fluid);
			if (tags != null)
			{
				if (tags.contains(BoozeTag.FERMENTED))
				{
					itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.renderEngine, new ItemStack(Items.nether_wart), x + 114, y + 16);
				}
				if (tags.contains(BoozeTag.EXTENDED))
				{
					itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.renderEngine, new ItemStack(Items.redstone), x + 114, y + 32);
				}
				if (tags.contains(BoozeTag.POTENT))
				{
					itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.renderEngine, new ItemStack(Items.glowstone_dust), x + 130, y + 32);
				}
			}
			itemRender.zLevel = 0.0F;
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		super.drawGuiContainerForegroundLayer(par1, par2);

		if (!tileEntity.isFluidTankEmpty(0))
		{
			final String s = String.valueOf(tileEntity.getFluidAmount(0));
			this.fontRendererObj.drawStringWithShadow(s, this.xSize - 62 - this.fontRendererObj.getStringWidth(s), this.ySize - 104, 0xFFFFFF);
		}
	}

	@Override
	public void addTooltips(String handle, List<String> tooltip)
	{
		switch (handle)
		{
			case "progress_indicator":
				tooltip.add(GrcI18n.translate("gui.grc.progress.format",
					EnumChatFormatting.GRAY + GrcI18n.translate("gui.grccellar.ferment_barrel.progress_name"),
					"" + EnumChatFormatting.WHITE + tileEntity.getTime(),
					"" + EnumChatFormatting.GRAY + tileEntity.getTimeMax()));
				break;
			case "fluidtank.primary":
				if (tileEntity.isFluidTankFilled(0))
				{
					addFermentTooltips(tileEntity.getFluidStack(0), tooltip);
				}
				break;
			case "discard.fluidtank.primary":
				tooltip.add(GrcI18n.translate("gui.grc.discard"));
				break;
			default:
				break;
		}
	}
}
