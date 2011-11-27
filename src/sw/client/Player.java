package sw.client;

import sw.shared.PlayerDataSet;
import sw.shared.PlayerInput;

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
    private PlayerDataSet _dataSet;
    
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
	protected PlayerInput getOldState()
	{
		return _oldState;
	}
	/**
	 * @param _oldState the _oldState to set
	 */
	protected void setOldState(PlayerInput _oldState)
	{
		this._oldState = _oldState;
	}
	/**
	 * @return the _localPlayer
	 */
	protected PlayerDataSet getDataSet()
	{
		return _dataSet;
	}
	/**
	 * @param _localPlayer the _localPlayer to set
	 */
	protected void setDataSet(PlayerDataSet _localPlayer)
	{
		this._dataSet = _localPlayer;
	}

	public IGameStateManager getStateManager()
	{
		return _stateManager;
	}
}
