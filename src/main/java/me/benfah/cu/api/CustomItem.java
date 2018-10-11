package me.benfah.cu.api;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import me.benfah.cu.util.Utils;

public class CustomItem extends CustomBase
{

	protected String title;
	protected List<String> lore;

	public List<String> getLore()
	{
		return lore;
	}

	public void setLore(List<String> lore)
	{
		this.lore = lore;
	}

	public CustomItem(String name, String modelPath, String title)
	{
		super(name, modelPath, Material.DIAMOND_HOE);
		this.title = title;
	}

	public CustomItem(String name, String modelPath, String title, List<String> lore)
	{
		super(name, modelPath, Material.DIAMOND_HOE);
		this.title = title;
		this.lore = lore;
	}

	public CustomItem(String name, String modelPath, String title, Material baseMat)
	{
		super(name, modelPath, baseMat);
		this.title = title;
	}

	public CustomItem(String name, String modelPath, String title, List<String> lore, Material baseMat)
	{
		super(name, modelPath, baseMat);
		this.title = title;
		this.lore = lore;
	}

	public ItemStack getItem()
	{
		ItemStack result = new ItemStack(baseMaterial);
		result.setDurability(getMainModelPathEntry().getId());
		ItemMeta im = result.getItemMeta();
		im.setUnbreakable(true);
		if (lore != null)
			im.setLore(lore);
		im.setDisplayName(ChatColor.RESET + title);
		result.setItemMeta(im);
		result = Utils.updateTags(result);
		return result;
	}

	public void onUpdate(Player p, int slot)
	{

	}

	public void onInteract(PlayerInteractEvent e, EquipmentSlot es)
	{

	}

	public Recipe getRecipe()
	{
		return null;
	}

	public void onInteractEntity(PlayerInteractEntityEvent e)
	{

	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

}
