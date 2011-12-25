package sw.lvqagent;

import sw.State;
import sw.shared.data.GameWorld;
import sw.shared.data.entities.players.SpaceShip;

public class LVQState extends State
{
	private SpaceShip _localPlayer;
	private GameWorld _world;
	
	public LVQState(SpaceShip localPlayer, GameWorld world)
	{
		super(2);
		_localPlayer = localPlayer;
		_world = world;
		this.init();
	}

	public void init()
	{
		this.setFeature(0, (int)this.distanceToNearestShip());
		this.setFeature(1, (int)_localPlayer.getDirection() * 180 / 2 / Math.PI);
	}
	
	private double distanceToNearestShip()
	{
		double minDist = 1000;
		for (SpaceShip pl : _world.getPlayers())
		{
			if (pl.getName().equals(_localPlayer.getName()))
				continue;
			double dist = pl.distanceTo(_localPlayer);
			if (dist < minDist)
			{
				minDist = dist;
			}
		}
		return minDist;
	}

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof LVQState))
			return false;
		LVQState s = (LVQState)o;
		if (s.getFeatures().length != this.getFeatures().length)
			return false;
		for (int i = 0; i < this.getFeatures().length; i++)
		{
			if (s.getFeature(i) != this.getFeature(i))
				return false;
		}
		return true;
	}
}
