package sw;

import java.util.ArrayList;

import sw.shared.data.GameWorld;


public abstract class State
{
	private double[] _features;
	private double[] _weights;

	public ArrayList<Byte> id;

	public State(int features)
	{
		_features = new double[features];
		_weights = new double[features];
	}
	
	protected double[] getFeatures()
	{
		return _features;
	}
	
	public void setFeature(int index, double value)
	{
		_features[index] = value;
	}
	
	public double getFeature(int index)
	{
		return _features[index];
	}
	
	public void setWeight(int index, double value)
	{
		_weights[index] = value;
	}
	
	public double getWeight(int index)
	{
		return _weights[index];
	}

	public double value()
	{
		double sum = 0;
		for (int i = 0; i < _features.length; i++)
		{
			sum += _features[i] * _weights[i];
		}
		return sum;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (double f : _features)
		{
			sb.append(f);
			sb.append("; ");
		}
		return sb.toString();
	}
}
