package sw.rlagent;

import sw.client.gcontrol.GameStateChangedEvent;
import sw.client.gcontrol.IGameStateManager;
import sw.client.player.ai.AIPlayer;
import sw.shared.data.PlayerInput;

public class RLAgent extends AIPlayer
{
	private World _world;

	public static RLAgent _self;

	public RLAgent(IGameStateManager stateManager)
	{
		super(stateManager);
		_self = this;
		_world = new World();
	}

	@Override
	public void gameStateChanged(GameStateChangedEvent e)
	{
		SimpleState currentState = new SimpleState(this.getDataSet(),
				this.getStateManager().getPlayerList());
		_world.exploreState(currentState);

		double bestValue = Integer.MIN_VALUE;
		Actions bestAction = null;
		for (Actions a : Actions.values())
		{
			SimpleState s = currentState.successor(a);
			if (s.value() > bestValue)
			{
				bestValue = s.value();
				bestAction = a;
			}
		}
		this.setCurrentState(applyAction(bestAction));
		this.update();
	}

	public PlayerInput applyAction(Actions a)
	{
		PlayerInput i = new PlayerInput(getCurrentState());
		switch (a)
		{
			case ACCELERATE:
			{
				i.setDirection(1);
				break;
			}
			case SHOOT:
			{
				i.setShot(1);
				break;
			}
			case TURN_LEFT:
			{
				i.setRotation(1);
				break;
			}
			case TURN_RIGHT:
			{
				i.setRotation(-1);
				break;
			}
		}
		return i;
	}

	@Override
	public void newRound(GameStateChangedEvent e)
	{
		// TODO Auto-generated method stub

	}

}
