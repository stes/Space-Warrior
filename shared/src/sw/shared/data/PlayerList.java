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

import java.io.Serializable;

import sw.shared.Packer;
import sw.shared.Packettype;
import sw.shared.Unpacker;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class PlayerList implements Serializable
{
	private static final long serialVersionUID = 4378922344210371238L;
	
	private PlayerData[] _list;

	/**
	 * Creates a new Playerlist out of a paket
	 * 
	 * @param p
	 *            The paket
	 * @return The new instance
	 */
	private static PlayerList fromSnapshot(Unpacker p)
	{
		PlayerList list = new PlayerList(p.readInt());
		for (int i = 0; i < list.size(); i++)
		{
			list.insert(PlayerData.fromSnapshot(p));
		}
		return list;
	}

	/**
	 * creates a new playerlist of the specified size
	 * 
	 * @param size
	 *            size of the list
	 */
	public PlayerList(int size)
	{
		_list = new PlayerData[size];
	}

	/**
	 * @return list
	 */
	public PlayerData dataAt(int index)
	{
		return _list[index];
	}

	/**
	 * Writes the list of players in a snapshot and returns
	 * 
	 * @return the packet
	 */
	public Packer createSnapshot(String localName)
	{
		Packer p = new Packer(Packettype.SV_SNAPSHOT);
		p.writeInt(this.count(false));
		for (PlayerData s : _list)
		{
			if (s != null)
			{
				s.snap(p, localName);
			}
		}
		return p;
	}

	/**
	 * adds a data record to the list
	 * 
	 * @param player
	 *            the data record
	 * @param input
	 *            the current player input, null for default
	 */
	public void insert(PlayerData player)
	{
		int index = findEmptyPlace();
		if (index == -1)
		{
			throw new ArrayIndexOutOfBoundsException("no free slot in the list");
		}
		_list[index] = player;
	}

	/**
	 * returns the current size of the list
	 * 
	 * @return the size
	 */
	public int size()
	{
		return _list.length;
	}

	/**
	 * the player data is deleted from the list
	 */
	public void clear()
	{
		_list = new PlayerData[_list.length];
	}

	/**
	 * Updated the packet
	 * 
	 * @param p
	 *            the packet
	 */
	public void update(Unpacker p)
	{
		PlayerList liste = PlayerList.fromSnapshot(p);
		_list = liste._list;
	}

	/**
	 * deletes the player with the given name from the list
	 * 
	 * @param name
	 *            name of the player
	 * @return the data record that belongs to the player or null if no player
	 *         is found
	 */
	public boolean remove(String name)
	{
		for (int i = 0; i < _list.length; i++)
		{
			if (_list[i] != null && _list[i].getName().equals(name))
			{
				_list[i] = null;
				return true;
			}
		}
		return false;
	}

	/**
	 * sets the player input
	 * 
	 * @param name
	 *            name of the player
	 * @param input
	 *            player input
	 * @return the data record that belongs to the player or null if no player
	 *         is found
	 */
	public boolean setInput(String name, PlayerInput input)
	{
		for (int i = 0; i < _list.length; i++)
		{
			PlayerData s = _list[i];
			if (s != null && s.getName().equals(name))
			{
				s.setInput(input);
				return true;
			}
		}
		return false;
	}

	/**
	 * looking for the player with the given name in the list
	 * 
	 * @param name
	 *            name of the player
	 * @return the data record that belongs to the player or null if no player
	 *         is found
	 */
	public PlayerData find(String name)
	{
		for (PlayerData s : _list)
		{
			if (s != null && s.getName().equals(name))
				return s;
		}
		return null;
	}

	/**
	 * counts the number of occupied elements in the list
	 * 
	 * @return number of occupied elements
	 */
	public int count(boolean alive)
	{
		int n = 0;
		for (int i = 0; i < _list.length; i++)
		{
			if (_list[i] != null && (_list[i].isAlive() || !alive))
			{
				n++;
			}
		}
		return n;
	}

	/**
	 * @return next free space in the list
	 * @return -1 , if no free space is available
	 */
	private int findEmptyPlace()
	{
		for (int i = 0; i < _list.length; i++)
		{
			if (_list[i] == null)
			{
				return i;
			}
		}
		return -1;
	}
}
