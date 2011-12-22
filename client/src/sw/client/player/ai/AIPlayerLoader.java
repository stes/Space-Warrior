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
			_properties.load(new FileInputStream(configFile));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		URLClassLoader loader = new URLClassLoader(new URL[] { p },
				ClassLoader.getSystemClassLoader());
		System.out.println("Loaded class: " + _properties.get("class"));
		Class<?> c = loader.loadClass(_properties.get("class").toString());
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
