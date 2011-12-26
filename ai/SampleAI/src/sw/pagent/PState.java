package sw.pagent;

import sw.State;
import sw.shared.data.GameWorld;
import sw.shared.data.entities.players.SpaceShip;

public class PState extends State
{
	private SpaceShip _localPlayer;
	private GameWorld _world;
	
	public PState(SpaceShip localPlayer, GameWorld world)
	{
		super(3);
		_localPlayer = localPlayer;
		_world = world;
		this.init();
	}

	public void init()
	{
		this.setFeature(0, (int)this.distanceToNearestShip());
		this.setFeature(1, (int)this.angleToNextPlayer() * 360 / (2* Math.PI));
		this.setFeature(2, (int)this.nextPlayersAngle() * 360 / (2* Math.PI));
	}
	
	private double distanceToNearestShip()
	{
		SpaceShip s = this.nextShip();
		if (s != null)
			return _localPlayer.distanceTo(s);
		else
			return 0;
	}
	
	private SpaceShip nextShip()
	{
		double minDist = 1000;
		SpaceShip next = null;
		for (SpaceShip pl : _world.getPlayers())
		{
			if (pl.getName().equals(_localPlayer.getName()))
				continue;
			double dist = pl.distanceTo(_localPlayer);
			if (dist < minDist)
			{
				minDist = dist;
				next = pl;
			}
		}
		return next;
	}
	
	public double angleToNextPlayer()
	{
		SpaceShip next = nextShip();
		
		if (next == null)
			return 0;
		double x0 = _localPlayer.getX();
		double y0 = _localPlayer.getY();
		double x1 = next.getX();
		double y1 = next.getY();
		
		double alpha = _localPlayer.getDirection();
		double beta = Math.atan2(y1 - y0, x1 - x0);
		
		return beta - alpha;
	}
	
	public void showAngle()
	{
		SpaceShip next = nextShip();
		
		if (next == null)
			return;
		
		double x0 = _localPlayer.getX();
		double y0 = _localPlayer.getY();
		double x1 = next.getX();
		double y1 = next.getY();
		
		double alpha = _localPlayer.getDirection() * 360 / (2 * Math.PI);
		double beta = Math.atan2(y1 - y0, x1 - x0) * 360 / (2 * Math.PI);
		
		System.out.println("a =  " + alpha + "; b = " + beta + "; diff = " + (beta - alpha));
	}
	
	private double nextPlayersAngle()
	{
		SpaceShip next = nextShip();
		
		if (next == null)
			return 0;
		
		double x0 = next.getX();
		double y0 = next.getY();
		double x1 = _localPlayer.getX();
		double y1 = _localPlayer.getY();
		
		double alpha = next.getDirection();
		double beta = Math.atan2(y1 - y0, x1 - x0);
		
		return beta - alpha;
	}

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof PState))
			return false;
		PState s = (PState)o;
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
