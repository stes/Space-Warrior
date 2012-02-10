package sw.client.player.ai.neuralagent;

import java.util.Random;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import sw.client.gcontrol.GameStateChangedEvent;
import sw.client.gcontrol.IGameStateManager;

public class NeuralAgent extends RLAgent
{
	public NeuralAgent(IGameStateManager stateManager)
	{
		super(stateManager);
	}

	@Override
	public void newRound(GameStateChangedEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	
	private static Random _random = new Random(System.currentTimeMillis());

	public void iteration()
	{
		((NeuralValueFunction) this.getValueFunction()).valueIteration((RewardFunction) this.getRewardFunction(),
				(TransitionFunction) this.getTransitionFunction());
	}

	public void move()
	{
		//if (isTerminated())
		//	return;
		
//		switch (this.getPolicy().getAction(this.getCurrentState()),
//				this.getTransitionFunction()))
//		{
//			case Actions.DOWN:
//				_player.move(0, 1);
//				break;
//			case Actions.UP:
//				_player.move(0, -1);
//				break;
//			case Actions.RIGHT:
//				_player.move(1, 0);
//				break;
//			case Actions.LEFT:
//				_player.move(-1, 0);
//				break;
//			default:
//				throw new IllegalStateException();
//		}
//		if (_player.isTerminated())
//		{
//			getValueFunction().setTerminal(GridWorld.stateID(_player.getX(), _player.getY()),
//					_player.getTerminalReward());
//			this.iteration();
//		}
	}

	private IState getCurrentState()
	{
		throw new NotImplementedException();
	}

	private void initFunctions()
	{
		this.setRewardFunction(new RewardFunction());
		this.setTransitionFunction(new TransitionFunction(this));
		this.setValueFunction(new NeuralValueFunction());

//		for (int x = 0; x < _worldWidth; x++)
//		{
//			for (int y = 0; y < _worldHeight; y++)
//			{
//				// TODO decide whether this is possible in other problems as well
//				 if (world.isTerminal(x, y))
//				 {
//					 this.getRewardFunction().setReward(GridWorld.stateID(x, y),
//							 world.getValue(x, y));
//					 this.getValueFunction().setTerminal(GridWorld.stateID(x, y),
//							 world.getValue(x, y));
//				 }
//				 else
//				{
//					this.getRewardFunction().setReward(GridWorld.stateID(x, y), GridWorld.STEPCOST);
//					this.getValueFunction().setValue(GridWorld.stateID(x, y),
//							_random.nextInt(100) - 50);
//				}
//			}
//		}

		this.setPolicy(new Policy(this.getValueFunction()));
	}

	@Override
	public double getValue(int x, int y)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see sw.client.player.ai.AIPlayer#gameStateChanged(sw.client.gcontrol.GameStateChangedEvent)
	 */
	@Override
	public void gameStateChanged(GameStateChangedEvent e)
	{
		// TODO Auto-generated method stub
		super.gameStateChanged(e);
	}
}
