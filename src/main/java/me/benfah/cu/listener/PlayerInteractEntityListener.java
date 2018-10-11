package me.benfah.cu.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import me.benfah.cu.api.CustomItem;
import me.benfah.cu.util.Utils;

public class PlayerInteractEntityListener implements Listener
{

	@EventHandler
	public void onInteractEntity(PlayerInteractEntityEvent e)
	{
		if (e.getHand().equals(EquipmentSlot.HAND))
		{
			CustomItem ci = Utils.getMainOrOffHandCI(e);
			if (ci != null)
				ci.onInteractEntity(e);
		}
	}
}
