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
package sw.test;

import sw.server.PropertyLoader;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PropertyLoaderTest extends TestCase
{
	private PropertyLoader _propLoader;
	
	@Override
	public void setUp()
	{
		_propLoader = new PropertyLoader();
	}
	
	@Override
	public void tearDown()
	{
		
	}
	
	public void test()
	{
		System.out.println(_propLoader.getMaxPlayers());
		System.out.println(_propLoader.getPort());
		System.out.println(_propLoader.getServerName());
	}
	
	public static Test suite()
	{
		return new TestSuite(PropertyLoaderTest.class);
	}
}
