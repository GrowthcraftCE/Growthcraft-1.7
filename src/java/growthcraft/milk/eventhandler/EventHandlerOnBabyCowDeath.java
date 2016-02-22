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
package growthcraft.milk.eventhandler;

import java.util.Random;

import growthcraft.api.core.util.RandomUtils;
import growthcraft.core.util.ItemUtils;
import growthcraft.milk.GrowthCraftMilk;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

// I feel sorry for the baby cows, but cheese is required!
public class EventHandlerOnBabyCowDeath
{
	private Random rng = new Random();

	@SubscribeEvent
	public void onLivingEntityDeath(LivingDeathEvent event)
	{
		if (event.entityLiving instanceof EntityCow)
		{
			if (event.entityLiving.isChild())
			{
				if (RandomUtils.thresh(rng, GrowthCraftMilk.getConfig().stomachDropRate))
				{
					final int count = RandomUtils.range(rng, GrowthCraftMilk.getConfig().stomachMinDropped, GrowthCraftMilk.getConfig().stomachMaxDropped);
					final ItemStack stack = GrowthCraftMilk.items.stomach.asStack(count);
					ItemUtils.spawnItemStackAtEntity(stack, event.entityLiving, rng);
				}
			}
		}
	}
}
