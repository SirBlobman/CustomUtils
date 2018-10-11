package me.benfah.cu.cmd.impl;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import me.benfah.cu.cmd.ISubCommand;
import me.benfah.cu.main.CustomUtils;
import me.benfah.cu.util.Config;

public class IReloadCommand implements ISubCommand
{

	@Override
	public String commandName()
	{
		return "reload";
	}

	@Override
	public String[] argumentNames()
	{
		return new String[] {};
	}

	@Override
	public String description()
	{
		return "Reloads the config.";
	}

	@Override
	public boolean playerOnly()
	{
		return false;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (sender.hasPermission(new Permission("cu.reload", PermissionDefault.OP)))
		{
			Config.load();
			sender.sendMessage(ChatColor.GREEN + "Config reloaded!");
		}
		return false;
	}

}
