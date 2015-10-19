package growthcraft.bees.common.item;

import growthcraft.bees.GrowthCraftBees;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemHoneyJar extends ItemFood
{
	public ItemHoneyJar()
	{
		super(6, false);
		this.setUnlocalizedName("grc.honeyJar");
		this.setCreativeTab(GrowthCraftBees.tab);
		this.setContainerItem(Items.flower_pot);
		this.setMaxStackSize(1);
	}

	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
	{
		super.onEaten(stack, world, player);
		return new ItemStack(Items.flower_pot);
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg)
	{
		this.itemIcon = reg.registerIcon("grcbees:honeyjar");
	}
}
