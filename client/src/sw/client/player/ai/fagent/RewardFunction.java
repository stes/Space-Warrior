/*******************************************************************************
 * Copyright (c) 2012 stes
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package sw.client.player.ai.fagent;

import java.util.HashMap;

/**
 * @author stes
 * @version 02.01.2012
 */
public class RewardFunction implements IRewardFunction
{
	private HashMap<IState, Double> _rewards;

	public RewardFunction()
	{
		_rewards = new HashMap<IState, Double>();
	}

	@Override
	public double getReward(IState state)
	{
		return _rewards.get(state);
	}

	@Override
	public void setReward(IState state, double value)
	{
		_rewards.put(state, value);
	}
}
