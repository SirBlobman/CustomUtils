package me.benfah.cu.util;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class CustomBreakEvent extends BlockBreakEvent
{

	public CustomBreakEvent(Block theBlock, Player player)
	{
		super(theBlock, player);
	}

	private boolean droppable = false;

	public boolean isDroppable()
	{
		return droppable;
	}

	public void setDroppable(boolean droppable)
	{
		this.droppable = droppable;
	}

}
