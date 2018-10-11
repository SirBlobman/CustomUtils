package me.benfah.cu.api;

import java.lang.reflect.Method;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import me.benfah.cu.util.ReflectionUtils;
import me.benfah.cu.util.Utils;

public class CustomBlock extends CustomBase
{

	public Plugin getPlugin()
	{
		return plugin;
	}

	public CustomBlock(String name, String modelPath, String title)
	{
		super(name, modelPath, Material.DIAMOND_HOE);
		this.title = title;
	}

	public CustomBlock(String name, String modelPath, String title, Material baseMat)
	{
		super(name, modelPath, baseMat);
		this.title = title;
	}

	protected String title;

	public void setName(String name)
	{
		getMainModelPathEntry().setName(name);
	}

	public short getId()
	{
		return getMainModelPathEntry().getId();
	}

	public void setId(short id)
	{
		getMainModelPathEntry().setId(id);
	}

	// public void setModelPath(String path)
	// {
	// this.pathToModel1 = path;
	// }

	public String getModelPath()
	{
		return getMainModelPathEntry().getPathToModel();
	}

	public ItemStack getBlockItem()
	{
		ItemStack is = new ItemStack(Material.DIAMOND_HOE);
		ItemMeta im = is.getItemMeta();
		im.setUnbreakable(true);
		im.setDisplayName(ChatColor.RESET + title);
		is.setDurability(getId());
		is.setItemMeta(im);
		is = Utils.updateTags(is);
		return is;
	}

	public void setBlock(World w, int x, int y, int z)
	{
		setBlock(new Location(w, x, y, z));
	}

	public void setBlock(Block b)
	{
		setBlock(b.getLocation());
	}

	public void onInteract(PlayerInteractEvent e)
	{

	}

	public Sound getPlaceSound()
	{
		return Sound.BLOCK_STONE_HIT;

	}

	public void onBlockPlaced(BlockPlaceEvent e)
	{

	}

	public void onBlockBroken(BlockBreakEvent e)
	{

	}

	public ItemStack[] getLoot(Block b)
	{
		return new ItemStack[] { getBlockItem() };
	}

	public void setBlock(Location l)
	{
		Block b = l.getWorld().getBlockAt(l);
		b.setType(Material.MOB_SPAWNER);

		CreatureSpawner cs = (CreatureSpawner) b.getState();
		cs.setSpawnedType(EntityType.ARMOR_STAND);
		Class<?> craftCreatureSpawnerClass = ReflectionUtils.getRefClass("{cb}.block.CraftCreatureSpawner");
		Class<?> mobSpawnerAbstractClass = ReflectionUtils.getRefClass("{nms}.MobSpawnerAbstract");
		Class<?> mojangsonParserClass = ReflectionUtils.getRefClass("{nms}.MojangsonParser");

		try
		{

			Object tileEntity = setAccessible(
					craftCreatureSpawnerClass.getSuperclass().getDeclaredMethod("getTileEntity")).invoke(cs);
			Object msa = tileEntity.getClass().getDeclaredMethod("getSpawner").invoke(tileEntity);

			Object nbtz = mojangsonParserClass.getMethod("parse", String.class).invoke(null,
					"{MaxNearbyEntities:0s,RequiredPlayerRange:0s,SpawnData:{id:\"minecraft:armor_stand\",Invisible:1,Marker:1,ArmorItems:[{},{},{},{id:\"minecraft:"
							+ Utils.getUnlocalizedName(baseMaterial) + "\",Count:1b,Damage:" + getId()
							+ "s,tag:{Unbreakable:1}}]}}");
			mobSpawnerAbstractClass.getDeclaredMethod("a", nbtz.getClass()).invoke(msa, nbtz);
		} catch (Exception e1)
		{
			e1.printStackTrace();
		}
		try
		{

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		l.getWorld().playSound(l, getPlaceSound(), 1, 1);
		if (getClass().isAssignableFrom(IInstanceProvider.class))
		{
			BlockInstance bi = BlockInstance.getBlockInstance(b);
			bi.setMetadataValue("shouldUpdate", true);
		}

	}

	public Recipe getRecipe()
	{
		return null;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public Method setAccessible(Method f)
	{
		f.setAccessible(true);
		return f;
	}

}
