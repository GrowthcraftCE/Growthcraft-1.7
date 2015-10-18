package growthcraft.nether.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemNetherMaliceFruit extends ItemFood
{
	public ItemNetherMaliceFruit()
	{
		super(0, false);
		setUnlocalizedName("grcnether.netherMaliceFruit");
		setTextureName("grcnether:malice_fruit");
		setAlwaysEdible();
	}

	protected void onFoodEaten(ItemStack itemstack, World world, EntityPlayer player)
	{
		if (!world.isRemote)
		{
			player.addPotionEffect(new PotionEffect(Potion.weakness.id, 20 * 20, 3));
			player.addPotionEffect(new PotionEffect(Potion.confusion.id, 40 * 20, 0));
		}
	}
}
