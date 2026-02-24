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
import growthcraft.core.client.gui.widget.WidgetHeatIcon;
import growthcraft.core.client.gui.widget.WidgetFluidTank;
import growthcraft.core.client.gui.widget.WidgetDeviceProgressIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;

@SideOnly(Side.CLIENT)
public class GuiBrewKettle extends GuiCellar<ContainerBrewKettle, TileEntityBrewKettle>
{
	private GuiButtonDiscard button0;
	private GuiButtonDiscard button1;
	private GuiButtonSwitch button2;

	public GuiBrewKettle(InventoryPlayer inv, TileEntityBrewKettle brewKettle)
	{
		super(GrcCellarResources.INSTANCE.textureGuiBrewKettle, new ContainerBrewKettle(inv, brewKettle), brewKettle);
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void initGui()
	{
		super.initGui();
		widgets.add(new WidgetHeatIcon(widgets, 67, 53, 14, 14).setTextureRect(176, 28, 14, 14));
		widgets.add(new WidgetFluidTank(widgets, 0, 46, 17, 16, 52));
		widgets.add(new WidgetFluidTank(widgets, 1, 114, 17, 16, 52));
		widgets.add(new WidgetDeviceProgressIcon(widgets, 98, 30, 9, 28)
			.setProgressDirection(WidgetDeviceProgressIcon.ProgressDirection.TOP_TO_BOTTOM)
			.setTextureRect(176, 0, 9, 28));

		if (GrowthCraftCellar.getConfig().enableDiscardButton)
		{
			this.button0 = new GuiButtonDiscard(guiResource, 1, guiLeft + 27, guiTop + 54);
			button0.enabled = false;

			this.button1 = new GuiButtonDiscard(guiResource, 1, guiLeft + 133, guiTop + 54);
			button1.enabled = false;
		}

		this.button2 = new GuiButtonSwitch(guiResource, 1, this.guiLeft + 133, this.guiTop + 37);
		this.button2.enabled = false;

		if (button0 != null) this.buttonList.add(this.button0);
		if (button1 != null) this.buttonList.add(this.button1);
		buttonList.add(this.button2);

		addTooltipIndex("fluidtank.input", 46, 17, 16, 52);
		addTooltipIndex("fluidtank.output", 114, 17, 16, 52);
		if (button0 != null) addTooltipIndex("discard.fluidtank.input", 27, 54, 16, 16);
		if (button1 != null) addTooltipIndex("discard.fluidtank.output", 133, 54, 16, 16);
		addTooltipIndex("switch.fluidtanks", 133, 37, 16, 16);
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
		if (button0 != null)
		{
			button0.enabled = tileEntity.isFluidTankFilled(0);
		}

		if (button1 != null)
		{
			button1.enabled = tileEntity.isFluidTankFilled(1);
		}

		//button2.enabled = tileEntity.isFluidTankFilled(1) && tileEntity.isFluidTankEmpty(0);
		button2.enabled = tileEntity.isFluidTankFilled(0) || tileEntity.isFluidTankFilled(1);
	}

	protected void actionPerformed(GuiButton button)
	{
		if (button0 != null && button == button0)
		{
			GrowthCraftCellar.packetPipeline.sendToServer(new PacketClearTankButtonWByte(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, (byte)0));
		}
		else if (button1 != null && button == button1)
		{
			GrowthCraftCellar.packetPipeline.sendToServer(new PacketClearTankButtonWByte(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, (byte)1));
		}
		else if (button == button2)
		{
			GrowthCraftCellar.packetPipeline.sendToServer(new PacketSwitchTankButton(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord));
		}
	}

	@Override
	public void addTooltips(String handle, List<String> tooltip)
	{
		switch (handle)
		{
			case "fluidtank.input":
				addFluidTooltips(tileEntity.getFluidStack(0), tooltip);
				break;
			case "fluidtank.output":
				addFluidTooltips(tileEntity.getFluidStack(1), tooltip);
				break;
			case "discard.fluidtank.input":
				tooltip.add(GrcI18n.translate("gui.grc.discard"));
				break;
			case "discard.fluidtank.output":
				tooltip.add(GrcI18n.translate("gui.grc.discard"));
				break;
			case "switch.fluidtanks":
				tooltip.add(GrcI18n.translate("gui.grc.switch"));
				break;
			default:
				break;
		}
	}
}
