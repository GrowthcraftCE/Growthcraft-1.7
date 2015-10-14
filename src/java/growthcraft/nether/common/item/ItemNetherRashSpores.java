package growthcraft.nether.common.item;

import growthcraft.nether.GrowthCraftNether;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemNetherRashSpores extends Item
{
	public ItemNetherRashSpores()
	{
		super();
		this.setUnlocalizedName("grcnether.netherRashSpores");
		this.setCreativeTab(GrowthCraftNether.tab);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg)
	{
		this.itemIcon = reg.registerIcon("grcnether:netherrash_spores");
	}
}
