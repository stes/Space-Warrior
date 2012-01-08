package sw.client.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import sw.client.gui.sprites.IShotSprite;
import sw.client.gui.sprites.Sprite;
import sw.shared.data.entities.shots.Projectile;

public class MineSprite extends Sprite implements IShotSprite
{
	public MineSprite(Projectile entity)
	{
		super(entity);

	}

	@Override
	public void render(Graphics2D g2d, double scaleX, double scaleY, double time)
	{
		g2d.setColor(Color.GREEN);
		g2d.fillRect((int) this.getEntity().getX() - 10, (int) this.getEntity().getY() - 10, 20, 20);
	}
}
