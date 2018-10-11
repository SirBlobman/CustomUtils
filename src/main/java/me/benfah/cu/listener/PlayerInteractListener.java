package me.benfah.cu.listener;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.benfah.cu.api.CustomBlock;
import me.benfah.cu.api.CustomItem;
import me.benfah.cu.api.CustomRegistry;
import me.benfah.cu.util.Utils;

public class PlayerInteractListener implements Listener
{
	@EventHandler
	public void onInteract(PlayerInteractEvent e)
	{
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if (CustomRegistry.isCustomBlock(e.getClickedBlock()))
			{
				e.setCancelled(true);
				CustomRegistry.getCustomBlockByBlock(e.getClickedBlock()).onInteract(e);
			}
		}
		if (((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)
				&& e.getHand().equals(EquipmentSlot.HAND))
				|| (e.getAction() == Action.RIGHT_CLICK_AIR && e.getHand().equals(EquipmentSlot.OFF_HAND)))
		{

			CustomItem ci = Utils.getMainOrOffHandCI(e);
			if (ci != null)
			{
				ItemStack stack = Utils.updateTags(Utils.getMainOrOffHandStack(e));
				Utils.setMainOrOffHandStack(e, stack);
				ci.onInteract(e, Utils.getEquipmentSlot(e));
				e.setCancelled(true);
			}

			if (e.getItem() != null && CustomRegistry.isItemStackItemStackOfCB(e.getItem())
					&& (e.getAction() == Action.RIGHT_CLICK_BLOCK))
			{
				e.setCancelled(true);
				CustomBlock cb = CustomRegistry.getCustomBlockByStack(e.getItem());
				int x = e.getClickedBlock().getX() + e.getBlockFace().getModX();
				int y = e.getClickedBlock().getY() + e.getBlockFace().getModY();
				int z = e.getClickedBlock().getZ() + e.getBlockFace().getModZ();
				Block b = new Location(e.getClickedBlock().getWorld(), x, y, z).getBlock();
				if (b.isEmpty())
					cb.setBlock(b);

				if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
					e.getItem().setAmount(e.getItem().getAmount() - 1);

			}
		}

	}

}
