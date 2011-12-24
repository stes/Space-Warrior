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

import java.awt.Point;
import java.util.Iterator;
import java.util.Vector;

import sw.shared.Packer;
import sw.shared.Packettype;
import sw.shared.Unpacker;
import sw.shared.data.entities.Shot;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class GameWorld
{
	private Vector<Entity> _entities;

	public GameWorld()
	{
		_entities = new Vector<Entity>();
	}

	public void clear()
	{
		_entities.clear();
	}

	public void fromSnap(Unpacker p)
	{
		Vector<Entity> tmp = new Vector<Entity>();

		int size = p.readInt();
		for (int i = 0; i < size; i++)
		{
			Entity newEnt;
			// move this somewhere else?
			byte type = p.readByte();
			if (type == Packettype.SNAP_PLAYERDATA)
				newEnt = new PlayerData("");
			else if (type == Packettype.SNAP_SHOT)
				newEnt = new Shot(new Point.Double(0, 0), 0);
			else
				return;
			newEnt.fromSnap(p);
			tmp.add(newEnt);
		}

		_entities = tmp;
	}

	public Entity[] getAllEntities()
	{
		return _entities.toArray(new Entity[0]);
	}

	public <T> T[] getEntitiesByType(byte type, T[] a)
	{
		Vector<Entity> tmp = new Vector<Entity>();
		for (Entity ent : _entities)
		{
			if (ent.getType() == type)
				tmp.add(ent);
		}
		return tmp.toArray(a);
	}

	public PlayerData[] getPlayers()
	{
		return this.getEntitiesByType(Packettype.SNAP_PLAYERDATA, new PlayerData[] {});
	}

	public void insert(Entity e)
	{
		e.setWorld(this);
		_entities.add(e);
	}

	public void remove(Entity e)
	{
		_entities.remove(e);
	}

	public void snap(Packer p, String name)
	{
		p.writeInt(_entities.size());

		for (Entity ent : _entities)
		{
			ent.snap(p, name);
		}
	}

	public void tick()
	{
		for (Iterator<Entity> i = _entities.iterator(); i.hasNext();)
		{
			while (i.hasNext() && i.next().isDestroyed())
				i.remove();
		}

		for (int i = 0; i < _entities.size(); i++)
		{
			_entities.get(i).tick();
		}
	}
}
