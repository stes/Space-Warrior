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
package sw.lvqagent;

import sw.State;
import sw.shared.data.GameWorld;
import sw.shared.data.entities.players.SpaceShip;

public class LVQState extends State
{
	private SpaceShip _localPlayer;
	private GameWorld _world;

	public LVQState(SpaceShip localPlayer, GameWorld world)
	{
		super(2);
		_localPlayer = localPlayer;
		_world = world;
		this.init();
	}

	public void init()
	{
		this.setFeature(0, (int) this.distanceToNearestShip());
		this.setFeature(1, (int) _localPlayer.getDirection() * 180 / 2 / Math.PI);
	}

	private double distanceToNearestShip()
	{
		double minDist = 1000;
		for (SpaceShip pl : _world.getPlayers())
		{
			if (pl.getName().equals(_localPlayer.getName()))
				continue;
			double dist = pl.distanceTo(_localPlayer);
			if (dist < minDist)
			{
				minDist = dist;
			}
		}
		return minDist;
	}

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof LVQState))
			return false;
		LVQState s = (LVQState) o;
		if (s.getFeatures().length != this.getFeatures().length)
			return false;
		for (int i = 0; i < this.getFeatures().length; i++)
		{
			if (s.getFeature(i) != this.getFeature(i))
				return false;
		}
		return true;
	}
}
