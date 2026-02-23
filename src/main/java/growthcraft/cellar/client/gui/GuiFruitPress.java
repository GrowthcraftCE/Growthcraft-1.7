package growthcraft.cellar.client.gui;

import java.util.List;

import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.cellar.client.gui.widget.GuiButtonDiscard;
import growthcraft.cellar.client.resource.GrcCellarResources;
import growthcraft.cellar.common.inventory.ContainerFruitPress;
import growthcraft.cellar.common.tileentity.TileEntityFruitPress;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.network.PacketClearTankButton;
import growthcraft.core.client.gui.widget.WidgetDeviceProgressIcon;
import growthcraft.core.client.gui.widget.WidgetFluidTank;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;

@SideOnly(Side.CLIENT)
public class GuiFruitPress extends GuiCellar<ContainerFruitPress, TileEntityFruitPress>
{
	private GuiButtonDiscard discardButton;

	public GuiFruitPress(InventoryPlayer inv, TileEntityFruitPress fruitPress)
	{
		super(GrcCellarResources.INSTANCE.textureGuiFruitPress, new ContainerFruitPress(inv, fruitPress), fruitPress);
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void initGui()
	{
		super.initGui();
		widgets.add(new WidgetFluidTank(widgets, 0, 89, 17, 16, 52));
		widgets.add(new WidgetDeviceProgressIcon(widgets, 63, 34, 25, 16)
			.setProgressDirection(WidgetDeviceProgressIcon.ProgressDirection.LEFT_TO_RIGHT)
			.setTextureRect(176, 0, 25, 16));
		if (GrowthCraftCellar.getConfig().enableDiscardButton)
		{
			this.discardButton = new GuiButtonDiscard(guiResource, 1, guiLeft + 108, guiTop + 54);
			buttonList.add(discardButton);
			discardButton.enabled = false;
		}

		addTooltipIndex("fluidtank.primary", 89, 17, 16, 52);
		if (discardButton != null) addTooltipIndex("discard.fluidtank.primary", 108, 54, 16, 16);
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
		if (discardButton != null) discardButton.enabled = tileEntity.isFluidTankFilled(0);
	}

	protected void actionPerformed(GuiButton butn)
	{
		GrowthCraftCellar.packetPipeline.sendToServer(new PacketClearTankButton(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord));
	}

	@Override
	public void addTooltips(String handle, List<String> tooltip)
	{
		switch (handle)
		{
			case "fluidtank.primary":
				addFluidTooltips(tileEntity.getFluidStack(0), tooltip);

				break;
			case "discard.fluidtank.primary":
				tooltip.add(GrcI18n.translate("gui.grc.discard"));

				break;
			default:
				break;
		}
	}
}
