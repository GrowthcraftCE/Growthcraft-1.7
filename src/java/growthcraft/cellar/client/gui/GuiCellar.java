package growthcraft.cellar.client.gui;

import growthcraft.cellar.common.tileentity.CellarTank;
import growthcraft.core.client.gui.GrcGuiContainer;
import growthcraft.core.util.Rectangle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.inventory.Container;
import net.minecraftforge.fluids.FluidStack;

@SideOnly(Side.CLIENT)
public class GuiCellar extends GrcGuiContainer
{
	public GuiCellar(Container container)
	{
		super(container);
	}

	protected void drawTank(int w, int h, int wp, int hp, int width, int amount, FluidStack fluidstack, CellarTank _tank)
	{
		drawFluidStack(w, h, wp, hp, width, amount, fluidstack);
	}
}
