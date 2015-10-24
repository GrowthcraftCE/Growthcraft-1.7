package growthcraft.api.cellar.booze;

import growthcraft.api.cellar.CellarRegistry;

import net.minecraftforge.fluids.Fluid;

public class Booze extends Fluid
{
	protected int color;

	public Booze(String fluidName)
	{
		super(fluidName);
		this.color = 0xFFFFFF;
	}

	@Override
	public int getColor()
	{
		return color;
	}

	public Booze setColor(int col)
	{
		this.color = col;
		return this;
	}
}
