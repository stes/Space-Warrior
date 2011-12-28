package sw.lvqagent;

import java.util.ArrayList;

import sw.State;
import sw.client.gcontrol.GameStateChangedEvent;
import sw.client.gcontrol.IGameStateManager;
import sw.client.player.ai.AIPlayer;

public class LVQAgent extends AIPlayer
{
	private ArrayList<State> _visitedStates;
	
	public LVQAgent(IGameStateManager stateManager)
	{
		super(stateManager);
		_visitedStates = new ArrayList<State>();
	}
	
	@Override
	public void newRound(GameStateChangedEvent e)
	{
	}

	@Override
	public void gameStateChanged(GameStateChangedEvent e)
	{
		super.gameStateChanged(e);
		LVQState state = new LVQState(this.getDataSet(), this.getGameWorld());
		if (!_visitedStates.contains(state))
		{
			_visitedStates.add(state);
			System.out.println("added state: " + state.toString());
		}
	}
	
	private void saveResults()
	{
		
	}
}
