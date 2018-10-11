package me.benfah.cu.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import me.benfah.cu.api.BlockInstance;
import me.benfah.cu.api.CustomBlock;
import me.benfah.cu.api.CustomRegistry;
import me.benfah.cu.api.WorldStore;

public class BlockBreakListener implements Listener
{
	@EventHandler
	public void onBreakBlock(BlockBreakEvent e)
	{
		Block b = e.getBlock();
		if (CustomRegistry.isCustomBlock(b))
		{

			CustomBlock cb = CustomRegistry.getCustomBlockByBlock(b);
			cb.onBlockBroken(e);
			for (ItemStack i : cb.getLoot(e.getBlock()))
			{
				if (i != null && i.getType() != Material.AIR)
					e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), i);
			}
			BlockInstance.removeBlockInstance(b);

			WorldStore ws = CustomRegistry.getWorldStore(e.getBlock().getWorld());
			ws.handleBlockBreak(e);

			e.setExpToDrop(0);

		}

	}

}
