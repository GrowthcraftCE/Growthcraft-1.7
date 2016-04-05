package growthcraft.cellar.common.item;

import java.util.List;

import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.BoozeUtils;
import growthcraft.core.common.item.GrcItemFoodBase;
import growthcraft.core.common.item.IFluidItem;
import growthcraft.core.lib.GrcCoreState;
import growthcraft.core.util.ItemUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class ItemBoozeBottle extends GrcItemFoodBase implements IFluidItem
{
	private Fluid[] boozes;

	@SideOnly(Side.CLIENT)
	private IIcon bottle;
	@SideOnly(Side.CLIENT)
	private IIcon contents;

	public ItemBoozeBottle(int nut, float sat, Fluid[] boozeAry)
	{
		super(nut, sat, false);
		this.setAlwaysEdible();
		this.setMaxStackSize(4);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setContainerItem(Items.glass_bottle);
		this.setCreativeTab(GrowthCraftCellar.tab);

		this.boozes = boozeAry;
	}

	public Fluid[] getFluidArray()
	{
		return this.boozes;
	}

	public Fluid getFluidByIndex(int i)
	{
		return (i < 0 || i >= boozes.length) ? boozes[0] : boozes[i];
	}

	@Override
	public Fluid getFluid(ItemStack stack)
	{
		if (stack == null) return null;
		return getFluidByIndex(stack.getItemDamage());
	}

	public int getColor(ItemStack stack)
	{
		final Fluid booze = getFluid(stack);
		if (booze != null) return booze.getColor();
		return 0xFFFFFF;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5)
	{
		if (stack.getItemDamage() >= getFluidArray().length)
		{
			stack.setItemDamage(0);
		}
	}

	/************
	 * ON USE
	 ************/
	@Override
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
	{
		if (!player.capabilities.isCreativeMode)
		{
			if (!world.isRemote)
			{
				final ItemStack result = ItemUtils.consumeStack(stack.splitStack(1));
				ItemUtils.addStackToPlayer(result, player, world, false);
			}
		}

		player.getFoodStats().func_151686_a(this, stack);
		world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
		this.onFoodEaten(stack, world, player);

		if (!world.isRemote)
		{
			BoozeUtils.addEffects(getFluid(stack), stack, world, player);
		}

		return stack.stackSize <= 0 ? null : stack;
	}

	/************
	 * TOOLTIP
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		super.addInformation(stack, player, list, bool);
		if (GrcCoreState.showDetailedInformation())
		{
			final Fluid booze = getFluid(stack);
			BoozeUtils.addBottleInformation(booze, stack, player, list, bool);
		}
		else
		{
			list.add(EnumChatFormatting.GRAY +
					GrcI18n.translate("grc.tooltip.detailed_information",
						EnumChatFormatting.WHITE + "SHIFT" + EnumChatFormatting.GRAY));
		}
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg)
	{
		this.bottle = reg.registerIcon("grccellar:booze");
		this.contents = reg.registerIcon("grccellar:booze_contents");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamageForRenderPass(int par1, int pass)
	{
		return pass == 0 ? this.contents : this.bottle;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass)
	{
		return pass == 0 ? getColor(stack) : 0xFFFFFF;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack, int pass)
	{
		return BoozeUtils.hasEffect(getFluid(stack));
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName();
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
	{
		return 32;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack)
	{
		return EnumAction.drink;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
		return stack;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		final Fluid booze = getFluid(stack);
		if (booze != null)
		{
			return GrcI18n.translate(booze.getUnlocalizedName());
		}
		return super.getItemStackDisplayName(stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		for (int i = 0; i < getFluidArray().length; i++)
		{
			list.add(new ItemStack(item, 1, i));
		}
	}
}
