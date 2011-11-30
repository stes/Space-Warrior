package sw.client.player.ai;

import sw.client.IGameStateManager;
import sw.shared.GameConstants;
import sw.shared.data.PlayerInput;

public class SampleAIPlayer extends AIPlayer
{
	public SampleAIPlayer(IGameStateManager stateManager)
	{
		super(stateManager);
	}

	@Override
	protected void tick()
	{
		boolean turn = false;
		if (getDataSet().xPosition() < 150)
		{
			turn = true;
			this.getCurrentState().setzeDrehung((int)Math.signum(90-getDataSet().richtung()));
		}
		if (getDataSet().yPosition() < 150)
		{
			turn = true;
			this.getCurrentState().setzeDrehung(-(int)Math.signum(180-getDataSet().richtung()));
		}
		if (getDataSet().xPosition() > GameConstants.PLAYING_FIELD_WIDTH-150)
		{
			turn = true;
			this.getCurrentState().setzeDrehung((int)Math.signum(270-getDataSet().richtung()));
		}
		if (getDataSet().yPosition() > GameConstants.PLAYING_FIELD_HEIGHT-150)
		{
			turn = true;
			this.getCurrentState().setzeDrehung((int)Math.signum(180-getDataSet().richtung()));
		}
		if (!turn)
		{
			this.getCurrentState().setzeDrehung(0);
		}
		this.update();
	}

	@Override
	protected void init()
	{
		System.out.println(getDataSet().richtung());
		PlayerInput inp = getCurrentState();
		inp.setzeBewegung(1);
		inp.setzeDrehung(0);
		inp.setzeSchuss(0);
	}

}
