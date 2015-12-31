package growthcraft.core.common.item;

import buildcraft.api.tools.IToolWrench;

import cpw.mods.fml.common.Optional;
import net.minecraft.entity.player.EntityPlayer;

/**
 * In place of a missing wrench, you can use a stick, which results in
 * this amazing wrench, which does amazing things, no really.
 */
@Optional.Interface(iface="buildcraft.api.tools.IToolWrench", modid="BuildCraft|Core")
public class AmazingStickWrench implements IToolWrench, IGrcWrench
{
	@Override
	public boolean canWrench(EntityPlayer player, int x, int y, int z)
	{
		return true;
	}

	@Override
	public void wrenchUsed(EntityPlayer player, int x, int y, int z)
	{
		// we should have an achivement for this.
	}
}
