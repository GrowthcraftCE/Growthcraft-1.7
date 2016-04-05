package growthcraft.cellar.common.item;

import java.util.List;

import growthcraft.api.core.fluids.FluidTest;
import growthcraft.api.core.i18n.GrcI18n;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.BoozeUtils;
import growthcraft.core.common.item.GrcItemBase;
import growthcraft.core.lib.GrcCoreState;
import growthcraft.core.util.UnitFormatter;
import growthcraft.cellar.event.EventWaterBag;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

public class ItemWaterBag extends GrcItemBase implements IFluidContainerItem
{
	protected int capacity;
	protected int dosage;

	@SideOnly(Side.CLIENT)
	protected IIcon[] icons;

	public ItemWaterBag()
	{
		super();
		setHasSubtypes(true);
		setMaxDamage(0);
		setUnlocalizedName("grc.waterBag");
		setTextureName("grccellar:water_bag");
		setCreativeTab(GrowthCraftCellar.tab);
		this.maxStackSize = 1;
		this.capacity = GrowthCraftCellar.getConfig().waterBagCapacity;
		this.dosage = GrowthCraftCellar.getConfig().waterBagDosage;
	}

	public NBTTagCompound getFluidTagFromStack(ItemStack stack)
	{
		if (stack != null)
		{
			if (stack.stackTagCompound != null)
			{
				if (stack.stackTagCompound.hasKey("Fluid"))
				{
					return stack.stackTagCompound.getCompoundTag("Fluid");
				}
			}
		}
		return null;
	}

	/* IFluidContainerItem */
	@Override
	public FluidStack getFluid(ItemStack container)
	{
		final NBTTagCompound tag = getFluidTagFromStack(container);
		return tag != null ? FluidStack.loadFluidStackFromNBT(tag) : null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg)
	{
		this.icons = new IIcon[17];
		this.icons[0] = reg.registerIcon(getIconString() + "/white");
		this.icons[1] = reg.registerIcon(getIconString() + "/orange");
		this.icons[2] = reg.registerIcon(getIconString() + "/magenta");
		this.icons[3] = reg.registerIcon(getIconString() + "/light_blue");
		this.icons[4] = reg.registerIcon(getIconString() + "/yellow");
		this.icons[5] = reg.registerIcon(getIconString() + "/lime");
		this.icons[6] = reg.registerIcon(getIconString() + "/pink");
		this.icons[7] = reg.registerIcon(getIconString() + "/gray");
		this.icons[8] = reg.registerIcon(getIconString() + "/light_gray");
		this.icons[9] = reg.registerIcon(getIconString() + "/cyan");
		this.icons[10] = reg.registerIcon(getIconString() + "/purple");
		this.icons[11] = reg.registerIcon(getIconString() + "/blue");
		this.icons[12] = reg.registerIcon(getIconString() + "/brown");
		this.icons[13] = reg.registerIcon(getIconString() + "/green");
		this.icons[14] = reg.registerIcon(getIconString() + "/red");
		this.icons[15] = reg.registerIcon(getIconString() + "/black");
		this.icons[16] = reg.registerIcon(getIconString() + "/default");
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		for (int i = 0; i < 17; ++i)
		{
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta)
	{
		return this.icons[meta];
	}

	public int getFluidAmount(ItemStack container)
	{
		final FluidStack stack = getFluid(container);
		if (FluidTest.isValid(stack)) return stack.amount;
		return 0;
	}

	@Override
	public int getCapacity(ItemStack container)
	{
		return capacity;
	}

	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill)
	{
		if (resource == null)
		{
			return 0;
		}

		final int amount = Math.min(resource.amount, dosage);

		if (!doFill)
		{
			if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Fluid"))
			{
				return Math.min(capacity, amount);
			}

			final FluidStack stack = FluidStack.loadFluidStackFromNBT(container.stackTagCompound.getCompoundTag("Fluid"));

			if (stack == null)
			{
				return Math.min(capacity, amount);
			}

			if (!stack.isFluidEqual(resource))
			{
				return 0;
			}

			return Math.min(capacity - stack.amount, amount);
		}

		if (container.stackTagCompound == null)
		{
			container.stackTagCompound = new NBTTagCompound();
		}

		if (!container.stackTagCompound.hasKey("Fluid"))
		{
			final FluidStack res = resource.copy();
			res.amount = amount;
			final NBTTagCompound fluidTag = res.writeToNBT(new NBTTagCompound());

			if (capacity < amount)
			{
				fluidTag.setInteger("Amount", capacity);
				container.stackTagCompound.setTag("Fluid", fluidTag);
				return capacity;
			}

			container.stackTagCompound.setTag("Fluid", fluidTag);
			return amount;
		}

		final NBTTagCompound fluidTag = container.stackTagCompound.getCompoundTag("Fluid");
		final FluidStack stack = FluidStack.loadFluidStackFromNBT(fluidTag);

		if (!stack.isFluidEqual(resource))
		{
			return 0;
		}

		int filled = capacity - stack.amount;
		if (amount < filled)
		{
			stack.amount += amount;
			filled = amount;
		}
		else
		{
			stack.amount = capacity;
		}

		container.stackTagCompound.setTag("Fluid", stack.writeToNBT(fluidTag));
		return filled;
	}

	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain)
	{
		final int expectedDrain = Math.min(maxDrain, dosage);

		if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Fluid"))
		{
			return null;
		}

		final FluidStack stack = FluidStack.loadFluidStackFromNBT(container.stackTagCompound.getCompoundTag("Fluid"));
		if (stack == null)
		{
			return null;
		}

		final int currentAmount = stack.amount;
		stack.amount = Math.min(stack.amount, expectedDrain);
		if (doDrain)
		{
			if (currentAmount == stack.amount)
			{
				container.stackTagCompound.removeTag("Fluid");

				if (container.stackTagCompound.hasNoTags())
				{
					container.stackTagCompound = null;
				}
				return stack;
			}

			final NBTTagCompound fluidTag = container.stackTagCompound.getCompoundTag("Fluid");
			fluidTag.setInteger("Amount", currentAmount - stack.amount);
			container.stackTagCompound.setTag("Fluid", fluidTag);
		}
		return stack;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.drink;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 32;
	}

	public boolean hasEnoughToDrink(ItemStack stack)
	{
		final FluidStack drained = drain(stack, dosage, false);
		if (drained != null)
		{
			if (drained.amount >= dosage)
			{
				return true;
			}
		}
		return false;
	}

	protected void applyEffects(ItemStack stack, World world, EntityPlayer player)
	{
		final FluidStack fluidstack = getFluid(stack);
		if (fluidstack != null)
		{
			BoozeUtils.addEffects(fluidstack.getFluid(), stack, world, player);
		}
	}

	@Override
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
	{
		if (hasEnoughToDrink(stack))
		{
			// This is not an ItemFood, and therefore, NOT FOOD. ;_;
			//player.getFoodStats().func_151686_a(this, stack);

			final boolean cancelled = GrowthCraftCellar.CELLAR_BUS.post(new EventWaterBag.PreDrink(stack, world, player));
			if (!cancelled)
			{
				world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
				if (!world.isRemote)
				{
					applyEffects(stack, world, player);
					if (!player.capabilities.isCreativeMode) drain(stack, dosage, true);
				}
				GrowthCraftCellar.CELLAR_BUS.post(new EventWaterBag.PostDrink(stack, world, player));
			}
		}
		return stack;
	}

	@Override
	public boolean onItemUse(ItemStack _stack, EntityPlayer _p, World _w, int _x, int _y, int _z, int _d, float _fx, float _fy, float _fz)
	{
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if (hasEnoughToDrink(stack))
		{
			player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
		}

		return stack;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) + stack.getItemDamage();
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		final String basename = super.getItemStackDisplayName(stack);
		final String fluidName = UnitFormatter.fluidName(getFluid(stack));
		if (fluidName != null)
		{
			return GrcI18n.translate("grc.cellar.format.waterBag.name", basename, fluidName);
		}
		return basename;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		super.addInformation(stack, player, list, bool);
		final FluidStack fluidstack = getFluid(stack);
		if (fluidstack != null)
		{
			if (GrcCoreState.showDetailedInformation())
			{
				final String fluidname = UnitFormatter.fluidNameForContainer(fluidstack);
				list.add(GrcI18n.translate("grc.cellar.format.waterBag.contents", fluidname, fluidstack.amount, getCapacity(stack)));
				final Fluid booze = fluidstack.getFluid();
				BoozeUtils.addEffectInformation(booze, stack, player, list, bool);
			}
			else
			{
				list.add(EnumChatFormatting.GRAY +
					GrcI18n.translate("grc.tooltip.detailed_information",
						EnumChatFormatting.WHITE + "SHIFT" + EnumChatFormatting.GRAY));
			}
		}
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return FluidTest.isValid(getFluid(stack));
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return 1D - ((double)getFluidAmount(stack) / (double)getCapacity(stack));
	}
}
