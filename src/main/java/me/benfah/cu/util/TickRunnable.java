package me.benfah.cu.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.benfah.cu.api.BlockInstance;
import me.benfah.cu.api.CustomGUI;
import me.benfah.cu.api.CustomItem;
import me.benfah.cu.api.CustomRegistry;
import me.benfah.cu.api.ITickable;
import me.benfah.cu.api.WorldStore;

public class TickRunnable implements Runnable
{

	@Override
	public void run()
	{
		for (Player p : Bukkit.getOnlinePlayers())
		{

			ItemStack[] contents = p.getInventory().getContents();
			for (int i = 0; i < contents.length; i++)
			{
				ItemStack stack = contents[i];
				if (stack != null && stack.getType() != Material.AIR)
					if (CustomRegistry.isCustomItem(stack))
					{
						CustomItem ci = CustomRegistry.getCustomItem(stack);
						ci.onUpdate(p, i);
					}
			}
		}

		for (WorldStore ws : CustomRegistry.worldStoreList)
		{
			for (Location loc : ws.getBlockLocationsForKey("shouldUpdate"))
			{

				BlockInstance bi = BlockInstance.getBlockInstance(loc.getBlock());
				if (bi != null)
					if (bi.getChunk().isLoaded())
						if (bi instanceof ITickable)
							((ITickable) bi).onUpdate();

				// CustomRegistry.getCustomBlockByBlock(loc.getBlock()).onUpdate(loc);
			}
		}
	}

}
