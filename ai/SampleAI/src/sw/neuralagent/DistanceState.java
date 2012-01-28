package sw.neuralagent;

import java.util.ArrayList;
import java.util.Random;

import stes.ai.ann.data.DataVector;
import sw.shared.data.GameWorld;
import sw.shared.data.entities.players.SpaceShip;

public class DistanceState extends State
{
	private static Random _random = new Random(System.currentTimeMillis());

	private SpaceShip _localPlayer;
	private GameWorld _world;

	public ArrayList<Byte> id;

	public DistanceState(SpaceShip localPlayer, GameWorld world)
	{
		super(1);
		_localPlayer = localPlayer;
		_world = world;
		this.setFeature(0, (int) Tools.distanceToNearestShip(_localPlayer, _world));
	}
	
	public DistanceState(DataVector data)
	{
		super(data);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof IState))
			return false;
		IState s = (IState) o;
		if (s.getFeatures().getSize() != this.getFeatures().getSize())
			return false;
		for (int i = 0; i < this.getFeatures().getSize(); i++)
		{
			if (s.getFeature(i) != this.getFeature(i))
				return false;
		}
		return true;
	}

	@Override
	public boolean isTerminal()
	{
		return false;
	}
}
