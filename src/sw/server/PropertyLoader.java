package sw.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyLoader
{
	private Properties _properties;
	private File _propertiesFile;
	
	
	private String _serverName;
	private String _maxPlayers;
	private String _port;
	
	/**
	 * Creates a new property loader
	 */
	public PropertyLoader()
	{
		_properties = new Properties();
		_propertiesFile = new File(System.getProperty("user.dir") + "/config.ini");
		try
		{
			if (!_propertiesFile.exists())
				this.init();
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
	public String getMaxPlayers()
	{
		return _maxPlayers;
	}
	
	/**
	 * @return the port
	 */
	public String getPort()
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
		_propertiesFile.createNewFile();
		
		FileOutputStream out = new FileOutputStream(_propertiesFile);
		_properties.setProperty("Server_Name", "Server_42");
		_properties.setProperty("Max_Players", "6");
		_properties.setProperty("Port", "2489");
		
		_properties.store(out, "This file contains the server properties.");
	}

	private void load() throws IOException
	{
		FileInputStream in = new FileInputStream(System.getProperty("user.dir") + "/config.ini");
		_properties.load(in);
		
		this.setServerName(_properties.getProperty("Server_Name"));
		this.setMaxPlayers(_properties.getProperty("Max_Players"));
		this.setPort(_properties.getProperty("Port"));
		
		in.close();
	}

	/**
	 * @param maxPlayers the maximum number of players
	 */
	private void setMaxPlayers(String maxPlayers)
	{
		this._maxPlayers = maxPlayers;
	}

	/**
	 * @param port the port
	 */
	private void setPort(String port)
	{
		this._port = port;
	}

	/**
	 * @param serverName the server name
	 */
	private void setServerName(String serverName)
	{
		this._serverName = serverName;
	}
}
