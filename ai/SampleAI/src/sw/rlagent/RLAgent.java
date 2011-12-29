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
