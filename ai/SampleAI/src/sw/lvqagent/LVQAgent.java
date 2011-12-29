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
package sw.lvqagent;

import java.util.ArrayList;

import sw.State;
import sw.client.gcontrol.GameStateChangedEvent;
import sw.client.gcontrol.IGameStateManager;
import sw.client.player.ai.AIPlayer;

public class LVQAgent extends AIPlayer
{
	private ArrayList<State> _visitedStates;

	public LVQAgent(IGameStateManager stateManager)
	{
		super(stateManager);
		_visitedStates = new ArrayList<State>();
	}

	@Override
	public void newRound(GameStateChangedEvent e)
	{}

	@Override
	public void gameStateChanged(GameStateChangedEvent e)
	{
		super.gameStateChanged(e);
		LVQState state = new LVQState(this.getDataSet(), this.getGameWorld());
		if (!_visitedStates.contains(state))
		{
			_visitedStates.add(state);
			System.out.println("added state: " + state.toString());
		}
	}

	private void saveResults()
	{

	}
}
