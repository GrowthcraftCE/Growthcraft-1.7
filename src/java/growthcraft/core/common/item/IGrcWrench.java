package growthcraft.core.common.item;

import net.minecraft.entity.player.EntityPlayer;

/* Copy of the Buildcraft wrench API */
public interface IGrcWrench
{
	boolean canWrench(EntityPlayer player, int x, int y, int z);
	void wrenchUsed(EntityPlayer player, int x, int y, int z);
}
