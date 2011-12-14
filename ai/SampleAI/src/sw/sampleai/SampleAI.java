package sw.sampleai;

import java.util.Random;

import sw.client.gcontrol.GameStateChangedEvent;
import sw.client.player.ai.AIPlayer;
import sw.shared.GameConstants;
import sw.shared.data.PlayerDataSet;
import sw.shared.data.Shot;

public class SampleAI extends AIPlayer
{
	private static Random _random = new Random(System.currentTimeMillis());
		
	private int _hold;
	
	public SampleAI()
	{
		super();
		System.out.println("init ai player");
		this.getCurrentState().setDirection(1);
	}
	
	@Override
	public void gameStateChanged(GameStateChangedEvent e)
	{
		boolean turn = false;
		if (getDataSet().xPosition() < 150)
		{
			turn = true;
			this.getCurrentState().setRotation((int)Math.signum(90-getDataSet().getDirection()));
		}
		if (getDataSet().yPosition() < 150)
		{
			turn = true;
			this.getCurrentState().setRotation(-(int)Math.signum(180-getDataSet().getDirection()));
		}
		if (getDataSet().xPosition() > GameConstants.PLAYING_FIELD_WIDTH-150)
		{
			turn = true;
			this.getCurrentState().setRotation((int)Math.signum(270-getDataSet().getDirection()));
		}
		if (getDataSet().yPosition() > GameConstants.PLAYING_FIELD_HEIGHT-150)
		{
			turn = true;
			this.getCurrentState().setRotation((int)Math.signum(180-getDataSet().getDirection()));
		}
		if (!turn && _hold == 0)
		{
			if (_random.nextDouble() > 0.9)
			{
				_hold = 1 + _random.nextInt(9);
				this.getCurrentState().setRotation(_random.nextInt(2)*2-1);
			}
			else
				this.getCurrentState().setRotation(0);
			this.getCurrentState().setDirection(1);
		}
		else if (_hold > 0)
		{
			_hold--;
		}
		Shot shot = new Shot(this.getDataSet().getPosition(), this.getDataSet().getDirection(), false);
		Shot mshot = new Shot(this.getDataSet().getPosition(), this.getDataSet().getDirection(), true);
		for (int i = 0; i < this.getPlayerList().size(); i++)
		{
			PlayerDataSet ds = this.getPlayerList().dataAt(i);
			if (ds == null || ds.equals(this.getDataSet()))
				continue;
			if (shot.distanceTo(ds.getPosition()) < GameConstants.PLAYER_SIZE / 2)
			{
				this.getCurrentState().setShot(1);
			}
			else if (mshot.distanceTo(ds.getPosition()) < GameConstants.PLAYER_SIZE / 2)
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
