package growthcraft.cellar.common.item;

import java.util.List;

import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.util.UnitFormatter;
import growthcraft.cellar.util.BoozeUtils;

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

public class ItemBucketBooze extends ItemBucket implements IBoozeContainer
{
	private String iconName;
	private Fluid booze;
	private int index;

	@SideOnly(Side.CLIENT)
	private IIcon bucket;
	@SideOnly(Side.CLIENT)
	private IIcon contents;

	public ItemBucketBooze(Block block, Fluid buze, CreativeTabs creativeTab)
	{
		super(block);
		setContainerItem(Items.bucket);
		setUnlocalizedName("grc.boozeBucket");
		setCreativeTab(creativeTab);
		this.booze = buze;
	}

	public ItemBucketBooze(Block block, Fluid buze)
	{
		this(block, buze, GrowthCraftCellar.tab);
	}

	public Fluid getBooze()
	{
		return booze;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return UnitFormatter.fluidBucketName(getBooze());
	}

	@SideOnly(Side.CLIENT)
	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		super.addInformation(stack, player, list, bool);
		BoozeUtils.addInformation(getBooze(), stack, player, list, bool);
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
		return pass == 1 ? booze.getColor() : 0xFFFFFF;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
}
