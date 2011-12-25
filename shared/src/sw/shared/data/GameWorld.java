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

import java.util.Iterator;
import java.util.Vector;

import sw.shared.Packettype;
import sw.shared.data.entities.IEntity;
import sw.shared.data.entities.LaserBeam;
import sw.shared.data.entities.SpaceShip;
import sw.shared.net.Packer;
import sw.shared.net.Unpacker;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class GameWorld
{
	private Vector<IEntity> _entities;

	public GameWorld()
	{
		_entities = new Vector<IEntity>();
	}

	public void clear()
	{
		_entities.clear();
	}

	public void fromSnap(Unpacker p)
	{
		Vector<IEntity> tmp = new Vector<IEntity>();

		int size = p.readInt();
		for (int i = 0; i < size; i++)
		{
			IEntity newEnt;
			// TODO move this somewhere else?
			byte type = p.readByte();
			if (type == Packettype.SNAP_PLAYERDATA)
				newEnt = new SpaceShip("");
			else if (type == Packettype.SNAP_SHOT)
				newEnt = new LaserBeam(0, 0, 0);
			else
				return;
			newEnt.fromSnap(p);
			tmp.add(newEnt);
		}

		_entities = tmp;
	}

	public IEntity[] getAllEntities()
	{
		return _entities.toArray(new IEntity[0]);
	}

	public <T> T[] getEntitiesByType(byte type, T[] a)
	{
		Vector<IEntity> tmp = new Vector<IEntity>();
		for (IEntity ent : _entities)
		{
			if (ent.getType() == type)
				tmp.add(ent);
		}
		return tmp.toArray(a);
	}

	public SpaceShip[] getPlayers()
	{
		return this.getEntitiesByType(Packettype.SNAP_PLAYERDATA, new SpaceShip[] {});
	}

	public void insert(IEntity e)
	{
		e.setWorld(this);
		_entities.add(e);
	}

	public void remove(IEntity e)
	{
		_entities.remove(e);
	}

	public void snap(Packer p, String name)
	{
		p.writeInt(_entities.size());

		for (IEntity ent : _entities)
		{
			ent.snap(p, name);
		}
	}

	public void tick()
	{
		for (Iterator<IEntity> i = _entities.iterator(); i.hasNext();)
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
