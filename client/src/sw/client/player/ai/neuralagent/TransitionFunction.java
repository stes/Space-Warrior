/*******************************************************************************
 * Copyright (c) 2012 stes
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl.getInput()-2.0.html
 ******************************************************************************/
package sw.client.player.ai.neuralagent;

import sw.shared.data.GameWorld;
import sw.shared.data.entities.players.SpaceShip;
import sw.shared.data.entities.shots.IWeapon.WeaponType;




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
		GameWorld world = _agent.getGameWorld().copy();
		SpaceShip pl = (SpaceShip) world.getEntityByID(_agent.getDataSet().getID());
		
		switch (action)
		{
			case NONE:
				pl.getInput().setDirection(0);
				break;
			case ACCELERATE:
				pl.getInput().setDirection(1);
				break;
			case SHOOT:
				pl.getInput().setShot(WeaponType.LASER.getID());
				break;
			case TURN_LEFT:
				pl.getInput().setRotation(1);
				break;
			case TURN_RIGHT:
				pl.getInput().setRotation(-1);
				break;
			default:
				throw new IllegalStateException();
		}
		
		for (int i = 0; i < 10; i++)
		{
			world.tick();
		}
		
		return new DistanceState(pl, world);
	}
}
