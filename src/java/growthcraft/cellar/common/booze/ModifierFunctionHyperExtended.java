package growthcraft.cellar.common.booze;

public class ModifierFunctionHyperExtended extends AbstractModifierFunction
{
	@Override
	public int applyTime(int t)
	{
		return (int)(t * 4F);
	}
}
