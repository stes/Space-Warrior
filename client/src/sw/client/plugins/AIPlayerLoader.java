/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 * Copyright (C) 2011-2012 Redix stes Abbadonn
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package sw.client.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import javax.naming.OperationNotSupportedException;

import sw.client.gcontrol.IGameStateManager;
import sw.client.player.ai.AIPlayer;

/**
 * Used for loading AI players
 * 
 * @author Redix, stes, Abbadonn
 * @version 19.12.11
 */
public class AIPlayerLoader
{
	class AIPlugin
	{
		private final File _dir;
		private final File _pluginFile;
		private final File _configFile;
		private final Properties _properties;
		
		public AIPlugin(String path) throws FileNotFoundException, IOException
		{
			_dir = new File(path);
			_configFile = new File(path + "/config.ini");
			_pluginFile = getJar();
			_properties = new Properties();
			_properties.load(new FileInputStream(getConfigFile()));
		}
		
		private File getJar()
		{
			File[] jars =  _dir.listFiles(new FilenameFilter()
			{
				@Override
				public boolean accept(File f, String s)
				{
					return (s.endsWith(".jar"));
				}
			});
			if (jars.length != 1)
				throw new UnsupportedOperationException("There are multiple jars in the specified directory");
			return jars[0];
		}
		
		public File getConfigFile()
		{
			return _configFile;
		}
		
		public Properties getProperties()
		{
			return _properties;
		}
		
		public File getPluginFile()
		{
			return _pluginFile;
		}
		
		public AIPlayer getInstance(IGameStateManager stateManager) throws ClassNotFoundException
		{
			URLClassLoader loader;
			try
			{
				loader = new URLClassLoader(new URL[] { getPluginFile().toURI().toURL() },
						ClassLoader.getSystemClassLoader());
			}
			catch (MalformedURLException e1)
			{
				e1.printStackTrace();
				return null;
			}
			System.out.println("Loaded class: " + getProperties().get("class"));
			Object obj = getProperties().get("class");
			if (obj == null)
				throw new UnsupportedOperationException("No class specified in config file");
			String classname = obj.toString();
			Class<?> c = loader.loadClass(classname);
			Object o;
			try
			{
				o = c.getConstructor(IGameStateManager.class).newInstance(stateManager);
			}
			catch (Exception e)
			{
				// TODO improve?
				throw new UnsupportedOperationException("No fitting constructor available");
			}
			System.out.println(o instanceof AIPlayer);
			return (AIPlayer) o;
		}
	}
	
	public static boolean isValid(File dir)
	{
		if (!dir.exists() || !dir.isDirectory())
			return false;
		File configFile = new File(dir + "/config.ini");
		File pluginFile = null;
		File[] jars =  dir.listFiles(new FilenameFilter()
		{
			@Override
			public boolean accept(File f, String s)
			{
				return (s.endsWith(".jar"));
			}
		});
		if (jars.length != 1)
			return false;
		pluginFile = jars[0];
		if (!configFile.exists())
			return false;
		URLClassLoader loader;
		try
		{
			loader = new URLClassLoader(new URL[] { new URL("file:\\" + pluginFile.getPath()) },
					ClassLoader.getSystemClassLoader());
		}
		catch (MalformedURLException e1)
		{
			return false;
		}
		Properties _properties = new Properties();
		try
		{
			_properties.load(new FileInputStream(configFile));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		if (_properties.get("class") == null)
			return false;
		String classname = _properties.get("class").toString();
		Class<?> c = null;
		try
		{
			c = loader.loadClass(classname);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			return false;
		}
		Constructor<?>[] cons = c.getConstructors();
		for (Constructor<?> con : cons)
		{
			Class<?>[] params = con.getParameterTypes();
			if (params.length != 1 || !params[0].equals(IGameStateManager.class))
				continue;
			return true;
		}
		return false;
	}

	/**
	 * Loads a player from an external file and returns an instance of the AI
	 * player implemented in the file
	 * 
	 * @param source
	 *            The source jar
	 * @param stateManager
	 * @return
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws OperationNotSupportedException
	 */
	public static AIPlayer load(File source, IGameStateManager stateManager)
			throws FileNotFoundException,
			ClassNotFoundException,
			OperationNotSupportedException
	{
		URL p;
		try
		{
			p = new URL("file:\\" + source.getPath());
		}
		catch (MalformedURLException e)
		{
			throw new FileNotFoundException("Invalid plugin file or path");
		}

		File configFile = new File(source.getParent() + "/config.ini");
		if (!configFile.exists())
		{
			throw new FileNotFoundException("No config file in the specified directory");
		}
		Properties properties = new Properties();
		try
		{
			properties.load(new FileInputStream(configFile));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}

		URLClassLoader loader = new URLClassLoader(new URL[] { p },
				ClassLoader.getSystemClassLoader());
		System.out.println("Loaded class: " + properties.get("class"));
		Class<?> c = loader.loadClass(properties.get("class").toString());
		try
		{
			Object o = c.getConstructor(IGameStateManager.class).newInstance(stateManager);
			System.out.println(o instanceof AIPlayer);
			return (AIPlayer) o;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new OperationNotSupportedException("Invalid plugin file");
		}
	}
}
