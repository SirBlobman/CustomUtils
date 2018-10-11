package me.benfah.cu.api;

import me.benfah.cu.init.IInitializationMethod;
import me.benfah.cu.init.InitializationMethodRegistry;
import me.benfah.cu.main.CustomUtils;
import me.benfah.cu.util.Config;

public class InitializationMethodProvider
{

	public static IInitializationMethod getCurrentMethod()
	{
		return InitializationMethodRegistry.getMethod(Config.getConfiguration().getString("init-method"));
	}

	public static void setCurrentMethod(String name)
	{
		Config.getConfiguration().set("init-method", name);
	}

	public static void init(boolean forceUpload)
	{
		getCurrentMethod().init(forceUpload);
	}

	public static void initRP(boolean forceUpload)
	{
		getCurrentMethod().initResourcePack(forceUpload);
	}

}
