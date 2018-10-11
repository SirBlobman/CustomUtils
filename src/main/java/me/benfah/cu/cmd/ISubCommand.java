package me.benfah.cu.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface ISubCommand
{
	public String commandName();

	public String[] argumentNames();

	public String description();

	public boolean playerOnly();

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args);

}
