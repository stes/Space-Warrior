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
package sw.shared;

import java.util.HashMap;

import sw.shared.data.GameWorld;
import sw.shared.data.PlayerInput;
import sw.shared.data.entities.GameState;
import sw.shared.data.entities.players.SpaceShip;

/**
 * Base class for game engines
 * 
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */

public abstract class GameEngine
{
	private GameWorld _world;
	private HashMap<String, SpaceShip> _players;
	private GameState _gameState;

	public GameWorld getWorld()
	{
		return _world;
	}

	public HashMap<String, SpaceShip> getPlayers()
	{
		return _players;
	}

	public GameState getGameState()
	{
		return _gameState;
	}

	/**
	 * Creates a new game controller
	 * 
	 * @param server
	 *            IServer
	 */
	public GameEngine()
	{
		_world = new GameWorld();
		_players = new HashMap<String, SpaceShip>();
		_gameState = new GameState();
		_world.insert(_gameState);
	}

	/**
	 * starts a new game
	 */
	public void startGame()
	{
		_world.removeShotEntities();
		for (SpaceShip pl : _players.values())
		{
			do
			{
				pl.respawn();
			}
			while(_world.checkCollision(pl));
		}
		_gameState.startNewRound();
	}

	public void tick()
	{
		this.checkTurn();
		_world.tick();
	}

	private void checkTurn()
	{
		int alive = 0;
		for (SpaceShip pl : _players.values())
		{
			if (pl.isAlive())
			{
				alive++;
			}
		}

		if ((alive == 1 && _players.size() > 1) || (alive == 0 && _players.size() > 0))
		{
			if (alive == 1)
			{
				for (SpaceShip pl : _players.values())
				{
					this.invokePlayerWon(pl);
					break;
				}
			}
			this.startGame();
		}
	}

	public abstract void invokePlayerWon(SpaceShip pl);
	
	public void playerInput(String name, PlayerInput input)
	{
		SpaceShip pl = getPlayers().get(name);
		if (pl == null)
			return;
		pl.setInput(input);
	}
	
	public void removePlayer(String name)
	{
		getPlayers().get(name).destroy();
		getPlayers().remove(name);
	}
	
	public void addPlayer(String name, int imageID, boolean local)
	{
		SpaceShip newPl = new SpaceShip(name, imageID, local);
		getPlayers().put(name, newPl);
		getWorld().insert(newPl);
	}
	
	public void addPlayer(String name, int imageID)
	{
		addPlayer(name, imageID, false);
	}
}
