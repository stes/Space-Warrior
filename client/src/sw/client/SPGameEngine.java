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
package sw.client;

import java.util.ArrayList;

import sw.shared.GameConstants;
import sw.shared.GameEngine;
import sw.shared.data.entities.players.SpaceShip;

public class SPGameEngine extends GameEngine
{
	private ArrayList<SPGameController> _controller;
	private boolean _isReady;
	
	public SPGameEngine()
	{
		super();
		_isReady = false;
		_controller = new ArrayList<SPGameController>();
		new Thread()
		{
			private long _lastTick = System.currentTimeMillis();
			
			@Override
			public void run()
			{
				while(!_isReady)
					Thread.yield();
				while(true)
				{
					while (System.currentTimeMillis() - _lastTick < GameConstants.TICK_INTERVAL)
						;
					tick();
					for (SPGameController c : _controller)
					{
						c.snapshot();
					}
					_lastTick = System.currentTimeMillis();
					Thread.yield();
				}
			}
		}.start();
	}
	
	@Override
	public void invokePlayerWon(SpaceShip pl)
	{
		// TODO Auto-generated method stub
		
	}
	
	public void addListener(SPGameController c)
	{
		_controller.add(c);
	}
	
	public void start()
	{
		_isReady = true;
	}
	
}
