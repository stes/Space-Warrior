package sw.client.gui.sprites;

import java.awt.Graphics2D;

import sw.shared.data.PlayerData;

public class SpaceShipSprite extends Sprite
{
	public SpaceShipSprite(PlayerData d)
	{
		super(d.getImageID());
	}

	public void update(PlayerData data)
	{
		this.setX((int) data.getPosition().x);
		this.setY((int) data.getPosition().y);
		this.setAngle(data.getDirection());
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		super.draw(g);
	}
}
