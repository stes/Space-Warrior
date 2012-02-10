/*******************************************************************************
 * Copyright (c) 2012 stes
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package sw.client.player.ai.neuralagent;

/**
 * Interface for a Reward Function. A reward function maps states to rewards
 * 
 * @author stes
 * @version 02.01.2012
 */
public interface IRewardFunction
{
	public double getReward(IState state);

	public void setReward(IState state, double value);

}
