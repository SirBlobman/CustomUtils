package me.benfah.cu.init.impl;

import java.io.*;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.google.common.collect.HashBiMap;

import me.benfah.cu.api.CustomRegistry;
import me.benfah.cu.api.WorldStore;
import me.benfah.cu.init.IInitializationMethod;
import me.benfah.cu.main.CustomUtils;
import me.benfah.cu.util.*;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class MinePackInitializationMethod implements IInitializationMethod
{

	public static PrintWriter pw;
	public static InitializationState initialize;

	public void init(boolean forceUpload)
	{
		initialize = InitializationState.INITIALIZING;
		try
		{
			System.out.println(CustomRegistry.PLUGIN_REGISTRY.size() + " plugin(s) registered.");
			System.out.println(CustomRegistry.CUSTOM_BLOCK_REGISTRY.size() + " block(s) registered.");
			System.out.println(CustomRegistry.CUSTOM_GUI_REGISTRY.size() + " gui(s) registered.");
			System.out.println(CustomRegistry.CUSTOM_ITEM_REGISTRY.size() + " item(s) registered.");

			System.out.println(CustomRegistry.CUSTOM_GUI_REGISTRY.size() + CustomRegistry.CUSTOM_BLOCK_REGISTRY.size()
					+ " in total.");

			initResourcePack(forceUpload);

			System.out.println("Loading WorldStores...");

			for (World w : Bukkit.getWorlds())
			{
				File f = new File(w.getWorldFolder(), "blocks.meta");
				if (f.exists())
				{
					FileInputStream fis = new FileInputStream(f);
					ObjectInputStream ois = new ObjectInputStream(fis);
					try
					{
						Object values = ois.readObject();
						try
						{
							CustomRegistry.worldStoreList
									.add(new WorldStore(w, (Map<Map<String, Object>, Map<String, Object>>) values));
						} catch (Exception e)
						{
							System.out.println("[CustomUtils] An entry of a world store could not be read!");
						}
					} catch (Exception e)
					{
						e.printStackTrace();
					}
					ois.close();
					fis.close();
				} else
				{
					CustomRegistry.worldStoreList.add(new WorldStore(w));
				}
			}

			System.out.println("Done!");

			initialize = InitializationState.SUCCESS;

		} catch (Exception e)
		{
			initialize = InitializationState.FAILED;
			e.printStackTrace();
		}

	}

	public void initResourcePack(boolean forceUpload)
	{
		try
		{
			System.out.println("Unzipping the resourcepack out of the plugins...");

			if (!CustomUtils.instance.getDataFolder().isDirectory())
				CustomUtils.instance.getDataFolder().mkdirs();

			File resourcepacks = new File(CustomUtils.instance.getDataFolder(), "resourcepacks");
			if (!resourcepacks.exists())
				resourcepacks.mkdir();

			File finalrp = new File(resourcepacks, "finalrp");
			if (!finalrp.exists())
				finalrp.mkdir();

			for (JavaPlugin pl : CustomRegistry.PLUGIN_REGISTRY)
			{
				File f = new File(resourcepacks, pl.getName());
				try
				{
					System.out.println(pl.getClass().getSimpleName());
					Method m = JavaPlugin.class.getDeclaredMethod("getFile");
					m.setAccessible(true);
					File f2 = (File) m.invoke(pl);
					System.gc();

					File resource = new File(f, "assets");
					if (f.exists())
						FileUtils.forceDelete(f);

					File assetsfolder = new File(finalrp, "assets");
					if (!assetsfolder.exists())
						assetsfolder.mkdirs();

					if (!f.exists())
						f.mkdirs();

					ZipFile zf = new ZipFile(f2);
					// zf.extractAll(f.getPath());
					// FileHeader fh = zf.getFileHeader("assets");

					// zf.extractFile(fh, f3.toString());
					FileHeader fh = zf.getFileHeader("assets.zip");
					zf.extractFile(fh, f.getAbsolutePath());
					new ZipFile(new File(f, "assets.zip")).extractAll(f.getAbsolutePath());

					// zf.extractFile("assets.zip", f.getPath() + "/assets.zip");
					// ZipUtils.unzip(f, f2);
					FileUtils.copyDirectory(resource, assetsfolder);

				} catch (Exception e)
				{
					e.printStackTrace();
				}

			}
			System.out.println("Writing blocks from registry into model file...");
			File modelsfolder = new File(finalrp, "assets/minecraft/models/item");

			if (!modelsfolder.exists())
				modelsfolder.mkdirs();

			List<Material> base = CustomRegistry.getUsedBaseMaterials();
			List<String> baseStr = new ArrayList<>();

			HashBiMap<Material, String> map = HashBiMap.create();

			for (Material s : base)
				baseStr.add(Utils.getUnlocalizedName(s));

			Iterator<Material> i1 = base.iterator();
			Iterator<String> i2 = baseStr.iterator();
			while (i1.hasNext() && i2.hasNext())
			{
				map.put(i1.next(), i2.next());
			}

			for (String s : baseStr)
			{

				File diamondhoe = new File(modelsfolder, s + ".json");

				if (diamondhoe.exists())
					diamondhoe.delete();

				try
				{
					FileOutputStream fos = new FileOutputStream(diamondhoe);
					pw = new PrintWriter(fos);
					pw.write("{ \"parent\": \"item/handheld\", \"textures\": { \"layer0\": \"items/" + s
							+ "\" }, \"overrides\": [ { \"predicate\": {\"damaged\": 0, \"damage\": 0}, \"model\": \"item/"
							+ s + "\"}");
					Material mat = map.inverse().get(s);
					System.out.println(mat);
					Utils.writeModels(new AbstractMap.SimpleEntry<String, Material>(s, mat),
							Utils.getMaxDurability(mat), pw);
					pw.println("]}");

					pw.close();
					try
					{
						fos.close();

					} catch (IOException e)
					{
						e.printStackTrace();
					}

				} catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
			}

			System.out.println("Copying needed resources out of plugin...");
			CustomUtils.instance.saveResource("pack.mcmeta", false);
			CustomUtils.instance.saveResource("pack.png", false);
			CustomUtils.instance.saveResource("mob_spawner.png", false);
			try
			{
				new File(finalrp, "pack.mcmeta").delete();
				new File(finalrp, "pack.png").delete();
				File blockstextures = new File(finalrp, "assets/minecraft/textures/blocks");
				if (!blockstextures.exists())
					blockstextures.mkdirs();

				File mobspawnerfile = new File(blockstextures, "mob_spawner.png");
				if (mobspawnerfile.exists())
					mobspawnerfile.delete();

				FileUtils.moveFile(new File(CustomUtils.instance.getDataFolder(), "pack.mcmeta"),
						new File(finalrp, "pack.mcmeta"));
				FileUtils.moveFile(new File(CustomUtils.instance.getDataFolder(), "pack.png"),
						new File(finalrp, "pack.png"));
				FileUtils.moveFile(new File(CustomUtils.instance.getDataFolder(), "mob_spawner.png"), mobspawnerfile);

			} catch (IOException e)
			{
				e.printStackTrace();
			}
			System.out.println("Compressing resourcepack...");
			File resourcepack = new File(CustomUtils.instance.getDataFolder(), "c-resourcepack.zip");

			resourcepack.delete();

			try
			{
				ZipFile zf = new ZipFile(resourcepack);
				ZipParameters zp = new ZipParameters();
				zp.setCompressionMethod(Zip4jConstants.COMP_STORE);

				zf.addFolder(new File(finalrp, "assets"), zp);
				zf.addFile(new File(finalrp, "pack.mcmeta"), zp);
				zf.addFile(new File(finalrp, "pack.png"), zp);

				zf.getFile();
			} catch (ZipException e)
			{
				e.printStackTrace();
			}
			System.out.println("Comparing latest and current resourcepack...");
			File finalrpzip = new File(CustomUtils.instance.getDataFolder(), "final-resourcepack.zip");
			if (!forceUpload)
			{
				if (!finalrpzip.exists())
				{
					System.out.println("No current resourcepack. Uploading...");
					FileUtils.copyFile(resourcepack, finalrpzip);
					uploadResourcePack(finalrpzip);

				} else
				{
					if (/* resourcepack.length() == finalrpzip.length() */ZipCompare.compare(resourcepack, finalrpzip))
					{

						System.out.println("resourcepack content is equal. Skipping upload!");
					} else
					{
						finalrpzip.delete();
						FileUtils.copyFile(resourcepack, finalrpzip);

						System.out.println("resourcepack content is not equal. Uploading...");
						uploadResourcePack(finalrpzip);
					}
				}
			} else
			{
				finalrpzip.delete();
				FileUtils.copyFile(resourcepack, finalrpzip);
				PrintWriter pw = new PrintWriter(new FileWriter(finalrpzip, true));
				pw.print(new Random().nextInt());
				pw.close();
				uploadResourcePack(finalrpzip);
				for (Player p : Bukkit.getOnlinePlayers())
					p.setResourcePack(Config.getConfiguration().getString("resourcepack-link"));
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static String getDamageForTool(double dmg, double maxSize)
	{
		return new BigDecimal(1D / 1562D * dmg).toPlainString();
	}

	public static void setPropertyInPropertyManager(String k, Object v)
	{
		Class<?> mserver = ReflectionUtils.getRefClass("{nms}.MinecraftServer");
		try
		{
			Object obj = mserver.getMethod("getServer").invoke(null);
			Object propertyManager = obj.getClass().getMethod("getPropertyManager").invoke(obj);
			propertyManager.getClass().getMethod("setProperty", String.class, Object.class).invoke(propertyManager, k,
					v);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void saveProperties()
	{
		Class<?> mserver = ReflectionUtils.getRefClass("{nms}.MinecraftServer");
		try
		{
			Object obj = mserver.getMethod("getServer").invoke(null);
			Object propertyManager = obj.getClass().getMethod("getPropertyManager").invoke(obj);
			propertyManager.getClass().getMethod("savePropertiesFile").invoke(propertyManager);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void uploadResourcePack(File f)
	{
		WebDriver driver = new HtmlUnitDriver();

		driver.get("https://minepack.net");

		WebElement element = driver.findElement(By.name("resourcepack"));
		element.sendKeys(f.getAbsolutePath());

		WebElement element2 = driver.findElement(By.name("submit"));
		element2.submit();

		List<WebElement> wel = driver.findElements(By.className("select"));
		// setPropertyInPropertyManager("resource-pack",
		// wel.get(0).getAttribute("value"));
		// setPropertyInPropertyManager("resource-pack-hash",
		// wel.get(1).getAttribute("value"));
		Config.getConfiguration().set("resourcepack-link", wel.get(0).getAttribute("value").replace("https", "http"));
		Config.getConfiguration().set("resourcepack-hash", wel.get(1).getAttribute("value"));

		Config.save();

		saveProperties();
	}

	public static void mergeTwoDirectories(File dir1, File dir2)
	{
		String targetDirPath = dir1.getAbsolutePath();
		File[] files = dir2.listFiles();
		for (File file : files)
		{
			file.renameTo(new File(targetDirPath + File.separator + file.getName()));
			System.out.println(file.getName() + " is moved!");
		}
	}

	@Override
	public InitializationState getInitializationState()
	{
		return initialize;
	}

}
