package growthcraft.cellar.client.gui;

import java.util.List;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.cellar.common.tileentity.CellarTank;
import growthcraft.cellar.common.tileentity.TileEntityCellarDevice;
import growthcraft.core.client.gui.GrcGuiContainer;
import growthcraft.core.util.UnitFormatter;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

@SideOnly(Side.CLIENT)
public class GuiCellar extends GrcGuiContainer
{
	private TileEntityCellarDevice cellarDevice;

	public GuiCellar(Container container, TileEntityCellarDevice cd)
	{
		super(container, cd);
		this.cellarDevice = cd;
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

		final String s = UnitFormatter.fluidModifier(fluid.getFluid());
		if (s != null) tooltip.add(s);
	}

	protected void addFermentTooltips(FluidStack fluid, List<String> tooltip)
	{
		if (fluid == null) return;
		if (fluid.amount <= 0) return;

		addFluidTooltips(fluid, tooltip);
		if (!CellarRegistry.instance().fermenting().canFerment(fluid))
		{
			tooltip.add("");
			tooltip.add(EnumChatFormatting.RED + GrcI18n.translate("gui.grc.cantferment"));
		}
	}
}
