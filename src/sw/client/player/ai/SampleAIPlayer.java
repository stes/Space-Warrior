/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 * Copyright (C) 2011 Redix stes Abbadonn
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
