package growthcraft.cellar.common.booze;

public class ModifierFunctionHyperExtended extends AbstractModifierFunction
{
	@Override
	public int applyLevel(int l)
	{
		return l + 1;
	}

	@Override
	public int applyTime(int t)
	{
		return (int)(t * 2.67F);
	}
}
