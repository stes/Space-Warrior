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
