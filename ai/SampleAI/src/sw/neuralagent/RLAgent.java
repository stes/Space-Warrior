/*******************************************************************************
 * Copyright (c) 2012 stes
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package sw.neuralagent;

import sw.client.control.IGameController;
import sw.client.player.ai.AIPlayer;
import sw.client.player.ai.neuralagent.IPolicy;
import sw.client.player.ai.neuralagent.IRewardFunction;
import sw.client.player.ai.neuralagent.ITransitionFunction;
import sw.client.player.ai.neuralagent.IValueFunction;

/**
 * Base class for reinforcement learning agents
 * 
 * @author stes
 * @version 02.01.2012
 */
public abstract class RLAgent extends AIPlayer
{
	public RLAgent(IGameController stateManager)
	{
		super(stateManager);
	}

	private IRewardFunction _rewardFunction;
	private ITransitionFunction _transitionFunction;
	private IValueFunction _valueFunction;
	private IPolicy _policy;

	/**
	 * @return the policy
	 */
	public IPolicy getPolicy()
	{
		return _policy;
	}

	/**
	 * @return the rewardFunction
	 */
	public IRewardFunction getRewardFunction()
	{
		return _rewardFunction;
	}

	/**
	 * @return the transitionFunction
	 */
	public ITransitionFunction getTransitionFunction()
	{
		return _transitionFunction;
	}

	/**
	 * @return the valueFunction
	 */
	public IValueFunction getValueFunction()
	{
		return _valueFunction;
	}

	/**
	 * @param policy
	 *            the policy to set
	 */
	public void setPolicy(IPolicy policy)
	{
		this._policy = policy;
	}

	/**
	 * @param rewardFunction
	 *            the rewardFunction to set
	 */
	public void setRewardFunction(IRewardFunction rewardFunction)
	{
		this._rewardFunction = rewardFunction;
	}

	/**
	 * @param transitionFunction
	 *            the transitionFunction to set
	 */
	public void setTransitionFunction(ITransitionFunction transitionFunction)
	{
		this._transitionFunction = transitionFunction;
	}

	/**
	 * @param valueFunction
	 *            the valueFunction to set
	 */
	public void setValueFunction(IValueFunction valueFunction)
	{
		this._valueFunction = valueFunction;
	}

	public abstract void iteration();

	public abstract void move();

	public abstract double getValue(int x, int y);
}
