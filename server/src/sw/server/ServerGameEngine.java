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

import sw.shared.GameEngine;
import sw.shared.Packettype;
import sw.shared.data.PlayerInput;
import sw.shared.data.entities.players.SpaceShip;
import sw.shared.net.Packer;

/**
 * A game engine running on a server and handling several clients using
 * the SpaceWarrior UDP protocol
 * 
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */

public class ServerGameEngine extends GameEngine implements ServerListener
{
	private IServer _server;

	/**
	 * Creates a new game controller
	 * 
	 * @param server
	 *            IServer
	 */
	public ServerGameEngine(IServer server)
	{
		super();
		_server = server;
	}

	@Override
	public void broadcastSnapshots()
	{
		for (SpaceShip pl : getPlayers().values())
		{
			Packer snapshot = new Packer(Packettype.SV_SNAPSHOT);
			getWorld().snap(snapshot, pl.getName());
			_server.sendPacket(pl.getName(), snapshot);
		}
	}

	@Override
	public void playerConnected(String name, int imageID)
	{
		this.addPlayer(name, imageID);
	}

	@Override
	public void playerLeft(String name, String reason)
	{
		this.removePlayer(name);
	}
	
	@Override
	public void processPlayerInput(String name, PlayerInput input)
	{
		this.playerInput(name, input);
	}
	
	@Override
	public void invokePlayerWon(SpaceShip pl)
	{
		Packer info = new Packer(Packettype.SV_CHAT_MESSAGE);
		info.writeUTF("Server");
		info.writeUTF(pl.getName() + " has won the round!");
		_server.sendBroadcast(info);
	}
}
