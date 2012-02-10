/*******************************************************************************
 * Copyright (c) 2012 stes
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package sw.client.player.ai.neuralagent;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import sw.shared.data.GameWorld;
import sw.shared.data.entities.players.SpaceShip;


/**
 * @author stes
 * @version 02.01.2012
 */
public class TransitionFunction implements ITransitionFunction
{
	private final NeuralAgent _agent;
	
	public TransitionFunction(NeuralAgent agent)
	{
		_agent = agent;
	}

	@Override
	public IState getSuccessor(IState state, Action action)
	{
		GameWorld world = _agent.getGameWorld();

		// TODO predict and return new state
		for (SpaceShip player : world.getPlayers())
		{
			
		}

		throw new NotImplementedException();
	}
}
