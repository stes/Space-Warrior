package sw.pagent;

import java.util.HashMap;

public class ProbabilityDistribution
{
	private HashMap<Integer, double[]> _distribution;
	private int _actions;
	
	public ProbabilityDistribution(int actions)
	{
		_actions = actions;
		_distribution = new HashMap<Integer, double[]>();
	}
	
	public int getActions()
	{
		return _actions;
	}

	public double getProbability(int action, PState givenState)
	{
		return _distribution.get(givenState)[action];
	}
	
	public void setProbabilty(int action, PState givenState, double value)
	{
		_distribution.get(givenState)[action] = value;
	}
	
	public void normalize()
	{
		for (double[] d : _distribution.values())
		{
			double sum = 0;
			for (int j = 0; j < getActions(); j++)
			{
				sum += d[j];
			}
			for (int j = 0; j < getActions(); j++)
			{
				d[j] /= sum;
			}
		}
	}
	
	public void init()
	{
		for (double[] d : _distribution.values())
		{
			for (int j = 0; j < getActions(); j++)
			{
				d[j] = 1;
			}
		}
		this.normalize();
	}
}
