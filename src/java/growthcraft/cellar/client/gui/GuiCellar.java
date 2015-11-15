package growthcraft.cellar.client.gui;

import java.util.List;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.common.tileentity.CellarTank;
import growthcraft.core.client.gui.GrcGuiContainer;
import growthcraft.core.util.UnitFormatter;
import growthcraft.cellar.common.tileentity.TileEntityCellarDevice;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

@SideOnly(Side.CLIENT)
public class GuiCellar extends GrcGuiContainer
{
	private TileEntityCellarDevice cellarDevice;

	public GuiCellar(Container container, TileEntityCellarDevice cd)
	{
		super(container);
		this.cellarDevice = cd;
	}

	protected void drawInventoryName(int x, int y)
	{
		final String s = cellarDevice.hasCustomInventoryName() ? cellarDevice.getInventoryName() : I18n.format(cellarDevice.getInventoryName());
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y)
	{
		super.drawGuiContainerForegroundLayer(x, y);
		drawInventoryName(x, y);
	}

	protected void drawTank(int w, int h, int wp, int hp, int width, int amount, FluidStack fluidstack, CellarTank _tank)
	{
		drawFluidStack(w, h, wp, hp, width, amount, fluidstack);
	}

	protected void addFluidTooltips(FluidStack fluid, List<String> tooltip)
	{
		if (fluid == null) return;
		if (fluid.amount <= 0) return;

		tooltip.add(fluid.getLocalizedName());

		final FluidStack altFluid = CellarRegistry.instance().booze().maybeAlternateBoozeStack(fluid);

		final String s = UnitFormatter.fluidModifier(altFluid.getFluid());
		if (s != null) tooltip.add(s);
	}

	protected void addFermentTooltips(FluidStack fluid, List<String> tooltip)
	{
		if (fluid == null) return;
		if (fluid.amount <= 0) return;

		final FluidStack altFluid = CellarRegistry.instance().booze().maybeAlternateBoozeStack(fluid);
		if (CellarRegistry.instance().fermenting().canFerment(altFluid))
		{
			addFluidTooltips(altFluid, tooltip);
		}
		else
		{
			tooltip.add(fluid.getLocalizedName());
			tooltip.add("");
			tooltip.add(EnumChatFormatting.RED + I18n.format("gui.grc.cantferment"));
		}
	}
}
