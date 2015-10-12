package growthcraft.cellar.item;

import java.util.List;

import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.utils.UnitFormatter;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;

public class ItemBucketBooze extends ItemBucket
{
	private String iconName;
	private Fluid[] boozes;
	private int index;

	@SideOnly(Side.CLIENT)
	private IIcon bucket;
	@SideOnly(Side.CLIENT)
	private IIcon contents;

	private int color = 0xFFFFFF;

	public ItemBucketBooze(Block block, Fluid[] boozeAry, int indx, CreativeTabs creativeTab)
	{
		super(block);
		setContainerItem(Items.bucket);
		setUnlocalizedName("grc.boozeBucket");
		setCreativeTab(creativeTab);
		this.index = indx;
		this.boozes = boozeAry;
	}

	public ItemBucketBooze(Block block, Fluid[] boozeAry, int indx)
	{
		this(block, boozeAry, indx, GrowthCraftCellar.tab);
	}

	public Fluid getBooze(int id)
	{
		return boozes[id];
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return UnitFormatter.fluidBucketName(getBooze(index));
	}

	public ItemBucketBooze setColor(int kolor)
	{
		this.color = kolor;
		return this;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		writeModifierTooltip(stack, player, list, bool);
	}

	protected void writeModifierTooltip(ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		final Fluid booze = getBooze(index);
		final String s = UnitFormatter.fluidModifier(booze);
		if (s != null) list.add(s);
	}

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
		return pass == 1 ? this.color : 0xFFFFFF;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
}
