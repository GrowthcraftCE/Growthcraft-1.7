/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015, 2016 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.cellar.client.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;

import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.cellar.client.gui.widget.GuiButtonDiscard;
import growthcraft.cellar.client.resource.GrcCellarResources;
import growthcraft.cellar.common.inventory.ContainerCultureJar;
import growthcraft.cellar.common.tileentity.TileEntityCultureJar;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.network.PacketClearTankButton;
import growthcraft.core.client.gui.widget.WidgetDeviceProgressIcon;
import growthcraft.core.client.gui.widget.WidgetFluidTank;
import growthcraft.core.client.gui.widget.WidgetHeatIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;

@SideOnly(Side.CLIENT)
public class GuiCultureJar extends GuiCellar<ContainerCultureJar, TileEntityCultureJar>
{
	private GuiButtonDiscard discardButton;

	public GuiCultureJar(InventoryPlayer inv, TileEntityCultureJar fermentJar)
	{
		super(GrcCellarResources.INSTANCE.textureGuiCultureJar, new ContainerCultureJar(inv, fermentJar), fermentJar);
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void initGui()
	{
		super.initGui();
		widgets.add(new WidgetFluidTank(widgets, 0, 36, 14, 16, 58).setRuleOverlay(176, 53, 16, 58));
		widgets.add(new WidgetHeatIcon(widgets, 82, 56, 14, 14).setTextureRect(176, 17, 14, 14));
		widgets.add(new WidgetDeviceProgressIcon(widgets, 55, 35, 22, 17)
			.setProgressDirection(WidgetDeviceProgressIcon.ProgressDirection.LEFT_TO_RIGHT)
			.setTextureRect(176, 0, 22, 17));

		if (GrowthCraftCellar.getConfig().enableDiscardButton)
		{
			this.discardButton = new GuiButtonDiscard(guiResource, 1, guiLeft + 116, guiTop + 54);
			discardButton.enabled = false;
			buttonList.add(discardButton);
		}

		addTooltipIndex("fluid_tank.primary", 36, 17, 16, 58);
		if (discardButton != null) addTooltipIndex("discard.fluid_tank.primary", 16, 52, 16, 16);
	}

	@Override
	protected void actionPerformed(GuiButton butn)
	{
		GrowthCraftCellar.packetPipeline.sendToServer(new PacketClearTankButton(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord));
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
		if (discardButton != null) discardButton.enabled = tileEntity.isFluidTankFilled(0);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int mx, int my)
	{
		super.drawGuiContainerBackgroundLayer(par1, mx, my);

		if (!tileEntity.isCulturing())
		{
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			bindGuiTexture();
			// Red Ring around Yeast Slot
			drawTexturedModalRect(getGuiX() + 77, getGuiY() + 32, 176, 31, 22, 22);
		}
	}

	@Override
	public void addTooltips(String handle, List<String> tooltip)
	{
		switch (handle)
		{
			case "fluid_tank.primary":
				addFluidTooltips(tileEntity.getFluidStack(0), tooltip);
				break;
			case "discard.fluid_tank.primary":
				tooltip.add(GrcI18n.translate("gui.grc.discard"));
				break;
			default:
				break;
		}
	}
}
