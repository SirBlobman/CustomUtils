package me.benfah.cu.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import me.benfah.cu.api.CustomRegistry;

public class InventoryCloseListener implements Listener
{

	@EventHandler
	public void onClose(InventoryCloseEvent e)
	{

		if (CustomRegistry.isCustomGUI(e.getInventory()))
		{
			CustomRegistry.getCustomGUIByInventory(e.getInventory()).onClose(e);
		}
	}

}
