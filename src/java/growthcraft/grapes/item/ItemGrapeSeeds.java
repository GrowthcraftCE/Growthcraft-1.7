package growthcraft.grapes.item;

import growthcraft.core.GrowthCraftCore;
import growthcraft.core.util.BlockCheck;
import growthcraft.grapes.GrowthCraftGrapes;
import growthcraft.grapes.block.BlockGrapeVine0;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemGrapeSeeds extends Item implements IPlantable
{
	public ItemGrapeSeeds()
	{
		super();
		this.setUnlocalizedName("grc.grapeSeeds");
		this.setCreativeTab(GrowthCraftCore.tab);
	}

	/************
	 * MAIN
	 ************/
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int dir, float par8, float par9, float par10)
	{
		if (dir != 1)
		{
			return false;
		}
		else if (player.canPlayerEdit(x, y, z, dir, stack) && player.canPlayerEdit(x, y + 1, z, dir, stack))
		{
			final BlockGrapeVine0 block = GrowthCraftGrapes.grapeVine0.getBlock();
			if (BlockCheck.canSustainPlant(world, x, y, z, ForgeDirection.UP, block) && world.isAirBlock(x, y + 1, z))
			{
				world.setBlock(x, y + 1, z, block);
				--stack.stackSize;
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg)
	{
		this.itemIcon = reg.registerIcon("grcgrapes:grape_seeds");
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
	{
		return EnumPlantType.Crop;
	}

	@Override
	public Block getPlant(IBlockAccess world, int x, int y, int z)
	{
		return GrowthCraftGrapes.grapeVine0.getBlock();
	}

	@Override
	public int getPlantMetadata(IBlockAccess world, int x, int y, int z)
	{
		return 0;
	}
}
