package me.benfah.cu.api;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import me.benfah.cu.util.JavassistUtil;
import me.benfah.cu.util.InformationEntry;
import me.benfah.cu.util.Utils;

public class CustomGUI extends CustomBase
{

	protected int size;
	// protected String pathToModel2;
	// protected String name2;
	// protected short id2;

	public CustomGUI(InformationEntry pathToUpperModel, InformationEntry pathToLowerModel, int size)
	{

		super(pathToUpperModel, Material.DIAMOND_HOE);
		this.getModelPathEntry().add(pathToLowerModel);
		this.size = size;
	}

	public CustomGUI(String name, InformationEntry pathToUpperModel, InformationEntry pathToLowerModel, int size,
			Material baseMat)
	{

		super(pathToUpperModel, baseMat);
		this.getModelPathEntry().add(pathToLowerModel);
		this.size = size;
	}

	public CustomGUI(String name, String pathToUpperModel, String pathToLowerModel, int size)
	{

		super(new InformationEntry(pathToUpperModel, name + "1"), Material.DIAMOND_HOE);
		this.getModelPathEntry().add(new InformationEntry(pathToLowerModel, name + "2"));
		this.size = size;
	}

	public int getSize()
	{
		return size;
	}

	public void setSize(int size)
	{
		this.size = size;
	}

	public Plugin getPlugin()
	{
		return plugin;
	}

	public void onClick(InventoryClickEvent e)
	{

	}

	protected ItemStack getUpperStack()
	{
		ItemStack upper = new ItemStack(baseMaterial);
		upper.setDurability(getMainModelPathEntry().getId());
		ItemMeta im = upper.getItemMeta();
		im.setDisplayName("");
		im.setUnbreakable(true);
		im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);
		upper.setItemMeta(im);
		upper = Utils.updateTags(upper);
		return upper;
	}

	protected ItemStack getLowerStack()
	{

		ItemStack lower = new ItemStack(baseMaterial);
		lower.setDurability(getSecondModelPathEntry().getId());
		ItemMeta lowerim = lower.getItemMeta();
		lowerim.setUnbreakable(true);
		lowerim.setDisplayName("");
		lowerim.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);

		lower.setItemMeta(lowerim);
		lower = Utils.updateTags(lower);
		return lower;
	}

	public CInventory createInventory()
	{
		return createInventory("");
	}

	public CInventory createInventory(String title)
	{

		Inventory inv = /* Bukkit.createInventory(null, size, title) */ null;

		try
		{
			inv = (Inventory) JavassistUtil.getCUInventoryCustomClass()
					.getConstructor(InventoryHolder.class, int.class, String.class).newInstance(null, size, title);
			JavassistUtil.getCUInventoryCustomClass().getField("customGui").set(inv, this.getName());
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		if (getMainModelPathEntry().getId() != null)
			inv.setItem(0, getUpperStack());
		if (getSecondModelPathEntry().getId() != null)
			inv.setItem(inv.getSize() - 9, getLowerStack());

		return new CInventory(inv, this);

	}

	public void onClose(InventoryCloseEvent e)
	{

	}

	public String getName()
	{
		return getMainModelPathEntry().getName();
	}

	public InformationEntry getSecondModelPathEntry()
	{
		return getModelPathEntry().get(1);
	}

}
