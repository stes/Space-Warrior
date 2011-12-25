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
package sw.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Loads the server properties from the file
 * <p>
 * specified by {@see sw.ServerConstants#PROPERTIES_PATH}
 * 
 * @author stes
 * @version 26.11.11
 */
public class PropertyLoader
{
	private Properties _properties;
	private File _propertiesFile;

	private String _serverName;
	private int _maxPlayers;
	private int _port;

	/**
	 * Creates a new property loader
	 */
	public PropertyLoader()
	{
		_properties = new Properties();
		_propertiesFile = new File(ServerConstants.PROPERTIES_PATH);
		try
		{
			if (!_propertiesFile.exists())
			{
				this.init();
			}
			this.load();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @return the maximum number of players
	 */
	public int getMaxPlayers()
	{
		return _maxPlayers;
	}

	/**
	 * @return the port
	 */
	public int getPort()
	{
		return _port;
	}

	/**
	 * @return the server name
	 */
	public String getServerName()
	{
		return _serverName;
	}

	private void init() throws IOException
	{
		System.out.println("init");
		_propertiesFile.createNewFile();

		FileOutputStream out = new FileOutputStream(_propertiesFile);
		_properties.setProperty("Server_Name", "Server_42");
		_properties.setProperty("Max_Players", "6");
		_properties.setProperty("Port", "2489");

		_properties.store(out, "This file contains the server properties.");
	}

	private void load() throws IOException
	{
		System.out.println("load");
		FileInputStream in = new FileInputStream(ServerConstants.PROPERTIES_PATH);
		_properties.load(in);

		this.setServerName(_properties.getProperty("Server_Name"));
		this.setMaxPlayers(Integer.parseInt(_properties.getProperty("Max_Players")));
		this.setPort(Integer.parseInt(_properties.getProperty("Port")));

		in.close();
	}

	/**
	 * @param maxPlayers
	 *            the maximum number of players
	 */
	private void setMaxPlayers(int maxPlayers)
	{
		this._maxPlayers = maxPlayers;
	}

	/**
	 * @param port
	 *            the port
	 */
	private void setPort(int port)
	{
		this._port = port;
	}

	/**
	 * @param serverName
	 *            the server name
	 */
	private void setServerName(String serverName)
	{
		this._serverName = serverName;
	}
}
