package growthcraft.api.core.util;

public abstract class HashKey
{
	protected int hash;

	@Override
	public int hashCode()
	{
		return hash;
	}

	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof ItemKey)) return false;
		return hashCode() == other.hashCode();
	}
}
