package growthcraft.cellar.stats;

import growthcraft.api.core.stats.IAchievement;
import growthcraft.cellar.GrowthCraftCellar;

import net.minecraft.entity.player.EntityPlayer;

public enum CellarAchievement implements IAchievement
{
	CRAFT_BARREL,
	FERMENT_BOOZE,
	GET_DRUNK,
	ON_THE_GO;

	@Override
	public void unlock(EntityPlayer player)
	{
		GrowthCraftCellar.achievements.unlock(this, player);
	}

	@Override
	public void addStat(EntityPlayer player, int n)
	{
		GrowthCraftCellar.achievements.addStat(this, player, n);
	}
}
