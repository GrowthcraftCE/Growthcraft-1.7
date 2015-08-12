package growthcraft.cellar;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

public class ItemChievDummy extends Item
{
	private IIcon[] icon;

	public ItemChievDummy()
	{
		super();
		this.setCreativeTab(null);
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setUnlocalizedName("grc.chievItemDummy");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg)
	{
		this.icon = new IIcon[1];
		this.icon[0] = reg.registerIcon("grccellar:chievicon_0");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta)
	{
		return this.icon[meta];
	}
}
