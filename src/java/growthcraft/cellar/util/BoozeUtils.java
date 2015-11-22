package growthcraft.cellar.util;

import java.util.List;
import java.util.Collection;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.booze.BoozeRegistry;
import growthcraft.api.cellar.booze.BoozeEffect;
import growthcraft.api.cellar.booze.PotionEntry;
import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.booze.IModifierFunction;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.stats.CellarAchievement;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class BoozeUtils
{
	private BoozeUtils() {}

	public static boolean isFermentedBooze(Fluid booze)
	{
		return CellarRegistry.instance().booze().hasTags(booze, BoozeTag.FERMENTED);
	}

	public static PotionEffect makePotionEffect(Fluid booze, ItemStack stack, int potionID, int potionTime, int potionLevel)
	{
		final BoozeRegistry reg = CellarRegistry.instance().booze();
		final Collection<BoozeTag> tags = reg.getTags(booze);

		if (tags != null)
		{
			int time = potionTime;
			int level = potionLevel;
			for (BoozeTag tag : tags)
			{
				final IModifierFunction func = tag.getModifierFunction();
				if (func != null)
				{
					time = func.applyTime(time);
					level = func.applyLevel(level);
				}
			}
			return new PotionEffect(potionID, time, level);
		}
		return null;
	}

	public static void addPotionEffect(Fluid booze, ItemStack stack, EntityPlayer player, int potionID, int potionTime, int potionLevel)
	{
		final PotionEffect potFx = makePotionEffect(booze, stack, potionID, potionTime, potionLevel);
		if (potFx != null)
		{
			player.addPotionEffect(potFx);
		}
	}

	public static void addTipsyEffect(BoozeEffect effect, ItemStack stack, World world, EntityPlayer player)
	{
		if (effect.canCauseTipsy())
		{
			if (world.rand.nextFloat() < effect.getTipsyChance())
			{
				int amplifier = 0;
				int time = 1200;
				if (player.isPotionActive(GrowthCraftCellar.potionTipsy))
				{
					amplifier = player.getActivePotionEffect(GrowthCraftCellar.potionTipsy).getAmplifier() + 1;
					if (amplifier > 4)
					{
						amplifier = 4;
					}
				}

				switch (amplifier)
				{
					case 1: time = 3000; break;
					case 2: time = 6750; break;
					case 3: time = 12000; break;
					case 4: time = 24000; break;
					default:
						break;
				}

				player.addPotionEffect(new PotionEffect(GrowthCraftCellar.potionTipsy.id, time, amplifier));

				if (amplifier >= 4)
				{
					CellarAchievement.GET_DRUNK.addStat(player, 1);
				}
			}
		}
	}

	public static void addEffects(Fluid booze, ItemStack stack, World world, EntityPlayer player)
	{
		if (booze == null) return;

		final BoozeEffect effect = CellarRegistry.instance().booze().getEffect(booze);
		if (effect != null)
		{
			addTipsyEffect(effect, stack, world, player);
			for (PotionEntry entry : effect.getPotionEntries())
			{
				addPotionEffect(booze, stack, player, entry.getID(), entry.getTime(), entry.getLevel());
			}
		}
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static void writePotionTooltip(Fluid booze, ItemStack stack, EntityPlayer player, List list, boolean bool, int potnID, int potnTime, int potionLevel)
	{
		final PotionEffect pe = BoozeUtils.makePotionEffect(booze, stack, potnID, potnTime, potionLevel);

		String s = StatCollector.translateToLocal(pe.getEffectName()).trim();
		if (pe.getAmplifier() > 0)
		{
			s = s + " " + StatCollector.translateToLocal("potion.potency." + pe.getAmplifier()).trim();
		}

		if (pe.getDuration() > 20)
		{
			s = s + " (" + Potion.getDurationString(pe) + ")";
		}
		list.add(EnumChatFormatting.GRAY + s);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static void writeNauseaTooltip(ItemStack stack, EntityPlayer player, List list, boolean bool, float nauseaChance, int nauseaTime)
	{
		final PotionEffect nausea = new PotionEffect(Potion.confusion.id, nauseaTime, 0);
		String n = "";
		final String p = StatCollector.translateToLocalFormatted("grc.cellar.format.tipsy_chance", Math.round(nauseaChance * 100));

		if (nausea.getDuration() > 20)
		{
			n = n + "(" + Potion.getDurationString(nausea) + ")";
		}
		list.add(EnumChatFormatting.GRAY + p + " " + n);
	}

	public static void addInformation(Fluid booze, ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		if (booze == null) return;
		final BoozeEffect effect = CellarRegistry.instance().booze().getEffect(booze);

		if (effect != null)
		{
			if (effect.canCauseTipsy())
			{
				writeNauseaTooltip(stack, player, list, bool, effect.getTipsyChance(), effect.getTipsyTime());
			}

			for (PotionEntry entry : effect.getPotionEntries())
			{
				writePotionTooltip(booze, stack, player, list, bool, entry.getID(), entry.getTime(), entry.getLevel());
			}
		}
	}

	public static boolean hasEffect(Fluid booze)
	{
		final BoozeEffect effect = CellarRegistry.instance().booze().getEffect(booze);
		if (effect != null)
		{
			return effect.isValid();
		}
		return false;
	}
}
