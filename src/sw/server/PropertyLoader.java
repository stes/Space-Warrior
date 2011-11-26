package sw.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.logging.Logger;

import me.stes.scp.PlayerInfo;
import me.stes.scp.Team;

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
		if (!_propertiesFile.exists())
			this.init();
		this.load();
	}
	
	private void load()
	{
		FileInputStream in = new FileInputStream(System.getProperty("user.dir") + "/config.ini");
		_properties.load(in);
		
		_serverName = _properties.getProperty("Server_Name");
		_maxPlayers = _properties.getProperty("Max_Players");
		_port = _properties.getProperty("Port");
		
		in.close();
	}
	
	private void init()
	{
		_propertiesFile.createNewFile();
		
		FileOutputStream out = new FileOutputStream(_propertiesFile);
		_properties.setProperty("Server_Name", "Server_42");
		_properties.setProperty("Max_Players", "6");
		_properties.setProperty("Port", "2489");
		
		_properties.store(out, "This file contains the server properties.");
	}

	/**
	 * @return the _serverName
	 */
	public String getServerName()
	{
		return _serverName;
	}

	/**
	 * @param _serverName the _serverName to set
	 */
	private void setServerName(String _serverName)
	{
		this._serverName = _serverName;
	}

	/**
	 * @return the _maxPlayers
	 */
	public String getMaxPlayers()
	{
		return _maxPlayers;
	}

	/**
	 * @param _maxPlayers the _maxPlayers to set
	 */
	private void setMaxPlayers(String _maxPlayers)
	{
		this._maxPlayers = _maxPlayers;
	}

	/**
	 * @return the _port
	 */
	public String getPort()
	{
		return _port;
	}

	/**
	 * @param _port the _port to set
	 */
	private void setPort(String _port)
	{
		this._port = _port;
	}
}
