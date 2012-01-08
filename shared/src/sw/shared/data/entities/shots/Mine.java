package sw.shared.data.entities.shots;

import java.awt.Dimension;

import sw.shared.GameConstants;
import sw.shared.data.entities.players.SpaceShip;

/**
 * @author Redix stes
 * @version 07.01.2012
 */
public class Mine extends Projectile
{
	private int _lifetime;

	public Mine(double x, double y, double direction, SpaceShip owner)
	{
		super(x, y, direction, owner, WeaponType.MINE.getID());
		_lifetime = 100;
	}

	@Override
	public void fire()
	{
		SpaceShip[] players = this.getWorld().getPlayers();
		for (SpaceShip pl : players)
		{
			if (_lifetime < 80 && this.distanceTo(pl.getPosition()) < GameConstants.MAX_HITRANGE)
			{
				this.causeDamage(pl);
			}
		}
	}

	@Override
	public double getAcceleration()
	{
		return 0;
	}

	@Override
	public double getAngularAcceleration()
	{
		return Math.PI / 8;
	}


	@Override
	public int getImageID()
	{
		return GameConstants.Images.SHOT_MINE.getID();
	}

	@Override
	public double getMaximumSpeed()
	{
		return 0;
	}

	@Override
	public Dimension getSize()
	{
		return new Dimension(GameConstants.MINE_SIZE, GameConstants.MINE_SIZE);
	}

	@Override
	public void tick()
	{
		_lifetime = Math.max(_lifetime - 1, 0);
		if (_lifetime == 0)
		{
			this.destroy();
		}
		super.tick();
	}
}
