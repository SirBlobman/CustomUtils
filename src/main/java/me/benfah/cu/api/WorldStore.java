package me.benfah.cu.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;

public class WorldStore
{
	protected final Map<Location, Map<String, Object>> values;
	final World world;

	public WorldStore(World world)
	{
		this.world = world;
		this.values = new HashMap<Location, Map<String, Object>>();
	}

	public void handleBlockBreak(BlockBreakEvent e)
	{
		values.remove(e.getBlock().getLocation());
	}

	public WorldStore(World world, Map<Map<String, Object>, Map<String, Object>> values)
	{
		this.world = world;
		Map<Location, Map<String, Object>> map = new HashMap<Location, Map<String, Object>>();
		for (Entry<Map<String, Object>, Map<String, Object>> entr : values.entrySet())
		{
			map.put(Location.deserialize(entr.getKey()), entr.getValue());
		}
		this.values = map;
	}

	public boolean hasMeta(Location loc, String key)
	{
		notNull(loc);
		return values.get(loc).containsKey(key);
	}

	public void remove(Location loc, String key)
	{
		notNull(loc);
		values.get(loc).remove(key);
	}

	private Object getValue(Location loc, String key)
	{
		notNull(loc);
		return values.get(loc).get(key);
	}

	private Map<String, Object> getMap(Location loc)
	{
		return values.get(loc);
	}

	private void notNull(Location loc)
	{
		if (!values.containsKey(loc))
			values.put(loc, new HashMap<String, Object>());
	}

	public void setMeta(Location loc, String key, Object value)
	{
		notNull(loc);
		getMap(loc).put(key, value);
	}

	public Object getMeta(Location loc, String key)
	{
		notNull(loc);
		return getMap(loc).get(key);
	}

	public Map<Map<String, Object>, Map<String, Object>> serialize()
	{
		Map<Map<String, Object>, Map<String, Object>> result = new HashMap<Map<String, Object>, Map<String, Object>>();

		for (Entry<Location, Map<String, Object>> entr : values.entrySet())
		{
			result.put(entr.getKey().serialize(), entr.getValue());
		}
		return result;

	}

	public void save()
	{

		File f = world.getWorldFolder();
		File metadataFile = new File(f, "blocks.meta");
		try
		{
			FileOutputStream fos = new FileOutputStream(metadataFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(serialize());
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public List<Location> getBlockLocationsForKey(String k)
	{
		List<Location> list = new ArrayList<>();
		for (Entry<Location, Map<String, Object>> entr : values.entrySet())
		{
			if (entr.getValue().containsKey(k))
				list.add(entr.getKey());
		}
		return list;
	}

	@Override
	public String toString()
	{
		return values.toString();
	}
}
