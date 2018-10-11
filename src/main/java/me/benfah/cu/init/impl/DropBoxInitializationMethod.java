package me.benfah.cu.init.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.DbxWebAuth.Request;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.sharing.RequestedVisibility;
import com.dropbox.core.v2.sharing.SharedLinkSettings;
import com.google.common.collect.HashBiMap;

import me.benfah.cu.api.CustomBase;
import me.benfah.cu.api.CustomBlock;
import me.benfah.cu.api.CustomGUI;
import me.benfah.cu.api.CustomItem;
import me.benfah.cu.api.CustomRegistry;
import me.benfah.cu.api.WorldStore;
import me.benfah.cu.init.IInitializationMethod;
import me.benfah.cu.main.CustomUtils;
import me.benfah.cu.util.Config;
import me.benfah.cu.util.InitializationState;
import me.benfah.cu.util.Utils;
import me.benfah.cu.util.ZipCompare;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import net.sourceforge.htmlunit.corejs.javascript.EcmaError;

public class DropBoxInitializationMethod implements IInitializationMethod
{

	public static PrintWriter pw;
	public static InitializationState initialize;

	public InitializationState getInitializationState()
	{
		return initialize;
	}

	public void init(boolean forceUpload)
	{
		try
		{
			initialize = InitializationState.INITIALIZING;
			try
			{
				System.out.println(CustomRegistry.PLUGIN_REGISTRY.size() + " plugins registered.");
				System.out.println(CustomRegistry.CUSTOM_BLOCK_REGISTRY.size() + " blocks registered.");
				System.out.println(CustomRegistry.CUSTOM_GUI_REGISTRY.size() + " guis registered.");
				System.out.println(CustomRegistry.CUSTOM_ITEM_REGISTRY.size() + " guis registered.");

				System.out
						.println(CustomRegistry.CUSTOM_GUI_REGISTRY.size() + CustomRegistry.CUSTOM_BLOCK_REGISTRY.size()
								+ CustomRegistry.CUSTOM_ITEM_REGISTRY.size() + " in total.");

				setup();

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

		} catch (Exception e)
		{
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
			CustomUtils.instance.saveResource("pack.mcmeta", true);
			CustomUtils.instance.saveResource("pack.png", true);
			CustomUtils.instance.saveResource("mob_spawner.png", true);
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

			if (resourcepack.exists())
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
					if (!Config.getConfiguration().contains("resourcepack-link"))
						uploadResourcePack(finalrpzip);
					else if (/* resourcepack.length() == finalrpzip.length() */ZipCompare.compare(resourcepack,
							finalrpzip))
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

	private static void setup()
	{
		if (!Config.getConfiguration().contains("dropbox.key")
				|| Config.getConfiguration().getString("dropbox.key").equals("null"))
		{

			System.out.println("DROPBOX UPLOAD SETUP:");
			System.out.println(
					"1. Go to https://www.dropbox.com/developers/apps/create (create a dropbox account if necessary).");
			System.out.println("2. Choose \"Dropbox API\", then choose \"App folder\", then name it somehow.");
			System.out.println("3. Look for \"Development users\" and hit \"Enable additional users\".");
			System.out.println("4. Enter the App key and the App secret into the cfg.yml of CustomUtils"); // TODO: <-
			Config.getConfiguration().set("dropbox.key", "null");
			Config.getConfiguration().set("dropbox.secret", "null");

			Config.save();

			while (Config.getConfiguration().getString("dropbox.key").equals("null")
					&& Config.getConfiguration().getString("dropbox.secret").equals("null"))
			{
				try
				{
					System.out.println("Hit enter when you're done.");
					System.in.read();
					Config.load();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			System.out.println("Now authorize your account and paste the code to dropbox.code in the cfg.yml.");

			Config.getConfiguration().set("dropbox.code", "null");
			Config.save();

			DbxAppInfo appInfo = new DbxAppInfo(Config.getConfiguration().getString("dropbox.key"),
					Config.getConfiguration().getString("dropbox.secret"));

			DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0");
			DbxWebAuth webAuth = new DbxWebAuth(config, appInfo);
			String url = webAuth.authorize(Request.newBuilder().withNoRedirect().build());
			System.out.println("Authorize: " + url);
			while (Config.getConfiguration().getString("dropbox.code").equals("null"))
			{
				System.out.println("Hit enter when you're done.");
				try
				{
					System.in.read();
					Config.load();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			try
			{
				DbxAuthFinish daf = webAuth.finishFromCode(Config.getConfiguration().getString("dropbox.code"));
				Config.getConfiguration().set("dropbox.token", daf.getAccessToken());
			} catch (DbxException e)
			{
				e.printStackTrace();
			}

		}
	}

	private static void uploadResourcePack(File f)
	{
		// WebDriver driver = new HtmlUnitDriver();
		//
		// driver.get("https://minepack.net");
		//
		// WebElement element = driver.findElement(By.name("resourcepack"));
		// element.sendKeys(f.getAbsolutePath());
		//
		//
		//
		//
		//
		// WebElement element2 = driver.findElement(By.name("submit"));
		// element2.submit();
		//
		//
		//
		//
		//
		// List<WebElement> wel = driver.findElements(By.className("select"));
		// setPropertyInPropertyManager("resource-pack",
		// wel.get(0).getAttribute("value"));
		// setPropertyInPropertyManager("resource-pack-hash",
		// wel.get(1).getAttribute("value"));
		// CustomUtils.cfg.set("resourcepack-link",
		// wel.get(0).getAttribute("value").replace("https", "http"));
		// CustomUtils.cfg.set("resourcepack-hash", wel.get(1).getAttribute("value"));
		// try {
		// CustomUtils.cfg.save(CustomUtils.cfgFile);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// saveProperties();

		try
		{

			DbxAppInfo appInfo = new DbxAppInfo(Config.getConfiguration().getString("dropbox.key"),
					Config.getConfiguration().getString("dropbox.secret"));

			DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0");
			DbxClientV2 c2 = new DbxClientV2(config, Config.getConfiguration().getString("dropbox.token"));
			c2.files().listFolder("").getEntries().forEach((x) ->
			{
				try
				{
					c2.files().deleteV2(x.getPathLower());
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			});
			String strippedUid = UUID.randomUUID().toString().substring(0, 7);
			FileMetadata fm = c2.files().uploadBuilder("/resourcepack-" + strippedUid + ".zip")
					.withMode(WriteMode.OVERWRITE).uploadAndFinish(new FileInputStream(f));
			String link = null;
			// for(SharedLinkMetadata slm : c2.sharing().listSharedLinks().getLinks())
			// {
			// if(slm.getId().equals(fm.getId()))
			// link = slm.getUrl();
			// }

			if (link == null)
				link = c2.sharing().createSharedLinkWithSettings("/resourcepack-" + strippedUid + ".zip",
						SharedLinkSettings.newBuilder().withRequestedVisibility(RequestedVisibility.PUBLIC).build())
						.getUrl();
			link = link.replace("www", "dl");

			Config.getConfiguration().set("resourcepack-link", link);
			Config.save();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
