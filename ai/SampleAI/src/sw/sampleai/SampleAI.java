package sw.sampleai;

import java.util.Random;

import sw.client.gcontrol.GameStateChangedEvent;
import sw.client.gcontrol.IGameStateManager;
import sw.client.player.ai.AIPlayer;
import sw.shared.GameConstants;
import sw.shared.data.entities.players.SpaceShip;
import sw.shared.data.entities.shots.LaserBeam;

public class SampleAI extends AIPlayer
{
	private static Random _random = new Random(System.currentTimeMillis());

	private int _hold;

	public SampleAI(IGameStateManager stateManager)
	{
		super(stateManager);
		System.out.println("init ai player");
		this.getCurrentState().setDirection(1);
	}

	@Override
	public void gameStateChanged(GameStateChangedEvent e)
	{
		boolean turn = false;
		if (getDataSet().getPosition().x < 150)
		{
			turn = true;
			this.getCurrentState().setRotation((int) Math.signum(90 - getDataSet().getDirection()));
		}
		if (getDataSet().getPosition().y < 150)
		{
			turn = true;
			this.getCurrentState().setRotation(-(int) Math.signum(180 - getDataSet().getDirection()));
		}
		if (getDataSet().getPosition().x > GameConstants.PLAYING_FIELD_WIDTH - 150)
		{
			turn = true;
			this.getCurrentState().setRotation((int) Math.signum(270 - getDataSet().getDirection()));
		}
		if (getDataSet().getPosition().y > GameConstants.PLAYING_FIELD_HEIGHT - 150)
		{
			turn = true;
			this.getCurrentState().setRotation((int) Math.signum(180 - getDataSet().getDirection()));
		}
		if (!turn && _hold == 0)
		{
			if (_random.nextDouble() > 0.9)
			{
				_hold = 1 + _random.nextInt(9);
				this.getCurrentState().setRotation(_random.nextInt(2) * 2 - 1);
			}
			else
				this.getCurrentState().setRotation(0);
			this.getCurrentState().setDirection(1);
		}
		else if (_hold > 0)
		{
			_hold--;
		}
		LaserBeam laserBeam = new LaserBeam(this.getDataSet().getX(),
				this.getDataSet().getY(),
				this.getDataSet().getDirection(),
				this.getDataSet());
		LaserBeam mshot = new LaserBeam(this.getDataSet().getX(),
				this.getDataSet().getY(),
				this.getDataSet().getDirection(),
				this.getDataSet(),
				true);
		for (SpaceShip d : this.getStateManager().getPlayerList())
		{
			if (d.equals(this.getDataSet()))
				continue;
			if (laserBeam.distanceTo(d.getPosition()) < GameConstants.PLAYER_SIZE / 2)
			{
				this.getCurrentState().setShot(1);
			}
			else if (mshot.distanceTo(d.getPosition()) < GameConstants.PLAYER_SIZE / 2)
			{
				this.getCurrentState().setShot(2);
			}
		}
		this.update();
		this.getCurrentState().setShot(0);
	}

	@Override
	public void newRound(GameStateChangedEvent arg0)
	{
		this.getCurrentState().setDirection(1);
	}
}
