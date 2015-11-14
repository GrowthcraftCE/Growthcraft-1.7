package growthcraft.core.common;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.stats.Achievement;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.AchievementPage;

public class AchievementPageGrowthcraft
{
	public static List<Achievement> chievMasterList = new ArrayList<Achievement>();
	public static AchievementPage chievPage;

	private AchievementPageGrowthcraft() {}

	public static void init()
	{
		final Achievement[] chievList = new Achievement[chievMasterList.size()];
		for(int i = 0; i < chievList.length; i++)
		{
			chievList[i] = chievMasterList.get(i);
		}

		chievPage = new AchievementPage(StatCollector.translateToLocal("achievementPage.pageGrowthCraft"), chievList);
		AchievementPage.registerAchievementPage(chievPage);
	}
}
