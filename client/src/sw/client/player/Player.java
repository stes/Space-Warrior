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
package sw.client.player;

import sw.client.gcontrol.IGameStateManager;
import sw.shared.data.GameWorld;
import sw.shared.data.PlayerInput;
import sw.shared.data.entities.players.SpaceShip;

/**
 * An abstract player which manages the local player data and supplies the
 * GameController with input
 * 
 * @author Redix, stes, Abbadonn
 * @version 27.11.2011
 */
public abstract class Player
{
	private PlayerInput _currentState;
	private PlayerInput _oldState;
	private SpaceShip _dataSet;

	private IGameStateManager _stateManager;

	/**
	 * Creates a new player given the controller
	 * 
	 * @param gameStateManager
	 *            An instance that supplies the player with information of the
	 *            current game state
	 */
	public Player(String name)
	{
		_currentState = new PlayerInput();
		_oldState = new PlayerInput();
		_dataSet = new SpaceShip(name);
	}
	
	public void setStateManager(IGameStateManager stateManager)
	{
		_stateManager = stateManager;
	}

	/**
	 * @return the player data set
	 */
	public SpaceShip getDataSet()
	{
		return _dataSet;
	}

	/**
	 * @return the state manager assigned to this player
	 */
	public IGameStateManager getStateManager()
	{
		return _stateManager;
	}

	/**
	 * @param data
	 *            the new player data set
	 */
	public void setDataSet(SpaceShip data)
	{
		this._dataSet = data;
	}

	/**
	 * @return the _currentState
	 */
	protected PlayerInput getCurrentInput()
	{
		return _currentState;
	}

	public GameWorld getGameWorld()
	{
		return _stateManager.getGameWorld();
	}

	protected void sendInput()
	{
		if (_stateManager.isReady())
		{
			_stateManager.stateUpdated(this.getCurrentInput());
		}
	}

	/**
	 * @param _currentState
	 *            the _currentState to set
	 */
	protected void setCurrentState(PlayerInput _currentState)
	{
		this._currentState = _currentState;
	}

	protected void update()
	{
		if (!this.getOldState().equals(this.getCurrentInput()))
		{
			this.setOldState(new PlayerInput(this.getCurrentInput()));
			this.sendInput();
		}
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
}
