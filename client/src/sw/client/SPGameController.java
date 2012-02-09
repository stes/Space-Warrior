package sw.client;

import sw.client.gcontrol.GameStateChangedEvent;
import sw.shared.GameConstants;
import sw.shared.GameEngine;
import sw.shared.Packettype;
import sw.shared.data.PlayerInput;
import sw.shared.data.entities.GameState;
import sw.shared.data.entities.players.SpaceShip;

/**
 * Controls all gameplay related issues in a singleplayer game
 * 
 * @author Redix, stes
 * @version 25.11.11
 */
public class SPGameController extends GameController
{
	private class SPGameEngine extends GameEngine
	{
		public SPGameEngine()
		{
			super();
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
						snapshot();
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
		
	}
	
	private SPGameEngine _gameEngine;
	private boolean _isReady;
	
	public SPGameController()
	{
		super();
		_gameEngine = new SPGameEngine();
	}
	
	@Override
	public void init()
	{
		super.init();
		_gameEngine.addPlayer(getLocalPlayer().getDataSet().getName(), getLocalPlayer().getDataSet().getImageID(), true);
		_gameEngine.addPlayer("SampleAI", GameConstants.Images.SHIP_1.getID());
		_gameEngine.startGame();
		_isReady = true;
	}

	@Override
	public void stateUpdated(PlayerInput input)
	{
		_gameEngine.playerInput(getLocalPlayer().getDataSet().getName(), input);
		_gameEngine.playerInput("SampleAI", input);
	}
	
	@Override
	public boolean isReady()
	{
		return _isReady;
	}

	public void snapshot()
	{
		this.setGameworld(_gameEngine.getWorld());
		for (SpaceShip pl : getPlayers())
		{
			if (pl.isLocal())
			{
				getLocalPlayer().setDataSet(pl);
			}
		}
		GameState[] state = getWorld().getEntitiesByType(Packettype.SNAP_GAMESTATE, new GameState[] {});
		if (state.length >= 1)
		{
			if (state[0].isNewRoundStarted())
			{
				this.newRound();
			}
		}
		GameStateChangedEvent e = new GameStateChangedEvent(this);
		e.setLocalDataSet(getLocalPlayer().getDataSet());
		// TODO only pass a copy!
		e.setGameWorld(getWorld());
		this.invokeStateChanged(e);
	}
}
