package sw.rlagent;

import java.util.HashMap;

public class World
{
	private HashMap<Integer, Double> _stateMap;
	
	public World()
	{
		_stateMap = new HashMap<Integer, Double>();
		
		//_qvalues = new double[_width][_height][AIConstants.ACTIONS_COUNT];
	}
	
	public HashMap<Integer, Double> getMap()
	{
		return _stateMap;
	}
	
	public void exploreState(SimpleState s)
	{
		if (!_stateMap.containsKey(s.getHash()))
		{
			_stateMap.put(s.getHash(), s.value());
//			System.out.println("explored state " + s.getHash()+" with value " + s.value());
//			for (byte b : s.id)
//				System.out.print(b + ";");
		}
	}
}
