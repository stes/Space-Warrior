package sw.client.gui.sprites;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import sw.client.gui.ImageContainer;

public abstract class Sprite
{
	private BufferedImage _image;

	private int _x;
	private int _y;
	private double _angle;
	private int _id;

	public Sprite(int id)
	{
		_id = id;
		this.loadImg(_id);
	}

	private void loadImg(int id)
	{
		_image = ImageContainer.getLocalInstance().getImage(id);
	}

	protected AffineTransform affineTransform(BufferedImage src, double degrees)
	{
		return AffineTransform.getRotateInstance(Math.toRadians(degrees),
				src.getWidth() / 2, src.getHeight() / 2);
	}

	protected BufferedImage rotateImage(BufferedImage src, double degrees)
	{
		BufferedImage rotatedImage = new BufferedImage(src.getWidth(),
				src.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = rotatedImage.createGraphics();
		g.drawImage(src, affineTransform(src, degrees), null);
		return rotatedImage;
	}

	public void draw(Graphics2D g)
	{
		g.drawImage(rotateImage(_image, 180 - getAngle()), null, (int) getX()
				- _image.getWidth() / 2, (int) getY() - _image.getHeight() / 2);
	}

	public void setAngle(double _angle)
	{
		this._angle = _angle;
	}

	public double getAngle()
	{
		return _angle;
	}

	public void setY(int _y)
	{
		this._y = _y;
	}

	public int getY()
	{
		return _y;
	}

	public void setX(int _x)
	{
		this._x = _x;
	}

	public int getX()
	{
		return _x;
	}
	
	public BufferedImage getImage()
	{
		return _image;
	}
}
