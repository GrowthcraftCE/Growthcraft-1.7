package growthcraft.rice.event;

import growthcraft.rice.GrowthCraftRice;
import growthcraft.rice.block.BlockPaddy;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class PlayerInteractEventRice
{
	@SubscribeEvent
	public void PlayerInteract(PlayerInteractEvent event)
	{
		if (event.action == event.action.RIGHT_CLICK_BLOCK)
		{
			final EntityPlayer player = event.entityPlayer;
			final ItemStack itemstack = player.getCurrentEquippedItem();
			if (itemstack != null && itemstack.getItem() instanceof ItemSpade)
			{
				final World world = player.worldObj;
				final BlockPaddy paddyField = (BlockPaddy)GrowthCraftRice.paddyField.getBlock();
				if (world.getBlock(event.x, event.y, event.z) == Blocks.farmland && event.face == 1)
				{
					world.setBlock(event.x, event.y, event.z, paddyField, world.getBlockMetadata(event.x, event.y, event.z), 3);
					world.playSoundEffect((double)((float)event.x + 0.5F), (double)((float)event.y + 0.5F), (double)((float)event.z + 0.5F),
						paddyField.stepSound.func_150496_b(),
						(paddyField.stepSound.getVolume() + 1.0F) / 2.0F,
						paddyField.stepSound.getPitch() * 0.8F);
					itemstack.damageItem(1, player);
				}
			}
		}
	}
}
