package me.benfah.cu.util;

public class MethodNames
{

	protected static String getTiledataMethodName()
	{

		if (ReflectionUtils.version.equals("v1_8_R3"))
		{
			return "b";
		}
		return "save";
	}

	protected static String getTypeMethodName()
	{
		if (ReflectionUtils.version.equals("v1_8_R3"))
		{
			return "b";
		}
		return "d";
	}

	protected static String getEntitynbtgetterMethodName()
	{
		return "b";
	}

	protected static String getEntitynbtsetterMethodName()
	{
		return "a";
	}

	protected static String getremoveMethodName()
	{
		if (ReflectionUtils.version.equals("v1_8_R3"))
		{
			return "a";
		}
		return "remove";
	}

}
