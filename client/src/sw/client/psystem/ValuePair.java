package sw.client.psystem;

public class ValuePair
{
	private double _x;
	private double _y;
	
	public ValuePair(double x, double y)
	{
		_x = x;
		_y = y;
	}
	
	public double getX()
	{
		return _x;
	}
	
	public void setX(double x)
	{
		_x = x;
	}
	
	public double getY()
	{
		return _y;
	}
	
	public void setY(double y)
	{
		_y = y;
	}
	
	public ValuePair add(ValuePair p)
	{
		return new ValuePair(getX() + p.getX(), getY() + p.getY());
	}
}
