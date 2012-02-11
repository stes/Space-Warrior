package sw.client.player.ai.fagent;

import java.util.Random;

import sw.client.gcontrol.GameStateChangedEvent;
import sw.shared.data.entities.shots.IWeapon.WeaponType;

public class FollowAgent extends RLAgent
{
	private int _currentWeapon;
	private Thread _moveThread;
	private int _updates;
	
	public FollowAgent(String name)
	{
		super(name);
		this.initFunctions();
		_currentWeapon = WeaponType.LASER.getID();
	}

	@Override
	public void newRound(GameStateChangedEvent e)
	{
		if (_moveThread != null && _moveThread.isAlive())
			return;
		_moveThread = new Thread(){
			public void run()
			{
				while(true)
				{
					while (_updates < 5)
						Thread.yield();
					move();

					_updates = 0;
					Thread.yield();
				}
			}
		};
		_moveThread.start();
	}

	private static Random _random = new Random(System.currentTimeMillis());

	public void move()
	{
		switch (this.getPolicy().getAction(this.getCurrentState(), this.getTransitionFunction()))
		{
			case NONE:
				getCurrentInput().setDirection(0);
				break;
			case ACCELERATE:
				getCurrentInput().setDirection(1);
				break;
			case SHOOT:
				getCurrentInput().setShot(WeaponType.LASER.getID());
				break;
			case TURN_LEFT:
				getCurrentInput().setRotation(1);
				break;
			case TURN_RIGHT:
				getCurrentInput().setRotation(-1);
				break;
			default:
				throw new IllegalStateException();
		}
		
		if (_random.nextDouble() < 0.01)
		{
			this.getCurrentInput().setShot(_currentWeapon);
			System.out.println("shot"+getDataSet().getAmmo());
		}
		if (_random.nextDouble() < 0.001)
		{
			WeaponType[] types = WeaponType.values();
			_currentWeapon = types[_random.nextInt(types.length)].getID();
		}
		
		this.update();
		// if (_player.isTerminated())
		// {
		// getValueFunction().setTerminal(GridWorld.stateID(_player.getX(),
		// _player.getY()),
		// _player.getTerminalReward());
		// this.iteration();
		// }
	}

	private IState getCurrentState()
	{
		return new DistanceState(getDataSet(), getGameWorld());
	}

	private void initFunctions()
	{
		this.setRewardFunction(new RewardFunction());
		this.setTransitionFunction(new TransitionFunction(this));
		this.setValueFunction(new DistanceValueFunction());

		// for (int x = 0; x < _worldWidth; x++)
		// {
		// for (int y = 0; y < _worldHeight; y++)
		// {
		// // TODO decide whether this is possible in other problems as well
		// if (world.isTerminal(x, y))
		// {
		// this.getRewardFunction().setReward(GridWorld.stateID(x, y),
		// world.getValue(x, y));
		// this.getValueFunction().setTerminal(GridWorld.stateID(x, y),
		// world.getValue(x, y));
		// }
		// else
		// {
		// this.getRewardFunction().setReward(GridWorld.stateID(x, y),
		// GridWorld.STEPCOST);
		// this.getValueFunction().setValue(GridWorld.stateID(x, y),
		// _random.nextInt(100) - 50);
		// }
		// }
		// }

		this.setPolicy(new Policy(this.getValueFunction()));
	}

	/*
	 * (non-Javadoc)
	 * @see sw.client.player.ai.AIPlayer#gameStateChanged(sw.client.gcontrol.
	 * GameStateChangedEvent)
	 */
	@Override
	public void gameStateChanged(GameStateChangedEvent e)
	{
		super.gameStateChanged(e);
		_updates++;
	}

	@Override
	public void iteration()
	{
		// TODO Auto-generated method stub
		
	}
}
