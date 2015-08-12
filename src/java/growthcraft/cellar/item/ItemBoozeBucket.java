package growthcraft.cellar.item;

import java.util.List;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.GrowthCraftCellar;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class ItemBoozeBucket extends Item
{
	private Fluid[] booze;

	@SideOnly(Side.CLIENT)
	private IIcon bucket;
	@SideOnly(Side.CLIENT)
	private IIcon contents;

	private int color = 16777215;
	public ItemBoozeBucket(Fluid[] booze)
	{
		super();
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setContainerItem(Items.bucket);
		this.setUnlocalizedName("grc.boozeBucket");
		this.setCreativeTab(GrowthCraftCellar.tab);

		this.booze = booze;
	}

	public ItemBoozeBucket setColor(int color)
	{
		this.color = color;
		return this;
	}

	public Fluid[] getBoozeArray()
	{
		return this.booze;
	}

	public Fluid getBooze(int i)
	{
		if (i >= this.booze.length)
		{
			return this.booze[0];
		}
		else
		{
			return this.booze[i];
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5)
	{
		if (stack.getItemDamage() >= this.booze.length)
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
		if (CellarRegistry.instance().isFluidBooze(this.getBooze(stack.getItemDamage())))
		{
			String s =  I18n.format(this.getBooze(stack.getItemDamage()).getUnlocalizedName() + ".modifier");
			list.add(EnumChatFormatting.GRAY + s);
		}
	}

	/************
	 * STUFF
	 ************/
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister reg)
	{
		this.bucket = reg.registerIcon("bucket_empty");
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
		String s = super.getItemStackDisplayName(stack);
		return s + " " + StatCollector.translateToLocal(CellarRegistry.instance().getBoozeName(this.booze));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List list)
	{
		for (int i = 0; i < this.booze.length; i++)
		{
			list.add(new ItemStack(par1, 1, i));
		}
	}
}
