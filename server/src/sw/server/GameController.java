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

import sw.shared.Packer;
import sw.shared.Packettype;
import sw.shared.data.GameWorld;
import sw.shared.data.PlayerData;
import sw.shared.data.PlayerInput;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */

public class GameController
{
	private GameWorld _world;
	private HashMap<String, PlayerData> _players;
	private IServer _server;

	/**
	 * Creates a new game controller
	 * 
	 * @param server
	 *            IServer
	 */
	public GameController(IServer server)
	{
		_world = new GameWorld();
		_players = new HashMap<String, PlayerData>();
		_server = server;
	}

	/**
	 * A new player joined the game
	 * 
	 * @param name
	 *            the player's name
	 * @param imageID
	 */
	public void playerConnected(String name, int imageID)
	{
		PlayerData newPl = new PlayerData(name, imageID);
		_players.put(name, newPl);
		_world.insert(newPl);
	}

	/**
	 * Sends a snapshot to every player
	 */
	public void broadcastSnapshots()
	{
		for (PlayerData pl : _players.values())
		{
			Packer snapshot = new Packer(Packettype.SV_SNAPSHOT);
			_world.snap(snapshot, pl.getName());
			_server.sendPacket(pl.getName(), snapshot);
		}
	}

	/**
	 * A player left the game
	 * 
	 * @param name
	 *            the player's name
	 */
	public void playerLeft(String name, String reason)
	{
		_players.get(name).destroy();
		_players.remove(name);
	}

	/**
	 * processes a server input
	 * 
	 * @param name
	 *            the name of the affected player
	 * @param input
	 *            the player's input
	 */
	public void processPlayerInput(String name, PlayerInput input)
	{
		_players.get(name).setInput(input);
	}

	/**
	 * starts a new game
	 */
	public void startGame()
	{
		for (PlayerData pl : _players.values())
		{
			pl.respawn();
		}
		Packer info = new Packer(Packettype.SV_CHAT_MESSAGE);
		info.writeUTF("Server");
		info.writeUTF("New round");
		_server.sendBroadcast(info);
		System.out.println("New round");
	}

	public void tick()
	{
		this.checkTurn();
		_world.tick();
	}

	private void checkCollisions(PlayerData d)
	{
		for (PlayerData pl : _players.values())
		{
			if (pl.intersects(d))
			{
				pl.setSpeed(0);
				d.setSpeed(0);
			}
		}
	}

	/**
	 * adds damage to the players in the range of a shot
	 * 
	 * @param attacker
	 *            the attacking player
	 * @param shot
	 *            the shot
	 */
	/*
	 * private void processShot(PlayerData attacker, Shot shot) { for
	 * (PlayerData pl : _players.values()) { if (pl.isAlive() &&
	 * !pl.getName().equals(attacker.getName())) { if
	 * (shot.distanceTo(pl.getPosition()) < GameConstants.PLAYER_SIZE / 2) {
	 * pl.takeDamage(shot.getDamage()); if (!pl.isAlive()) {
	 * attacker.setScore(attacker.getScore() + 1); } } } } }
	 */

	private void checkTurn()
	{
		int alive = 0;
		for (PlayerData pl : _players.values())
		{
			this.checkCollisions(pl);
			if (pl.isAlive())
				alive++;
		}

		if ((alive == 1 && _players.size() > 1)
				|| (alive == 0 && _players.size() == 1))
		{
			if (alive == 1)
			{
				for (PlayerData pl : _players.values())
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
