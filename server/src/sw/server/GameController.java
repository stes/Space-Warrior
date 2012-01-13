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
package sw.server;

import java.util.HashMap;

import sw.shared.Packettype;
import sw.shared.data.GameWorld;
import sw.shared.data.PlayerInput;
import sw.shared.data.entities.GameState;
import sw.shared.data.entities.players.SpaceShip;
import sw.shared.net.Packer;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */

public class GameController implements ServerListener
{
	private GameWorld _world;
	private HashMap<String, SpaceShip> _players;
	private IServer _server;
	private GameState _gameState;

	/**
	 * Creates a new game controller
	 * 
	 * @param server
	 *            IServer
	 */
	public GameController(IServer server)
	{
		_world = new GameWorld();
		_players = new HashMap<String, SpaceShip>();
		_server = server;
		_gameState = new GameState();
		_world.insert(_gameState);
	}

	@Override
	public void broadcastSnapshots()
	{
		for (SpaceShip pl : _players.values())
		{
			Packer snapshot = new Packer(Packettype.SV_SNAPSHOT);
			_world.snap(snapshot, pl.getName());
			_server.sendPacket(pl.getName(), snapshot);
		}
	}

	@Override
	public void playerConnected(String name, int imageID)
	{
		SpaceShip newPl = new SpaceShip(name, imageID);
		_players.put(name, newPl);
		_world.insert(newPl);
	}

	@Override
	public void playerLeft(String name, String reason)
	{
		_players.get(name).destroy();
		_players.remove(name);
	}

	@Override
	public void processPlayerInput(String name, PlayerInput input)
	{
		_players.get(name).setInput(input);
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

	@Override
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

		if ((alive == 1 && _players.size() > 1) || (alive == 0 && _players.size() == 1))
		{
			if (alive == 1)
			{
				for (SpaceShip pl : _players.values())
				{
					Packer info = new Packer(Packettype.SV_CHAT_MESSAGE);
					info.writeUTF("Server");
					info.writeUTF(pl.getName() + " has won the round!");
					_server.sendBroadcast(info);
					break;
				}
			}
			this.startGame();
		}
	}
}
