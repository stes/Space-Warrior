package sw.client.gui.sprites;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import sw.shared.GameConstants;
import sw.shared.data.entities.shots.LaserBeam;

public class LaserSprite extends Sprite implements IShotSprite
{
	public LaserSprite(LaserBeam entity)
	{
		super(entity);
	}
	
	@Override
	public void render(Graphics2D g2d, double scaleX, double scaleY, double time)
	{
		LaserBeam s = (LaserBeam) getEntity();
		Color c = new Color(0, 0, 255, (int)alpha(s.getLifetime()));
		g2d.setColor(c);
		g2d.setStroke(new BasicStroke(3));
		g2d.drawLine((int) (s.getX() * scaleX),
				(int) (s.getY() * scaleY),
				(int) (s.endPoint().getX() * scaleX),
				(int) (s.endPoint().getY() * scaleY));
	}
	
    private double alpha(double time)
    {
        double a = -1020 / (double)(GameConstants.SHOT_TTL*GameConstants.SHOT_TTL);
        double d = (double)GameConstants.SHOT_TTL / 2;
        double f = 255;
        return a * (time - d) * (time - d) + f;
    }

}
