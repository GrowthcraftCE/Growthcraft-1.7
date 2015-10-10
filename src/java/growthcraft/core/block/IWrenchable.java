package growthcraft.core.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IWrenchable
{
	public boolean wrenchBlock(World world, int x, int y, int z, EntityPlayer player, ItemStack wrench);
}
