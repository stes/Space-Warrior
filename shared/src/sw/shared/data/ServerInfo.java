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
package sw.shared.data;

import java.net.InetSocketAddress;

import sw.shared.GameConstants;
import sw.shared.Packer;
import sw.shared.Unpacker;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class ServerInfo
{
	public static ServerInfo unpack(Unpacker p)
	{
		return new ServerInfo(p.readUTF(), p.readShort(), p.readShort());
	}
	private String _serverName;
	private int _maxPlayers;
	
	private int _numPayers;

	private InetSocketAddress _addr;
	
	public ServerInfo()
	{
		this("Server", GameConstants.MAX_PLAYERS, 0);
	}
	
	public ServerInfo(String name, int max, int num)
	{
		_serverName = name;
		_maxPlayers = max;
		_numPayers = num;
	}

	public InetSocketAddress getAddress()
	{
		return _addr;
	}

	public int getMaxPlayers()
	{
		return _maxPlayers;
	}

	public int getNumPayers()
	{
		return _numPayers;
	}

	public String getServerName()
	{
		return _serverName;
	}

	public Packer pack()
	{
		Packer info = new Packer((byte)0);
        info.writeUTF(_serverName);
        info.writeShort(_maxPlayers);
        info.writeShort(_numPayers);
        return info;
	}

	public void setAddress(InetSocketAddress addr)
	{
		_addr = addr;
	}
	
	public void setMaxPlayers(int maxPlayers)
	{
		_maxPlayers = maxPlayers;
	}

	public void setNumPayers(int numPayers)
	{
		_numPayers = numPayers;
	}
	
	public void setServerName(String serverName)
	{
		_serverName = serverName;
	}
}
