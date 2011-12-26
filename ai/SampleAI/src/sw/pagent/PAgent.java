package sw.pagent;

import java.util.ArrayList;

import sw.client.gcontrol.GameStateChangedEvent;
import sw.client.gcontrol.IGameStateManager;
import sw.client.player.ai.AIPlayer;

public class PAgent extends AIPlayer
{
	public static final int ACTIONS = 5;
	public static final int STATES = 50;
	
	ProbabilityDistribution _distribution;
	private ArrayList<PState> _visitedStates;
	
	public PAgent(IGameStateManager stateManager)
	{
		super(stateManager);
		this.init();
	}
	
	private void init()
	{
		_distribution = new ProbabilityDistribution(ACTIONS);
		_distribution.init();
		_visitedStates = new ArrayList<PState>();
	}

	@Override
	public void gameStateChanged(GameStateChangedEvent e)
	{
		super.gameStateChanged(e);
		
		if (e.getGameWorld().getPlayers().length == 1)
			return;
		
		PState state = new PState(this.getDataSet(), this.getGameWorld());
		state.showAngle();
		if (!_visitedStates.contains(state))
		{
			_visitedStates.add(state);
		}
	}

	@Override
	public void newRound(GameStateChangedEvent e)
	{
		// TODO Auto-generated method stub
		
	}

}
