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
package sw.client.player.ai;

import sw.client.gcontrol.IGameStateManager;
import sw.client.player.Player;

/**
 * The basic class for an artificial intelligence player
 * 
 * @author Redix, stes, Abbadonn
 * @version 27.11.2011
 */
public abstract class AIPlayer extends Player
{
	private AIPlayer _self;
	private Thread _actionThread;
	
	public AIPlayer(IGameStateManager stateManager)
	{
		super(stateManager);
		_self = this;
		_actionThread = new Thread()
		{
			@Override
			public void run()
			{
				_self.init();
				while(true)
				{
					_self.tick();
					Thread.yield();
				}
			}
		};
		_actionThread.start();
	}
	
	/**
	 * Initializes the player
	 */
	protected abstract void init();
	
	/**
	 * Called frequently, used to process data and give new instructions
	 * to the game controller
	 */
	protected abstract void tick();
}
