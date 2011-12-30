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
package sw.shared.data.entities;

import sw.shared.Packettype;
import sw.shared.net.Packer;
import sw.shared.net.Unpacker;

public class GameState extends Entity
{
	private final static byte STATE_RUNNING = 0;
	//private final static byte STATE_PAUSEDE = 1; // not used right now
	private final static byte STATE_NEW_ROUND = 2;
	
	private byte _state;
	
	public GameState()
	{
		super(Packettype.SNAP_GAMESTATE);
		_state = STATE_NEW_ROUND;
	}

	@Override
	public void tick()
	{
	}
	
	@Override
	public void snap(Packer p, String name)
	{
		super.snap(p, name);
		p.writeByte(_state);
		
		if(_state == STATE_NEW_ROUND)
			_state = STATE_RUNNING;
	}
	
	@Override
	public void fromSnap(Unpacker p)
	{
		super.fromSnap(p);
		_state = p.readByte();
	}
	
	public void startNewRound()
	{
		System.out.println("New round");
		_state = STATE_NEW_ROUND;
	}
	
	public boolean isNewRoundStarted()
	{
		return _state == STATE_NEW_ROUND;
	}
}
