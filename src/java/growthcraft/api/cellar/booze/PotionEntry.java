package growthcraft.api.cellar.booze;

public class PotionEntry
{
	private int id;
	private int time;

	public PotionEntry(int i, int t)
	{
		this.id = i;
		this.time = t;
	}

	public int getID()
	{
		return id;
	}

	public int getTime()
	{
		return time;
	}
}
