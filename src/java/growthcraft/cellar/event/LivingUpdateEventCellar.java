package growthcraft.cellar.event;

import growthcraft.cellar.GrowthCraftCellar;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class LivingUpdateEventCellar
{
	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event)
	{
		EntityLivingBase ent = event.entityLiving;

		if (ent.isPotionActive(GrowthCraftCellar.potionTipsy))
		{
			if (ent.getActivePotionEffect(GrowthCraftCellar.potionTipsy).getDuration() == 0)
			{
				ent.removePotionEffect(GrowthCraftCellar.potionTipsy.id);
				return;
			}

			int lvl = ent.getActivePotionEffect(GrowthCraftCellar.potionTipsy).getAmplifier();

			if (lvl >= 3)
			{
				ent.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 0));

				if (lvl >= 4)
				{
					ent.addPotionEffect(new PotionEffect(Potion.blindness.id, 100, 0));
				}
			}
		}
	}
}
