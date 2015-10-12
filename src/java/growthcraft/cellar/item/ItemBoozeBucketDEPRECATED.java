package growthcraft.cellar.item;

import java.util.List;

import growthcraft.core.utils.UnitFormatter;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class ItemBoozeBucketDEPRECATED extends Item
{
	private Fluid[] boozes;

	@SideOnly(Side.CLIENT)
	private IIcon bucket;
	@SideOnly(Side.CLIENT)
	private IIcon contents;

	private int color = 16777215;
	public ItemBoozeBucketDEPRECATED(Fluid[] boozeAry)
	{
		super();
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setContainerItem(Items.bucket);
		this.setUnlocalizedName("grc.boozeBucket");
		this.boozes = boozeAry;
	}

	public ItemBoozeBucketDEPRECATED setColor(int kolor)
	{
		this.color = kolor;
		return this;
	}

	public Fluid[] getBoozeArray()
	{
		return this.boozes;
	}

	public Fluid getBooze(int i)
	{
		if (i >= this.boozes.length)
		{
			return this.boozes[0];
		}
		else
		{
			return this.boozes[i];
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5)
	{
		if (stack.getItemDamage() >= this.boozes.length)
		{
			stack.setItemDamage(0);
		}
	}

	/************
	 * TOOLTIP
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		writeModifierTooltip(stack, player, list, bool);
	}

	protected void writeModifierTooltip(ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		final String s = UnitFormatter.fluidModifier(getBooze(stack.getItemDamage()));
		if (s != null) list.add(s);
		list.add(StatCollector.translateToLocal("grc.cellar.item.booze_bucket.deprecated"));
	}

	/************
	 * STUFF
	 ************/
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister reg)
	{
		this.bucket = reg.registerIcon("grccellar:bucket_deprecated");
		this.contents = reg.registerIcon("grccellar:bucket_contents");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIconFromDamageForRenderPass(int par1, int pass)
	{
		return pass == 1 ? this.contents : this.bucket;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass)
	{
		return pass == 1 ? this.color : 16777215;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return UnitFormatter.fluidBucketName(getBooze(stack.getItemDamage())) +
			StatCollector.translateToLocal("grc.cellar.item.booze_bucket.deprecated_suffix");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		for (int i = 0; i < this.boozes.length; i++)
		{
			list.add(new ItemStack(item, 1, i));
		}
	}
}
