package sw.client.gui.sprites;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import sw.shared.data.entities.shots.LaserBeam;

public class LaserSprite extends Sprite
{
	public LaserSprite(LaserBeam entity)
	{
		super(entity);
	}

	@Override
	public void render(Graphics2D g2d, double scaleX, double scaleY, double time)
	{
		LaserBeam s = (LaserBeam) getEntity();
		g2d.setColor(Color.BLUE);
		g2d.setStroke(new BasicStroke(3));
		g2d.drawLine((int) (s.getX() * scaleX),
				(int) (s.getY() * scaleY),
				(int) (s.endPoint().getX() * scaleX),
				(int) (s.endPoint().getY() * scaleY));
	}

}
