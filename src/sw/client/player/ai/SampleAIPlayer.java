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

import sw.client.gcontrol.IGameStateManager;
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
		if (!turn)
		{
			this.getCurrentState().setRotation(0);
		}
		this.update();
	}

	@Override
	protected void init()
	{
		System.out.println(getDataSet().getDirection());
		PlayerInput inp = getCurrentState();
		inp.setDirection(1);
		inp.setRotation(0);
		inp.setShot(0);
	}

}
