package sw.client.plugins;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;


/**
 * Loading of external plugins
 * 
 * @author Redix stes
 * @version 15.01.2012
 */
public class PluginLoader
{	
	private final HashMap<String, File> _directories;
	
	public PluginLoader()
	{
		_directories = new HashMap<String, File>();
	}
	
	public void addDirectory(String path, String name)
	{
		File f = new File(path);
		if (!f.exists())
			throw new IllegalArgumentException("Directory does not exist");
		else if (!f.isDirectory())
			throw new IllegalArgumentException("Path does not specify a directory");
		_directories.put(name, f);
	}
	
	public File[] getAIs(String name)
	{
		File dir = _directories.get(name);
		if (dir == null)
			return new File[]{};
		return dir.listFiles(new FileFilter()
		{
			@Override
			public boolean accept(File pathname)
			{
				return (pathname.exists() && pathname.isDirectory()); // && AIPlayerLoader.isValid(pathname));
			}
		});
	}
}
