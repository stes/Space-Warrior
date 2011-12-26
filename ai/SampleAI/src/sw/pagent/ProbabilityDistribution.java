package sw.pagent;

public class ProbabilityDistribution
{
	private double[][] _distribution;
	private int _actions;
	private int _states;
	
	public ProbabilityDistribution(int actions, int states)
	{
		_actions = actions;
		_states = states;
		_distribution = new double[getActions()][getStates()];
	}
	
	public int getActions()
	{
		return _actions;
	}
	
	public int getStates()
	{
		return _states;
	}
	
	public double getProbability(int action, int givenState)
	{
		return _distribution[action][givenState];
	}
	
	public void setProbabilty(int action, int givenState, double value)
	{
		_distribution[action][givenState] = value;
	}
	
	public void normalize()
	{
		for (int i = 0; i < getStates(); i++)
		{
			double sum = 0;
			for (int j = 0; j < getActions(); j++)
			{
				sum += getProbability(j, i);
			}
			for (int j = 0; j < getActions(); j++)
			{
				setProbabilty(j, i, getProbability(j, i) / sum);
			}
		}
	}
	
	public void init()
	{
		for (int i = 0; i < getStates(); i++)
		{
			for (int j = 0; j < getActions(); j++)
			{
				setProbabilty(j, i, 1);
			}
		}
		this.normalize();
	}
}
