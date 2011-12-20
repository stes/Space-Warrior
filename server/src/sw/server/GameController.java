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

import sw.shared.GameConstants;
import sw.shared.Packettype;
import sw.shared.data.Packer;
import sw.shared.data.PlayerData;
import sw.shared.data.PlayerInput;
import sw.shared.data.PlayerList;
import sw.shared.data.Shot;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */

public class GameController
{
	private PlayerList _players;
	private IServer _server;

	/**
	 * Creates a new game controller
	 * 
	 * @param server
	 *            IServer
	 */
	public GameController(IServer server)
	{
		_players = new PlayerList(GameConstants.MAX_PLAYERS);
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
		PlayerData newDataSet = new PlayerData(name, imageID);
		_players.insert(newDataSet, null);
	}

	/**
	 * Sends a snapshot to every player
	 */
	public void broadcastSnapshots()
	{
		for (int i = 0; i < _players.size(); i++)
		{
			PlayerData data = _players.dataAt(i);
			if (data != null)
			{
				Packer snapshot = _players.createSnapshot(data.getName());
				_server.sendPacket(data.getName(), snapshot);
			}
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
		_players.setInput(name, input);
	}

	/**
	 * starts a new game
	 */
	public void startGame()
	{
		for (int i = 0; i < _players.size(); i++)
		{
			PlayerData data = _players.dataAt(i);
			if (data != null)
			{
				data.respawn();
			}
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
		this.updateData();
	}

	/**
	 * adds damage to the players in the range of a shot
	 * 
	 * @param attacker
	 *            the attacking player
	 * @param shot
	 *            the shot
	 */
	private void processShot(PlayerData attacker, Shot shot)
	{
		for (int i = 0; i < _players.size(); i++)
		{
			PlayerData data = _players.dataAt(i);
			if (data != null && data.isAlive() && !data.getName().equals(attacker.getName()))
			{
				if (shot.distanceTo(data.getPosition()) < GameConstants.PLAYER_SIZE / 2)
				{
					data.takeDamage(shot.getDamage());
					if (!data.isAlive())
					{
						attacker.setScore(attacker.getScore() + 1);
					}
				}
			}
		}
	}
	
	private void checkTurn()
	{
		if ((_players.count(true) == 1 && _players.count(false) > 1)
				|| (_players.count(true) == 0 && _players.count(false) == 1))
		{
			if (_players.count(true) == 1)
			{
				for (int i = 0; i < _players.size(); i++)
				{
					PlayerData data = _players.dataAt(i);
					if (data != null)
					{
						Packer info = new Packer(Packettype.SV_CHAT_MESSAGE);
						info.writeUTF("Server");
						info.writeUTF(data.getName() + " has won the round!");
						_server.sendBroadcast(info);
						break;
					}
				}
			}
			this.startGame();
		}
	}

	private void updateData()
	{
		for (int i = 0; i < _players.size(); i++)
		{
			PlayerData data = _players.dataAt(i);
			PlayerInput input = _players.inputAt(i);
			if (data != null && data.isAlive())
			{
				if (input.shot() > 0)
				{
					Shot s = data.shoot(input.shot() == 2);
					if (s != null)
					{
						this.processShot(data, s);
						Packer p = s.write();
						_server.sendBroadcast(p);
					}
				}
				if (input.moveDirection() == 0)
					data.accelerate(-GameConstants.ACCELERATION);
				else
				{
					data.accelerate(GameConstants.ACCELERATION * input.moveDirection());
				}
				
				if (input.turnDirection() == 0)
				{
					data.angularDecelerate(GameConstants.ANGULAR_ACCELERATION);
				}
				else
				{
					data.angularAccelerate(GameConstants.ANGULAR_ACCELERATION * input.turnDirection());
				}

				data.reload();
				data.move();
				// TODO improve
				//this.checkCollision(data);
			}
		}
	}

	// TODO: do this in the PlayerDataSet
	/*private void checkCollision(PlayerDataSet s2)
	{
		for (int i = 0; i < _activePlayers.size(); i++)
		{
			PlayerDataSet s1 = _activePlayers.dataAt(i);
			if (s1 == null || s1.equals(s2))
				continue;
			double diff = s1.getPosition().distance(s2.getPosition());
			double dmg = 0;
			if (diff < GameConstants.MAX_COLLISION_DAMAGE_RANGE)
			{
				dmg = -((double) GameConstants.MAX_COLLISION_DAMAGE)
						/ ((double) GameConstants.MAX_COLLISION_DAMAGE_RANGE)
						* diff + GameConstants.MAX_COLLISION_DAMAGE;
				
				double speed = s2.getSpeed();
				s1.setSpeed(s2.getSpeed() * 0.8);
				s2.setSpeed(speed * 0.8);
				
				double direction = s2.getDirection();
				s1.setDirection(s2.getDirection());
				s2.setSpeed(direction);
			}
			addDamage(s1, (int)dmg);
			addDamage(s2, (int)dmg);
		}
	}*/
}
