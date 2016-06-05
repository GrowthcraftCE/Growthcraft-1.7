/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.core.stats;

import java.util.Map;
import java.util.EnumMap;

import growthcraft.api.core.item.EnumSkull;
import growthcraft.core.common.AchievementPageGrowthcraft;
import growthcraft.core.GrowthCraftCore;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.Achievement;

public class GrcCoreAchievements
{
	private static final Achievement NO_ACHIEVEMENT = (Achievement)null;

	private final Map<CoreAchievement, Achievement> achievements = new EnumMap<CoreAchievement, Achievement>(CoreAchievement.class);

	public GrcCoreAchievements()
	{
		achievements.put(CoreAchievement.TRUSTY_HARDWARE,
			(new Achievement("grc.achievement.trusty_hardware", "trusty_hardware",
				-4, 0,
				GrowthCraftCore.items.crowbar.asStack(),
				NO_ACHIEVEMENT)
			).initIndependentStat().registerStat()
		);
		achievements.put(CoreAchievement.HALF_LIFE_CONFIRMED,
			(new Achievement("grc.achievement.half_life_confirmed", "half_life_confirmed",
				-2, 0,
				EnumSkull.ZOMBIE.asStack(),
				achievements.get(CoreAchievement.TRUSTY_HARDWARE))
			).registerStat()
		);
		achievements.put(CoreAchievement.SALTY_SITUATION,
			(new Achievement("grc.achievement.salty_situation", "salty_situation",
				2, 0,
				GrowthCraftCore.items.saltBucket.asStack(),
				NO_ACHIEVEMENT)
			).registerStat()
		);

		for (Achievement a : achievements.values())
		{
			AchievementPageGrowthcraft.masterList.add(a);
		}
	}

	public void unlock(CoreAchievement a, EntityPlayer player)
	{
		final Achievement achievement = achievements.get(a);
		if (achievement != null)
		{
			player.triggerAchievement(achievement);
		}
	}

	public void addStat(CoreAchievement a, EntityPlayer player, int n)
	{
		final Achievement achievement = achievements.get(a);
		if (achievement != null)
		{
			player.addStat(achievement, n);
		}
	}
}
