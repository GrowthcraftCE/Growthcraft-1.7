package growthcraft.nether.common.item;

import growthcraft.nether.GrowthCraftNether;
import growthcraft.nether.util.DamageSources;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class ItemNetherMuertecap extends ItemFood implements IPlantable
{
	public ItemNetherMuertecap()
	{
		super(0, false);
		setAlwaysEdible();
		setUnlocalizedName("grcnether.netherMuertecapFood");
		setTextureName("grcnether:muertecap");
		setCreativeTab(GrowthCraftNether.tab);
	}

	protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player)
	{
		if (!world.isRemote)
		{
			player.attackEntityFrom(DamageSources.muertecapFood, 20.0F);
		}
	}

	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
	{
		return EnumPlantType.Nether;
	}

	public Block getPlant(IBlockAccess world, int x, int y, int z)
	{
		return GrowthCraftNether.blocks.netherMuertecap.getBlock();
	}

	public int getPlantMetadata(IBlockAccess world, int x, int y, int z)
	{
		return 0;
	}
}
