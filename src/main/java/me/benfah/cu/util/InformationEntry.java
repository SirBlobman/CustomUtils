package me.benfah.cu.util;

public class InformationEntry
{
	private Short id;
	private String pathToModel;
	private String name;

	public Short getId()
	{
		return id;
	}

	public void setId(Short id)
	{
		this.id = id;
	}

	public String getPathToModel()
	{
		return pathToModel;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public InformationEntry(String pathToModel, String name)
	{
		this.pathToModel = pathToModel;
		this.name = name;
	}
}
