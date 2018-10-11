package me.benfah.cu.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import me.benfah.cu.api.CustomRegistry;

public class ItemCraftListener implements Listener
{
	@EventHandler
	public void onCraft(PrepareItemCraftEvent e)
	{
		List<ItemStack> stackList = new ArrayList<>();
		for (int i = 1; i < e.getInventory().getSize(); i++)
			stackList.add(e.getInventory().getContents()[i]);
		int hoe = 0;
		for (ItemStack stack : stackList)
		{
			if (CustomRegistry.isCustomItem(stack))
				hoe++;
		}
		if (e.isRepair() && hoe >= 2)
			e.getInventory().setResult(null);

	}
}
