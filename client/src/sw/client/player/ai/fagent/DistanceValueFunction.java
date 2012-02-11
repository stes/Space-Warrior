/*******************************************************************************
 * Copyright (c) 2012 stes
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package sw.client.player.ai.fagent;

/**
 * Value function which uses a neural network for state value mapping
 * 
 * @author stes
 * @version 14.01.2012
 */
public class DistanceValueFunction implements IValueFunction
{

	public DistanceValueFunction()
	{
	}

	@Override
	public double getValue(IState state)
	{
		return -(state.getFeature(0) * 0.95 + 0.05 * state.getFeature(1));
	}

	@Override
	public void setValue(IState state, double i)
	{
		;
	}
}
