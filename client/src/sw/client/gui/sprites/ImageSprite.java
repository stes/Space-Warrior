package sw.client.gui.sprites;

import java.awt.Dimension;
import java.awt.Graphics2D;

import sw.client.gui.ImageContainer;
import sw.shared.data.entities.IImageEntity;
import sw.shared.data.entities.IStaticEntity;

/**
 * 
 * @author Redix, stes
 * @version 05.01.2012
 */
public class ImageSprite extends Sprite
{
	public ImageSprite(IStaticEntity entity)
	{
		super(entity);
		if (!(entity instanceof IImageEntity))
			throw new IllegalArgumentException();
	}

	@Override
	public void render(Graphics2D g2d, double scaleX, double scaleY, double time)
	{
		IStaticEntity ent = getEntity();
		Dimension d = ((IImageEntity) ent).getSize();
		g2d.drawImage(this.rotateImage(ImageContainer.getLocalInstance().getImage(((IImageEntity)ent).getImageID()),
				-getDirection(time)),
				(int) (scaleX * (getPosition(time).getX() - d.width / 2)),
				(int) (scaleY * (getPosition(time).getY() - d.height / 2)),
				(int) (d.width * scaleX),
				(int) (d.height * scaleY),
				null);
	}

}
