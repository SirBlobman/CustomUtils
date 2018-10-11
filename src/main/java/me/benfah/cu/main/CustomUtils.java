package me.benfah.cu.main;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import me.benfah.cu.api.BlockInstance;
import me.benfah.cu.api.CustomBlock;
import me.benfah.cu.api.CustomItem;
import me.benfah.cu.api.CustomRegistry;
import me.benfah.cu.api.InitializationMethodProvider;
import me.benfah.cu.cmd.CommandRegistry;
import me.benfah.cu.cmd.CustomUtilsCommandExecutor;
import me.benfah.cu.init.InitializationMethodRegistry;
import me.benfah.cu.init.impl.DropBoxInitializationMethod;
import me.benfah.cu.init.impl.MinePackInitializationMethod;
import me.benfah.cu.listener.BlockBreakListener;
import me.benfah.cu.listener.InventoryClickListener;
import me.benfah.cu.listener.InventoryCloseListener;
import me.benfah.cu.listener.ItemCraftListener;
import me.benfah.cu.listener.PlayerInteractEntityListener;
import me.benfah.cu.listener.PlayerInteractListener;
import me.benfah.cu.listener.PlayerJoinListener;
import me.benfah.cu.listener.SlotChangeListener;
import me.benfah.cu.util.Config;
import me.benfah.cu.util.JavassistUtil;
import me.benfah.cu.util.TickRunnable;

public class CustomUtils extends JavaPlugin
{
	public static CustomUtils instance;

	@Override
	public void onEnable()
	{

		JavassistUtil.getCUInventoryClass();
		JavassistUtil.getCUInventoryCustomClass();
		JavassistUtil.getContainerClass();
		instance = this;
		CustomRegistry.initMaps();

		Config.onEnable();

		InitializationMethodRegistry.initMethods();
		InitializationMethodProvider.setCurrentMethod(Config.getConfiguration().getString("init-method"));
		Bukkit.getPluginManager().registerEvents(new SlotChangeListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerInteractEntityListener(), this);
		Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
		Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), this);
		Bukkit.getPluginManager().registerEvents(new ItemCraftListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
		Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(), this);
		CommandRegistry.initSubCommands();
		Bukkit.getPluginCommand("customutils").setExecutor(new CustomUtilsCommandExecutor());
		System.out.println("####### CUSTOMUTILS #######");

		Bukkit.getScheduler().runTask(instance, new Runnable()
		{

			@Override
			public void run()
			{
				InitializationMethodProvider.init(false);

			}
		});

		Bukkit.getScheduler().runTaskTimer(instance, new TickRunnable(), 1, 1);
	}

	@Override
	public void onDisable()
	{

		for (Entry<Location, BlockInstance> entr : BlockInstance.getBlockInstanceMap().entrySet())
		{
			if (entr.getValue() != null)
				entr.getValue().write();
		}

		CustomRegistry.saveIDNameMap();
		CustomRegistry.saveWorldStores();

		Config.onDisable();
	}

	public static CustomUtils getInstance()
	{
		return instance;
	}

}
