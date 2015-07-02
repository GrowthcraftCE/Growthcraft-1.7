package growthcraft.bees;

import growthcraft.core.GrowthCraftCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemHoneyJar extends ItemFood
{
	public ItemHoneyJar() 
	{
		super(6, false);
		this.setUnlocalizedName("grc.honeyJar");
		this.setCreativeTab(GrowthCraftCore.tab);
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
