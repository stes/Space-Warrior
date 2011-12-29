package sw.pagent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import sw.client.gcontrol.GameStateChangedEvent;
import sw.client.gcontrol.IGameStateManager;
import sw.client.player.ai.AIPlayer;
import sw.shared.data.entities.shots.IShot;

public class PAgent extends AIPlayer
{
	public static final int ACTIONS = 6;
	public static final double LEARNRATE = 0.5;
	
	ProbabilityDistribution _distribution;
	private HashMap<PState, int[]> _visitedStates;
	private int _oldScore;
	
	public PAgent(IGameStateManager stateManager)
	{
		super(stateManager);
		this.init();
	}
	
	private void init()
	{
		_distribution = new ProbabilityDistribution(ACTIONS);
		_visitedStates = new HashMap<PState, int[]>();
		_oldScore = 0;
	}

	@Override
	public void gameStateChanged(GameStateChangedEvent e)
	{
		super.gameStateChanged(e);
		
//		if (e.getGameWorld().getPlayers().length == 1)
//			return;
		
		// determine current state
		PState state = new PState(this.getDataSet(), this.getGameWorld());
		
		// add state, if new
		this.exploreState(state);
		
		int action = _distribution.sampleAction(state);
		
		// perform action
		this.performAction(state, action);
	}

	private void exploreState(PState state)
	{
		if (!_visitedStates.containsKey(state))
		{
			_visitedStates.put(state, new int[ACTIONS]);
			_distribution.initProbabilities(state);
		}
		_distribution.normalize();
	}
	
	private void performAction(PState state, int action)
	{	
		switch(action)
		{
			case Actions.HALT:
				getCurrentState().setDirection(0);
				getCurrentState().setRotation(0);
				getCurrentState().setShot(0);
				break;
			case Actions.ACCELERATE:
				getCurrentState().setDirection(1);
				break;
			case Actions.TURN_LEFT:
				getCurrentState().setRotation(1);
				break;		
			case Actions.TURN_RIGHT:
				getCurrentState().setRotation(-1);
				break;
			case Actions.SHOOT:
				getCurrentState().setShot(IShot.LASER);
				break;			
			case Actions.MASTER_SHOOT:
				getCurrentState().setShot(IShot.MASTER_LASER);
				break;
		}
		
		this.update();
		getCurrentState().setShot(0);
		
		_visitedStates.get(state)[action]++;
	}
	
	private void updateProbabilities(int reward)
	{
		for (Entry<PState, int[]> stateSet : _visitedStates.entrySet())
		{
			for (int i = 0; i < _distribution.getActions(); i++)
			{
				double oldProb = _distribution.getProbability(i, stateSet.getKey());
				
				double delta = Math.tanh((double)reward * (double)stateSet.getValue()[i] / 200.0);
				if (delta > 0)
					System.out.println("delta: " + delta);
				
				double newProb = oldProb + delta;
//				System.out.println("Updated probability for action " + i + ": "+ newProb);
				_distribution.setProbabilty(i, stateSet.getKey(), newProb);
			}
		}
		_distribution.normalize();
//		System.out.println("new distribution: \n"+_distribution.toString());
		try
		{
			_distribution.save(System.getProperty("user.dir")+"/" + this.getDataSet().getName());
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void newRound(GameStateChangedEvent e)
	{
		int players = getGameWorld().getPlayers().length;
		System.out.println(this.getDataSet().getScore());
		int reward  = 0;
		if (players > 1)
			reward = (this.getDataSet().getScore() - _oldScore) * 100 / players;
		_oldScore = this.getDataSet().getScore();
//		if (e.getWinner().equals(this.getDataSet()))
//			reward += 100;
		System.out.println("got a reward of " + reward);
		this.updateProbabilities(reward);
	}

}
