package growthcraft.nether.utils;

import net.minecraft.util.DamageSource;

public class DamageSources
{
	static class DamageSourceMuertecap extends DamageSource
	{
		public DamageSourceMuertecap()
		{
			super("muertecapFood");
		}
	}

	public static final DamageSource muertecapFood = new DamageSourceMuertecap();

	private DamageSources() {}
}
