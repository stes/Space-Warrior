package sw.neuralagent;

import sw.client.player.ai.fagent.data.DataVector;
import sw.client.player.ai.neuralagent.IState;

public abstract class State implements IState
{
	private final DataVector _featureSet;
	
	public State(int size)
	{
		_featureSet = new DataVector(size);
	}
	
	public State(DataVector inputs)
	{
		this(inputs.getSize());
		_featureSet.setData(inputs);
	}
	
	public DataVector getFeatures()
	{
		return _featureSet;
	}
	
	public double getFeature(int i)
	{
		return _featureSet.getDataAt(i);
	}
	
	protected void setFeature(int i, int distanceToNearestShip)
	{
		_featureSet.setDataAt(i, distanceToNearestShip);
	}
	
	
}
