package sw.client.psystem;

import java.awt.Graphics2D;

public class CircularParticle extends Particle
{
	public CircularParticle(ValuePair location, ValuePair velocity, ValuePair acceleration)
	{
		super(location, velocity, acceleration);
	}

	@Override
	public void render(Graphics2D g)
	{
		if (!isAlive())
			return;
		g.drawOval((int)(getLocation().getX() - getSize() / 2),
				(int)(getLocation().getY() - getSize() / 2),
				(int)getSize(),
				(int)getSize());
	}

}
