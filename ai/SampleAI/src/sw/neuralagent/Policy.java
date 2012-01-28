/*******************************************************************************
 * Copyright (c) 2012 stes
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package sw.neuralagent;

import java.util.Random;

import stes.ai.rldemo.rl.IPolicy;
import stes.ai.rldemo.rl.ITransitionFunction;
import stes.ai.rldemo.rl.IValueFunction;

/**
 * @author stes
 * @version 02.01.2012
 */
public class Policy implements IPolicy
{
	private static Random _random = new Random(System.currentTimeMillis());

	private IValueFunction _valueFunction;

	public Policy(IValueFunction valueFunction)
	{
		_valueFunction = valueFunction;
	}

	@Override
	public int getAction(int state, ITransitionFunction t)
	{
		double maxValue = Double.NEGATIVE_INFINITY;
		int action = -1;

		for (int a = 0; a < RLConstants.MAX_ACTIONS; a++)
		{
			double curValue = _valueFunction.getValue(t.getSuccessor(state, a));
			if (curValue > maxValue)
			{
				maxValue = curValue;
				action = a;
			}
		}

		if (_random.nextDouble() < 0.4)
		{
			do
			{
				action =  _random.nextInt(RLConstants.MAX_ACTIONS);
			}
			while (_valueFunction.getValue(t.getSuccessor(state, action)) == GridWorldPanel.oo);
		}
		return action;
	}
	// @Override
	// public int getAction(int state, ITransitionFunction t)
	// {
	// double valueSum = 0;
	// double minValue = Double.NEGATIVE_INFINITY;
	//
	// HashMap<Double, Integer> actions = new HashMap<Double, Integer>();
	// for (int a = 0; a < RLConstants.MAX_ACTIONS; a++)
	// {
	// double curValue = _valueFunction.getValue(t.getSuccessor(state, a));
	// if (curValue == GridWorldPanel.oo)
	// continue;
	// if (curValue < minValue)
	// minValue = curValue;
	// }
	// for (int a = 0; a < RLConstants.MAX_ACTIONS; a++)
	// {
	// double curValue = _valueFunction.getValue(t.getSuccessor(state, a));
	// if (curValue == GridWorldPanel.oo)
	// continue;
	// valueSum += curValue - minValue;
	// actions.put(valueSum, a);
	// }
	//
	// double d = _random.nextDouble() * valueSum;
	//
	// Double[] keys = actions.keySet().toArray(new Double[] {});
	// double[] k = new double[keys.length];
	// for (int i = 0; i < k.length; i++)
	// {
	// k[i] = keys[i];
	// }
	// Arrays.sort(k);
	//
	// for (int i = 0; i < k.length; i++)
	// {
	// System.out.println(d + "/" + k[i]);
	// if (d <= k[i])
	// return actions.get(k[i]);
	// }
	// throw new IllegalStateException();
	// }
}
