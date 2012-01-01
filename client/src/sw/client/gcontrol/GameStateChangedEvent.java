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
package sw.client.gcontrol;

import java.util.EventObject;

import sw.shared.data.GameWorld;
import sw.shared.data.entities.players.SpaceShip;

/**
 * @author Redix stes Abbadonn
 * @version 02.12.2011
 */
public class GameStateChangedEvent extends EventObject
{
	private static final long serialVersionUID = -6781713802525662904L;

	private SpaceShip _localDataSet;
	private GameWorld _world;
	private SpaceShip _winner;

	public GameStateChangedEvent(Object source)
	{
		super(source);
	}

	/**
	 * @return the _playerList
	 */
	public GameWorld getGameWorld()
	{
		return _world;
	}

	/**
	 * @return the _localDataSet
	 */
	public SpaceShip getLocalDataSet()
	{
		return _localDataSet;
	}

	/**
	 * @return the _winner
	 */
	public SpaceShip getWinner()
	{
		return _winner;
	}

	/**
	 * @param _playerList
	 *            the _playerList to set
	 */
	public void setGameWorld(GameWorld world)
	{
		_world = world;
	}

	/**
	 * @param _localDataSet
	 *            the _localDataSet to set
	 */
	public void setLocalDataSet(SpaceShip localDataSet)
	{
		_localDataSet = localDataSet;
	}

	/**
	 * @param _winner
	 *            the _winner to set
	 */
	public void setWinner(SpaceShip winner)
	{
		_winner = winner;
	}
}
