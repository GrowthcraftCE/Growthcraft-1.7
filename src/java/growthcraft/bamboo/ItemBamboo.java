package growthcraft.bamboo;

import growthcraft.core.GrowthCraftCore;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBamboo extends Item
{
	public ItemBamboo() 
	{
		super();
		this.setUnlocalizedName("grc.bamboo");
		this.setCreativeTab(GrowthCraftCore.tab);
	}

	/************
	 * MAIN
	 ************/	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int dir, float par8, float par9, float par10)
	{
		Block block1 = world.getBlock(x, y, z);

		if (block1 == Blocks.snow && (world.getBlockMetadata(x, y, z) & 7) < 1)
		{
			dir = 1;
		}
		else if (block1 != Blocks.vine && block1 != Blocks.tallgrass && block1 != Blocks.deadbush)
		{
			if (dir == 0)
			{
				--y;
			}

			if (dir == 1)
			{
				++y;
			}

			if (dir == 2)
			{
				--z;
			}

			if (dir == 3)
			{
				++z;
			}

			if (dir == 4)
			{
				--x;
			}

			if (dir == 5)
			{
				++x;
			}
		}

		if (!player.canPlayerEdit(x, y, z, dir, stack))
		{
			return false;
		}
		else if (stack.stackSize == 0)
		{
			return false;
		}
		else
		{
			Block block = GrowthCraftBamboo.bambooStalk;
			if (world.canPlaceEntityOnSide(block, x, y, z, false, dir, (Entity)null, stack))
			{
				if (world.setBlock(x, y, z, block, 1, 3))
				{
					if (world.getBlock(x, y, z) == block)
					{
						block.onBlockPlacedBy(world, x, y, z, player, stack);
						block.onPostBlockPlaced(world, x, y, z, 1);
					}

					world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
					--stack.stackSize;
				}
			}

			return true;
		}
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg)
	{
		this.itemIcon = reg.registerIcon("grcbamboo:bamboo");
	}
}
