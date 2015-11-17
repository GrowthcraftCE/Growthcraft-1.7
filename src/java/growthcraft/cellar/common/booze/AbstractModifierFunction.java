package growthcraft.cellar.common.booze;

import growthcraft.api.cellar.booze.IModifierFunction;

public class AbstractModifierFunction implements IModifierFunction
{
	@Override
	public int applyLevel(int l)
	{
		return l;
	}

	@Override
	public int applyTime(int t)
	{
		return t;
	}
}
