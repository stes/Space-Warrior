/*******************************************************************************
 * Copyright (c) 2012 stes
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package sw.neuralagent;

import java.util.HashMap;

import stes.ai.ann.BackpropagationTrainer;
import stes.ai.ann.NeuralNetwork;
import stes.ai.ann.NeuronLayer;
import stes.ai.ann.functions.Sigmoid;
import sw.client.player.ai.fagent.data.DataVector;
import sw.client.player.ai.neuralagent.DistanceState;
import sw.client.player.ai.neuralagent.IState;
import sw.client.player.ai.neuralagent.IValueFunction;
import sw.client.player.ai.neuralagent.RLConstants;
import sw.client.player.ai.neuralagent.RewardFunction;
import sw.client.player.ai.neuralagent.TransitionFunction;
/**
 * Value function which uses a neural network for state value mapping
 * 
 * @author stes
 * @version 14.01.2012
 */
public class NeuralValueFunction implements IValueFunction
{
	private HashMap<Integer, Double> _terminalValues;
	private NeuralNetwork _network;
	private BackpropagationTrainer _trainer;

	public NeuralValueFunction(int states)
	{
		_network = new NeuralNetwork(new Sigmoid());
		_network.addLayer(new NeuronLayer(2));
		_network.addLayer(new NeuronLayer(3));
		_network.addLayer(new NeuronLayer(1));
		_network.sealNetwork();
		_trainer = new BackpropagationTrainer(_network, 0.5);
		_terminalValues = new HashMap<Integer, Double>();
	}

	@Override
	public double getValue(IState state)
	{
		if (state.isTerminal())
			return _terminalValues.get(state);
		_network.calculateOutput(state.getFeatures());
		return _network.getOutput().getDataAt(0);
	}

	@Override
	public void setValue(IState state, double i)
	{
		_trainer.addTrainingSet(state.getFeatures(), new DataVector(new double[] { i }));
	}

	public void valueIteration(RewardFunction rewardFunction, TransitionFunction transitionFunction)
	{
		for (DataVector inputs : _trainer.getTrainingSets().keySet())
		{
			IState state = new DistanceState(inputs);
			if (state.isTerminal())
				continue;
			_trainer.modifyTrainingSet(inputs,
					new DataVector(new double[] { this.maxFutureReward(state, rewardFunction, transitionFunction)
							- this.getValue(state) }));
		}
		_trainer.train(0.2, 10000, false);
		System.out.println(_trainer.totalNetworkError());
		System.out.println(_trainer.getTrainingSetCount());
	}

	private double maxFutureReward(IState state, RewardFunction r, TransitionFunction t)
	{
		double maxValue = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < RLConstants.MAX_ACTIONS; i++)
		{
			double curValue = r.getReward(state) + RLConstants.DISCOUNT_FACTOR
					* this.getValue(t.getSuccessor(state, i));
			maxValue = Math.max(curValue, maxValue);
		}
		return maxValue;
	}
}
