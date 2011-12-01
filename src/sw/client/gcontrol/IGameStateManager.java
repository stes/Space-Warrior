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
package sw.client.gcontrol;

import sw.client.player.Player;
import sw.shared.data.PlayerInput;
import sw.shared.data.PlayerList;

public interface IGameStateManager
{
	/**
	 * @return a list of all players currently participating in the game
	 */
	public PlayerList getPlayerList();
	
	public Player getLocalPlayer();
	
	public boolean isReady();
	
	//TODO further methods that can be generalized for easy connection
	// between player and controller
	
	public void stateUpdated(PlayerInput input);
}
