package me.benfah.cu.api;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.benfah.cu.util.JavassistUtil;
import me.benfah.cu.util.ReflectionUtils;

public class CInventory
{

	private Inventory inv;
	private CustomGUI customGui;

	protected CInventory(Inventory inv, CustomGUI cgui)
	{
		this.inv = inv;
		this.customGui = cgui;
	}

	/**
	 * DON'T USE THE {@link Player#openInventory(Inventory)} method WITH THIS
	 * INVENTORY Please use the {@link CInventory#openInventory(Player)} method
	 * instead.
	 * 
	 * @return the internal inventory
	 */
	public Inventory getInventory()
	{
		return inv;
	}

	public void openInventory(Player p)
	{
		Class<?> classIInventory = ReflectionUtils.getRefClass("{nms}.IInventory");
		Class<?> classCraftPlayer = ReflectionUtils.getRefClass("{cb}.entity.CraftPlayer");
		Class<?> classCraftInventory = ReflectionUtils.getRefClass("{cb}.inventory.CraftInventory");
		Class<?> classPacketPlayOutOpenWindow = ReflectionUtils.getRefClass("{nms}.PacketPlayOutOpenWindow");
		Class<?> classPacket = ReflectionUtils.getRefClass("{nms}.Packet");
		Class<?> classChatComponentText = ReflectionUtils.getRefClass("{nms}.ChatComponentText");
		Class<?> classChatClickable = ReflectionUtils.getRefClass("{nms}.ChatClickable");
		Class<?> classChatHoverable = ReflectionUtils.getRefClass("{nms}.ChatHoverable");

		try
		{

			Object iinventory = classCraftInventory.getMethod("getInventory").invoke(p.getInventory());
			Object iinventory1 = classCraftInventory.getMethod("getInventory").invoke(inv);

			Object entityHuman = classCraftPlayer.getMethod("getHandle").invoke(p);

			Class<?> classEntityHuman = ReflectionUtils.getRefClass("{nms}.EntityHuman");
			Object guiContainer = JavassistUtil.getContainerClass()
					.getConstructor(classIInventory, classIInventory, classEntityHuman, String.class)
					.newInstance(iinventory, iinventory1, entityHuman, customGui.getName());
			int i = (int) entityHuman.getClass().getMethod("nextContainerCounter").invoke(entityHuman);
			// Object score =
			// iinventory1.getClass().getMethod("getScoreboardDisplayName").invoke(iinventory1);
			Object cct = classChatComponentText.getConstructor(String.class).newInstance(inv.getName());

			Object ppoow = classPacketPlayOutOpenWindow.getConstructor(int.class, String.class,
					ReflectionUtils.getRefClass("{nms}.IChatBaseComponent"), int.class)
					.newInstance(i, "minecraft:container", cct, inv.getSize());

			Object playerConnection = entityHuman.getClass().getField("playerConnection").get(entityHuman);

			playerConnection.getClass().getMethod("sendPacket", classPacket).invoke(playerConnection, ppoow);

			// ((EntityPlayer)entityHuman).playerConnection.sendPacket(new
			// PacketPlayOutOpenWindow(i, "minecraft:container", cct,
			// ((CraftInventory)cc).getInventory().getSize()));
			entityHuman.getClass().getField("activeContainer").set(entityHuman, guiContainer);
			Object activeContainer = entityHuman.getClass().getField("activeContainer").get(entityHuman);
			activeContainer.getClass().getField("windowId").set(activeContainer, i);
			entityHuman.getClass().getField("activeContainer").set(entityHuman, activeContainer);
			p.updateInventory();
			// eh.activeContainer = g;
			// eh.activeContainer.windowId = i;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public HashMap<Integer, ItemStack> addItem(ItemStack... arg0) throws IllegalArgumentException
	{
		return inv.addItem(arg0);
	}

	public HashMap<Integer, ? extends ItemStack> all(int arg0)
	{
		return inv.all(arg0);
	}

	public HashMap<Integer, ? extends ItemStack> all(Material arg0) throws IllegalArgumentException
	{
		return inv.all(arg0);
	}

	public HashMap<Integer, ? extends ItemStack> all(ItemStack arg0)
	{
		return inv.all(arg0);
	}

	public void clear()
	{
		inv.clear();
	}

	public void clear(int arg0)
	{
		inv.clear(arg0);
	}

	public boolean contains(int arg0)
	{
		return inv.contains(arg0);
	}

	public boolean contains(Material arg0) throws IllegalArgumentException
	{
		return inv.contains(arg0);
	}

	public boolean contains(ItemStack arg0)
	{
		return inv.contains(arg0);
	}

	public boolean contains(int arg0, int arg1)
	{
		return inv.contains(arg0, arg1);
	}

	public boolean contains(Material arg0, int arg1) throws IllegalArgumentException
	{
		return inv.contains(arg0, arg1);
	}

	public boolean contains(ItemStack arg0, int arg1)
	{
		return inv.contains(arg0, arg1);
	}

	public boolean containsAtLeast(ItemStack arg0, int arg1)
	{
		return inv.containsAtLeast(arg0, arg1);
	}

	public int first(int arg0)
	{
		return inv.first(arg0);
	}

	public int first(Material arg0) throws IllegalArgumentException
	{
		return inv.first(arg0);
	}

	public int first(ItemStack arg0)
	{
		return inv.first(arg0);
	}

	public int firstEmpty()
	{
		return inv.firstEmpty();
	}

	public ItemStack[] getContents()
	{
		return inv.getContents();
	}

	public InventoryHolder getHolder()
	{
		return inv.getHolder();
	}

	public ItemStack getItem(int arg0)
	{
		return inv.getItem(arg0);
	}

	public Location getLocation()
	{
		return inv.getLocation();
	}

	public int getMaxStackSize()
	{
		return inv.getMaxStackSize();
	}

	public String getName()
	{
		return inv.getName();
	}

	public int getSize()
	{
		return inv.getSize();
	}

	public ItemStack[] getStorageContents()
	{
		return inv.getStorageContents();
	}

	public String getTitle()
	{
		return inv.getTitle();
	}

	public InventoryType getType()
	{
		return inv.getType();
	}

	public List<HumanEntity> getViewers()
	{
		return inv.getViewers();
	}

	public ListIterator<ItemStack> iterator()
	{
		return inv.iterator();
	}

	public ListIterator<ItemStack> iterator(int arg0)
	{
		return inv.iterator(arg0);
	}

	public void remove(int arg0)
	{
		inv.remove(arg0);
	}

	public void remove(Material arg0) throws IllegalArgumentException
	{
		inv.remove(arg0);
	}

	public void remove(ItemStack arg0)
	{
		inv.remove(arg0);
	}

	public HashMap<Integer, ItemStack> removeItem(ItemStack... arg0) throws IllegalArgumentException
	{
		return inv.removeItem(arg0);
	}

	public void setContents(ItemStack[] arg0) throws IllegalArgumentException
	{
		inv.setContents(arg0);
	}

	public void setItem(int arg0, ItemStack arg1)
	{
		inv.setItem(arg0, arg1);
	}

	public void setMaxStackSize(int arg0)
	{
		inv.setMaxStackSize(arg0);
	}

	public void setStorageContents(ItemStack[] arg0) throws IllegalArgumentException
	{
		inv.setStorageContents(arg0);
	}

}
