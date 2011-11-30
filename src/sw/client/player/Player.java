package sw.client.player;

import sw.client.IGameStateManager;
import sw.shared.data.PlayerDataSet;
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
    	_currentState = new PlayerInput();
    	_oldState = new PlayerInput();
    	_dataSet = new PlayerDataSet();
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
	protected PlayerDataSet getDataSet()
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
	public void setDataSet(PlayerDataSet data)
	{
		this._dataSet = data;
	}
}
