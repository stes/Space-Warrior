package sw.rlagent;

public enum Actions
{
	ACCELERATE(1), TURN_RIGHT(2), TURN_LEFT(3), SHOOT(4);

	private final int _id;

	public int getID()
	{
		return _id;
	}

	Actions(int id)
	{
		_id = id;
	}
}
