package growthcraft.api.fishtrap;

import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

public class FishTrapEntry extends WeightedRandom.Item
{
	public final ItemStack fishable;
	private float damage;
	private boolean isEnchantable;
	public FishTrapEntry(ItemStack fish, int weight)
	{
		super(weight);
		this.fishable = fish;
	}

	public ItemStack getFishable(Random random)
	{
		ItemStack ret = this.fishable.copy();

		if (this.damage > 0.0F)
		{
			int i = (int)(this.damage * this.fishable.getMaxDamage());
			int j = ret.getMaxDamage() - random.nextInt(random.nextInt(i) + 1);
			if (j > i) j = i;
			if (j < 1) j = 1;
			ret.setItemDamage(j);
		}

		if (this.isEnchantable)
		{
			EnchantmentHelper.addRandomEnchantment(random, ret, 30);
			//1.6.4
			/*if (ret.getItem() instanceof ItemEnchantedBook)
			{
				ItemEnchantedBook item = (ItemEnchantedBook) ret.getItem();
				Enchantment enchantment = Enchantment.enchantmentsBookList[random.nextInt(Enchantment.enchantmentsBookList.length)];
				int l = MathHelper.getRandomIntegerInRange(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
				item.addEnchantment(ret, new EnchantmentData(enchantment, l));
			}
			else
			{
				EnchantmentHelper.addRandomEnchantment(random, ret, 30);
			}*/
		}

		return ret;
	}

	public FishTrapEntry setDamage(float f)
	{
		this.damage = f;
		return this;
	}

	public FishTrapEntry setEnchantable()
	{
		this.isEnchantable = true;
		return this;
	}
}
