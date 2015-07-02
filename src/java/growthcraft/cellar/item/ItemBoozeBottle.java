package growthcraft.cellar.item;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.Utils;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBoozeBottle extends ItemFood
{
	private Fluid[] booze;

	private boolean tipsyBool   = false;
	private float   tipsyChance = 0.0F;
	private int     tipsyTime   = 0;

	private boolean potionBool   = false;
	private int     potionAmount = 0;
	private int[]   potionID;
	private int[]   potionTime;

	@SideOnly(Side.CLIENT)
	private IIcon bottle;
	@SideOnly(Side.CLIENT)
	private IIcon contents;
	@SideOnly(Side.CLIENT)
	private IIcon liquid;

	private int color = 16777215;

	public ItemBoozeBottle(int nut, float sat, Fluid[] booze) 
	{
		super(nut, sat, false);
		this.setAlwaysEdible();
		this.setMaxStackSize(4);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setContainerItem(Items.glass_bottle);
		this.setCreativeTab(GrowthCraftCellar.tab);

		this.booze = booze;
	}

	public ItemBoozeBottle setColor(int color)
	{
		this.color = color;
		return this;
	}

	public ItemBoozeBottle setTipsy(float chance, int time)
	{
		this.tipsyBool = true;
		if (chance > 1.0F)
		{
			this.tipsyChance = 1.0F;
		}
		else if (chance < 0.1F)
		{
			this.tipsyChance = 0.1F;
		}
		else
		{
			this.tipsyChance = chance;
		}
		this.tipsyTime = time;
		return this;
	}

	public ItemBoozeBottle setPotionEffects(int[] ids, int[] times)
	{
		if (ids.length == times.length)
		{
			this.potionBool   = true;
			this.potionID     = ids;
			this.potionTime   = times;
			this.potionAmount = ids.length;
		}
		else
		{
			throw new IllegalArgumentException("[GrowthCraft Cellar] Error at creating a new bottle of booze. Check array lengths of potion effects.");
		}
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
	 * ON USE
	 ************/
	@Override
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
	{
		if (!player.capabilities.isCreativeMode)
		{
			--stack.stackSize;
		}

		player.getFoodStats().func_151686_a(this, stack);
		world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
		this.onFoodEaten(stack, world, player);

		if (!world.isRemote)
		{
			if (Utils.isIntegerInRange(stack.getItemDamage(), 1, 3))
			{
				/*int amplifier = 0;

				if (player.isPotionActive(Potion.confusion))
				{
					amplifier = player.getActivePotionEffect(Potion.confusion).getAmplifier();
					amplifier += 1;

					if (amplifier >= 6)
					{
						amplifier = 6;
					}
				}

				float chance = this.nauseaChance + (((this.nauseaChance / 4.0F) * (float)amplifier));
				int time = this.nauseaTime + (((this.nauseaTime / 4) * amplifier));

				if (world.rand.nextFloat() < chance && this.nauseaBool)
				{
					player.addPotionEffect(new PotionEffect(Potion.confusion.id, time, amplifier));

					if (amplifier >= 3)
					{
						player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, time, amplifier - 3));
						player.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, time, amplifier - 3));
					}
				}*/

				if (this.tipsyBool)
				{
					if (world.rand.nextFloat() < this.tipsyChance)
					{
						int amplifier = 0;
						int time = 1200;
						if (player.isPotionActive(GrowthCraftCellar.potionTipsy))
						{
							amplifier = player.getActivePotionEffect(GrowthCraftCellar.potionTipsy).getAmplifier() + 1;
							if (amplifier > 4)
							{
								amplifier = 4;
							}
						}

						switch (amplifier)
						{
						case 1: time = 3000; break;
						case 2: time = 6750; break;
						case 3: time = 12000; break;
						case 4: time = 24000; break;
						}

						player.addPotionEffect(new PotionEffect(GrowthCraftCellar.potionTipsy.id, time, amplifier));

						if (amplifier >= 4)
						{
							player.addStat(GrowthCraftCellar.getDrunk, 1);
						}
					}
				}

				if (this.potionBool)
				{
					for (int loop = 0; loop < this.potionAmount; ++loop)
					{
						this.addPotionEffect(stack, player, this.potionID[loop], this.potionTime[loop]);
					}
				}
			}
		}

		return stack.stackSize <= 0 ? new ItemStack(Items.glass_bottle) : stack;
	}


	protected void addPotionEffect(ItemStack stack, EntityPlayer player, int potionID, int potionTime)
	{
		if (stack.getItemDamage() == 1)
		{
			player.addPotionEffect(new PotionEffect(potionID, potionTime, 0));
		}
		else if (stack.getItemDamage() == 2)
		{
			player.addPotionEffect(new PotionEffect(potionID, Math.round(potionTime / 2), 1));
		}
		else if (stack.getItemDamage() == 3)
		{
			player.addPotionEffect(new PotionEffect(potionID, Math.round(potionTime * 2.67F), 0));
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

		if (Utils.isIntegerInRange(stack.getItemDamage(), 1, 3))
		{
			if (this.tipsyBool)
			{
				writeNauseaTooltip(stack, player, list, bool, this.tipsyChance, this.tipsyTime);
			}

			if (this.potionBool)
			{
				for (int loop = 0; loop < this.potionAmount; ++loop)
				{
					writePotionTooltip(stack, player, list, bool, this.potionID[loop], this.potionTime[loop]);
				}
			}
		}
	}

	protected void writeModifierTooltip(ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		if (CellarRegistry.instance().isFluidBooze(this.getBooze(stack.getItemDamage())))
		{
			String s = I18n.format(this.getBooze(stack.getItemDamage()).getUnlocalizedName() + ".modifier");
			list.add(EnumChatFormatting.GRAY + s);
		}
	}

	protected void writePotionTooltip(ItemStack stack, EntityPlayer player, List list, boolean bool, int potionID, int potionTime)
	{
		PotionEffect pe;
		if (stack.getItemDamage() == 1)
		{
			pe = new PotionEffect(potionID, potionTime, 0);
		}
		else if (stack.getItemDamage() == 2)
		{
			pe = new PotionEffect(potionID, Math.round(potionTime / 2), 1);
		}
		else
		{
			pe = new PotionEffect(potionID, Math.round(potionTime * 2.67F), 0);
		}

		String s = StatCollector.translateToLocal(pe.getEffectName()).trim();
		if (pe.getAmplifier() > 0)
		{
			s = s + " " + StatCollector.translateToLocal("potion.potency." + pe.getAmplifier()).trim();
		}

		if (pe.getDuration() > 20)
		{
			s = s + " (" + Potion.getDurationString(pe) + ")";
		}
		list.add(EnumChatFormatting.GRAY + s);
	}

	protected void writeNauseaTooltip(ItemStack stack, EntityPlayer player, List list, boolean bool, float nauseaChance, int nauseaTime)
	{
		PotionEffect nausea = new PotionEffect(Potion.confusion.id, nauseaTime, 0);
		String n = "";
		String p = Math.round(nauseaChance * 100) + "% Chance of Tipsy";

		if (nausea.getDuration() > 20)
		{
			n = n + "(" + Potion.getDurationString(nausea) + ")";
		}
		list.add(EnumChatFormatting.GRAY + p + " " + n);
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
		return pass == 0 ? this.color : 16777215;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack)
	{
		if (Utils.isIntegerInRange(stack.getItemDamage(), 1, 3))
		{
			return true;
		}
		return false;
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
		return StatCollector.translateToLocal(CellarRegistry.instance().getBoozeName(this.booze));
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
