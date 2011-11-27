package sw.client.player;

import sw.client.IGameStateManager;

/**
 * The basic class for an artificial intelligence player
 * 
 * @author Redix, stes, Abbadonn
 * @version 27.11.2011
 */
public abstract class AIPlayer extends Player
{
	public AIPlayer(IGameStateManager stateManager)
	{
		super(stateManager);
	}
}
