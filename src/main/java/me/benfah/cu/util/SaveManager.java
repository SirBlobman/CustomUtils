package me.benfah.cu.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.OnbeforeunloadHandler;

public class SaveManager
{

	static List<Save> fieldList = new ArrayList<Save>();

	public static void saveAndLoad(Field f, String fileNameWithEnding, File saveLocation)
	{
		if (Modifier.isStatic(f.getModifiers()))
			fieldList.add(new Save(f, saveLocation));
	}

	public static void onSave()
	{
		for (Save s : fieldList)
		{
			try
			{
				Object ots = s.f.get(null);

				if (!s.saveLocation.getParentFile().exists())
					s.saveLocation.getParentFile().mkdirs();

				if (s.saveLocation.exists())
					s.saveLocation.delete();
				FileOutputStream fos = new FileOutputStream(s.saveLocation);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(ots);
				oos.close();
				fos.close();

			} catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}

	public void onLoad()
	{
		for (Save s : fieldList)
		{
			if (s.saveLocation.exists())
			{
				try
				{

					FileInputStream fis = new FileInputStream(s.saveLocation);
					ObjectInputStream ois = new ObjectInputStream(fis);
					s.f.set(null, ois);
					ois.close();
					fis.close();
				} catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		}
	}

	public static class Save
	{
		Field f;
		File saveLocation;

		public Field getF()
		{
			return f;
		}

		public File getSaveLocation()
		{
			return saveLocation;
		}

		public Save(Field f, File saveLocation)
		{
			this.f = f;
			this.saveLocation = saveLocation;
		}

	}
}
