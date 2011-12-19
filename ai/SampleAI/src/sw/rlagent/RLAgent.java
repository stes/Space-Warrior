package sw.rlagent;

import sw.client.gcontrol.GameStateChangedEvent;
import sw.client.gcontrol.IGameStateManager;
import sw.client.player.ai.AIPlayer;

public class RLAgent extends AIPlayer
{
	private World _world;

	public RLAgent(IGameStateManager stateManager)
	{
		super(stateManager);
		_world = new World();
	}
	
	@Override
	public void gameStateChanged(GameStateChangedEvent e)
	{
		State s = new State(this.getDataSet(), this.getPlayerList());
		_world.exploreState(s);
		
		
	}
	
	@Override
	public void newRound(GameStateChangedEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	
}
