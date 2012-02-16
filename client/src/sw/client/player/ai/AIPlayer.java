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
package sw.client.player.ai;

import sw.client.control.IGameController;
import sw.client.events.gcontrol.GameStateChangedEvent;
import sw.client.events.gcontrol.IGameStateChangedListener;
import sw.client.player.Player;

/**
 * The basic class for an artificial intelligence player / bot
 * 
 * @author Redix, stes, Abbadonn
 * @version 27.11.2011
 */
public abstract class AIPlayer extends Player implements IGameStateChangedListener
{
	/**
	 * Creates a new ai player given the controller
	 * 
	 * @param gameStateManager
	 *            An instance that supplies the player with information of the
	 *            current game state
	 */
	public AIPlayer(String name)
	{
		super(name);
	}
	
	public AIPlayer(String name, int imageID)
	{
		super(name, imageID);
	}
	
	public void setStateManager(IGameController stateManager)
	{
		super.setStateManager(stateManager);
		stateManager.addGameStateChangedListener(this);
	}

	@Override
	/**
	 * Invoked after the game state has changed
	 * When overriding, always call super.gameStateChanged(e) first!
	 */
	public void gameStateChanged(GameStateChangedEvent e)
	{}

	@Override
	/**
	 * Invoked after the game controller initialized the players.
	 * When overriding, always call super.playerInit(e) first!
	 */
	public void playerInit(GameStateChangedEvent e)
	{}
}
