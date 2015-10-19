package growthcraft.api.cellar.booze;

import growthcraft.api.cellar.CellarRegistry;

import net.minecraftforge.fluids.Fluid;

public class Booze extends Fluid
{
	public Booze(String fluidName)
	{
		super(fluidName);
	}

	@Override
	public int getColor()
	{
		return CellarRegistry.instance().booze().getBoozeColor(this);
	}
}
