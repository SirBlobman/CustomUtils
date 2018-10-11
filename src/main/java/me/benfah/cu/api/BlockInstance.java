package me.benfah.cu.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class BlockInstance implements Block, IBlockInstance
{
	Location loc;
	static Map<Location, BlockInstance> biMap = new HashMap<>();

	public BlockInstance(Block b)
	{
		this.loc = b.getLocation();
	}

	private Block getBlock()
	{
		return loc.getBlock();
	}

	public static Map<Location, BlockInstance> getBlockInstanceMap()
	{
		return biMap;
	}

	public void write()
	{
	}

	public void read()
	{
	}

	public static void removeBlockInstance(Block b)
	{
		biMap.remove(b.getLocation());
	}

	public static BlockInstance getBlockInstance(Block b)
	{
		if (biMap.containsKey(b.getLocation()))
			return biMap.get(b.getLocation());

		if (CustomRegistry.isCustomBlock(b) && CustomRegistry.getCustomBlockByBlock(b) instanceof IInstanceProvider)
		{
			IInstanceProvider bip = (IInstanceProvider) CustomRegistry.getCustomBlockByBlock(b);
			Class<?> clazz = bip.getInstanceName();

			BlockInstance bi = null;
			try
			{
				bi = (BlockInstance) clazz.getConstructor(Block.class).newInstance(b);
				bi.read();
			} catch (ReflectiveOperationException e)
			{
				e.printStackTrace();
			}

			biMap.put(b.getLocation(), bi);
			return bi;

		}
		return null;

	}

	public CustomBlock getCustomBlock()
	{
		return CustomRegistry.getCustomBlockByBlock(loc.getBlock());
	}

	public void setMetadataValue(String k, Object v)
	{
		CustomRegistry.getWorldStore(getWorld()).setMeta(getLocation(), k, v);
	}

	public void removeMetadataValue(String k)
	{
		CustomRegistry.getWorldStore(getWorld()).remove(loc, k);
	}

	public boolean hasMetadataValue(String k)
	{
		return CustomRegistry.getWorldStore(getWorld()).hasMeta(getLocation(), k);
	}

	public Object getMetadataValue(String k)
	{
		return CustomRegistry.getWorldStore(getWorld()).getMeta(getLocation(), k);
	}

	// Block methods
	public int getX()
	{
		return getBlock().getLocation().getBlockX();
	}

	public int getY()
	{
		return getBlock().getLocation().getBlockY();
	}

	public int getZ()
	{
		return getBlock().getLocation().getBlockZ();
	}

	@Override
	public List<MetadataValue> getMetadata(String arg0)
	{
		return getBlock().getMetadata(arg0);
	}

	@Override
	public boolean hasMetadata(String arg0)
	{
		return getBlock().hasMetadata(arg0);
	}

	@Override
	public void removeMetadata(String arg0, Plugin arg1)
	{
		getBlock().removeMetadata(arg0, arg1);
	}

	@Override
	public void setMetadata(String arg0, MetadataValue arg1)
	{
		getBlock().setMetadata(arg0, arg1);
	}

	@Override
	public boolean breakNaturally()
	{
		return getBlock().breakNaturally();
	}

	@Override
	public boolean breakNaturally(ItemStack arg0)
	{
		return getBlock().breakNaturally(arg0);
	}

	@Override
	public Biome getBiome()
	{
		return getBlock().getBiome();
	}

	@Override
	public int getBlockPower()
	{
		return getBlock().getBlockPower();
	}

	@Override
	public int getBlockPower(BlockFace arg0)
	{
		return getBlock().getBlockPower(arg0);
	}

	@Override
	public Chunk getChunk()
	{
		return getBlock().getChunk();
	}

	@Override
	public byte getData()
	{

		return getBlock().getData();
	}

	@Override
	public Collection<ItemStack> getDrops()
	{

		return getBlock().getDrops();
	}

	@Override
	public Collection<ItemStack> getDrops(ItemStack arg0)
	{

		return getBlock().getDrops(arg0);
	}

	@Override
	public BlockFace getFace(Block arg0)
	{

		return null;
	}

	@Override
	public double getHumidity()
	{
		return getBlock().getHumidity();
	}

	@Override
	public byte getLightFromBlocks()
	{
		return getBlock().getLightFromBlocks();
	}

	@Override
	public byte getLightFromSky()
	{
		return getBlock().getLightFromSky();
	}

	@Override
	public byte getLightLevel()
	{
		return getBlock().getLightLevel();
	}

	@Override
	public Location getLocation()
	{

		return getBlock().getLocation();
	}

	@Override
	public Location getLocation(Location arg0)
	{

		return getBlock().getLocation(arg0);
	}

	@Override
	public PistonMoveReaction getPistonMoveReaction()
	{

		return getBlock().getPistonMoveReaction();
	}

	@Override
	public Block getRelative(BlockFace arg0)
	{

		return getBlock().getRelative(arg0);
	}

	@Override
	public Block getRelative(BlockFace arg0, int arg1)
	{

		return getBlock().getRelative(arg0, arg1);
	}

	@Override
	public Block getRelative(int arg0, int arg1, int arg2)
	{

		return getBlock().getRelative(arg0, arg1, arg2);
	}

	@Override
	public BlockState getState()
	{
		return getBlock().getState();
	}

	@Override
	public double getTemperature()
	{
		return getBlock().getTemperature();
	}

	@Override
	public Material getType()
	{

		return getBlock().getType();
	}

	@Override
	public int getTypeId()
	{

		return getBlock().getTypeId();
	}

	@Override
	public World getWorld()
	{

		return getBlock().getWorld();
	}

	@Override
	public boolean isBlockFaceIndirectlyPowered(BlockFace arg0)
	{

		return getBlock().isBlockFaceIndirectlyPowered(arg0);
	}

	@Override
	public boolean isBlockFacePowered(BlockFace arg0)
	{

		return getBlock().isBlockFacePowered(arg0);
	}

	@Override
	public boolean isBlockIndirectlyPowered()
	{

		return getBlock().isBlockIndirectlyPowered();
	}

	@Override
	public boolean isBlockPowered()
	{

		return getBlock().isBlockPowered();
	}

	@Override
	public boolean isEmpty()
	{

		return getBlock().isEmpty();
	}

	@Override
	public boolean isLiquid()
	{

		return getBlock().isLiquid();
	}

	@Override
	public void setBiome(Biome arg0)
	{
		getBlock().setBiome(arg0);

	}

	@Override
	public void setData(byte arg0)
	{
		getBlock().setData(arg0);

	}

	@Override
	public void setData(byte arg0, boolean arg1)
	{
		getBlock().setData(arg0, arg1);

	}

	@Override
	public void setType(Material arg0)
	{
		getBlock().setType(arg0);

	}

	@Override
	public void setType(Material arg0, boolean arg1)
	{
		getBlock().setType(arg0, arg1);

	}

	@Override
	public boolean setTypeId(int arg0)
	{

		return getBlock().setTypeId(arg0);
	}

	@Override
	public boolean setTypeId(int arg0, boolean arg1)
	{

		return getBlock().setTypeId(arg0, arg1);
	}

	@Override
	public boolean setTypeIdAndData(int arg0, byte arg1, boolean arg2)
	{

		return getBlock().setTypeIdAndData(arg0, arg1, arg2);
	}
}
