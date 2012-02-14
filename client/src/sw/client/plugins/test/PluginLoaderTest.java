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
package sw.client.plugins.test;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import sw.client.plugins.AIPlayerLoader;
import sw.client.plugins.PluginLoader;

public class PluginLoaderTest
{
	PluginLoader _ploader;
	
	@Before
	public void setUp() throws Exception
	{
		_ploader = new PluginLoader();
		_ploader.addDirectory("C:\\Users\\Steffen\\Projekte\\Projekte\\SpaceWarrior\\current_build", "sample");
	}

	@Test
	public void testGetAIs()
	{
		File[] files = _ploader.getAIs("sample");
		for (File f : files)
		{
			System.out.println(f.getAbsolutePath() + "  " + AIPlayerLoader.isValid(f));
		}
	}

}
