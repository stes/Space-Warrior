package sw.client.player.ai;

import sw.client.IGameStateManager;
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
