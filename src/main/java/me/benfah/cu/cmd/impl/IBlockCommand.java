package me.benfah.cu.cmd.impl;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import me.benfah.cu.api.CustomBase;
import me.benfah.cu.api.CustomBlock;
import me.benfah.cu.api.CustomRegistry;
import me.benfah.cu.cmd.ISubCommand;
import me.benfah.cu.util.Utils;

public class IBlockCommand implements ISubCommand
{

	@Override
	public String commandName()
	{
		return "block";
	}

	@Override
	public String[] argumentNames()
	{
		return new String[] { "{BLOCK_ID}" };
	}

	@Override
	public String description()
	{
		return "Gives the player the specific custom block.";
	}

	@Override
	public boolean playerOnly()
	{
		return true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (args.length == 1)
		{
			Utils.sendDetailedList(sender, CustomRegistry.CUSTOM_BLOCK_REGISTRY);
			return true;
		}

		Player p = (Player) sender;
		if (args[0].equalsIgnoreCase("block"))
		{
			for (CustomBlock cb : CustomRegistry.CUSTOM_BLOCK_REGISTRY)
			{
				if (args[1].equals(cb.getName()))
				{
					if (p.hasPermission(new Permission("cu.block." + cb.getName(), PermissionDefault.OP)))
						p.getInventory().addItem(cb.getBlockItem());
					else
						p.sendMessage("No permission!");
				}
			}
		}
		return false;
	}

}
