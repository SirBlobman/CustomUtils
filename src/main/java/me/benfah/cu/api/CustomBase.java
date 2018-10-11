package me.benfah.cu.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import me.benfah.cu.util.InformationEntry;

public class CustomBase
{
	protected Plugin plugin;

	final private ArrayList<InformationEntry> mpe = new ArrayList<>();

	protected Material baseMaterial;

	public CustomBase(InformationEntry entry, Material baseMaterial)
	{
		mpe.add(entry);
		this.baseMaterial = baseMaterial;

	}

	public CustomBase(String name, String modelPath, Material baseMaterial)
	{
		mpe.add(new InformationEntry(modelPath, name));
		this.baseMaterial = baseMaterial;

	}

	public Material getBaseMaterial()
	{
		return baseMaterial;
	}

	public InformationEntry getMainModelPathEntry()
	{
		return mpe.get(0);
	}

	public List<InformationEntry> getModelPathEntry()
	{
		return mpe;
	}

	public String getName()
	{
		return getMainModelPathEntry().getName();
	}

	public Plugin getPlugin()
	{
		return plugin;
	}

	public void setPlugin(Plugin plugin)
	{
		this.plugin = plugin;
	}

}
