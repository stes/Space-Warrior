package sw.shared;

import java.util.Random;

public class Tools
{
	private static final Random _random = new Random(System.currentTimeMillis());
	public final static Random getRandom()
	{
		return _random;
	}
}
