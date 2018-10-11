package me.benfah.cu.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;

import me.benfah.cu.util.JavassistUtil;

public class SlotChangeListener implements Listener
{
	@EventHandler
	public void onSlotChange(PlayerItemHeldEvent e)
	{
		Inventory inv = e.getPlayer().getOpenInventory().getTopInventory();
		if (JavassistUtil.getCUInventoryClass().isInstance(inv)
				|| JavassistUtil.getCUInventoryCustomClass().isInstance(inv))
		{
			e.setCancelled(true);
		}
	}
}
