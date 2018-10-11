package me.benfah.cu.cmd;

import java.util.ArrayList;
import java.util.List;

import me.benfah.cu.cmd.impl.IBlockCommand;
import me.benfah.cu.cmd.impl.IChooseMethodCommand;
import me.benfah.cu.cmd.impl.IItemCommand;
import me.benfah.cu.cmd.impl.IReloadCommand;
import me.benfah.cu.cmd.impl.IRepackageCommand;

public class CommandRegistry
{
	private static List<ISubCommand> subCommands = new ArrayList<>();

	public static void initSubCommands()
	{
		subCommands.clear();
		subCommands.add(new IBlockCommand());
		subCommands.add(new IItemCommand());
		subCommands.add(new IReloadCommand());
		subCommands.add(new IRepackageCommand());
		subCommands.add(new IChooseMethodCommand());
	}

	public static List<ISubCommand> getSubCommands()
	{
		return subCommands;
	}
}
