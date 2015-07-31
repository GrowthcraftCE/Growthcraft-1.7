package growthcraft.apples;

import growthcraft.core.GrowthCraftCore;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAppleSeeds extends Item implements IPlantable
{
	private Block cropBlock;

	public ItemAppleSeeds()
	{
		super();
		this.cropBlock = GrowthCraftApples.appleSapling;
		this.setUnlocalizedName("grc.appleSeeds");
		this.setCreativeTab(GrowthCraftCore.tab);
	}

	/************
	 * MAIN
	 ************/
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int dir, float par8, float par9, float par10)
	{
		Block block = world.getBlock(x, y, z);

		if (block == Blocks.snow_layer && (world.getBlockMetadata(x, y, z) & 7) < 1)
		{
			dir = 1;
		}
		else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush)
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
			if (world.canPlaceEntityOnSide(cropBlock, x, y, z, false, dir, (Entity)null, stack))
			{
				int meta = cropBlock.onBlockPlaced(world, x, y, z, dir, par8, par9, par10, 0);

				if (world.setBlock(x, y, z, cropBlock, meta, 3))
				{
					if (world.getBlock(x, y, z) == cropBlock)
					{
						cropBlock.onBlockPlacedBy(world, x, y, z, player, stack);
						cropBlock.onPostBlockPlaced(world, x, y, z, meta);
					}

					world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), cropBlock.stepSound.func_150496_b(), (cropBlock.stepSound.getVolume() + 1.0F) / 2.0F, cropBlock.stepSound.getPitch() * 0.8F);
					--stack.stackSize;
				}
			}

			return true;
		}
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
	{
		return EnumPlantType.Plains;
	}

	@Override
	public Block getPlant(IBlockAccess world, int x, int y, int z)
	{
		return cropBlock;
	}

	@Override
	public int getPlantMetadata(IBlockAccess world, int x, int y, int z)
	{
		return 0;
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg)
	{
		this.itemIcon = reg.registerIcon("grcapples:apple_seed");
	}
}
