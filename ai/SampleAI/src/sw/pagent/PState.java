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
package sw.pagent;

import sw.State;
import sw.shared.GameConstants;
import sw.shared.data.GameWorld;
import sw.shared.data.entities.players.SpaceShip;

public class PState extends State
{
	private static final int VALUE_RANGE = 255;

	private SpaceShip _localPlayer;
	private GameWorld _world;

	public PState(SpaceShip localPlayer, GameWorld world)
	{
		this();
		_localPlayer = localPlayer;
		_world = world;
		this.init();
	}

	public PState()
	{
		super(3);
	}

	public void init()
	{
		this.setFeature(0, (int) this.distanceToNearestShip());
		this.setFeature(1, (int) this.angleToNextPlayer());
		this.setFeature(2, (int) this.nextPlayersAngle());
	}

	private double distanceToNearestShip()
	{
		double max = Math.sqrt(Math.pow(GameConstants.PLAYING_FIELD_WIDTH, 2)
				+ Math.pow(GameConstants.PLAYING_FIELD_HEIGHT, 2));
		SpaceShip s = this.nextShip();
		if (s != null)
			return _localPlayer.distanceTo(s) * VALUE_RANGE / max;
		else
			return PState.VALUE_RANGE;
	}

	private SpaceShip nextShip()
	{
		double minDist = 1000;
		SpaceShip next = null;
		for (SpaceShip pl : _world.getPlayers())
		{
			if (pl.getName().equals(_localPlayer.getName()))
				continue;
			double dist = pl.distanceTo(_localPlayer);
			if (dist < minDist)
			{
				minDist = dist;
				next = pl;
			}
		}
		return next;
	}

	/**
	 * @return the angle between the local player's direction and the location
	 *         of the next player
	 */
	public double angleToNextPlayer()
	{
		SpaceShip next = nextShip();

		if (next == null)
			return 0;

		double x0 = _localPlayer.getX();
		double y0 = _localPlayer.getY();
		double x1 = next.getX();
		double y1 = next.getY();

		// TODO simplify :D
		double alpha = _localPlayer.getDirection() * PState.VALUE_RANGE / (2 * Math.PI);
		double beta = Math.atan2(x0 - x1, y0 - y1) * PState.VALUE_RANGE / (2 * Math.PI);

		double diff = Math.min(Math.abs(beta - alpha), Math.abs(alpha - beta));
		diff = Math.abs(Math.abs(diff - PState.VALUE_RANGE / 2) - PState.VALUE_RANGE / 2);

		return diff;
	}

	private double nextPlayersAngle()
	{
		SpaceShip next = nextShip();

		if (next == null)
			return 0;

		double x0 = next.getX();
		double y0 = next.getY();
		double x1 = _localPlayer.getX();
		double y1 = _localPlayer.getY();

		// TODO simplify :D
		double alpha = next.getDirection() * PState.VALUE_RANGE / (2 * Math.PI);
		double beta = Math.atan2(x0 - x1, y0 - y1) * PState.VALUE_RANGE / (2 * Math.PI);

		double diff = Math.min(Math.abs(beta - alpha), Math.abs(alpha - beta));
		diff = Math.abs(Math.abs(diff - PState.VALUE_RANGE / 2) - PState.VALUE_RANGE / 2);

		return diff;
	}

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof PState))
			return false;
		PState s = (PState) o;
		if (s.getFeatures().length != this.getFeatures().length)
			return false;
		for (int i = 0; i < this.getFeatures().length; i++)
		{
			if (s.getFeature(i) != this.getFeature(i))
				return false;
		}
		return true;
	}

	public static PState fromString(String s)
	{
		String[] parts = s.split(";");
		PState state = new PState();
		// TODO replace 3 by some method
		for (int i = 0; i < 3; i++)
		{
			state.setFeature(i, Integer.parseInt(parts[i]));
		}
		return state;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 3; i++)
		{
			sb.append((int) getFeature(i));
			sb.append(";");
		}
		return sb.toString();
	}

	/**
	 * for debug purposes.. TODO remove when no longer used
	 */
	// public void showAngle()
	// {
	// System.out.println(this.angleToNextPlayer() + " ; " +
	// this.nextPlayersAngle());
	// }
}
