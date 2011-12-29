/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 * Copyright (C) 2011 Redix stes Abbadonn
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
package sw.client.player.ai;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import javax.naming.OperationNotSupportedException;

import sw.client.gcontrol.IGameStateManager;

/**
 * Used for loading AI players
 * 
 * @author Redix, stes, Abbadonn
 * @version 19.12.11
 */
public class AIPlayerLoader
{
	static Properties _properties = new Properties();

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
		try
		{
			AIPlayerLoader._properties.load(new FileInputStream(configFile));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		URLClassLoader loader = new URLClassLoader(new URL[] { p },
				ClassLoader.getSystemClassLoader());
		System.out.println("Loaded class: " + AIPlayerLoader._properties.get("class"));
		Class<?> c = loader.loadClass(AIPlayerLoader._properties.get("class").toString());
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
