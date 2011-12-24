package sw.shared.data.entities;

import sw.shared.GameConstants;
import sw.shared.net.Packer;
import sw.shared.net.Unpacker;

public abstract class Projectile extends MoveableEntity
{
	private double _damage;
	
	public Projectile(byte type)
	{
		super(type);
		
		// TODO improve
		this.setAcceleration(GameConstants.ACCELERATION);
		this.setAngularAcceleration(0);
		this.setMaximumSpeed(GameConstants.MAX_SPEED * 2);
	}

	@Override
	public void fromSnap(Unpacker p)
	{
		super.fromSnap(p);
		this.setDamage(p.readDouble());
	}

	@Override
	public void snap(Packer p, String name)
	{
		super.snap(p, name);
		p.writeDouble(getDamage());
		
	}

	public void setDamage(double damage)
	{
		this._damage = damage;
	}

	public double getDamage()
	{
		return _damage;
	}

	@Override
	public void tick()
	{
		this.setAcceleration(MoveableEntity.ACCELERATION);
		super.tick();
	}
}
