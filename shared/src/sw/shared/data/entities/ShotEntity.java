package sw.shared.data.entities;

import sw.shared.GameConstants;
import sw.shared.Packettype;
import sw.shared.net.Packer;
import sw.shared.net.Unpacker;

public abstract class ShotEntity extends MoveableEntity implements IShot
{
	public final static byte LASER = 0x10;
	public final static byte MASTER_LASER = 0x20;
	public final static byte ROCKET = 0x30;
	public final static byte MG = 0x40;
	
	private SpaceShip _owner;
	private int _shottype;

	
	public ShotEntity(double x, double y, double direction, SpaceShip owner, byte shottype)
	{
		super((byte)(Packettype.SNAP_SHOT | shottype), x, y, direction);
		setOwner(owner);
		setShotType(shottype);
	}
	
	@Override
	public SpaceShip getOwner()
	{
		return _owner;
	}
	
	private void setOwner(SpaceShip owner)
	{
		_owner = owner;
	}
	
	@Override
	public void fromSnap(Unpacker p)
	{
		super.fromSnap(p);
		setShotType(p.readInt());
	}

	@Override
	public void snap(Packer p, String name)
	{
		super.snap(p, name);
		p.writeInt(getShotType());
	}
	
	@Override
	public void fire()
	{
		SpaceShip[] players = this.getWorld().getPlayers();
		for (SpaceShip pl : players)
		{
			if (pl.isAlive() && !pl.getName().equals(getOwner().getName())
					&& this.distanceTo(pl.getPosition()) < GameConstants.MAX_RANGE)
			{
				pl.takeDamage(this.getDamage());
				if (!pl.isAlive())
					getOwner().setScore(getOwner().getScore() + 1);
			}
		}
	}

	private void setShotType(int shottype)
	{
		this._shottype = shottype;
	}

	public int getShotType()
	{
		return _shottype;
	}

}
