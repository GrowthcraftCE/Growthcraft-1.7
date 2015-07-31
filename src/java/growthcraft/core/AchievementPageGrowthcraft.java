package growthcraft.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.stats.Achievement;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.AchievementPage;

public class AchievementPageGrowthcraft
{
	public static List chievMasterList = new ArrayList();

	public static void init(AchievementPage chievPage)
	{
		Achievement[] chievList = new Achievement[chievMasterList.size()];
		for(int i = 0; i < chievList.length; i++)
		{
			chievList[i] = (Achievement)chievMasterList.get(i);
		}

		chievPage = new AchievementPage(StatCollector.translateToLocal("achievementPage.pageGrowthCraft"), chievList);
		AchievementPage.registerAchievementPage(chievPage);
	}
}
