package growthcraft.api.fishtrap;

import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

public class FishTrapEntry extends WeightedRandom.Item
{
	private final ItemStack fishable;
	private float damage;
	private boolean isEnchantable;

	public FishTrapEntry(ItemStack fish, int weight)
	{
		super(weight);
		this.fishable = fish;
	}

	public ItemStack getItemStack()
	{
		return fishable;
	}

	public ItemStack getFishable(Random random)
	{
		final ItemStack ret = this.fishable.copy();

		if (this.damage > 0.0F)
		{
			final int i = (int)(this.damage * this.fishable.getMaxDamage());
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

	public float getDamage()
	{
		return damage;
	}

	public boolean getEnchanted()
	{
		return isEnchantable;
	}

	public FishTrapEntry setDamage(float f)
	{
		this.damage = f;
		return this;
	}

	public FishTrapEntry setEnchantable(boolean b)
	{
		this.isEnchantable = b;
		return this;
	}

	public FishTrapEntry setEnchantable()
	{
		return setEnchantable(true);
	}
}
