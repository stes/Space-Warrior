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
package sw.rlagent;

import java.util.HashMap;

public class World
{
	private HashMap<Integer, Double> _stateMap;

	public World()
	{
		_stateMap = new HashMap<Integer, Double>();

		// _qvalues = new double[_width][_height][AIConstants.ACTIONS_COUNT];
	}

	public HashMap<Integer, Double> getMap()
	{
		return _stateMap;
	}

	public void exploreState(SimpleState s)
	{
		if (!_stateMap.containsKey(s.getHash()))
		{
			_stateMap.put(s.getHash(), s.value());
			// System.out.println("explored state " + s.getHash()+" with value "
			// + s.value());
			// for (byte b : s.id)
			// System.out.print(b + ";");
		}
	}
}
