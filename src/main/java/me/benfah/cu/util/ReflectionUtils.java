package me.benfah.cu.util;

import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author DPOH-VAR
 * @version 1.0
 */
public class ReflectionUtils
{

	/** prefix of bukkit classes */
	public static String preClassB = "org.bukkit.craftbukkit";
	/** prefix of minecraft classes */
	public static String preClassM = "net.minecraft.server";

	public static String version;

	/** boolean value, TRUE if server uses forge or MCPC+ */
	private static boolean forge = false;

	/** check server version and class names */
	static
	{
		if (Bukkit.getServer() != null)
		{
			if (Bukkit.getVersion().contains("MCPC") || Bukkit.getVersion().contains("Forge"))
				forge = true;
			Server server = Bukkit.getServer();
			Class<?> bukkitServerClass = server.getClass();
			String[] pas = bukkitServerClass.getName().split("\\.");
			if (pas.length == 5)
			{
				String verB = pas[3];
				version = verB;
				preClassB += "." + verB;
			}
			try
			{
				Method getHandle = bukkitServerClass.getDeclaredMethod("getHandle");
				Object handle = getHandle.invoke(server);
				Class<?> handleServerClass = handle.getClass();
				pas = handleServerClass.getName().split("\\.");
				if (pas.length == 5)
				{
					String verM = pas[3];
					preClassM += "." + verM;
				}
			} catch (Exception ignored)
			{
			}
		}
	}

	/**
	 * Get class for name. Replace {nms} to net.minecraft.server.V*. Replace {cb} to
	 * org.bukkit.craftbukkit.V*. Replace {nm} to net.minecraft
	 * 
	 * @param classes
	 *            possible class paths
	 * @return RefClass object
	 * @throws RuntimeException
	 *             if no class found
	 */
	public static Class<?> getRefClass(String... classes)
	{
		for (String className : classes)
			try
			{
				className = className.replace("{cb}", preClassB).replace("{nms}", preClassM).replace("{nm}",
						"net.minecraft");
				return Class.forName(className);
			} catch (ClassNotFoundException ignored)
			{
			}
		throw new RuntimeException("no class found");
	}

}
