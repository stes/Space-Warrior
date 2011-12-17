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
package sw.client.player;

import sw.client.gcontrol.IGameStateManager;
import sw.shared.data.PlayerData;
import sw.shared.data.PlayerInput;
import sw.shared.data.PlayerList;

/**
 * An abstract player which manages the local player data and
 * supplies the GameController with input
 * 
 * @author Redix, stes, Abbadonn
 * @version 27.11.2011
 *
 */
public abstract class Player
{
    private PlayerInput _currentState;
    private PlayerInput _oldState;
    private PlayerData _dataSet;
    
    private IGameStateManager _stateManager;
    
    /**
     * Creates a new player given the controller
     * @param gameStateManager
     * An instance that supplies the player with information
     * of the current game state
     */
    public Player(IGameStateManager gameStateManager)
    {
    	_stateManager = gameStateManager;
    	_currentState = new PlayerInput();
    	_oldState = new PlayerInput();
    	_dataSet = new PlayerData("");
    }
    
    protected void sendInput()
    {
    	if (_stateManager.isReady())
    	{
    		_stateManager.stateUpdated(this.getCurrentState());
    	}
    }
    
    protected void update()
    {
        if (!this.getOldState().equals(this.getCurrentState()))
        {
            this.setOldState(new PlayerInput(this.getCurrentState()));
            this.sendInput();
        }
    }
    
	/**
	 * @return the _currentState
	 */
	protected PlayerInput getCurrentState()
	{
		return _currentState;
	}
	/**
	 * @param _currentState the _currentState to set
	 */
	protected void setCurrentState(PlayerInput _currentState)
	{
		this._currentState = _currentState;
	}
	/**
	 * @return the _oldState
	 */
	private PlayerInput getOldState()
	{
		return _oldState;
	}

	private void setOldState(PlayerInput oldState)
	{
		this._oldState = oldState;
	}
	/**
	 * @return the player data set
	 */
	public PlayerData getDataSet()
	{
		return _dataSet;
	}
	
	protected PlayerList getPlayerList()
	{
		return _stateManager.getPlayerList();
	}
	/**
	 * @param data the new player data set
	 */
	public void setDataSet(PlayerData data)
	{
		this._dataSet = data;
	}
}
